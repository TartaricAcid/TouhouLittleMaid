package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public interface IMaidTask {
    /**
     * 模式 ID，用于后续模式的判断，也用于本地化的 key
     *
     * @return 用 ResourceLocation 类描述的模式 ID
     */
    ResourceLocation getUid();

    /**
     * 该模式的图标
     *
     * @return 用 ItemStack 类描述的图标
     */
    ItemStack getIcon();

    /**
     * 该模式的环境音效
     *
     * @param maid 女仆对象
     * @return 对应的环境音效
     */
    @Nullable
    SoundEvent getAmbientSound(EntityMaid maid);

    /**
     * 该模式下调用的 AI
     *
     * @param maid 女仆对象
     * @return 如果什么都不做，请返回空集合
     */
    List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid);

    /**
     * 当前 Task 是否可使用
     *
     * @param maid 女仆对象
     * @return 当前 Task 是否可以用
     */
    default boolean isEnable(EntityMaid maid) {
        return true;
    }

    /**
     * 获取当前模式名称
     *
     * @return 当前模式名称
     */
    default TranslationTextComponent getName() {
        return new TranslationTextComponent(String.format("task.%s.%s", getUid().getNamespace(), getUid().getPath()));
    }

    /**
     * 获取当前模式的描述文本
     *
     * @param maid 女仆对象
     * @return 模式的描述文本，可以多行<br>如果没有文本描述，请返回空集合
     */
    List<ITextComponent> getDescription(EntityMaid maid);
}
