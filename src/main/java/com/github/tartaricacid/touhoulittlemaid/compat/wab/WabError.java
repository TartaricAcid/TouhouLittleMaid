package com.github.tartaricacid.touhoulittlemaid.compat.wab;

/**
 * 桥内部的可预期失败：带协议错误码，由派发器翻译成 result{ok:false}。
 */
public final class WabError extends RuntimeException {
    private final String code;

    public WabError(String code, String message) {
        super(message);
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static WabError of(String code, String message) {
        return new WabError(code, message);
    }

    public static WabError notFound(String message) {
        return new WabError("maid_not_found", message);
    }

    public static WabError invalid(String message) {
        return new WabError("invalid_params", message);
    }
}
