package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class MaidTombstoneEvent extends Event implements ICancellableEvent {
    private final EntityMaid maid;
    private final EntityTombstone tombstone;

    public MaidTombstoneEvent(EntityMaid maid, EntityTombstone tombstone) {
        this.maid = maid;
        this.tombstone = tombstone;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public EntityTombstone getTombstone() {
        return tombstone;
    }
}
