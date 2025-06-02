package com.github.tartaricacid.touhoulittlemaid.util.http;

import com.google.common.collect.Lists;
import com.google.common.net.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.cert.CRL;
import java.util.List;

/**
 * 参考自：<a href="https://varaprasadh.medium.com/how-to-send-multipart-form-data-requests-using-java-native-httpclient-989f6921dbfa"></a>
 */
public class MultipartBodyBuilder {
    private static final String CRLF = "\r\n";
    private static final String CONTENT_TYPE = "Content-Type: %s\r\n\r\n";

    private final List<MultiPartRecord> parts = Lists.newArrayList();

    public MultipartBodyBuilder addText(String fieldName, String fieldValue) {
        return addPart(fieldName, fieldValue, MediaType.PLAIN_TEXT_UTF_8.toString());
    }

    public MultipartBodyBuilder addPart(String fieldName, String fieldValue, String contentType) {
        MultiPartRecord part = new MultiPartRecord();
        part.setFieldName(fieldName);
        part.setContent(fieldValue);
        part.setContentType(contentType);
        parts.add(part);
        return this;
    }

    public MultipartBodyBuilder addPart(String fieldName, Object fieldValue, String contentType, String fileName) {
        MultiPartRecord part = new MultiPartRecord();
        part.setFieldName(fieldName);
        part.setContent(fieldValue);
        part.setContentType(contentType);
        part.setFileName(fileName);
        parts.add(part);
        return this;
    }

    public MultipartBody build() throws IOException {
        String boundary = new BigInteger(256, new SecureRandom()).toString();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (MultiPartRecord record : parts) {
            out.write(this.getHead(record, boundary));
            Object content = record.getContent();
            if (content instanceof String text) {
                out.write(CRLF.getBytes(StandardCharsets.UTF_8));
                out.write(text.getBytes(StandardCharsets.UTF_8));
            } else if (content instanceof byte[] bytes) {
                out.write(CONTENT_TYPE.formatted(record.getContentType()).getBytes(StandardCharsets.UTF_8));
                out.write(bytes);
            } else if (content instanceof File file) {
                out.write(CONTENT_TYPE.formatted(record.getContentType()).getBytes(StandardCharsets.UTF_8));
                Files.copy(file.toPath(), out);
            } else {
                throw new IllegalArgumentException("Unsupported content type: " + content.getClass());
            }
            out.write(CRLF.getBytes(StandardCharsets.UTF_8));
        }
        out.write(this.getEnd(boundary));
        return new MultipartBody(out.toByteArray(), boundary);
    }

    private byte[] getHead(MultiPartRecord record, String boundary) {
        byte[] head;
        if (record.getFileName() == null) {
            head = """
                    --%s\r
                    Content-Disposition: form-data; name="%s"\r
                    """
                    .formatted(boundary, record.getFieldName())
                    .getBytes(StandardCharsets.UTF_8);
        } else {
            head = """
                    --%s\r
                    Content-Disposition: form-data; name="%s"; filename="%s"\r
                    """
                    .formatted(boundary, record.getFieldName(), record.getFileName())
                    .getBytes(StandardCharsets.UTF_8);
        }
        return head;
    }

    private byte[] getEnd(String boundary) {
        return """
                --%s--\r
                """.formatted(boundary)
                .getBytes(StandardCharsets.UTF_8);
    }
}
