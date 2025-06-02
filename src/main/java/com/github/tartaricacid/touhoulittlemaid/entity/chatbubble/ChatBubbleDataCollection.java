package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public record ChatBubbleDataCollection(Long2ObjectSortedMap<IChatBubbleData> chatBubbles) {
    public static final int MAX_SIZE = 5;

    public static ChatBubbleDataCollection getEmptyCollection() {
        return new ChatBubbleDataCollection(new Long2ObjectAVLTreeMap<>());
    }

    public long add(IChatBubbleData chatBubbleData) {
        long existTime = System.currentTimeMillis() + chatBubbleData.existTick() * 50L;
        if (this.chatBubbles.size() >= MAX_SIZE) {
            return addWhenFull(chatBubbleData, existTime);
        } else {
            this.chatBubbles.put(existTime, chatBubbleData);
        }
        return existTime;
    }

    private long addWhenFull(IChatBubbleData chatBubbleData, long existTime) {
        long remove = -1L;
        for (Long2ObjectMap.Entry<IChatBubbleData> entry : this.chatBubbles.long2ObjectEntrySet()) {
            long key = entry.getLongKey();
            IChatBubbleData value = entry.getValue();
            if (value.priority() <= chatBubbleData.priority()) {
                remove = key;
                break;
            }
        }
        if (remove != -1L) {
            this.chatBubbles.remove(remove);
            this.chatBubbles.put(existTime, chatBubbleData);
        } else {
            return -1L;
        }
        return existTime;
    }

    public boolean update() {
        long currentTime = System.currentTimeMillis();
        LongBidirectionalIterator iterator = chatBubbles.keySet().iterator();
        boolean dirty = false;
        while (iterator.hasNext()) {
            long time = iterator.nextLong();
            if (currentTime - time > 0) {
                iterator.remove();
                dirty = true;
            } else {
                return dirty;
            }
        }
        return dirty;
    }

    public int size() {
        return this.chatBubbles.size();
    }

    public LongSortedSet keySet() {
        return this.chatBubbles.keySet();
    }

    public IChatBubbleData get(long key) {
        return this.chatBubbles.get(key);
    }

    public boolean containsKey(long key) {
        return this.chatBubbles.containsKey(key);
    }

    public void remove(long key) {
        this.chatBubbles.remove(key);
    }

    public void put(long key, IChatBubbleData bubble) {
        this.chatBubbles.put(key, bubble);
    }

    public boolean isEmpty() {
        return this.chatBubbles == null || this.chatBubbles.isEmpty();
    }

    public IChatBubbleData getFirst() {
        return this.chatBubbles.get(this.chatBubbles.firstLongKey());
    }

    public IChatBubbleData getLast() {
        return this.chatBubbles.get(this.chatBubbles.lastLongKey());
    }

    public ObjectIterator<IChatBubbleData> iterator() {
        return this.chatBubbles.values().iterator();
    }
}
