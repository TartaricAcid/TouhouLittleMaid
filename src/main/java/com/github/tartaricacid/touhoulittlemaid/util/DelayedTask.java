package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author TartaricAcid
 * @date 2019/9/8 20:46
 **/
public final class DelayedTask {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public static LinkedList<BooleanSupplier> SUPPLIERS = Lists.newLinkedList();

    private DelayedTask() {
    }

    public static void add(final Runnable runnable, final int delayedTick) {
        final int[] tickArray = {delayedTick};
        SUPPLIERS.add(() -> {
            if (--tickArray[0] < 0) {
                runnable.run();
                return true;
            }
            return false;
        });
    }

    public static void add(final Consumer<Integer> consumer, final int delayedTick, final int times) {
        final int[] tickArray = {delayedTick};
        SUPPLIERS.add(() -> {
            if (--tickArray[0] < 0) {
                consumer.accept(times);
                return true;
            }
            return false;
        });
    }
}
