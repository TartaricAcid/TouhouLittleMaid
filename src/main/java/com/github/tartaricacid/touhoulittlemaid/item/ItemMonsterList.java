package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.MonsterListScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ItemMonsterList extends Item {
    public static final Codec<Map<ResourceLocation, MonsterType>> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, MonsterType.CODEC)
            .xmap(HashMap::new, Function.identity());

    public static final StreamCodec<ByteBuf, Map<ResourceLocation, MonsterType>> STREAM_CODEC = ByteBufCodecs.map(HashMap::new,
            ResourceLocation.STREAM_CODEC,
            ByteBufCodecs.fromCodec(MonsterType.CODEC),
            65536
    );

    public static Map<ResourceLocation, MonsterType> getMonsterList(ItemStack stack) {
        return Objects.requireNonNullElse(stack.get(InitDataComponent.MONSTER_LIST_TAG), Maps.newHashMap());
    }

    public static void addMonster(ItemStack stack, Map<ResourceLocation, MonsterType> monsterList) {
        stack.set(InitDataComponent.MONSTER_LIST_TAG, monsterList);
    }

    public ItemMonsterList() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (handIn == InteractionHand.MAIN_HAND) {
            ItemStack stack = playerIn.getMainHandItem();
            if (FMLEnvironment.dist == Dist.CLIENT) {
                openMonsterListSetScreen(stack, playerIn);
            }
            return InteractionResultHolder.success(stack);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @OnlyIn(Dist.CLIENT)
    private void openMonsterListSetScreen(ItemStack stack, Player playerIn) {
        if (playerIn.level.isClientSide) {
            Minecraft.getInstance().setScreen(new MonsterListScreen(stack));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.monster_list.desc").withStyle(ChatFormatting.GRAY));
    }
}