package com.github.tartaricacid.touhoulittlemaid.network;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabAnimationMessage;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabBindingSync;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabDetailsReply;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabDetailsRequest;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabHeadInfo;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabPhotoReply;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabPhotoRequest;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabVoiceAudio;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabVoiceClip;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabVoiceText;
import com.github.tartaricacid.touhoulittlemaid.network.message.*;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static void registerPacket(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION);

        registrar.playToServer(MaidModelPackage.TYPE, MaidModelPackage.STREAM_CODEC, MaidModelPackage::handle);
        registrar.playToServer(ChairModelPackage.TYPE, ChairModelPackage.STREAM_CODEC, ChairModelPackage::handle);
        registrar.playToClient(OpenChairGuiPackage.TYPE, OpenChairGuiPackage.STREAM_CODEC, OpenChairGuiPackage::handle);
        registrar.playToServer(MaidConfigPackage.TYPE, MaidConfigPackage.STREAM_CODEC, MaidConfigPackage::handle);
        registrar.playToServer(MaidTaskPackage.TYPE, MaidTaskPackage.STREAM_CODEC, MaidTaskPackage::handle);
        registrar.playToServer(SendNameTagPackage.TYPE, SendNameTagPackage.STREAM_CODEC, SendNameTagPackage::handle);
        registrar.playToClient(ItemBreakPackage.TYPE, ItemBreakPackage.STREAM_CODEC, ItemBreakPackage::handle);
        registrar.playToClient(SpawnParticlePackage.TYPE, SpawnParticlePackage.STREAM_CODEC, SpawnParticlePackage::handle);
        registrar.playToClient(SyncDataPackage.TYPE, SyncDataPackage.STREAM_CODEC, SyncDataPackage::handle);
        registrar.playToServer(WirelessIOGuiPackage.TYPE, WirelessIOGuiPackage.STREAM_CODEC, WirelessIOGuiPackage::handle);
        registrar.playToServer(WirelessIOSlotConfigPackage.TYPE, WirelessIOSlotConfigPackage.STREAM_CODEC, WirelessIOSlotConfigPackage::handle);
        registrar.playToClient(OpenBeaconGuiPackage.TYPE, OpenBeaconGuiPackage.STREAM_CODEC, OpenBeaconGuiPackage::handle);
        registrar.playToServer(SetBeaconPotionPackage.TYPE, SetBeaconPotionPackage.STREAM_CODEC, SetBeaconPotionPackage::handle);
        registrar.playToServer(StorageAndTakePowerPackage.TYPE, StorageAndTakePowerPackage.STREAM_CODEC, StorageAndTakePowerPackage::handle);
        registrar.playToServer(SetBeaconOverflowPackage.TYPE, SetBeaconOverflowPackage.STREAM_CODEC, SetBeaconOverflowPackage::handle);
        registrar.playToClient(BeaconAbsorbPackage.TYPE, BeaconAbsorbPackage.STREAM_CODEC, BeaconAbsorbPackage::handle);
        registrar.playToClient(OpenSwitcherGuiPackage.TYPE, OpenSwitcherGuiPackage.STREAM_CODEC, OpenSwitcherGuiPackage::handle);
        registrar.playToServer(SaveSwitcherDataPackage.TYPE, SaveSwitcherDataPackage.STREAM_CODEC, SaveSwitcherDataPackage::handle);
        registrar.playToServer(ToggleTabPackage.TYPE, ToggleTabPackage.STREAM_CODEC, ToggleTabPackage::handle);
        registrar.playToServer(RequestEffectPackage.TYPE, RequestEffectPackage.STREAM_CODEC, RequestEffectPackage::handle);
        registrar.playToClient(SendEffectPackage.TYPE, SendEffectPackage.STREAM_CODEC, SendEffectPackage::handle);
        registrar.playToClient(PlayMaidSoundPackage.TYPE, PlayMaidSoundPackage.STREAM_CODEC, PlayMaidSoundPackage::handle);
        registrar.playToClient(PlayMaidSoundAtPosPackage.TYPE, PlayMaidSoundAtPosPackage.STREAM_CODEC, PlayMaidSoundAtPosPackage::handle);
        registrar.playToServer(SetMaidSoundIdPackage.TYPE, SetMaidSoundIdPackage.STREAM_CODEC, SetMaidSoundIdPackage::handle);
        registrar.playToClient(GomokuClientPackage.TYPE, GomokuClientPackage.STREAM_CODEC, GomokuClientPackage::handle);
        registrar.playToServer(GomokuServerPackage.TYPE, GomokuServerPackage.STREAM_CODEC, GomokuServerPackage::handle);
        registrar.playToClient(FoxScrollPackage.TYPE, FoxScrollPackage.STREAM_CODEC, FoxScrollPackage::handle);
        registrar.playToServer(SetScrollPackage.TYPE, SetScrollPackage.STREAM_CODEC, SetScrollPackage::handle);
        registrar.playToClient(CheckSchedulePosPacket.TYPE, CheckSchedulePosPacket.STREAM_CODEC, CheckSchedulePosPacket::handle);
        registrar.playToClient(SyncMaidAreaPackage.TYPE, SyncMaidAreaPackage.STREAM_CODEC, SyncMaidAreaPackage::handle);
        registrar.playToServer(ServantBellSetPackage.TYPE, ServantBellSetPackage.STREAM_CODEC, ServantBellSetPackage::handle);
        registrar.playToServer(SetAttackListPackage.TYPE, SetAttackListPackage.STREAM_CODEC, SetAttackListPackage::handle);
        registrar.playToServer(RefreshMaidBrainPackage.TYPE, RefreshMaidBrainPackage.STREAM_CODEC, RefreshMaidBrainPackage::handle);
        registrar.playToServer(MaidSubConfigPackage.TYPE, MaidSubConfigPackage.STREAM_CODEC, MaidSubConfigPackage::handle);
        registrar.playToClient(CChessToClientPackage.TYPE, CChessToClientPackage.STREAM_CODEC, CChessToClientPackage::handle);
        registrar.playToServer(CChessToServerPackage.TYPE, CChessToServerPackage.STREAM_CODEC, CChessToServerPackage::handle);
        registrar.playToClient(WChessToClientPackage.TYPE, WChessToClientPackage.STREAM_CODEC, WChessToClientPackage::handle);
        registrar.playToServer(WChessToServerPackage.TYPE, WChessToServerPackage.STREAM_CODEC, WChessToServerPackage::handle);
        registrar.playToServer(SendUserChatPackage.TYPE, SendUserChatPackage.STREAM_CODEC, SendUserChatPackage::handle);
        registrar.playToClient(TTSAudioToClientPackage.TYPE, TTSAudioToClientPackage.STREAM_CODEC, TTSAudioToClientPackage::handle);
        // registrar.playToClient(SyncAiSettingPackage.TYPE, SyncAiSettingPackage.STREAM_CODEC, SyncAiSettingPackage::handle);
        // 仅安装 YSM 后才会发送此包
        registrar.playToServer(YsmMaidModelPackage.TYPE, YsmMaidModelPackage.STREAM_CODEC, YsmMaidModelPackage::handle);
        registrar.playToServer(SaveMaidAIDataPackage.TYPE, SaveMaidAIDataPackage.STREAM_CODEC, SaveMaidAIDataPackage::handle);
        // registrar.playToServer(GetMaidAIDataPackage.TYPE, GetMaidAIDataPackage.STREAM_CODEC, GetMaidAIDataPackage::handle);
        // registrar.playToClient(OpenMaidAIDataScreenPackage.TYPE, OpenMaidAIDataScreenPackage.STREAM_CODEC, OpenMaidAIDataScreenPackage::handle);
        // 仅安装 YSM 后才会发送此包
        registrar.playToClient(SyncYsmMaidDataPackage.TYPE, SyncYsmMaidDataPackage.STREAM_CODEC, SyncYsmMaidDataPackage::handle);
        registrar.playToClient(TTSSystemAudioToClientPackage.TYPE, TTSSystemAudioToClientPackage.STREAM_CODEC, TTSSystemAudioToClientPackage::handle);
        registrar.playToServer(ClearMaidAIDataPacket.TYPE, ClearMaidAIDataPacket.STREAM_CODEC, ClearMaidAIDataPacket::handle);
        registrar.playToServer(OpenMaidGuiPackage.TYPE, OpenMaidGuiPackage.STREAM_CODEC, OpenMaidGuiPackage::handle);
        registrar.playToClient(OpenPlayerInventoryPackage.TYPE, OpenPlayerInventoryPackage.STREAM_CODEC, OpenPlayerInventoryPackage::handle);
        registrar.playToServer(DismountPackage.TYPE, DismountPackage.STREAM_CODEC, DismountPackage::handle);
        registrar.playToClient(MaidAnimationPackage.TYPE, MaidAnimationPackage.STREAM_CODEC, MaidAnimationPackage::handle);
        registrar.playToClient(SyncBaublePackage.TYPE, SyncBaublePackage.STREAM_CODEC, SyncBaublePackage::handle);
        registrar.playToClient(CuriosS2CUpdatePacket.TYPE, CuriosS2CUpdatePacket.STREAM_CODEC, CuriosS2CUpdatePacket::handle);

        registrar.playToServer(OpenMaidAIChatPacket.TYPE, OpenMaidAIChatPacket.STREAM_CODEC, OpenMaidAIChatPacket::handle);
        registrar.playToClient(SyncMaidAIDataPacket.TYPE, SyncMaidAIDataPacket.STREAM_CODEC, SyncMaidAIDataPacket::handle);
        registrar.playToServer(OpenAIConfigPacket.TYPE, OpenAIConfigPacket.STREAM_CODEC, OpenAIConfigPacket::handle);
        registrar.playToClient(SyncAISitesPacket.TYPE, SyncAISitesPacket.STREAM_CODEC, SyncAISitesPacket::handle);
        registrar.playToServer(SaveLLMSitePacket.TYPE, SaveLLMSitePacket.STREAM_CODEC, SaveLLMSitePacket::handle);
        registrar.playToServer(SaveTTSSitePacket.TYPE, SaveTTSSitePacket.STREAM_CODEC, SaveTTSSitePacket::handle);

        // WAB 桥三条：动作（服务器→周边客户端）、拍照请求（服务器→主人客户端）、拍照回执（客户端→服务器）。
        // 桥上没配对时这三条路径一次都不会被走到，注册本身只是让包 ID 有个名字。
        registrar.playToClient(WabAnimationMessage.TYPE, WabAnimationMessage.STREAM_CODEC, WabAnimationMessage::handle);
        registrar.playToClient(WabPhotoRequest.TYPE, WabPhotoRequest.STREAM_CODEC, WabPhotoRequest::handle);
        registrar.playToServer(WabPhotoReply.TYPE, WabPhotoReply.STREAM_CODEC, WabPhotoReply::handle);

        // 语音四条：绑定名单（服务器→主人客户端）、录音上传（客户端→服务器）、
        // 听写结果与合成音频（服务器→主人客户端）。老宿主不宣告 voice 时客户端一次都不会走。
        registrar.playToClient(WabBindingSync.TYPE, WabBindingSync.STREAM_CODEC, WabBindingSync::handle);
        registrar.playToServer(WabVoiceClip.TYPE, WabVoiceClip.STREAM_CODEC, WabVoiceClip::handle);
        registrar.playToClient(WabVoiceText.TYPE, WabVoiceText.STREAM_CODEC, WabVoiceText::handle);
        registrar.playToClient(WabVoiceAudio.TYPE, WabVoiceAudio.STREAM_CODEC, WabVoiceAudio::handle);

        // 喂食三条：详细信息请求（客户端→服务器）、详细信息回执与头顶信息（服务器→主人客户端）。
        // H 键没按、头顶开关没开时这些路径一次都不会被走到。
        registrar.playToServer(WabDetailsRequest.TYPE, WabDetailsRequest.STREAM_CODEC, WabDetailsRequest::handle);
        registrar.playToClient(WabDetailsReply.TYPE, WabDetailsReply.STREAM_CODEC, WabDetailsReply::handle);
        registrar.playToClient(WabHeadInfo.TYPE, WabHeadInfo.STREAM_CODEC, WabHeadInfo::handle);
    }

    public static void sendToClientPlayer(CustomPacketPayload payload, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void sendToNearby(Entity entity, CustomPacketPayload toSend) {
        if (entity.level instanceof ServerLevel) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, toSend);
        }
    }

    public static void sendToNearby(Entity entity, CustomPacketPayload toSend, int distance) {
        if (entity.level instanceof ServerLevel serverLevel) {
            BlockPos pos = entity.blockPosition();
            PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), distance, toSend);
        }
    }
}
