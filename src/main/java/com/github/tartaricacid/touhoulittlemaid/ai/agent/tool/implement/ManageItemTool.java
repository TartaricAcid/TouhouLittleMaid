package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.IntegerParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.MaidItemQueryHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * AI Tool: manage_item，女仆物品管理。
 * <p>
 * 支持四种操作：inspect（检索物品详情）、swap（搜索并交换）、equip（搜索并穿戴）、drop（丢弃）。
 * 搜索范围覆盖背包、饰品栏、装备槽以及通过 {@code IExtraStorageBackpack} 暴露的额外存储。
 * 核心逻辑委托给 {@link MaidItemQueryHelper}。
 * </p>
 */
public class ManageItemTool implements ITool<ManageItemTool.Result> {
    public static final String TOOL_ID = "manage_item";

    private static final String TOOL_DESC = """
            Inspect, swap, equip, or drop items in the maid's inventory, bauble slots, and equipment slots.
            Use inspect with slot for a known slot, or with item_desc to locate and inspect exactly one matching item.
            Use swap to move exactly one matching item to main hand by default, or to target_slot when specified.
            Use equip to wear exactly one matching armor item automatically, or move it to target_slot when specified.
            Inspect, swap, and equip can request matching items from external backpack containers when no carried item matches.
            Use drop to discard an item from a known slot.
            """.trim();

    private static final String ACTION_PARAM_ID = "action";
    private static final String SLOT_PARAM_ID = "slot";
    private static final String ITEM_DESC_PARAM_ID = "item_desc";
    private static final String AMOUNT_PARAM_ID = "amount";
    private static final String TARGET_SLOT_PARAM_ID = "target_slot";

    private static final List<String> ACTIONS = List.of("inspect", "swap", "drop", "equip");

    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf(ACTION_PARAM_ID).forGetter(Result::action),
            Codec.STRING.optionalFieldOf(SLOT_PARAM_ID, StringUtils.EMPTY).forGetter(Result::slot),
            Codec.STRING.optionalFieldOf(ITEM_DESC_PARAM_ID, StringUtils.EMPTY).forGetter(Result::itemDesc),
            Codec.INT.optionalFieldOf(AMOUNT_PARAM_ID).forGetter(Result::amount),
            Codec.STRING.optionalFieldOf(TARGET_SLOT_PARAM_ID, StringUtils.EMPTY).forGetter(Result::targetSlot)
    ).apply(instance, Result::new));

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return TOOL_DESC;
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter action = StringParameter.create()
                .setDescription("Action to perform: inspect reads item data, swap moves a matching item to a hand or equipment slot, drop discards a slot item, equip wears matching armor.");
        ACTIONS.forEach(action::addEnumValues);

        StringParameter slot = StringParameter.create()
                .setDescription("Required for drop, or inspect when the slot is known. Source slot: mainhand, offhand, head, chest, legs, feet, backpack_N, bauble_N, or extra_N.");
        MaidItemQueryHelper.getSlotNames(maid).forEach(slot::addEnumValues);

        StringParameter itemDesc = StringParameter.create()
                .setDescription("Required for swap/equip, or inspect when slot is unknown. Match keywords against display name, item id, item components, and custom data.");

        IntegerParameter amount = IntegerParameter.create()
                .setMinimum(1)
                .setDescription("Optional for drop. Omit to drop the whole stack; when provided, it must be at least 1.");

        StringParameter targetSlot = StringParameter.create()
                .setDescription("Optional for swap/equip. Destination slot: mainhand, offhand, head, chest, legs, or feet. Swap defaults to mainhand; equip auto-detects armor slot when omitted.");
        MaidItemQueryHelper.getTargetSlotNames().forEach(targetSlot::addEnumValues);

        root.addProperties(ACTION_PARAM_ID, action);
        root.addProperties(SLOT_PARAM_ID, slot, false);
        root.addProperties(ITEM_DESC_PARAM_ID, itemDesc, false);
        root.addProperties(AMOUNT_PARAM_ID, amount, false);
        root.addProperties(TARGET_SLOT_PARAM_ID, targetSlot, false);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, Result result, LLMCallback callback) {
        EntityMaid maid = callback.getMaid();
        String action = result.action().toLowerCase(Locale.ROOT);
        return switch (action) {
            case "inspect" -> {
                if (StringUtils.isNotBlank(result.slot())) {
                    yield callback.addToolResult(MaidItemQueryHelper.inspect(maid, result.slot()), toolId);
                }
                if (StringUtils.isBlank(result.itemDesc())) {
                    yield callback.addToolResult("Missing required parameter: slot or item_desc", toolId);
                }
                yield callback.addToolResult(MaidItemQueryHelper.inspectMatching(maid, result.itemDesc()), toolId);
            }
            case "swap" -> {
                if (StringUtils.isBlank(result.itemDesc())) {
                    yield callback.addToolResult("Missing required parameter: item_desc", toolId);
                }
                yield callback.addToolResult(MaidItemQueryHelper.swap(maid, result.itemDesc(), result.targetSlot()), toolId);
            }
            case "drop" -> {
                if (StringUtils.isBlank(result.slot())) {
                    yield callback.addToolResult("Missing required parameter: slot", toolId);
                }
                yield callback.addToolResult(MaidItemQueryHelper.drop(maid, result.slot(), result.amount().orElse(-1)), toolId);
            }
            case "equip" -> {
                if (StringUtils.isBlank(result.itemDesc())) {
                    yield callback.addToolResult("Missing required parameter: item_desc", toolId);
                }
                yield callback.addToolResult(MaidItemQueryHelper.equip(maid, result.itemDesc(), result.targetSlot()), toolId);
            }
            default -> {
                String text = "Unknown action '%s'".formatted(result.action());
                yield callback.addToolResult(ITool.invalidParam(ACTION_PARAM_ID, ACTIONS, text), toolId);
            }
        };
    }

    @Override
    public Component invocationSummaryComponent(Result result) {
        String action = result.action().toLowerCase(Locale.ROOT);
        if (!ACTIONS.contains(action)) {
            return Component.empty();
        }
        return Component.translatable("ai.touhou_little_maid.chat.tool_call.manage_item.%s".formatted(action))
                .withStyle(ChatFormatting.GRAY);
    }

    public record Result(String action, String slot, String itemDesc, Optional<Integer> amount, String targetSlot) {
    }
}
