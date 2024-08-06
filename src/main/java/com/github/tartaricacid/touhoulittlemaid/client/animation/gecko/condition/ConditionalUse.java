package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.isValidResourceLocation;

public class ConditionalUse {
    private static final String EMPTY = "";
    private final int preSize;
    private final String idPre;
    private final String tagPre;
    private final String extraPre;
    private final List<ResourceLocation> idTest = Lists.newArrayList();
    private final List<TagKey<Item>> tagTest = Lists.newArrayList();
    private final List<UseAnim> extraTest = Lists.newArrayList();
    private final List<String> innerTest = Lists.newArrayList();

    public ConditionalUse(InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            idPre = "use_mainhand$";
            tagPre = "use_mainhand#";
            extraPre = "use_mainhand:";
            preSize = 13;
        } else {
            idPre = "use_offhand$";
            tagPre = "use_offhand#";
            extraPre = "use_offhand:";
            preSize = 12;
        }
    }

    public void addTest(String name) {
        if (name.length() <= preSize) {
            return;
        }
        String substring = name.substring(preSize);
        if (name.startsWith(idPre) && isValidResourceLocation(substring)) {
            idTest.add(new ResourceLocation(substring));
        }
        if (name.startsWith(tagPre) && isValidResourceLocation(substring)) {
            ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
            if (tags == null) {
                return;
            }
            TagKey<Item> tagKey = tags.createTagKey(new ResourceLocation(substring));
            tagTest.add(tagKey);
        }
        if (name.startsWith(extraPre)) {
            if (substring.equals(UseAnim.NONE.name().toLowerCase(Locale.US))) {
                return;
            }
            Arrays.stream(UseAnim.values()).filter(a -> a.name().toLowerCase(Locale.US).equals(substring)).findFirst().ifPresent(extraTest::add);
            innerTest.add(name);
        }
    }

	public String doTest(IMaid maid, InteractionHand hand) {
		if (maid.asEntity().getItemInHand(hand).isEmpty()) {
            return EMPTY;
        }
        String result = doIdTest(maid, hand);
        if (result.isEmpty()) {
            result = doTagTest(maid, hand);
            if (result.isEmpty()) {
                return doExtraTest(maid, hand);
            }
            return result;
        }
        return result;
    }

	private String doIdTest(IMaid maid, InteractionHand hand) {
        if (idTest.isEmpty()) {
            return EMPTY;
        }
		ItemStack itemInHand = maid.asEntity().getItemInHand(hand);
        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(itemInHand.getItem());
        if (registryName == null) {
            return EMPTY;
        }
        if (idTest.contains(registryName)) {
            return idPre + registryName;
        }
        return EMPTY;
    }

	private String doTagTest(IMaid maid, InteractionHand hand) {
        if (tagTest.isEmpty()) {
            return EMPTY;
        }
		ItemStack itemInHand = maid.asEntity().getItemInHand(hand);
        ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
        if (tags == null) {
            return EMPTY;
        }
        return tagTest.stream().filter(itemInHand::is).findFirst().map(itemTagKey -> tagPre + itemTagKey.location()).orElse(EMPTY);
    }

	private String doExtraTest(IMaid maid, InteractionHand hand) {
        if (extraTest.isEmpty() && innerTest.isEmpty()) {
            return EMPTY;
        }
        String innerName = InnerClassify.doClassifyTest(extraPre, maid, hand);
        if (StringUtils.isNotBlank(innerName) && this.innerTest.contains(innerName)) {
            return innerName;
        }
		UseAnim anim = maid.asEntity().getItemInHand(hand).getUseAnimation();
        if (this.extraTest.contains(anim)) {
            return extraPre + anim.name().toLowerCase(Locale.US);
        }
        return EMPTY;
    }
}
