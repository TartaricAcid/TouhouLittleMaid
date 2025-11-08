package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.register.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public abstract class TaskBuilder<T extends TaskBuilder<T, E>, E> {
    @HideFromJS
    public final ResourceLocation id;
    @HideFromJS
    public final ItemStack icon;
    @HideFromJS
    public final List<Pair<Integer, BiFunction<E, EntityMaid, BehaviorControl<? super EntityMaid>>>> brains = Lists.newArrayList();
    @HideFromJS
    public final List<Pair<Integer, BiFunction<E, EntityMaid, BehaviorControl<? super EntityMaid>>>> rideBrains = Lists.newArrayList();
    @HideFromJS
    public final List<Pair<String, Predicate<EntityMaid>>> enableConditionDesc = Lists.newArrayList();
    @HideFromJS
    public final List<Pair<String, Predicate<EntityMaid>>> conditionDesc = Lists.newArrayList();
    @HideFromJS
    public @Nullable Predicate<EntityMaid> enable = null;
    @HideFromJS
    public @Nullable Predicate<EntityMaid> isHidden = null;
    @HideFromJS
    public @Nullable Predicate<EntityMaid> enableLookAndRandomWalk = null;
    @HideFromJS
    public @Nullable Predicate<EntityMaid> enableEating = null;
    @HideFromJS
    public @Nullable SoundEvent sound;

    public TaskBuilder(ResourceLocation id, ItemStack icon) {
        this.id = id;
        this.icon = icon;
    }

    @Info(value = """
            Adds a brain to the task. Generally, no content needs to be added here unless you need to add some special behavior control. <br>
            向任务添加一个新的 Brain。一般来说这里不需要添加任何内容，除非你需要添加一些特殊的行为控制。
            """, params = {
            @Param(name = "priority", value = """
                    The priority of the brain task, higher values are executed first. <br>
                    Brain 的优先级，值越小越先执行，一般为 5。
                    """),
            @Param(name = "control", value = """
                    The behavior control object. <br>
                    Brain 对象。
                    """)
    })
    public T addBrain(int priority, BiFunction<E, EntityMaid, BehaviorControl<? super EntityMaid>> control) {
        this.brains.add(Pair.of(priority, control));
        return (T) this;
    }

    @Info(value = """
            Adds a ride brain to the task. Generally, no content needs to be added here unless you need to add some special behavior control. <br>
            向任务添加一个新的骑乘状态下的 Brain。一般来说这里不需要添加任何内容，除非你需要添加一些特殊的行为控制。
            """, params = {
            @Param(name = "priority", value = """
                    The priority of the ride brain task, higher values are executed first. <br>
                    骑乘状态下的 Brain 的优先级，值越小越先执行，一般为 5。
                    """),
            @Param(name = "control", value = """
                    The behavior control object. <br>
                    骑乘状态下的 Brain 对象。
                    """)
    })
    public T addRideBrain(int priority, BiFunction<E, EntityMaid, BehaviorControl<? super EntityMaid>> control) {
        this.rideBrains.add(Pair.of(priority, control));
        return (T) this;
    }

    @Info(value = """
            Adds a description for the enable condition of the task. <br>
            向任务添加一个启用条件的描述。
            """, params = {
            @Param(name = "languageKey", value = """
                    The language key for the description. <br>
                    描述的语言文件 key。
                    """),
            @Param(name = "condition", value = """
                    The condition that must be met for the task to be enabled. <br>
                    任务启用所需满足的条件。
                    """)
    })
    public T addEnableConditionDesc(String languageKey, Predicate<EntityMaid> condition) {
        this.enableConditionDesc.add(Pair.of(languageKey, condition));
        return (T) this;
    }

    @Info(value = """
            Adds a description for the condition of the task. <br>
            向任务添加一个普通描述。
            """, params = {
            @Param(name = "languageKey", value = """
                    The language key for the description. <br>
                    描述的语言文件 key。
                    """),
            @Param(name = "condition", value = """
                    The condition that must be met for the task to be considered valid. <br>
                    任务被认为有效所需满足的条件。
                    """)
    })
    public T addConditionDesc(String languageKey, Predicate<EntityMaid> condition) {
        this.conditionDesc.add(Pair.of(languageKey, condition));
        return (T) this;
    }

    @Info(value = """
            Sets the enable condition for the task. <br>
            设置任务的启用条件。
            """)
    public T enable(Predicate<EntityMaid> enable) {
        this.enable = enable;
        return (T) this;
    }

    @Info(value = """
            Sets the hidden condition for the task. Default is not hidden. <br>
            设置该任务是否在切换界面中隐藏，默认不隐藏。
            """)
    @ApiStatus.AvailableSince("1.4.2")
    public T isHidden(Predicate<EntityMaid> isHidden) {
        this.isHidden = isHidden;
        return (T) this;
    }

    @Info(value = """
            Sets the condition for enabling look and random walk behavior in the task. <br>
            设置任务中是否启用四处张望和随机行走 AI。
            """)
    public T enableLookAndRandomWalk(Predicate<EntityMaid> enableLookAndRandomWalk) {
        this.enableLookAndRandomWalk = enableLookAndRandomWalk;
        return (T) this;
    }

    @Info(value = """
            Sets the condition for enabling eating behavior in the task. <br>
            设置任务中是否启用吃东西 AI。
            """)
    public T enableEating(Predicate<EntityMaid> enableEating) {
        this.enableEating = enableEating;
        return (T) this;
    }

    @Info(value = """
            Set the sound for the task. Generally, it does not need to be modified unless you want to customize the sound. <br>
            设置该任务所播放的音效，一般不需要修改，除非你需要自定义音效。
            """)
    public T sound(SoundEvent sound) {
        this.sound = sound;
        return (T) this;
    }
}
