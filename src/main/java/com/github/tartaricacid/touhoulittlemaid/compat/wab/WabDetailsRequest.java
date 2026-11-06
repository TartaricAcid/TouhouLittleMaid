package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 主人按了「详细信息」键：服务器把这位女仆的全部数值（WAB stats + 模组状态 + 口味规则）
 * 拼成有序的 label/value 行送回，由 {@link WabDetailsScreen} 分页展示。
 * 标签发的是语言键，客户端按自己的语言渲染；数值是服务端算好的纯文本。
 */
public record WabDetailsRequest(UUID maid) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WabDetailsRequest> TYPE =
            new CustomPacketPayload.Type<>(getResourceLocation("wab_details_request"));

    public static final StreamCodec<FriendlyByteBuf, WabDetailsRequest> STREAM_CODEC = StreamCodec.of(
            (buf, message) -> buf.writeUUID(message.maid()),
            buf -> new WabDetailsRequest(buf.readUUID())
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WabDetailsRequest message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer sender) {
                    NetworkHandler.sendToClientPlayer(
                            new WabDetailsReply(build(message.maid(), sender)), sender);
                }
            });
        }
    }

    private static JsonObject build(UUID maidId, ServerPlayer requester) {
        JsonObject out = new JsonObject();
        WabBridge bridge = WabBridge.get();
        EntityMaid maid = bridge == null ? null : bridge.ownedMaids().stream()
                .filter(m -> m.getUUID().equals(maidId) && m.isOwnedBy(requester) && m.isAlive())
                .findFirst().orElse(null);
        if (maid == null) {
            out.addProperty("error", "wab.details.error.no_maid");
            return out;
        }
        out.addProperty("name", WabBridge.cleanName(maid));
        JsonArray rows = new JsonArray();

        section(rows, "wab.details.sec.basic");
        row(rows, "wab.details.health",
                Math.round(maid.getHealth() * 10) / 10.0 + " / " + Math.round(maid.getMaxHealth() * 10) / 10.0);
        row(rows, "wab.details.task", maid.getTask().getUid().toString());
        row(rows, "wab.details.schedule", maid.getSchedule().name());
        row(rows, "wab.details.activity", maid.getScheduleDetail().getName());
        row(rows, "wab.details.dimension", maid.level().dimension().location().toString());
        BlockPos pos = maid.blockPosition();
        row(rows, "wab.details.pos", pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
        Player owner = maid.getOwner() instanceof Player p ? p : null;
        row(rows, "wab.details.owner_distance", owner != null && owner.level() == maid.level()
                ? String.format("%.1f", Math.sqrt(maid.distanceToSqr(owner))) : "wab.details.na");

        WabHud.Stats stats = bridge.stats(maid.getUUID());
        section(rows, "wab.details.sec.wab");
        if (stats == null) {
            row(rows, "wab.details.affection", "wab.details.na");
        } else {
            if (!stats.character.isEmpty()) row(rows, "wab.details.character", stats.character);
            row(rows, "wab.details.affection", String.valueOf(stats.affection));
            row(rows, "wab.details.affection_points", String.valueOf(stats.affectionPoints));
            row(rows, "wab.details.trust", String.valueOf(stats.trust));
            row(rows, "wab.details.energy", String.valueOf(stats.energy));
            row(rows, "wab.details.mood", String.valueOf(stats.mood));
        }

        section(rows, "wab.details.sec.mod");
        row(rows, "wab.details.favorability", String.valueOf(maid.getFavorability()));
        row(rows, "wab.details.experience", String.valueOf(maid.getExperience()));
        row(rows, "wab.details.hunger", String.valueOf(maid.getHunger()));
        row(rows, "wab.details.home_mode", boolText(maid.isHomeModeEnable()));
        row(rows, "wab.details.restrict_radius", String.valueOf(Math.round(maid.getRestrictRadius())));

        section(rows, "wab.details.sec.state");
        row(rows, "wab.details.sitting", boolText(maid.isInSittingPose()));
        row(rows, "wab.details.sleeping", boolText(maid.isSleeping()));
        row(rows, "wab.details.in_water", boolText(maid.isInWater()));
        row(rows, "wab.details.pickup", boolText(maid.isPickup()));

        section(rows, "wab.details.sec.hands");
        row(rows, "wab.details.main_hand", handText(maid.getMainHandItem()));
        row(rows, "wab.details.off_hand", handText(maid.getOffhandItem()));

        if (WabFeedRules.hasRules()) {
            section(rows, "wab.details.sec.taste");
            row(rows, "wab.details.taste_picky", String.valueOf(WabFeedRules.pickyCount()));
            row(rows, "wab.details.taste_favorite", String.valueOf(WabFeedRules.favoriteCount()));
            row(rows, "wab.details.taste_limit", String.valueOf(WabFeedRules.boredomLimit()));
            row(rows, "wab.details.taste_tracked", String.valueOf(WabFeedRules.trackedCount()));
        }

        out.add("rows", rows);
        return out;
    }

    private static void section(JsonArray rows, String key) {
        JsonObject r = new JsonObject();
        r.addProperty("k", key);
        r.addProperty("v", "");
        rows.add(r);
    }

    private static void row(JsonArray rows, String key, String value) {
        JsonObject r = new JsonObject();
        r.addProperty("k", key);
        r.addProperty("v", value == null ? "" : value);
        rows.add(r);
    }

    private static String boolText(boolean b) {
        return b ? "wab.details.yes" : "wab.details.no";
    }

    private static String handText(ItemStack stack) {
        if (stack.isEmpty()) return "wab.details.na";
        String name = stack.getHoverName().getString();
        return stack.getCount() > 1 ? name + " x" + stack.getCount() : name;
    }
}
