package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityKeyboard extends TileEntityJoy {
    public static final TileEntityType<TileEntityKeyboard> TYPE = TileEntityType.Builder.of(TileEntityKeyboard::new, InitBlocks.KEYBOARD.get()).build(null);

    public TileEntityKeyboard() {
        super(TYPE);
    }
}