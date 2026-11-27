package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * 客户端这边「哪些女仆归 WAB 管」的一份只读副本，由 {@link WabBindingSync} 推下来。
 * 按键那一下要立刻决定走哪条录音路线，不能先绕服务器问一圈——那点延迟会吃掉第一个字。
 */
public final class WabClientState {
    private static volatile Set<UUID> bound = Collections.emptySet();
    private static volatile boolean stt;
    private static volatile boolean tts;
    private static volatile boolean chat;

    private WabClientState() {
    }

    static void apply(WabBindingSync message) {
        bound = Set.copyOf(message.maids());
        stt = message.stt();
        tts = message.tts();
        chat = message.chat();
    }

    public static boolean sttFor(EntityMaid maid) {
        return stt && maid != null && bound.contains(maid.getUUID());
    }

    /** 桥还挂着、且这具身体绑了角色：回话由 WAB 出，模组自己的「启用大语言模型」不该拦这次开口。 */
    public static boolean chatFor(EntityMaid maid) {
        return chat && maid != null && bound.contains(maid.getUUID());
    }

    public static boolean connected() {
        return chat;
    }

    /** 宿主此刻能听写：设置页据此把游戏自己的语音识别那几项标成「已由 WAB 承担」。 */
    public static boolean sttByWab() {
        return stt;
    }

    /** 宿主此刻能合成语音，同上。这两个值已经把「桥断了」算进去了，不必再叠 connected()。 */
    public static boolean ttsByWab() {
        return tts;
    }
}
