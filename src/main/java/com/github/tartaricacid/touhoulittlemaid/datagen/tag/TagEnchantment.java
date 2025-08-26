package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datagen.EnchantmentKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagEnchantment extends EnchantmentTagsProvider {
    // 不知为何无法使用 datagen 生成，故仅保留此类作为参考
    public TagEnchantment(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, completableFuture, TouhouLittleMaid.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EnchantmentTags.NON_TREASURE).add(EnchantmentKeys.SPEEDY, EnchantmentKeys.IMPEDING);
        tag(EnchantmentTags.TREASURE).add(EnchantmentKeys.ENDERS_ENDER);
        tag(EnchantmentTags.TRADEABLE).add(EnchantmentKeys.ENDERS_ENDER);
    }
}
