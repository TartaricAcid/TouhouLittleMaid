package com.github.tartaricacid.touhoulittlemaid.ai.service.stt;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.player.Player;

public interface STTClient {
    void startRecord(Player player, EntityMaid maid);

    void stopRecord(Player player, EntityMaid maid);
}
