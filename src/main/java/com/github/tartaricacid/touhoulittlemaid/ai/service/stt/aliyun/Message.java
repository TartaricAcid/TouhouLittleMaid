package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("task_id")
    private String taskId;

    @SerializedName("result")
    private String result;

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    public String getTaskId() {
        return taskId;
    }

    public String getResult() {
        return result;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
