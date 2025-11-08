// java
package com.github.tartaricacid.touhoulittlemaid.api.game.gomoku;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 将五子棋棋局编码的工具类，方便数据包填写
 * <p>
 * 格式说明：
 * 1) 棋盘部分采用紧凑二进制表示：先是 15x15 位的黑子位图，再是 15x15 位的白子位图；
 * 每位表示一个格子（按行优先，从上到下、从左到右），位为 1 表示该颜色有子，0 表示没有子。
 * 2) 对上述字节数组先使用 Deflate 算法压缩后，再通过 Base64 URL 无填充编码得到字符串。
 * 3) 最终字符串为：<base64>, turnCount, x_y
 */
public final class GomokuCodec {
    private GomokuCodec() {
    }

    public static String encode(StateData stateData) {
        byte[][] board = stateData.board();
        int turnCount = stateData.turnCount();
        Point latestPoint = stateData.latestPoint();

        // 将棋盘数据编码为位图
        byte[] data = new byte[2 * 15 * 15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int type = board[i][j];
                int pos = i * 15 + j;
                if (type == Point.BLACK) {
                    data[pos] = 1;
                } else if (type == Point.WHITE) {
                    data[15 * 15 + pos] = 1;
                }
            }
        }

        // 压缩
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, false);
        deflater.setInput(data);
        deflater.finish();

        // 输出压缩结果
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            while (!deflater.finished()) {
                int len = deflater.deflate(buf);
                if (len > 0) {
                    out.write(buf, 0, len);
                }
            }
            byte[] compressed = out.toByteArray();
            String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(compressed);
            return payload + ", " + turnCount + ", " + latestPoint.x + "_" + latestPoint.y;
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode gomoku state", e);
        } finally {
            deflater.end();
        }
    }

    public static StateData decode(String dataStr) {
        // 去掉空白方便解析
        String clean = StringUtils.deleteWhitespace(dataStr);
        String[] parts = clean.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Data must be in format <base64>, turnCount, x_y");
        }

        // 解压缩
        byte[] compressed = Base64.getUrlDecoder().decode(parts[0]);
        Inflater inflater = new Inflater();
        inflater.setInput(compressed);
        byte[] data = new byte[2 * 15 * 15];
        try {
            int resultLength = inflater.inflate(data);
            if (resultLength != data.length) {
                throw new IllegalArgumentException("Decompressed data length mismatch");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decompress data", e);
        } finally {
            inflater.end();
        }

        // 解析棋盘
        byte[][] board = new byte[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int pos = i * 15 + j;
                if (data[pos] == 1) {
                    board[i][j] = Point.BLACK;
                } else if (data[15 * 15 + pos] == 1) {
                    board[i][j] = Point.WHITE;
                } else {
                    board[i][j] = Point.EMPTY;
                }
            }
        }

        int turnCount = Integer.parseInt(parts[1]);
        String[] pointParts = parts[2].split("_");
        if (pointParts.length != 2) {
            throw new IllegalArgumentException("Latest point must be in format x_y");
        }
        int x = Integer.parseInt(pointParts[0]);
        int y = Integer.parseInt(pointParts[1]);
        int type = board[x][y];
        Point latestPoint = new Point(x, y, type);

        return new StateData(board, turnCount, latestPoint);
    }

    public record StateData(byte[][] board, int turnCount, Point latestPoint) {
    }
}