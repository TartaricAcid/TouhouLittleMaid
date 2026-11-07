package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 女仆特质：一些补充性格的开关（目前先做「怕黑」）。状态存实体持久数据里。
 * AI 可用 maid_act interact_addon arg=trait:&lt;id&gt;（切换）或 trait:&lt;id&gt;:on/off（设置）开关，
 * 也可在插件设置里由人开关。物理表现由模组这侧负责：怕黑时天黑就往亮处跑；
 * 主人在旁边就缩着坐下、偶尔冒一句害怕的话（连桥时再交给 AI 开口）。
 */
public final class WabTraits {
    public static final String FEAR_DARK = "fear_dark";
    private static final String TAG = "WabTraitSet";
    private static final int DARK_LEVEL = 7;
    private static final String BUBBLE_SCARED = "chat_bubble.touhou_little_maid.trait.fear_dark.scared";

    private WabTraits() {
    }

    public static List<String> list(EntityMaid maid) {
        List<String> out = new ArrayList<>();
        String raw = maid.getPersistentData().getString(TAG);
        if (raw.isEmpty()) return out;
        for (String s : raw.split(",")) if (!s.trim().isEmpty()) out.add(s.trim());
        return out;
    }

    public static boolean has(EntityMaid maid, String id) {
        return list(maid).contains(id);
    }

    private static void save(EntityMaid maid, List<String> ids) {
        maid.getPersistentData().putString(TAG, String.join(",", ids));
    }

    /** 切换/设置某个特质。on=null 表示切换；返回切换后的状态。 */
    public static boolean set(EntityMaid maid, String id, Boolean on) {
        List<String> ids = list(maid);
        boolean now = ids.contains(id);
        boolean target = on == null ? !now : on;
        if (target && !now) ids.add(id);
        if (!target && now) ids.remove(id);
        save(maid, ids);
        return target;
    }

    /** maid_act interact_addon 的 arg 以 trait: 开头就切特质；返回反馈文本，否则 null。 */
    public static String handleAct(EntityMaid maid, String arg) {
        String a = arg == null ? "" : arg.trim();
        if (!a.toLowerCase(Locale.ROOT).startsWith("trait:")) return null;
        String rest = a.substring("trait:".length());
        String id;
        Boolean on = null;
        int colon = rest.indexOf(':');
        if (colon >= 0) {
            id = rest.substring(0, colon);
            String v = rest.substring(colon + 1).toLowerCase(Locale.ROOT);
            if (v.equals("on") || v.equals("true") || v.equals("1")) on = true;
            else if (v.equals("off") || v.equals("false") || v.equals("0")) on = false;
        } else {
            id = rest;
        }
        if (id.isEmpty()) return "未知特质。";
        boolean now = set(maid, id, on);
        return "特质 " + id + " 已" + (now ? "开启" : "关闭") + "。";
    }

    public static void tick(WabBridge bridge) {
        for (EntityMaid maid : bridge.ownedMaids()) {
            fearDark(maid);
        }
    }

    private static void fearDark(EntityMaid maid) {
        if (maid == null || !maid.isAlive() || maid.level().isClientSide) return;
        if (!has(maid, FEAR_DARK)) return;
        Level level = maid.level();
        BlockPos pos = maid.blockPosition();
        if (level.getMaxLocalRawBrightness(pos) >= DARK_LEVEL) return;

        long now = level.getGameTime();
        CompoundTag data = maid.getPersistentData();
        ServerPlayer owner = maid.getOwner() instanceof ServerPlayer sp ? sp : null;
        boolean ownerNear = owner != null && owner.level() == level && owner.distanceToSqr(maid) <= 36.0;

        if (ownerNear) {
            // 缩在主人身边：坐下、停住，隔一会儿冒一句害怕的话（连桥时 AI 会接着开口）。
            if (!maid.isInSittingPose()) maid.setInSittingPose(true);
            maid.getNavigation().stop();
            if (now - data.getLong("WabFearCry") >= 160) {
                data.putLong("WabFearCry", now);
                maid.getChatBubbleManager().addTextChatBubble(BUBBLE_SCARED);
            }
        } else {
            if (maid.isInSittingPose()) maid.setInSittingPose(false);
            if (now - data.getLong("WabFearFlee") >= 120) {
                data.putLong("WabFearFlee", now);
                BlockPos lit = findLit(level, pos, 8);
                if (lit != null) maid.getNavigation().moveTo(lit.getX() + 0.5, lit.getY(), lit.getZ() + 0.5, 1.0);
            }
        }
    }

    private static BlockPos findLit(Level level, BlockPos center, int r) {
        BlockPos best = null;
        int bestLight = DARK_LEVEL;
        for (int dx = -r; dx <= r; dx += 2) {
            for (int dz = -r; dz <= r; dz += 2) {
                for (int dy = -2; dy <= 2; dy += 2) {
                    BlockPos p = center.offset(dx, dy, dz);
                    if (!level.hasChunkAt(p)) continue;
                    int l = level.getMaxLocalRawBrightness(p);
                    if (l > bestLight) {
                        bestLight = l;
                        best = p;
                    }
                }
            }
        }
        return best;
    }
}
