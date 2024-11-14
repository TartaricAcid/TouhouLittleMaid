package com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("all")
public class Util {
    public static int MIN_MAX(int min, int mid, int max) {
        return mid < min ? min : Math.min(mid, max);
    }

    private static byte[] POP_COUNT_16 = new byte[65536];

    static {
        for (int i = 0; i < 65536; i++) {
            int n = ((i >> 1) & 0x5555) + (i & 0x5555);
            n = ((n >> 2) & 0x3333) + (n & 0x3333);
            n = ((n >> 4) & 0x0f0f) + (n & 0x0f0f);
            POP_COUNT_16[i] = (byte) ((n >> 8) + (n & 0x00ff));
        }
    }

    public static int POP_COUNT_16(int data) {
        return POP_COUNT_16[data];
    }

    public static int readShort(InputStream in) throws IOException {
        int b0 = in.read();
        int b1 = in.read();
        if (b0 == -1 || b1 == -1) {
            throw new IOException();
        }
        return b0 | (b1 << 8);
    }

    public static int readInt(InputStream in) throws IOException {
        int b0 = in.read();
        int b1 = in.read();
        int b2 = in.read();
        int b3 = in.read();
        if (b0 == -1 || b1 == -1 || b2 == -1 || b3 == -1) {
            throw new IOException();
        }
        return b0 | (b1 << 8) | (b2 << 16) | (b3 << 24);
    }

    public static class RC4 {
        public int[] state = new int[256];
        public int x, y;

        public void swap(int i, int j) {
            int t = state[i];
            state[i] = state[j];
            state[j] = t;
        }

        public RC4(byte[] key) {
            x = 0;
            y = 0;
            for (int i = 0; i < 256; i++) {
                state[i] = i;
            }
            int j = 0;
            for (int i = 0; i < 256; i++) {
                j = (j + state[i] + key[i % key.length]) & 0xff;
                swap(i, j);
            }
        }

        public int nextByte() {
            x = (x + 1) & 0xff;
            y = (y + state[x]) & 0xff;
            swap(x, y);
            int t = (state[x] + state[y]) & 0xff;
            return state[t];
        }

        public int nextLong() {
            int n0, n1, n2, n3;
            n0 = nextByte();
            n1 = nextByte();
            n2 = nextByte();
            n3 = nextByte();
            return n0 + (n1 << 8) + (n2 << 16) + (n3 << 24);
        }
    }

    public static int binarySearch(int vl, int[] vls, int from, int to) {
        int low = from;
        int high = to - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (vls[mid] < vl) {
                low = mid + 1;
            } else if (vls[mid] > vl) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    private static final int[] SHELL_STEP = {0, 1, 4, 13, 40, 121, 364, 1093};

    public static void shellSort(int[] mvs, int[] vls, int from, int to) {
        int stepLevel = 1;
        while (SHELL_STEP[stepLevel] < to - from) {
            stepLevel++;
        }
        stepLevel--;
        while (stepLevel > 0) {
            int step = SHELL_STEP[stepLevel];
            for (int i = from + step; i < to; i++) {
                int mvBest = mvs[i];
                int vlBest = vls[i];
                int j = i - step;
                while (j >= from && vlBest > vls[j]) {
                    mvs[j + step] = mvs[j];
                    vls[j + step] = vls[j];
                    j -= step;
                }
                mvs[j + step] = mvBest;
                vls[j + step] = vlBest;
            }
            stepLevel--;
        }
    }
}