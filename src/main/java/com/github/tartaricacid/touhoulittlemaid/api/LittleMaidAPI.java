package com.github.tartaricacid.touhoulittlemaid.api;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.tartaricacid.touhoulittlemaid.api.util.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.api.util.ItemDefinition;
import com.google.common.base.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LittleMaidAPI
{
    private static ILittleMaidAPI INSTANCE = null;

    public static void setInstance(ILittleMaidAPI instance)
    {
        INSTANCE = instance;
    }

    @Nullable
    public static IMaidBauble registerBauble(ItemDefinition item, IMaidBauble bauble)
    {
        return INSTANCE.registerBauble(item, bauble);
    }

    @Nullable
    public static IMaidBauble getBauble(ItemDefinition item)
    {
        return INSTANCE.getBauble(item);
    }

    @Nullable
    public static IMaidBauble getBauble(ItemStack item)
    {
        return INSTANCE.getBauble(item);
    }

    @Nonnull
    public static int getBaubleSlotInMaid(AbstractEntityMaid maid, IMaidBauble bauble)
    {
        return INSTANCE.getBaubleSlotInMaid(maid, bauble);
    }

    public static void registerTask(IMaidTask task)
    {
        INSTANCE.registerTask(task);
    }

    public static Optional<IMaidTask> findTask(ResourceLocation uid)
    {
        return INSTANCE.findTask(uid);
    }

    public static List<IMaidTask> getTasks()
    {
        return INSTANCE.getTasks();
    }

    public static IMaidTask getIdleTask()
    {
        return INSTANCE.getIdleTask();
    }

    public static interface ILittleMaidAPI
    {
        IMaidBauble registerBauble(ItemDefinition item, IMaidBauble bauble);

        IMaidBauble getBauble(ItemDefinition item);

        default IMaidBauble getBauble(ItemStack item)
        {
            return getBauble(ItemDefinition.of(item));
        }

        default int getBaubleSlotInMaid(AbstractEntityMaid maid, IMaidBauble bauble)
        {
            BaubleItemHandler handler = maid.getBaubleInv();
            if (handler != null)
            {
                for (int i = 0; i < handler.getSlots(); i++)
                {
                    IMaidBauble baubleIn = handler.getBaubleInSlot(i);
                    if (baubleIn == bauble)
                    {
                        return i;
                    }
                }
            }
            return -1;
        }

        void registerTask(IMaidTask task);

        Optional<IMaidTask> findTask(ResourceLocation uid);

        List<IMaidTask> getTasks();

        IMaidTask getIdleTask();
    }
}
