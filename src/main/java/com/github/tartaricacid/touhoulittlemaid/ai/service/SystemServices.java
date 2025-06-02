package com.github.tartaricacid.touhoulittlemaid.ai.service;

/**
 * 系统服务站点
 * <p>
 * 一些服务并不是通过网络通信调用，而是通过系统直接进行通信
 * <p>
 * 如果 TTS 站点继承了此接口，那么将调用玩家客户端服务进行音频生成与播放
 */
public interface SystemServices {
}
