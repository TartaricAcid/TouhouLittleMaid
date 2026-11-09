package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidAISoundInstance;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端侧：绑了 WAB 角色的女仆，录音走 WAB（经服务器转发），播放 WAB 合成回来的 WAV。
 * 录音设备与格式仍沿用游戏原来的麦克风设置，只有「谁来听写」换成了 WAB。
 */
@OnlyIn(Dist.CLIENT)
public final class WabVoiceClient {
    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, false);
    private static final long ASSEMBLY_TIMEOUT_MS = 60_000L;

    private static EntityMaid recordingMaid;
    private static ResponseCallback<String> recordingCallback;
    private static boolean recording;

    private static final Map<Integer, Assembly> ASSEMBLIES = new ConcurrentHashMap<>();

    private WabVoiceClient() {
    }

    private static final class Assembly {
        final int maid;
        final byte[][] chunks;
        final long at = System.currentTimeMillis();
        int filled;

        Assembly(int maid, int parts) {
            this.maid = maid;
            this.chunks = new byte[parts][];
        }
    }

    public static boolean isRecordingFor(EntityMaid maid) {
        return recording && recordingMaid != null && maid != null && recordingMaid.getId() == maid.getId();
    }

    public static void start(EntityMaid maid, ResponseCallback<String> callback) {
        Mixer.Info info = MicrophoneManager.getMicrophoneInfo(FORMAT);
        if (info == null) {
            callback.onFailure(null, new Throwable("No suitable microphone found"), ErrorCode.MICROPHONE_NOT_FOUND);
            return;
        }
        recordingMaid = maid;
        recordingCallback = callback;
        recording = true;
        MicrophoneManager.startRecord(info.getName(), FORMAT, data -> sendClip(maid, data));
    }

    public static void stop() {
        recording = false;
        MicrophoneManager.stopRecord();
    }

    private static void sendClip(EntityMaid maid, byte[] wav) {
        int maidId = maid.getId();
        Minecraft.getInstance().execute(() -> {
            if (wav == null || wav.length == 0) {
                onText(new WabVoiceText(maidId, false, "", "这段录音是空的。"));
                return;
            }
            NetworkHandler.CHANNEL.sendToServer(new WabVoiceClip(maidId, wav));
        });
    }

    public static void onText(WabVoiceText message) {
        ResponseCallback<String> callback = recordingCallback;
        EntityMaid maid = recordingMaid;
        if (callback == null || maid == null || maid.getId() != message.maid()) return;
        recordingCallback = null;
        recordingMaid = null;
        recording = false;
        if (message.ok()) {
            callback.onSuccess(message.text());
        } else {
            String error = message.error() == null || message.error().isEmpty() ? "没听清这句。" : message.error();
            callback.onFailure(null, new Throwable(error), ErrorCode.REQUEST_RECEIVED_ERROR);
        }
    }

    public static void onAudio(WabVoiceAudio message) {
        int parts = message.parts();
        if (parts <= 0 || parts > 64) return;
        if (message.part() < 0 || message.part() >= parts) return;
        if (message.data().length == 0) return;

        long now = System.currentTimeMillis();
        ASSEMBLIES.entrySet().removeIf(entry -> now - entry.getValue().at > ASSEMBLY_TIMEOUT_MS);

        Assembly assembly = ASSEMBLIES.get(message.request());
        if (assembly == null || assembly.chunks.length != parts || message.part() == 0) {
            assembly = new Assembly(message.maid(), parts);
        }
        ASSEMBLIES.put(message.request(), assembly);
        if (assembly.chunks[message.part()] == null) {
            assembly.chunks[message.part()] = message.data();
            assembly.filled++;
        }
        if (assembly.filled < parts) return;

        ASSEMBLIES.remove(message.request());
        play(assembly);
    }

    private static void play(Assembly assembly) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        Entity entity = mc.level.getEntity(assembly.maid);
        if (!(entity instanceof EntityMaid maid) || !maid.isAlive()) return;
        int total = 0;
        for (byte[] chunk : assembly.chunks) {
            if (chunk == null) return;
            total += chunk.length;
        }
        byte[] wav = new byte[total];
        int offset = 0;
        for (byte[] chunk : assembly.chunks) {
            System.arraycopy(chunk, 0, wav, offset, chunk.length);
            offset += chunk.length;
        }
        Minecraft.getInstance().getSoundManager().play(new MaidAISoundInstance(maid, wav));
    }
}
