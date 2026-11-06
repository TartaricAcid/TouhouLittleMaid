package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidUtensil;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticlePackage;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Locale;

/**
 * 喂食系统：放置类食物经餐具中转（右键食物装一口 → 右键女仆喂出去），
 * 非放置食物直接潜行 + 右键就能喂。口味规则（挑食 / 喜爱 / 吃腻）来自 WAB 下发的 feed_rules 帧，
 * 没连桥时规则为空、人人可喂、行为与原版一致。
 * 好感数值走两条道：模组自己的 favorability（WorkMeal，CakeEdible 同一档）在这里给，
 * WAB 那边的好感由宿主收到 feed 事件后自己判，这里不替它决定。
 */
public final class WabFeeding {
    /** 一口中转的食物：食物 id、显示名、来源（方块或直接手持）、这一口占整个食物的比例与营养。 */
    public record Portion(String foodId, String foodName, String source, float fraction,
                          int nutrition, float saturationMod) {
    }

    /** 从方块取一口的中间产物：bites 属性有值就走「咬一口」，否则整块就是一份食物。 */
    private record Bite(BlockState state, @Nullable IntegerProperty bites, int current, int max,
                        ItemStack food, Portion portion) {
    }

    private static final String TAG = ItemMaidUtensil.PORTION_TAG;

    private WabFeeding() {
    }

    // ------------------------------------------------------------------ NBT

    public static Portion read(ItemStack utensil) {
        CustomData data = utensil.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        CompoundTag tag = data.copyTag();
        if (!tag.contains(TAG, CompoundTag.TAG_COMPOUND)) return null;
        CompoundTag p = tag.getCompound(TAG);
        String foodId = p.getString("food");
        if (foodId.isEmpty()) return null;
        return new Portion(foodId, p.getString("foodName"), p.getString("source"),
                p.getFloat("fraction"), p.getInt("nutrition"), p.getFloat("saturationMod"));
    }

    public static void write(ItemStack utensil, @Nullable Portion portion) {
        if (portion == null) {
            utensil.remove(DataComponents.CUSTOM_DATA);
            return;
        }
        CompoundTag p = new CompoundTag();
        p.putString("food", portion.foodId());
        p.putString("foodName", portion.foodName());
        p.putString("source", portion.source());
        p.putFloat("fraction", portion.fraction());
        p.putInt("nutrition", portion.nutrition());
        p.putFloat("saturationMod", portion.saturationMod());
        CompoundTag tag = new CompoundTag();
        tag.put(TAG, p);
        utensil.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    // ------------------------------------------------------------ 餐具装一口

    public static InteractionResult takeBite(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        Level level = ctx.getLevel();
        if (player == null) return InteractionResult.PASS;
        Bite bite = resolveBite(level, ctx.getClickedPos());
        if (bite == null) return InteractionResult.PASS;
        if (!level.isClientSide) {
            consumeBite(level, ctx.getClickedPos(), bite);
            write(ctx.getItemInHand(), bite.portion());
            level.playSound(null, ctx.getClickedPos(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8f, 1.0f);
            player.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.take_bite",
                    bite.portion().foodName()).withStyle(ChatFormatting.GRAY), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** 通用取一口：有 bites 属性的方块（原版蛋糕、蜡烛蛋糕、其他模组复用同属性或自带 bites 的餐点）咬一口；方块本体是食物的直接整份取走。 */
    private static @Nullable Bite resolveBite(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        String source = "block:" + BuiltInRegistries.BLOCK.getKey(state.getBlock());
        for (Property<?> prop : state.getProperties()) {
            if (!(prop instanceof IntegerProperty ip) || !"bites".equals(ip.getName())) continue;
            ItemStack food = edibleBlockFood(level, pos, state);
            if (food.isEmpty()) return null;
            Portion portion = portionOf(food, 1.0f / (Collections.max(ip.getPossibleValues()) + 1), source);
            if (portion == null) return null;
            return new Bite(state, ip, state.getValue(ip), Collections.max(ip.getPossibleValues()), food, portion);
        }
        ItemStack food = edibleBlockFood(level, pos, state);
        if (food.isEmpty()) return null;
        Portion portion = portionOf(food, 1.0f, source);
        if (portion == null) return null;
        return new Bite(state, null, 0, 0, food, portion);
    }

    /** 方块对应的可吃物品：先取 clone item（多数餐点方块都有），不行再试方块自己的物品。 */
    private static ItemStack edibleBlockFood(Level level, BlockPos pos, BlockState state) {
        try {
            ItemStack clone = state.getBlock().getCloneItemStack(level, pos, state);
            if (isFood(clone)) return clone;
        } catch (Exception ignored) {
        }
        ItemStack asItem = new ItemStack(state.getBlock());
        if (isFood(asItem)) return asItem;
        return ItemStack.EMPTY;
    }

    private static boolean isFood(ItemStack stack) {
        // 1.21 没有 ItemStack.isEdible：食物判定就是「能取到 FoodProperties」。
        return !stack.isEmpty() && foodProps(stack) != null;
    }

    private static @Nullable FoodProperties foodProps(ItemStack stack) {
        try {
            return stack.getFoodProperties(null);
        } catch (Exception e) {
            return null;
        }
    }

    private static @Nullable Portion portionOf(ItemStack food, float fraction, String source) {
        FoodProperties props = foodProps(food);
        if (props == null) return null;
        String id = BuiltInRegistries.ITEM.getKey(food.getItem()).toString();
        return new Portion(id, food.getHoverName().getString(), source, fraction,
                props.nutrition(), props.saturation());
    }

    private static void consumeBite(Level level, BlockPos pos, Bite bite) {
        if (bite.bites() != null && bite.current() < bite.max()) {
            level.setBlock(pos, bite.state().setValue(bite.bites(), bite.current() + 1), Block.UPDATE_ALL);
        } else {
            // 最后一口：整块拿走，不再掉落——它已经变成餐具里那一口了。
            level.destroyBlock(pos, false);
        }
    }

    // -------------------------------------------------------------- 喂女仆

    /** 餐具里有这一口才吞事件；空餐具什么都不做，右键照旧打开她的界面。 */
    public static InteractionResult feedMaidFromUtensil(ItemStack utensil, Player player, EntityMaid maid, InteractionHand hand) {
        Portion portion = read(utensil);
        if (portion == null) return InteractionResult.PASS;
        if (player.level().isClientSide) return InteractionResult.SUCCESS;
        ItemStack food = foodStack(portion);
        if (food.isEmpty()) {
            write(utensil, null);
            player.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.food_gone")
                    .withStyle(ChatFormatting.GRAY), false);
            return InteractionResult.SUCCESS;
        }
        boolean accepted = applyToMaid(player, maid, portion, utensilKind(utensil), food);
        if (accepted) write(utensil, null);
        return InteractionResult.SUCCESS;
    }

    /** 潜行 + 右键直接把手里食物喂出去（InteractMaidEvent，只有主人能走到这）。 */
    public static void feedFoodDirect(Player player, EntityMaid maid, ItemStack food, InteractionHand hand) {
        Portion portion = portionOf(food, 1.0f, "item");
        if (portion == null) return;
        if (applyToMaid(player, maid, portion, "hand", food) && !player.getAbilities().instabuild) {
            food.shrink(1);
        }
    }

    /** 客户端与服务端共用判据：潜行 + 拿着吃的 + 系统开着，这一右键就被投喂吃掉、不再开她的界面。 */
    public static boolean wantsDirectFeed(Player player, EntityMaid maid, ItemStack held) {
        return WabConfig.feedingEnabled() && maid.isAlive() && foodProps(held) != null && player.isShiftKeyDown();
    }

    /**
     * 非主人右键的喂食关卡：喂食系统开着、且拿的是盛着食物的餐具或潜行拿着食物才拦下。
     * 只许主人喂开着 → 她把头扭开并提醒对方；关了 → 真的喂出去（餐具那一口 / 手里那一份）。
     * 返回 true 表示这次右键已经被喂食系统吃掉，调用方不要再走驯服。
     */
    public static boolean refuseNonOwner(Player player, EntityMaid maid, ItemStack held) {
        if (!WabConfig.feedingEnabled()) return false;
        boolean utensilBite = held.getItem() instanceof ItemMaidUtensil && read(held) != null;
        boolean directFood = player.isShiftKeyDown() && foodProps(held) != null;
        if (!utensilBite && !directFood) return false;
        if (WabConfig.ownerOnlyFeeding() && !maid.isOwnedBy(player)) {
            if (!player.level().isClientSide) {
                ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.feed.refuse_owner");
                player.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.refuse_owner")
                        .withStyle(ChatFormatting.GRAY), false);
                report(player, maid, read(held), utensilBite ? utensilKind(held) : "hand", "refused_owner", 0);
            }
            return true;
        }
        // 权限放开：非主人也能喂。客户端只表态，喂的动作在服务端做。
        if (!player.level().isClientSide) {
            if (utensilBite) {
                feedMaidFromUtensil(held, player, maid, InteractionHand.MAIN_HAND);
            } else {
                feedFoodDirect(player, maid, held, InteractionHand.MAIN_HAND);
            }
        }
        return true;
    }

    /**
     * 投喂主流程：权限 → 口味（挑食 / 吃腻拒收）→ 真的吃下去（治疗 + 模组 favorability + 反馈）→ 上报 feed 事件。
     */
    private static boolean applyToMaid(Player feeder, EntityMaid maid, Portion portion, String utensil, ItemStack food) {
        if (WabConfig.ownerOnlyFeeding() && !maid.isOwnedBy(feeder)) {
            ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.feed.refuse_owner");
            feeder.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.refuse_owner")
                    .withStyle(ChatFormatting.GRAY), false);
            report(feeder, maid, portion, utensil, "refused_owner", 0);
            return false;
        }

        WabFeedRules.Verdict verdict = WabFeedRules.classify(food);
        switch (verdict) {
            case PICKY -> {
                ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.feed.refuse_picky");
                feeder.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.refuse_picky",
                        portion.foodName()).withStyle(ChatFormatting.GRAY), false);
                report(feeder, maid, portion, utensil, "picky", WabFeedRules.count(food));
                return false;
            }
            case BORED -> {
                ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.feed.bored");
                feeder.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.bored",
                        portion.foodName()).withStyle(ChatFormatting.GRAY), false);
                report(feeder, maid, portion, utensil, "bored", WabFeedRules.count(food));
                return false;
            }
            default -> {
            }
        }

        // 吃：挥手、食物粒子、吃声；最爱吃的多来几颗爱心。
        maid.swing(InteractionHand.MAIN_HAND);
        maid.spawnItemParticles(food, verdict == WabFeedRules.Verdict.FAVORITE ? 10 : 6);
        maid.playSound(SoundEvents.GENERIC_EAT, 0.8f, 1.0f);
        if (verdict == WabFeedRules.Verdict.FAVORITE) {
            NetworkHandler.sendToNearby(maid, new SpawnParticlePackage(maid.getId(), SpawnParticlePackage.Type.HEART, 20));
        }
        maid.heal((int) Math.max(1, Math.round(portion.nutrition() * portion.fraction() * 0.5f)));
        // 模组自己的 favorability：CakeEdible 一口是 1，最爱吃的给 3，其余 1。
        maid.getFavorabilityManager().apply(Type.WORK_MEAL, verdict == WabFeedRules.Verdict.FAVORITE ? 3 : 1);
        int count = WabFeedRules.note(food);

        if (verdict == WabFeedRules.Verdict.FAVORITE) {
            ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.feed.favorite");
            feeder.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.favorite",
                    portion.foodName()).withStyle(ChatFormatting.GRAY), false);
        } else {
            feeder.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.eat",
                    portion.foodName()).withStyle(ChatFormatting.GRAY), false);
        }
        // 连着桥的话，这句反馈也让她用自己的嗓子说出来。
        WabBridge bridge = WabBridge.get();
        if (bridge != null && bridge.connected() && bridge.isBound(maid)) {
            WabVoice.speak(bridge, maid, Component.translatable(verdict == WabFeedRules.Verdict.FAVORITE
                    ? "chat_bubble.touhou_little_maid.feed.favorite" : "chat_bubble.touhou_little_maid.feed.eat").getString());
        }
        report(feeder, maid, portion, utensil, verdict.name().toLowerCase(Locale.ROOT), count);
        return true;
    }

    // ------------------------------------------------------------ 喂其他玩家

    public static InteractionResult feedPlayerFromUtensil(ItemStack utensil, Player feeder, Player eater, InteractionHand hand) {
        Portion portion = read(utensil);
        if (portion == null) return InteractionResult.PASS;
        if (feeder.level().isClientSide) return InteractionResult.SUCCESS;
        if (eater.getFoodData().getFoodLevel() >= 20) {
            feeder.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.player_full",
                    eater.getName()).withStyle(ChatFormatting.GRAY), false);
            return InteractionResult.SUCCESS;
        }
        eater.getFoodData().eat(Math.max(1, Math.round(portion.nutrition() * portion.fraction())),
                portion.saturationMod() * portion.fraction());
        ItemStack food = foodStack(portion);
        if (!food.isEmpty() && eater.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, food),
                    eater.getX(), eater.getY() + eater.getBbHeight() * 0.5, eater.getZ(), 6,
                    eater.getBbWidth() * 0.5, eater.getBbHeight() * 0.25, eater.getBbWidth() * 0.5, 0.0);
        }
        eater.playSound(SoundEvents.GENERIC_EAT, 0.8f, 1.0f);
        write(utensil, null);
        feeder.displayClientMessage(Component.translatable("message.touhou_little_maid.feed.player_fed",
                eater.getName()).withStyle(ChatFormatting.GRAY), false);
        return InteractionResult.SUCCESS;
    }

    // ---------------------------------------------------------------- 上报

    private static void report(Player feeder, EntityMaid maid, @Nullable Portion portion, String utensil,
                               String verdict, int count) {
        WabBridge bridge = WabBridge.get();
        if (bridge == null || !bridge.connected()) return;
        String foodName = portion == null ? "" : portion.foodName();
        String text = switch (verdict) {
            case "favorite" -> feeder.getName().getString() + "用" + utensilName(utensil) + "喂了她最爱的" + foodName + "，她吃得很开心。";
            case "picky" -> feeder.getName().getString() + "想喂她" + foodName + "，但那是她挑食的东西，她把头扭开了。";
            case "bored" -> feeder.getName().getString() + "又喂了她" + foodName + "，她已经吃腻了。";
            case "refused_owner" -> feeder.getName().getString() + "不是她的主人，想给她递吃的，被她拒绝了。";
            default -> feeder.getName().getString() + "用" + utensilName(utensil) + "喂了她一口" + foodName + "。";
        };
        JsonObject extra = new JsonObject();
        extra.addProperty("food", portion == null ? "" : portion.foodId());
        extra.addProperty("foodName", foodName);
        extra.addProperty("utensil", utensil);
        extra.addProperty("source", portion == null ? "" : portion.source());
        extra.addProperty("fraction", portion == null ? 0 : portion.fraction());
        extra.addProperty("verdict", verdict);
        extra.addProperty("count", count);
        extra.addProperty("eater", feeder.getName().getString());
        bridge.sendEvent("feed", maid, text, 0.6, 0, null, extra);
    }

    private static String utensilName(String kind) {
        return switch (kind) {
            case "spoon" -> "勺子";
            case "fork_knife" -> "刀叉";
            case "chopsticks" -> "筷子";
            default -> "手";
        };
    }

    private static String utensilKind(ItemStack utensil) {
        if (utensil.getItem() instanceof ItemMaidUtensil item) return item.kind();
        return "hand";
    }

    private static ItemStack foodStack(Portion portion) {
        ResourceLocation rl = ResourceLocation.tryParse(portion.foodId());
        if (rl == null) return ItemStack.EMPTY;
        Item item = BuiltInRegistries.ITEM.getOptional(rl).orElse(null);
        if (item == null || item == Items.AIR) return ItemStack.EMPTY;
        return new ItemStack(item, 1);
    }
}
