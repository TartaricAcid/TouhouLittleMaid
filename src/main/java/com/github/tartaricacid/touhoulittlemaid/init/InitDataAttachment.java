package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.data.GeckoMaidEntityAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class InitDataAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TouhouLittleMaid.MOD_ID);
    public static final Supplier<AttachmentType<MaidNumAttachment>> MAID_NUM = ATTACHMENT_TYPES.register("maid_num", () -> AttachmentType.builder(() -> new MaidNumAttachment(0)).serialize(MaidNumAttachment.CODEC).build());
    public static final Supplier<AttachmentType<PowerAttachment>> POWER_NUM = ATTACHMENT_TYPES.register("power", () -> AttachmentType.builder(() -> new PowerAttachment(0)).serialize(PowerAttachment.CODEC).build());
    public static final Supplier<AttachmentType<GeckoMaidEntityAttachment>> GECKO_MAID = ATTACHMENT_TYPES.register("gecko_maid", () -> AttachmentType.builder(() -> new GeckoMaidEntityAttachment()).serialize(GeckoMaidEntityAttachment.CODEC).build());
}
