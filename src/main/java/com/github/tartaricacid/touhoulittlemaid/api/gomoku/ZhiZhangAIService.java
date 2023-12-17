/*
 * https://github.com/anlingyi/xechat-idea
 *
 *        Apache License
 *   Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package com.github.tartaricacid.touhoulittlemaid.api.gomoku;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author anlingyi
 * @date 2021/11/1 3:31 下午
 */
public class ZhiZhangAIService implements AIService {
    /**
     * 用于初始化 Zobrist 随机值
     */
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    /**
     * 声明一个最大值
     */
    private static final int INFINITY = 999999999;
    /**
     * 棋型分数表
     */
    private static final Map<String, Integer> SCORE = new LinkedHashMap<>();
    /**
     * 黑子 Zobrist
     */
    private static final long[][] BLACK_ZOBRIST = new long[15][15];
    /**
     * 白子 Zobrist
     */
    private static final long[][] WHITE_ZOBRIST = new long[15][15];

    static {
        // 初始化棋型分数表
        for (ChessModel chessScore : ChessModel.values()) {
            for (String value : chessScore.values) {
                SCORE.put(value, chessScore.score);
            }
        }

        // 初始化Zobrist随机值
        for (int i = 0; i < BLACK_ZOBRIST.length; i++) {
            for (int j = 0; j < BLACK_ZOBRIST.length; j++) {
                BLACK_ZOBRIST[i][j] = RANDOM.nextLong();
                WHITE_ZOBRIST[i][j] = RANDOM.nextLong();
            }
        }
    }

    /**
     * AI配置
     */
    private final AIConfig aiConfig;
    /**
     * 已下棋子数据
     */
    private int[][] chessData;
    /**
     * 棋盘行数
     */
    private int rows;
    /**
     * 棋盘列数
     */
    private int cols;
    /**
     * AI棋子类型
     */
    private int ai;
    /**
     * 进攻系数
     */
    private float attack;
    /**
     * AI最佳下棋点位
     */
    private Point bestPoint;
    /**
     * 当前回合数
     */
    private int rounds;
    /**
     * 统计
     */
    private Statistics statistics;
    /**
     * 最佳落子路径
     */
    private Stack<Point> pathStack;
    /**
     * 最佳落子路径
     */
    private Stack<Point> bestPathStack;
    /**
     * 当前局面的hashcode
     */
    private long hashcode;
    /**
     * 局面缓存
     */
    private Map<Long, SituationCache> situationCacheMap;

    public ZhiZhangAIService() {
        this(new AIConfig(6, 10, false, 0, 6));
    }

    public ZhiZhangAIService(AIConfig aiConfig) {
        this.aiConfig = aiConfig;
    }

    @Override
    public Point getPoint(int[][] chessData, Point point) {
        initChessData(chessData);
        this.statistics = new Statistics();
        this.ai = 3 - point.type;
        this.bestPoint = null;
        // AI是黑棋则偏进攻，是白棋则偏防守
        this.attack = this.ai == 1 ? 1.8f : 0.5f;
        int depth = this.aiConfig.getDepth();

        if (this.rounds == 1 && this.ai == 1) {
            // AI先下，首子天元
            int centerX = this.cols / 2;
            int centerY = this.rows / 2;
            return new Point(centerX, centerY, this.ai);
        }

        // 基于普通方式获取最佳棋位
        if (this.aiConfig.getDepth() < 2) {
            return getBestPoint(point);
        }

        if (this.aiConfig.getDepth() > 4 && this.rounds < 4) {
            // 当AI级别大于4时，将前三个回合的搜索深度设置为4
            depth = 4;
        }

        // 算杀模式
        int vcx = aiConfig.getVcx();
        if (vcx > 0) {
            // 算杀最大深度
            int vcxDepth = aiConfig.getVcxDepth();
            long vcxStartTime = System.currentTimeMillis();
            // VCT/VCF
            this.bestPoint = deepeningVcx(true, vcxDepth, vcx == 2);
            long vcxEndTime = System.currentTimeMillis();
            this.statistics.setVcxTime((vcxEndTime - vcxStartTime) / 1000.00d);
        }

        if (this.bestPoint == null) {
            this.statistics.setNodes(0);
            this.statistics.setCacheHits(0);
            long minimaxStartTime = System.currentTimeMillis();
            // 基于极大极小值搜索获取最佳棋位
            this.bestPoint = deepeningMinimax(2, depth);
            long minimaxEndTime = System.currentTimeMillis();
            this.statistics.setMinimaxTime((minimaxEndTime - minimaxStartTime) / 1000.00d);
        }

        this.statistics.setPoint(this.bestPoint);
        this.statistics.setScore(this.bestPoint.score);
        this.statistics.setCaches(situationCacheMap.size());
        if (this.aiConfig.isDebug()) {
            TouhouLittleMaid.LOGGER.debug("============AI 统计[第" + this.rounds + "回合]==========");
            TouhouLittleMaid.LOGGER.debug("搜索深度：" + this.statistics.getDepth());
            TouhouLittleMaid.LOGGER.debug("搜索节点数：" + this.statistics.getNodes());
            TouhouLittleMaid.LOGGER.debug("发生剪枝数：" + this.statistics.getCuts());
            TouhouLittleMaid.LOGGER.debug("缓存总数：" + this.statistics.getCaches());
            TouhouLittleMaid.LOGGER.debug("缓存命中数：" + this.statistics.getCacheHits());
            if (vcx > 0) {
                TouhouLittleMaid.LOGGER.debug("算杀深度：" + this.statistics.getVcxDepth());
                TouhouLittleMaid.LOGGER.debug("算杀命中：" + (this.statistics.getVcx() == 1 ? "VCF" : this.statistics.getVcx() == 2 ? "VCT" : "未命中"));
            }
            TouhouLittleMaid.LOGGER.debug("最佳落子点：" + this.statistics.getPoint());
            TouhouLittleMaid.LOGGER.debug("得分：" + this.statistics.getScore());
            double time = this.statistics.getMinimaxTime() + this.statistics.getVcxTime();
            TouhouLittleMaid.LOGGER.debug("耗时：" + String.format("%.3f", time) + "s" + (vcx > 0 ? ", VCX(" + this.statistics.getVcxTime() + "s)" : "") + ", MINIMAX(" + this.statistics.getMinimaxTime() + "s)");
            TouhouLittleMaid.LOGGER.debug("==================================");
        }
        this.situationCacheMap = null;

        return this.bestPoint;
    }

    @Override
    public Statue getStatue(int[][] chessData, Point point) {
        int rows = chessData[0].length;
        int cols = chessData.length;
        int x = point.x;
        int y = point.y;
        int type = point.type;

        // 横轴
        int k = 1;
        for (int i = 1; i < 5; i++) {
            int preX = x - i;
            if (preX < 0) {
                break;
            }
            if (chessData[preX][y] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }
        for (int i = 1; i < 5; i++) {
            int nextX = x + i;
            if (nextX > rows - 1) {
                break;
            }
            if (chessData[nextX][y] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        // 纵轴
        k = 1;
        for (int i = 1; i < 5; i++) {
            int preY = y - i;
            if (preY < 0) {
                break;
            }
            if (chessData[x][preY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }
        for (int i = 1; i < 5; i++) {
            int nextY = y + i;
            if (nextY > cols - 1) {
                break;
            }
            if (chessData[x][nextY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        // 左对角线
        k = 1;
        for (int i = 1; i < 5; i++) {
            int preX = x - i;
            int preY = y - i;
            if (preX < 0 || preY < 0) {
                break;
            }
            if (chessData[preX][preY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }
        for (int i = 1; i < 5; i++) {
            int nextX = x + i;
            int nextY = y + i;
            if (nextX > rows - 1 || nextY > cols - 1) {
                break;
            }
            if (chessData[nextX][nextY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        // 右对角线
        k = 1;
        for (int i = 1; i < 5; i++) {
            int nextX = x + i;
            int preY = y - i;
            if (nextX > rows - 1 || preY < 0) {
                break;
            }
            if (chessData[nextX][preY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }
        for (int i = 1; i < 5; i++) {
            int preX = x - i;
            int nextY = y + i;
            if (preX < 0 || nextY > cols - 1) {
                break;
            }
            if (chessData[preX][nextY] != type) {
                break;
            }
            if (++k == 5) {
                return Statue.WIN;
            }
        }

        return Statue.IN_PROGRESS;
    }

    /**
     * 初始化棋盘数据
     *
     * @param chessData 当前棋盘数据
     */
    private void initChessData(int[][] chessData) {
        this.rows = chessData.length;
        this.cols = chessData[0].length;
        this.chessData = new int[this.cols][this.rows];
        int chessTotal = 0;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int type = chessData[i][j];
                if (type != 0) {
                    putChess(new Point(i, j, type));
                    chessTotal++;
                }
            }
        }

        this.rounds = chessTotal / 2 + 1;
    }

    /**
     * 迭代加深VCX
     *
     * @param isAi     是否是AI
     * @param maxDepth 最大深度
     * @param isVcf    true:VCF false:VCT
     * @return
     */
    private Point deepeningVcx(boolean isAi, int maxDepth, boolean isVcf) {
        this.ai = isAi ? this.ai : 3 - this.ai;
        Point point = deepening(1, maxDepth, isVcf);
        if (!isAi) {
            this.ai = 3 - this.ai;
            if (point != null) {
                point.type = this.ai;
            }
        }

        return point;
    }

    /**
     * 迭代加深算杀搜索
     *
     * @param depth    当前搜索深度
     * @param maxDepth 最大搜索深度
     * @param isVcf    true:VCF false:VCT
     * @return
     */
    private Point deepening(int depth, int maxDepth, boolean isVcf) {
        this.situationCacheMap = new HashMap<>(2048);
        Point point = null;
        for (; depth <= maxDepth; depth += 2) {
            this.statistics.setVcxDepth(depth);
            if (this.aiConfig.isDebug()) {
                this.pathStack = new Stack<>();
            }

            point = vcx(0, depth, isVcf);
            if (point != null) {
                if (this.aiConfig.isDebug()) {
                    StringBuilder pathOut = new StringBuilder();
                    pathOut.append(isVcf ? "VCF" : "VCT").append("路径：");
                    this.bestPathStack.forEach(p -> pathOut.append(p).append(" "));
                    //TouhouLittleMaid.LOGGER.debug(pathOut.toString());
                }

                // 算杀成功
                this.statistics.setVcx(isVcf ? 1 : 2);
                break;
            }
        }

        return point;
    }

    /**
     * 算杀（VCF、VCT）
     *
     * @param type  当前走棋方 1.AI 2.玩家
     * @param depth 搜索深度
     * @param isVcf true:VCF false:VCT
     * @return
     */
    private Point vcx(int type, int depth, boolean isVcf) {
        // 先查看当前局面是否存在缓存
        SituationCache situationCache = this.situationCacheMap.get(this.hashcode);
        if (situationCache != null && situationCache.depth >= depth) {
            if (this.aiConfig.isDebug()) {
                this.statistics.incrCacheHits();
            }

            // 缓存存在，且记录的搜索深度比当前深度要深或相等，则直接返回记录的局面点位
            return situationCache.point;
        }

        if (depth == 0) {
            // 算杀失败
            return null;
        }

        boolean isRoot = type == 0;
        if (isRoot) {
            type = this.ai;
        }
        boolean isAI = type == this.ai;

        Point best = null;
        List<Point> pointList = getVcxPoints(type, isVcf);
        for (Point point : pointList) {
            if (this.aiConfig.isDebug()) {
                this.statistics.incrNodes();
                this.pathStack.push(point);
            }

            if (point.score >= RiskScore.HIGH_RISK.score) {
                if (this.aiConfig.isDebug()) {
                    if (isAI) {
                        this.bestPathStack = (Stack<Point>) this.pathStack.clone();
                    }
                    this.pathStack.pop();
                }

                // 已经可以形成必胜棋型了，如果是AI落子，就返回当前节点，否则返回空
                return isAI ? point : null;
            }

            putChess(point);
            best = vcx(3 - type, depth - 1, isVcf);
            revokeChess(point);

            if (this.aiConfig.isDebug()) {
                this.pathStack.pop();
            }

            if (best == null) {
                if (isAI) {
                    // AI还没找到可以算杀成功的棋子，继续找...
                    continue;
                }

                // 对手拦截成功了，直接返回空出去，表示算杀失败了
                return null;
            }

            // 记录当前节点
            best = point;

            if (isAI) {
                // AI已经找到可以算杀的棋子了，同层后续的节点都不用看了
                break;
            }
        }

        // 缓存当前局面点位
        this.situationCacheMap.put(this.hashcode, new SituationCache(best, depth));

        return best;
    }

    /**
     * 获取算杀落子点
     *
     * @param type  当前走棋方 0.根节点表示AI走棋 1.黑棋 2.白棋
     * @param isVcf 是否是连续冲四
     * @return
     */
    private List<Point> getVcxPoints(int type, boolean isVcf) {
        boolean isAI = type == this.ai;
        // 进攻点列表
        List<Point> attackPointList = new ArrayList<>();
        // 防守点列表
        List<Point> defensePointList = new ArrayList<>();
        // VCX列表
        List<Point> vcxPointList = new ArrayList<>();

        // 局势是否危险
        boolean isDanger = false;
        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                if (this.chessData[i][j] != 0) {
                    // 该处已有棋子，跳过
                    continue;
                }

                // 考虑自己的落子情况
                Point point = new Point(i, j, type);
                int score = evaluate(point);
                if (score >= ChessModel.LIANWU.score) {
                    // 自己可以连五，直接返回
                    return Collections.singletonList(point);
                }

                if (isDanger) {
                    // 存在危险，继续找自己可以连五的棋子
                    continue;
                }

                // 考虑对手的落子情况
                Point foePoint = new Point(i, j, 3 - type);
                int foeScore = evaluate(foePoint);
                if (foeScore >= ChessModel.LIANWU.score) {
                    // 对手连五了，局势很危险！！赶紧找自己可以连五的点位，不行就防守
                    isDanger = true;
                    defensePointList.clear();
                    defensePointList.add(point);
                    continue;
                }

                // 看看自己有没有大于等于中风险分值的点位
                if (score >= RiskScore.MEDIUM_RISK.score) {
                    attackPointList.add(point);
                    continue;
                }

                if (isAI) {
                    // AI才进行VCX
                    if (checkSituation(point, ChessModel.CHONGSI)) {
                        // 不论是VCF还是VCT，AI都可先选择冲四
                        vcxPointList.add(point);
                    } else if (!isVcf && checkSituation(point, ChessModel.HUOSAN)) {
                        // 记录VCT点位
                        vcxPointList.add(point);
                    }
                } else {
                    // 对手需防守VCT
                    if (!isVcf) {
                        if (checkSituation(point, ChessModel.CHONGSI) || foeScore >= ChessModel.HUOSI.score) {
                            // 选择冲四或防守对方的活四
                            defensePointList.add(point);
                        }
                    }
                }
            }
        }

        List<Point> pointList = new ArrayList<>();
        // 没风险就进攻
        if (!isDanger) {
            // 优先强进攻
            if (!attackPointList.isEmpty()) {
                // 按分数从大到小排序
                attackPointList.sort((p1, p2) -> {
                    if (p1.score == p2.score) {
                        return 0;
                    } else if (p1.score > p2.score) {
                        return -1;
                    }
                    return 1;
                });

                if (isAI) {
                    // AI有强进攻点位了，就不用考虑后面的点位了
                    return attackPointList;
                }

                // 对手可以选择进攻和防守
                pointList.addAll(attackPointList);
            }

            // VCX进攻
            if (!vcxPointList.isEmpty()) {
                pointList.addAll(vcxPointList);
            }
        }

        if (!defensePointList.isEmpty()) {
            // 进行防守
            if (isAI) {
                // AI优先选择进攻，把防守点位放后面
                pointList.addAll(defensePointList);
            } else {
                // 对手优先考虑防守，把防守点位放前面
                pointList.addAll(0, defensePointList);
            }
        }

        return pointList;
    }

    /**
     * 下棋子
     *
     * @param point 棋子
     */
    private void putChess(Point point) {
        this.chessData[point.x][point.y] = point.type;
        // 计算当前局面的hashcode
        calculateHashCode(point);
    }

    /**
     * 撤销下的棋子
     *
     * @param point 棋子
     */
    private void revokeChess(Point point) {
        this.chessData[point.x][point.y] = 0;
        // 计算当前局面的hashcode
        calculateHashCode(point);
    }

    /**
     * 计算当前落子局面的hashcode
     *
     * @param point
     * @return
     */
    private long calculateHashCode(Point point) {
        int x = point.x;
        int y = point.y;
        this.hashcode ^= point.type == 1 ? BLACK_ZOBRIST[x][y] : WHITE_ZOBRIST[x][y];
        return this.hashcode;
    }

    /**
     * 获取最佳下棋点位（只考虑一步）
     *
     * @param point 对手下棋点位
     * @return
     */
    private Point getBestPoint(Point point) {
        Point best = null;
        // 初始分值为最小
        int score = -INFINITY;

        /* 遍历所有能下棋的点位，评估各个点位的分值，选择分值最大的点位 */
        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                if (this.chessData[i][j] != 0) {
                    // 该点已有棋子，跳过
                    continue;
                }

                Point p = new Point(i, j, this.ai);
                // 该点得分 = AI落子得分 * 进攻系数 + 对手落子得分
                int val = Math.round(evaluate(p) * this.attack) + evaluate(new Point(i, j, 3 - this.ai));
                // 选择得分最高的点位
                if (val > score) {
                    // 最高分被刷新
                    score = val;
                    // 更新最佳点位
                    best = p;
                }
            }
        }

        return best;
    }

    /**
     * 极大极小值搜索
     *
     * @param type  当前走棋方 0.根节点表示AI走棋 1.AI 2.玩家
     * @param depth 搜索深度
     * @return
     */
    private int minimax(int type, int depth) {
        // 是否是根节点
        boolean isRoot = type == 0;
        if (isRoot) {
            // 根节点是AI走棋
            type = this.ai;
        }

        // 当前是否是AI走棋
        boolean isAI = type == this.ai;
        // 当前分值，
        int score;
        if (isAI) {
            // AI因为要选择最高分，所以初始化一个难以到达的低分
            score = -INFINITY;
        } else {
            // 对手要选择最低分，所以初始化一个难以到达的高分
            score = INFINITY;
        }

        // 到达叶子结点
        if (depth == 0) {
            /*
             * 评估每棵博弈树的叶子结点的局势
             * 比如：depth=2时，表示从AI开始走两步棋之后的局势评估，AI(走第一步) -> 玩家(走第二步)，然后对局势进行评估
             * 注意：局势评估是以AI角度进行的，分值越大对AI越有利，对玩家越不利
             */
            return evaluateAll();
        }

        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                if (this.chessData[i][j] != 0) {
                    // 该处已有棋子，跳过
                    continue;
                }

                /* 模拟 AI -> 玩家 交替落子 */
                Point p = new Point(i, j, type);
                // 落子
                putChess(p);
                // 递归生成博弈树，并评估叶子结点的局势获取分值
                int curScore = minimax(3 - type, depth - 1);
                // 撤销落子
                revokeChess(p);

                if (isAI) {
                    // AI要选对自己最有利的节点（分最高的）
                    if (curScore > score) {
                        // 最高值被刷新
                        score = curScore;
                        if (isRoot) {
                            // 根节点处更新AI最好的棋位
                            this.bestPoint = p;
                        }
                    }
                } else {
                    // 对手要选对AI最不利的节点（分最低的）
                    if (curScore < score) {
                        // 最低值被刷新
                        score = curScore;
                    }
                }
            }
        }

        return score;
    }

    /**
     * 极大极小值搜索、AlphaBeta剪枝
     *
     * @param type  当前走棋方 0.根节点表示AI走棋 1.AI 2.玩家
     * @param depth 搜索深度
     * @param alpha 极大值
     * @param beta  极小值
     * @return
     */
    private int minimax(int type, int depth, int alpha, int beta) {
        // 是否是根节点
        boolean isRoot = type == 0;
        if (isRoot) {
            // 根节点是AI走棋
            type = this.ai;
        }

        // 当前是否是AI走棋
        boolean isAI = type == this.ai;

        // 先查看当前局面是否存在缓存
        SituationCache situationCache = this.situationCacheMap.get(this.hashcode);
        if (situationCache != null && situationCache.depth >= depth) {
            if (this.aiConfig.isDebug()) {
                this.statistics.incrCacheHits();
            }

            // 缓存存在，且记录的搜索深度比当前深度要深或相等，则直接返回记录的分值
            return situationCache.score;
        }

        // 到达叶子结点
        if (depth == 0) {
            /**
             * 评估每棵博弈树的叶子结点的局势
             * 比如：depth=2时，表示从AI开始走两步棋之后的局势评估，AI(走第一步) -> 玩家(走第二步)，然后对局势进行评估
             * 注意：局势评估是以AI角度进行的，分值越大对AI越有利，对玩家越不利
             */
            return evaluateAll();
        }

        // 启发式搜索
        List<Point> pointList = getHeuristicPoints(type);
        if (isRoot && pointList.size() == 1) {
            if (this.aiConfig.isDebug()) {
                this.statistics.incrNodes();
            }

            // 只有一个落子点，直接返回就好
            this.bestPoint = pointList.get(0);
            return this.bestPoint.score;
        }

        // 记录选择的最好落子点
        List<Point> bestPointList = new ArrayList<>();
        for (Point point : pointList) {
            if (this.aiConfig.isDebug()) {
                this.statistics.incrNodes();
            }

            if (point.score >= ChessModel.LIANWU.score) {
                // 落子到这里就赢了，如果是AI落的子就返回最高分，否则返回最低分
                point.score = isAI ? INFINITY - 1 : -INFINITY + 1;
            } else {
                /* 模拟 AI -> 玩家 交替落子 */
                // 落子
                putChess(point);
                // 递归生成博弈树，并评估叶子结点的局势
                point.score = minimax(3 - type, depth - 1, alpha, beta);
                // 撤销落子
                revokeChess(point);
            }

            if (isAI) {
                // AI要选对自己最有利的节点（分最高的）
                if (point.score >= alpha) {
                    if (isRoot) {
                        if (point.score > alpha) {
                            // 找到了更好的落子点，将之前选择的落子点清空
                            bestPointList.clear();
                        }
                        // 记录该落子点
                        bestPointList.add(point);
                    }

                    // 最高值被刷新，更新alpha值
                    alpha = point.score;
                }
            } else {
                // 对手要选对AI最不利的节点（分最低的）
                if (point.score < beta) {
                    // 最低值被刷新，更新beta值
                    beta = point.score;
                }
            }

            if (alpha >= beta) {
                if (this.aiConfig.isDebug()) {
                    this.statistics.incrCuts();
                }

                /*
                 AlphaBeta剪枝

                 解释：
                 AI当前最大分数为：alpha 搜索区间 (alpha, +∞]
                 对手当前最小分数为：beta 搜索区间 [-∞, beta)

                 因为对手要选择分数小于beta的分支，AI要从对手给的分支里面选最大的分支，这个最大的分支要和当前的分支(alpha)做比较，
                 现在alpha都比beta大了，下面搜索给出的分支也都是小于alpha的，所以搜索下去没有意义，剪掉提高搜索效率。
                 */
                break;
            }
        }

        if (isRoot) {
            // 如果有多个落子点，则通过getBestPoint方法选择一个最好的
            this.bestPoint = bestPointList.size() > 1 ? getBestPoint(bestPointList) : bestPointList.get(0);
        }

        int score = isAI ? alpha : beta;
        // 缓存当前局面分值
        this.situationCacheMap.put(this.hashcode, new SituationCache(score, depth));

        return score;
    }

    /**
     * 启发式获取落子点位
     *
     * @param type 当前走棋方 1.黑棋 2.白棋
     * @return
     */
    private List<Point> getHeuristicPoints(int type) {
        // 落子点上限
        int max = this.aiConfig.getMaxNodes();
        // 高优先级落子点
        List<Point> highPriorityPointList = new ArrayList<>();
        // 低优先级落子点
        List<Point> lowPriorityPointList = new ArrayList<>();
        // 候补落子点
        List<Point> alternatePointList = new ArrayList<>();
        // 杀棋点
        List<Point> killPointList = new ArrayList<>();

        // 局势危险等级 0.不危险 1.有危险 2.很危险
        int dangerLevel = 0;
        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                if (this.chessData[i][j] != 0) {
                    // 该处已有棋子，跳过
                    continue;
                }

                // 考虑自己的落子情况
                Point point = new Point(i, j, type);
                int score = evaluate(point);
                if (score >= ChessModel.LIANWU.score) {
                    // 优先检查自己连五的情况，如果该落子点可以形成连五，则结束循环，直接返回
                    return Collections.singletonList(point);
                }

                if (dangerLevel == 2) {
                    // 局势很危险，只检查自己可以连五的落子点
                    continue;
                }

                if (score >= RiskScore.MEDIUM_RISK.score) {
                    // 必杀棋
                    killPointList.add(point);
                }

                // 考虑对手的落子情况
                Point foePoint = new Point(i, j, 3 - type);
                int foeScore = evaluate(foePoint);
                // 当前局势危险等级
                int level = 0;
                if (foeScore >= ChessModel.LIANWU.score) {
                    // 对手连五了，局势很危险！！
                    level = 2;
                } else if (foeScore >= RiskScore.MEDIUM_RISK.score) {
                    // 对手有活四、双冲四、冲四活三的点位了，局势有危险！
                    level = 1;
                }

                if (level > 0) {
                    // 当前局势存在危险
                    if (dangerLevel < level) {
                        // 危险升级
                        dangerLevel = level;
                        // 局势危险等级如果上升，则清空之前选择的高优先级节点，防止AI误入歧途
                        highPriorityPointList.clear();
                    }

                    // 将此节点加入到高优先级队列
                    highPriorityPointList.add(point);
                }

                if (dangerLevel > 0) {
                    // 局势有危险，下面的检查逻辑不用走了
                    continue;
                }

                if (RiskScore.between(score, RiskScore.LOW_RISK, RiskScore.MEDIUM_RISK) || RiskScore.between(foeScore, RiskScore.LOW_RISK, RiskScore.MEDIUM_RISK)) {
                    // 高优先级落子点：多活三，需考虑对手
                    highPriorityPointList.add(point);
                    continue;
                }

                if (highPriorityPointList.isEmpty()) {
                    if (score >= ChessModel.CHONGSI.score || foeScore >= ChessModel.CHONGSI.score) {
                        // 低优先级落子点：冲四、活三，需考虑对手
                        lowPriorityPointList.add(point);
                        continue;
                    }

                    if (lowPriorityPointList.isEmpty() && score >= ChessModel.MIANYI.score) {
                        // 候补落子点：活二、活一、眠三、眠二、眠一，不用考虑对手
                        alternatePointList.add(point);
                    }
                }
            }
        }

        if (dangerLevel < 2 && !killPointList.isEmpty()) {
            // 局势不是特别危险，且杀棋队列不为空，直接进攻就好
            return killPointList;
        }

        List<Point> pointList;
        if (highPriorityPointList.isEmpty()) {
            // 无高优先级落子点，则判断是否有低优先级落子点
            if (lowPriorityPointList.isEmpty()) {
                // 低优先级落子点也没有，就返回候补落子点
                if (alternatePointList.isEmpty()) {
                    // 候补落子点也没有，就随机取
                    return randomPoint(type, 1);
                }

                // 打乱一下
                Collections.shuffle(alternatePointList);
                // 返回打乱后的候补落子点
                pointList = alternatePointList;
            } else {
                // 返回低优先级落子点
                pointList = lowPriorityPointList;
            }
        } else {
            // 返回高优先级落子点
            pointList = highPriorityPointList;
        }

        // 按分数从大到小排序
        pointList.sort((p1, p2) -> {
            if (p1.score == p2.score) {
                return 0;
            } else if (p1.score > p2.score) {
                return -1;
            }
            return 1;
        });

        // 取最大节点个数
        return pointList.subList(0, Math.min(pointList.size(), max));
    }

    /**
     * 迭代加深 minimax 搜索
     *
     * @param depth    当前搜索深度
     * @param maxDepth 最大搜索深度
     * @return
     */
    private Point deepeningMinimax(int depth, int maxDepth) {
        this.situationCacheMap = new HashMap<>(2048);

        Point best = null;
        for (; depth <= maxDepth; depth += 2) {
            int score = minimax(0, depth, -INFINITY, INFINITY);
            best = this.bestPoint;
            this.statistics.setPoint(best);
            this.statistics.setDepth(depth);
            this.statistics.setScore(score);

            if (Math.abs(score) >= INFINITY - 1) {
                // 找到最优解了，结束搜索
                break;
            }
        }

        return best;
    }

    /**
     * 检查高优先级落子点
     *
     * @param point 检查的点位
     * @return
     */
    private boolean checkHighPriorityPoint(Point point) {
        // 活三数
        int huosanTotal = 0;
        // 冲四数
        int chongsiTotal = 0;
        // 活二数
        int huoerTotal = 0;

        for (int i = 1; i < 5; i++) {
            // 获取当前局势
            String situation = getSituation(point, i);
            // 获取当前局势的棋型
            ChessModel chessModel = getChessModel(situation);

            // 棋型统计
            if (chessModel != null) {
                switch (chessModel) {
                    case HUOSI:
                        return true;
                    case HUOSAN:
                        // 活三+1
                        huosanTotal++;
                        break;
                    case CHONGSI:
                        // 冲四+1
                        chongsiTotal++;
                        break;
                    case HUOER:
                        // 活二+1
                        huoerTotal++;
                        break;
                }
            }
        }

        if (chongsiTotal > 1 || (chongsiTotal > 0 && huosanTotal > 0)) {
            // 冲四数大于1、冲四又活三
            return true;
        }
        if (huosanTotal > 1) {
            // 活三数大于1
            return true;
        }
        if (huosanTotal > 0 && huoerTotal > 0) {
            // 活三又活二
            // return true;
        }

        return false;
    }

    /**
     * 从给定的点位列表中获取最佳点位
     *
     * @param pointList 点位列表
     * @return
     */
    private Point getBestPoint(List<Point> pointList) {
        Point bestPoint = null;
        int bestScore = -INFINITY;

        for (Point point : pointList) {
            int score = Math.round(evaluate(point) * this.attack) + evaluate(new Point(point.x, point.y, 3 - point.type));
            if (score > bestScore) {
                bestScore = score;
                bestPoint = point;
            }
        }

        return bestPoint;
    }

    /**
     * 随机获取落子点
     *
     * @param type 棋子类型
     * @param num  数量
     * @return
     */
    private List<Point> randomPoint(int type, int num) {
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                if (this.chessData[i][j] == 0) {
                    pointList.add(new Point(i, j, type));
                }
            }
        }

        Collections.shuffle(pointList);
        return pointList.subList(0, Math.min(num, pointList.size()));
    }

    /**
     * 对当前棋位进行评估
     *
     * @param point 当前棋位
     * @return
     */
    private int evaluate(Point point) {
        // 分值
        int score = 0;
        // 活三数
        int huosanTotal = 0;
        // 冲四数
        int chongsiTotal = 0;
        // 统计同一方向既冲四又活三的情况，出现这种情况的优先按活三分计算
        int tfTotal = 0;

        for (int i = 1; i < 5; i++) {
            // 获取当前局势
            String situation = getSituation(point, i);
            // 获取当前局势的棋型
            ChessModel chessModel = getChessModel(situation);

            // 棋型统计
            if (chessModel != null) {
                switch (chessModel) {
                    case HUOSAN:
                        // 活三+1
                        huosanTotal++;
                        if (checkSituation(situation, ChessModel.CHONGSI)) {
                            // 同一方向出现活三，又出现冲四
                            tfTotal++;
                        }
                        break;
                    case CHONGSI:
                        // 冲四+1
                        chongsiTotal++;
                        break;
                }

                // 下此步的得分
                score += chessModel.score;
            }
        }

        if (chongsiTotal > 1 || tfTotal > 1) {
            // 冲四数大于1，+高风险评分
            score += RiskScore.HIGH_RISK.score;
        } else if (chongsiTotal > 0 && huosanTotal > 0 || tfTotal > 0 && huosanTotal > 1) {
            // 冲四又活三，+中风险评分
            score += RiskScore.MEDIUM_RISK.score;
        } else if (huosanTotal > 1) {
            // 活三数大于1，+低风险评分
            score += RiskScore.LOW_RISK.score;
        }

        point.score = score;
        return score;
    }

    /**
     * 以AI角度对当前局势进行评估，分数越大对AI越有利
     *
     * @return
     */
    private int evaluateAll() {
        // AI得分
        int aiScore = 0;
        // 对手得分
        int foeScore = 0;

        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                int type = this.chessData[i][j];
                if (type == 0) {
                    // 该点没有棋子，跳过
                    continue;
                }

                // 评估该棋位分值
                int val = evaluate(new Point(i, j, type));
                if (type == this.ai) {
                    // 累积AI得分
                    aiScore += val;
                } else {
                    // 累积对手得分
                    foeScore += val;
                }
            }
        }

        // 该局AI最终得分 = AI得分 * 进攻系数 - 对手得分
        return Math.round(aiScore * this.attack) - foeScore;
    }

    /**
     * 检查当前落子是否处于某一局势
     *
     * @param point       当前棋位
     * @param chessModels 检查的局势
     * @return
     */
    private boolean checkSituation(Point point, ChessModel... chessModels) {
        // 要检查4个大方向
        for (int i = 1; i < 5; i++) {
            String situation = getSituation(point, i);
            for (ChessModel chessModel : chessModels) {
                if (checkSituation(situation, chessModel)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查当前局势是否处于某个局势
     *
     * @param situation  当前局势
     * @param chessModel 检查的局势
     * @return
     */
    private boolean checkSituation(String situation, ChessModel chessModel) {
        for (String value : chessModel.values) {
            if (situation.contains(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前局势的棋型（按顺序匹配）
     * <p>
     * 如当前局势棋型为："210111002"（同时包含活三和冲四的棋型）
     * 该方法会优先匹配到活三 "011100"，然后返回该棋型
     * 满足冲四 "10111" 棋型，但由于顺序问题，将不会返回
     *
     * @param situation 当前局势
     * @return
     */
    private ChessModel getChessModel(String situation) {
        for (ChessModel chessModel : ChessModel.values()) {
            for (String value : chessModel.values) {
                if (situation.contains(value)) {
                    return chessModel;
                }
            }
        }

        return null;
    }

    /**
     * 获取局势分数
     *
     * @param situation 局势
     * @return
     */
    private int getScore(String situation) {
        for (String key : SCORE.keySet()) {
            if (situation.contains(key)) {
                return SCORE.get(key);
            }
        }
        return 0;
    }

    /**
     * 获取棋位局势
     *
     * @param point     当前棋位
     * @param direction 大方向 1.横 2.纵 3.左斜 4.右斜
     * @return
     */
    private String getSituation(Point point, int direction) {
        // 下面用到了 relativePoint 函数，根据传入的四个大方向做转换
        direction = direction * 2 - 1;
        // 以下是将各个方向的棋子拼接成字符串返回
        StringBuilder sb = new StringBuilder();
        appendChess(sb, point, direction, 4);
        appendChess(sb, point, direction, 3);
        appendChess(sb, point, direction, 2);
        appendChess(sb, point, direction, 1);
        sb.append(1); // 当前棋子统一标记为1(黑)
        appendChess(sb, point, direction + 1, 1);
        appendChess(sb, point, direction + 1, 2);
        appendChess(sb, point, direction + 1, 3);
        appendChess(sb, point, direction + 1, 4);
        return sb.toString();
    }

    /**
     * 拼接各个方向的棋子
     * <p>
     * 由于现有评估模型是对黑棋进行评估
     * 所以，为了方便对局势进行评估，如果当前是白棋方，需要将扫描到的白棋转换为黑棋，黑棋转换为白棋
     * 如：point(x=0,y=0,type=2) 即当前为白棋方
     * 扫描到的某个方向局势为：20212 -> 转换后 -> 10121
     *
     * @param sb        字符串容器
     * @param point     当前棋子
     * @param direction 方向 1.左横 2.右横 3.上纵 4.下纵  5.左斜上 6.左斜下 7.右斜上 8.右斜下
     * @param offset    偏移量
     */
    private void appendChess(StringBuilder sb, Point point, int direction, int offset) {
        int chess = relativePoint(point, direction, offset);
        if (chess > -1) {
            if (point.type == 2) {
                // 对白棋进行转换
                if (chess > 0) {
                    // 对棋子颜色进行转换，2->1，1->2
                    chess = 3 - chess;
                }
            }
            sb.append(chess);
        }
    }

    /**
     * 获取相对点位棋子
     *
     * @param point     当前棋位
     * @param direction 方向 1.左横 2.右横 3.上纵 4.下纵  5.左斜上 6.左斜下 7.右斜上 8.右斜下
     * @param offset    偏移量
     * @return -1:越界 0:空位 1:黑棋 2:白棋
     */
    private int relativePoint(Point point, int direction, int offset) {
        int x = point.x, y = point.y;
        switch (direction) {
            case 1:
                x -= offset;
                break;
            case 2:
                x += offset;
                break;
            case 3:
                y -= offset;
                break;
            case 4:
                y += offset;
                break;
            case 5:
                x += offset;
                y -= offset;
                break;
            case 6:
                x -= offset;
                y += offset;
                break;
            case 7:
                x -= offset;
                y -= offset;
                break;
            case 8:
                x += offset;
                y += offset;
                break;
        }

        if (x < 0 || y < 0 || x >= this.cols || y >= this.rows) {
            // 越界
            return -1;
        }

        // 返回该位置的棋子
        return this.chessData[x][y];
    }

    private enum ChessModel {
        /**
         * 连五
         */
        LIANWU(10000000, new String[]{"11111"}),
        /**
         * 活四
         */
        HUOSI(1000000, new String[]{"011110"}),
        /**
         * 活三
         */
        HUOSAN(10000, new String[]{"001110", "011100", "010110", "011010"}),
        /**
         * 冲四
         */
        CHONGSI(9000, new String[]{"11110", "01111", "10111", "11011", "11101"}),
        /**
         * 活二
         */
        HUOER(100, new String[]{"001100", "011000", "000110", "001010", "010100"}),
        /**
         * 活一
         */
        HUOYI(80, new String[]{"010200", "002010", "020100", "001020", "201000", "000102", "000201"}),
        /**
         * 眠三
         */
        MIANSAN(30, new String[]{"001112", "010112", "011012", "211100", "211010"}),
        /**
         * 眠二
         */
        MIANER(10, new String[]{"011200", "001120", "002110", "021100", "110000", "000011", "000112", "211000"}),
        /**
         * 眠一
         */
        MIANYI(1, new String[]{"001200", "002100", "000210", "000120", "210000", "000012"});

        /**
         * 分数
         */
        final int score;
        /**
         * 局势数组
         */
        final String[] values;

        ChessModel(int score, String[] values) {
            this.score = score;
            this.values = values;
        }
    }

    /**
     * 风险评分
     */
    private enum RiskScore {
        /**
         * 高风险
         */
        HIGH_RISK(800000),
        /**
         * 中风险
         */
        MEDIUM_RISK(500000),
        /**
         * 低风险
         */
        LOW_RISK(100000);

        final int score;

        RiskScore(int score) {
            this.score = score;
        }

        /**
         * 判断分数是否处于区间 [leftScore, rightScore)
         *
         * @param score      分数
         * @param leftScore  左区分值
         * @param rightScore 右区分值
         * @return
         */
        public static boolean between(int score, RiskScore leftScore, RiskScore rightScore) {
            return score >= leftScore.score && score < rightScore.score;
        }
    }

    /**
     * 统计
     */
    private static class Statistics {
        /**
         * 搜索深度
         */
        private int depth;
        /**
         * 最佳点位
         */
        private Point point;
        /**
         * 分数
         */
        private int score;
        /**
         * 极大极小搜索耗时（秒）
         */
        private double minimaxTime;
        /**
         * 搜索节点数
         */
        private int nodes;
        /**
         * 剪枝数
         */
        private int cuts;
        /**
         * 算杀深度
         */
        private int vcxDepth;
        /**
         * 算杀命中 0.未命中 1.vcf 2.vct
         */
        private int vcx;
        /**
         * 算杀耗时（秒）
         */
        private double vcxTime;
        /**
         * 局面缓存数
         */
        private int caches;
        /**
         * 局面缓存命中数
         */
        private int cacheHits;

        public void incrNodes() {
            this.nodes++;
        }

        public void incrCuts() {
            this.cuts++;
        }

        public void incrCacheHits() {
            this.cacheHits++;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public double getMinimaxTime() {
            return minimaxTime;
        }

        public void setMinimaxTime(double minimaxTime) {
            this.minimaxTime = minimaxTime;
        }

        public int getNodes() {
            return nodes;
        }

        public void setNodes(int nodes) {
            this.nodes = nodes;
        }

        public int getCuts() {
            return cuts;
        }

        public void setCuts(int cuts) {
            this.cuts = cuts;
        }

        public int getVcxDepth() {
            return vcxDepth;
        }

        public void setVcxDepth(int vcxDepth) {
            this.vcxDepth = vcxDepth;
        }

        public int getVcx() {
            return vcx;
        }

        public void setVcx(int vcx) {
            this.vcx = vcx;
        }

        public double getVcxTime() {
            return vcxTime;
        }

        public void setVcxTime(double vcxTime) {
            this.vcxTime = vcxTime;
        }

        public int getCaches() {
            return caches;
        }

        public void setCaches(int caches) {
            this.caches = caches;
        }

        public int getCacheHits() {
            return cacheHits;
        }

        public void setCacheHits(int cacheHits) {
            this.cacheHits = cacheHits;
        }
    }

    /**
     * 局面缓存
     */
    private static class SituationCache {
        /**
         * 搜索深度
         */
        private final int depth;
        /**
         * 存储VCX点位
         */
        private Point point;
        /**
         * 局面分数
         */
        private int score;

        public SituationCache(int score, int depth) {
            this.score = score;
            this.depth = depth;
        }

        public SituationCache(Point point, int depth) {
            this.point = point;
            this.depth = depth;
        }
    }
}
