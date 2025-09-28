package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;

import java.util.Arrays;

public class NodeNeighborCache {
    public final int tickCount;

    private final int centerX;
    private final int centerY;
    private final int centerZ;

    private final int dx;
    private final int dy;
    private final int dz;

    private final int rx;
    private final int ry;
    private final int rz;

    // 链表next，使用offset描述，默认情况下从0->n-1依次相连，所以使用id+offset+1来获取nextId，可以省略初始化步骤
    private final int[] nextOffset;

    // data用于存储节点描述信息，用这些信息重新创建node
    private final long[] data;
    private final long[] data2;
    private final float[] data3;

    // 某个方块位置的链表起始节点。0代表不存在，后续ID顺次减一
    private final int[] start;

    // 下一个空闲节点的id，从这个节点开始的data节点都是空的，可以覆盖
    private int nextFree = 1;

    public NodeNeighborCache(int x, int y, int z, int rx, int ry, int rz, int tickCount) {
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
        this.dx = rx * 2 + 1;
        this.dy = ry * 2 + 1;
        this.dz = rz * 2 + 1;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.nextOffset = new int[dx * dy * dz * 10];
        this.start = new int[dx * dy * dz];
        this.data = new long[dx * dy * dz * 10];
        this.data2 = new long[dx * dy * dz * 10];
        this.data3 = new float[dx * dy * dz * 10];
        this.nextOffset[dx * dy * dz - 1] = 0;
        this.tickCount = tickCount;
    }

    private NodeNeighborCache(int x, int y, int z, NodeNeighborCache cache) {
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
        this.dx = cache.dx;
        this.dz = cache.dz;
        this.dy = cache.dy;
        this.rx = cache.rx;
        this.ry = cache.ry;
        this.rz = cache.rz;
        this.nextOffset = Arrays.copyOf(cache.nextOffset, cache.nextOffset.length);
        this.start = new int[cache.start.length];
        this.data = Arrays.copyOf(cache.data, cache.data.length);
        this.data2 = Arrays.copyOf(cache.data2, cache.data2.length);
        this.data3 = Arrays.copyOf(cache.data3, cache.data3.length);
        this.nextFree = cache.nextFree;
        this.tickCount = cache.tickCount;
    }

    public void record(Node pos, Node[] nodes, int nodeId) {
        if (!isInRange(pos.x, pos.y, pos.z)) {
            return;
        }
        int nid = id(pos.x, pos.y, pos.z);
        // 确保可以存下所有节点
        if (!grantNode(nodeId)) {
            return;
        }
        int lastNode = 0;
        for (int i = 0; i < nodeId; i++) {
            int nextFreeNode = getNextFreeNode();
            if (lastNode != 0) {
                next(lastNode, nextFreeNode);
            } else {
                start[nid] = nextFreeNode;
            }
            storeNode(nextFreeNode, nodes[i]);
            lastNode = nextFreeNode;
        }
        if (lastNode != 0) {
            next(lastNode, 0);
        }
    }

    private void storeNode(int i, Node node) {
        data[i - 1] = (((long) node.x) << 32) | node.z;
        data2[i - 1] = (((long) node.y) << 32) | (node.type.ordinal() << 1) | (node.closed ? 1 : 0);
        data3[i - 1] = node.costMalus;
    }

    private Node addNode(int i, INodeCacheEvaluator nodeCreator) {
        Node node = nodeCreator.createNode((int) (data[i - 1] >> 32), (int) (data2[i - 1] >> 32), (int) (data[i - 1] & 0xFFFFFFFFL));
        node.type = PathType.values()[(int) (data2[i - 1] & 0xFFFFFFFFL) >> 1];
        node.costMalus = data3[i - 1] == -1F ? -1F : Math.max(node.costMalus, data3[i - 1]);
        return node;
    }

    public int get(Node node, Node[] nodes, INodeCacheEvaluator nodeCreator) {
        if (!isInRange(node.x, node.y, node.z)) {
            return -1;
        }
        int nid = id(node.x, node.y, node.z);
        if (start[nid] == 0) {
            return -1;
        }
        int i = start[nid];
        int count = 0;
        while (i != 0) {
            nodes[count++] = addNode(i, nodeCreator);
            i = next(i);
        }
        return count;
    }

    public boolean grantNode(int count) {
        int t = this.nextFree;
        for (int i = 0; i < count; i++) {
            if (t == 0) {
                return false;
            }
            t = next(t);
        }
        return true;
    }

    public int getNextFreeNode() {
        int tmp = nextFree;
        nextFree = next(nextFree);
        return tmp;
    }

    public void clearForId(int nid) {
        if (start[nid] == 0) {
            return;
        }
        next(finalFrom(start[nid]), nextFree);
        nextFree = start[nid];
        start[nid] = 0;
    }

    public int id(BlockPos pos) {
        return id(pos.getX(), pos.getY(), pos.getZ());
    }

    public int id(int x, int y, int z) {
        return ((x - this.centerX + this.rx) * this.dy * this.dz) +
               ((y - this.centerY + this.ry) * this.dz) +
               (z - this.centerZ + this.rz);
    }

    public int next(int id) {
        return id + nextOffset[id - 1] + 1;
    }

    public void next(int id, int i) {
        nextOffset[id - 1] = i - id - 1;
    }

    public int finalFrom(int id) {
        int i = id;
        while (true) {
            if (next(i) == 0) {
                return i;
            }
            i = next(i);
        }
    }

    public boolean isInRange(int ix, int iy, int iz) {
        return ix >= this.centerX - this.rx && ix <= this.centerX + this.rx &&
               iy >= this.centerY - this.ry && iy <= this.centerY + this.ry &&
               iz >= this.centerZ - this.rz && iz <= this.centerZ + this.rz;
    }

    public static NodeNeighborCache copyToAnotherCenter(NodeNeighborCache cache, int x, int y, int z) {
        NodeNeighborCache t = new NodeNeighborCache(x, y, z, cache);
        for (int tx = cache.centerX - cache.rx; tx <= cache.centerX + cache.rx; tx++) {
            for (int ty = cache.centerY - cache.ry; ty <= cache.centerY + cache.ry; ty++) {
                for (int tz = cache.centerZ - cache.rz; tz <= cache.centerZ + cache.rz; tz++) {
                    if (t.isInRange(tx, ty, tz)) {
                        t.start[t.id(tx, ty, tz)] = cache.start[cache.id(tx, ty, tz)];
                    } else {
                        t.clearForId(cache.id(tx, ty, tz));
                    }
                }
            }
        }
        return t;
    }
}
