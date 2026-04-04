package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.tencent;

import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import net.minecraft.Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.TreeMap;

import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

/**
 * 腾讯云的 v3 版本摘要加密
 */
public class TencentSign {
    @SuppressWarnings("all")
    private static String canonicalRequestSha256Hex(TreeMap<String, String> sortHeaders, String signedHeaders, String request) {
        // 1. 头部 key 和 value 统一转成小写，并去掉首尾空格，按照 key:value\n 格式拼接；
        // 2. 多个头部，按照头部 key（小写）的 ASCII 升序进行拼接。
        String canonicalHeaders = sortHeaders.entrySet().stream()
                .map(entry -> "%s:%s\n".formatted(entry.getKey(), entry.getValue()))
                .reduce("", String::concat);

        // 请求正文做 SHA256 哈希，十六进制编码，转换成小写字母
        String hashedRequestPayload = DigestUtils.sha256Hex(request);

        String canonicalRequest = String.join("\n",
                "POST", // HTTP 请求方法（GET、POST），我们这里固定为 POST
                "/", // URI 参数，API 3.0 固定为正斜杠（/）
                "", // GET 的查询字符串，POST 固定为空
                canonicalHeaders, // 参与签名的头部信息的键值对
                signedHeaders,  // 参与签名的头部信息，说明此次请求有哪些头部参与了签名
                hashedRequestPayload // 请求正文 SHA256 哈希
        );

        return DigestUtils.sha256Hex(canonicalRequest);
    }

    @SuppressWarnings("all")
    private static String stringToSign(String canonicalRequestSha256Hex, String credentialScope, long timestamp) {
        return String.join("\n",
                "TC3-HMAC-SHA256", // 签名算法，目前固定为 TC3-HMAC-SHA256。
                String.valueOf(timestamp), // 请求时间戳，单位为秒
                credentialScope, // 凭证范围
                canonicalRequestSha256Hex // 规范请求串的哈希值
        );
    }

    private static String signature(String secretKey, String date, String stringToSign) {
        byte[] secretDate = new HmacUtils(HMAC_SHA_256, "TC3" + secretKey).hmac(date);
        byte[] secretService = new HmacUtils(HMAC_SHA_256, secretDate).hmac("asr");
        byte[] secretSigning = new HmacUtils(HMAC_SHA_256, secretService).hmac("tc3_request");
        return new HmacUtils(HMAC_SHA_256, secretSigning).hmacHex(stringToSign);
    }

    public static String authorization(STTTencentSite site, long timestamp, String request) {
        URI uri = URI.create(site.url());
        String secretId = site.getSecretId();
        String secretKey = site.getSecretKey();

        // 小写 key 和 value，并去掉首尾空格，然后按照 key 字典排序
        TreeMap<String, String> sortHeaders = Util.make(Maps.newTreeMap(), map -> {
            map.put(lowerCaseAndTrim(HttpHeaders.CONTENT_TYPE), lowerCaseAndTrim(MediaType.JSON_UTF_8.toString()));
            map.put(lowerCaseAndTrim(HttpHeaders.HOST), lowerCaseAndTrim(uri.getHost()));
        });

        // 1. 头部 key 统一转成小写；
        // 2. 多个头部 key（小写）按照 ASCII 升序进行拼接，并且以分号（;）分隔。
        String signedHeaders = sortHeaders.keySet().stream()
                .reduce((a, b) -> a + ";" + b)
                .orElse("");

        // Date 为 UTC 标准时间的日期，比如 2019-02-25
        String date = Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.UTC).toLocalDate().toString();

        // 凭证范围，格式为 Date/service/tc3_request，包含日期、所请求的服务和终止字符串（tc3_request）；语音识别对应 asr
        String credentialScope = "%s/asr/tc3_request".formatted(date);

        // 计算 signature
        String canonicalRequestSha256Hex = canonicalRequestSha256Hex(sortHeaders, signedHeaders, request);
        String stringToSign = stringToSign(canonicalRequestSha256Hex, credentialScope, timestamp);
        String signature = signature(secretKey, date, stringToSign);

        return "TC3-HMAC-SHA256 Credential=%s/%s, SignedHeaders=%s, Signature=%s"
                .formatted(secretId, credentialScope, signedHeaders, signature);
    }

    private static String lowerCaseAndTrim(String string) {
        return StringUtils.trimToEmpty(string).toLowerCase();
    }
}
