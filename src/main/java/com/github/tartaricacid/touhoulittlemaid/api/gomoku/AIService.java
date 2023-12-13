/*
 * https://github.com/anlingyi/xechat-idea
 *
 *        Apache License
 *   Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package com.github.tartaricacid.touhoulittlemaid.api.gomoku;

/**
 * @author anlingyi
 * @date 2021/11/1 3:26 下午
 */
public interface AIService {
    /**
     * 获取AI棋位
     *
     * @param chessData 已下棋子数据
     * @param point     对手棋位
     */
    Point getPoint(int[][] chessData, Point point);

    /**
     * 获取棋局状态
     *
     * @param chessData 已下棋子数据
     * @param point     落子棋位
     */
    Statue getStatue(int[][] chessData, Point point);

    /**
     * AI配置
     */
    class AIConfig {
        /**
         * 搜索深度
         */
        private final int depth;
        /**
         * 最大启发式节点数
         */
        private final int maxNodes;
        /**
         * debug
         */
        private final boolean debug;
        /**
         * 算杀 0.不开启 1.VCT 2.VCF
         */
        private final int vcx;
        /**
         * 算杀深度
         */
        private final int vcxDepth;

        public AIConfig(int depth, int maxNodes, boolean debug, int vcx, int vcxDepth) {
            this.depth = depth;
            this.maxNodes = maxNodes;
            this.debug = debug;
            this.vcx = vcx;
            this.vcxDepth = vcxDepth;
        }

        public int getDepth() {
            return depth;
        }

        public int getMaxNodes() {
            return maxNodes;
        }

        public boolean isDebug() {
            return debug;
        }

        public int getVcx() {
            return vcx;
        }

        public int getVcxDepth() {
            return vcxDepth;
        }
    }
}
