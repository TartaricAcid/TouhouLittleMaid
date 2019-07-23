package com.github.tartaricacid.touhoulittlemaid.internal;

import java.util.Map;

import com.github.tartaricacid.touhoulittlemaid.api.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.ItemDefinition;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI.ILittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;

public class LittleMaidAPIImpl implements ILittleMaidAPI
{
    private final Map<ItemDefinition, IMaidBauble> baubles = Maps.newHashMap();

    @Override
    public IMaidBauble registerBauble(ItemDefinition item, IMaidBauble bauble)
    {
        return baubles.put(item, bauble);
    }

    @Override
    public IMaidBauble getBauble(ItemDefinition item)
    {
        return baubles.get(item);
    }

    @Override
    public BaubleItemHandler getBaubleInventory(Entity maid)
    {
        if (maid instanceof EntityMaid)
        {
            return ((EntityMaid) maid).getBaubleInv();
        }
        return null;
    }

}
