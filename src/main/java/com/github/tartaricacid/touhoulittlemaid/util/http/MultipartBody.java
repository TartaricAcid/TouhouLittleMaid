package com.github.tartaricacid.touhoulittlemaid.util.http;

public class MultipartBody {
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private final byte[] bytes;
    private String boundary;

    public MultipartBody(byte[] bytes, String boundary) {
        this.bytes = bytes;
        this.boundary = boundary;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public String getContentType() {
        return MULTIPART_FORM_DATA + "; boundary=" + this.getBoundary();
    }

    public byte[] getBody() {
        return this.bytes;
    }
}
