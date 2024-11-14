/*
Position.java - Source Code for Mobile Chess, Part I

Mobile Chess - a Chess Program for Java ME
Designed by Morning Yellow, Version: 1.05, Last Modified: Mar. 2008
Copyright (C) 2008 mobilechess.sourceforge.net

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package com.github.tartaricacid.touhoulittlemaid.api.game.chess;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;

@SuppressWarnings("all")
public class Position {
    public static final int MATE_VALUE = 10000;
    public static final int WIN_VALUE = MATE_VALUE - 100;
    public static final int NULL_SAFE_MARGIN = 1000;
    public static final int NULL_OKAY_MARGIN = 500;
    public static final int DRAW_VALUE = 50;
    public static final int ADVANCED_VALUE = 10;

    public static final int MAX_MOVE_NUM = 256;
    public static final int MAX_GEN_MOVES = 128;
    public static final int MAX_BOOK_SIZE = 16384;

    public static final int PIECE_KING = 0;
    public static final int PIECE_QUEEN = 1;
    public static final int PIECE_ROOK = 2;
    public static final int PIECE_BISHOP = 3;
    public static final int PIECE_KNIGHT = 4;
    public static final int PIECE_PAWN = 5;

    public static final int DIFF_LINE = 0;
    public static final int SAME_RANK = 1;
    public static final int SAME_FILE = 2;
    public static final int SAME_DIAG_A1H8 = 3;
    public static final int SAME_DIAG_A8H1 = 4;

    public static final int RANK_TOP = 0;
    public static final int RANK_BOTTOM = 7;
    public static final int FILE_LEFT = 4;
    public static final int FILE_RIGHT = 11;

    public static final byte[] LEGAL_SPAN = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0,
    };

    public static final byte[] SAME_LINE = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            4, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 3, 0,
            0, 4, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 3, 0, 0,
            0, 0, 4, 0, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0,
            0, 0, 0, 4, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 0,
            0, 0, 0, 0, 4, 0, 0, 2, 0, 0, 3, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 4, 0, 2, 0, 3, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 4, 2, 3, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0,
            0, 0, 0, 0, 0, 0, 3, 2, 4, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 3, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 3, 0, 0, 2, 0, 0, 4, 0, 0, 0, 0, 0,
            0, 0, 0, 3, 0, 0, 0, 2, 0, 0, 0, 4, 0, 0, 0, 0,
            0, 0, 3, 0, 0, 0, 0, 2, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 3, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 4, 0, 0,
            3, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 4, 0,
            0, 0, 0, 0, 0, 0, 0,
    };

    public static final byte[] PAWN_LINE = {
            0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0,
            0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0,
            0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0,
    };

    public static final int[] KING_DELTA = {-17, -16, -15, -1, 1, 15, 16, 17};
    public static final int[] ROOK_DELTA = {-16, -1, 1, 16};
    public static final int[] BISHOP_DELTA = {-17, -15, 15, 17};
    public static final int[] KNIGHT_DELTA = {-33, -31, -18, -14, 14, 18, 31, 33};
    public static final int[] MMV_VALUE = {0, 900, 500, 300, 300, 100};
    public static final int[] CASTLING_DIRECTION = {1, -1, 1, -1};
    public static final int[] CASTLING_KING_SRC = {0x78, 0x78, 0x08, 0x08};
    public static final int[] CASTLING_ROOK_DST = {0x79, 0x77, 0x09, 0x07};
    public static final int[] CASTLING_KING_DST = {0x7a, 0x76, 0x0a, 0x06};
    public static final int[] CASTLING_ROOK_SRC = {0x7b, 0x74, 0x0b, 0x04};

    public static final String PIECE_STRING = "KQRBNP";

    public static final String[] STARTUP_FEN = {
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R1BQKBNR w KQkq - 0 1",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1NBQKBNR w KQkq - 0 1",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNB1KBNR w KQkq - 0 1",
    };

    public static boolean IN_BOARD(int sq) {
        return ((sq - 4) & 0x88) == 0;
    }

    public static int RANK_Y(int sq) {
        return sq >> 4;
    }

    public static int FILE_X(int sq) {
        return sq & 15;
    }

    public static int COORD_XY(int x, int y) {
        return x + (y << 4);
    }

    public static int SQUARE_FLIP(int sq) {
        return 127 - sq;
    }

    public static int SQUARE_FORWARD(int sq, int sd) {
        return sq - 16 + (sd << 5);
    }

    public static int FORWARD_DELTA(int sd) {
        return (sd << 5) - 16;
    }

    public static boolean PAWN_INIT(int sq, int sd) {
        return PAWN_LINE[sq] == sd + 1;
    }

    public static boolean PAWN_PROMOTION(int sq, int sd) {
        return PAWN_LINE[sq] == sd + 3;
    }

    public static boolean PAWN_EN_PASSANT(int sq, int sd) {
        return PAWN_LINE[sq] == sd + 5;
    }

    public static boolean KING_SPAN(int sqSrc, int sqDst) {
        return LEGAL_SPAN[sqDst - sqSrc + 128] == 1;
    }

    public static boolean KNIGHT_SPAN(int sqSrc, int sqDst) {
        return LEGAL_SPAN[sqDst - sqSrc + 128] == 2;
    }

    public static int SAME_LINE(int sqSrc, int sqDst) {
        return SAME_LINE[sqDst - sqSrc + 128];
    }

    public static int CASTLING_TYPE(int sd, int sqSrc, int sqDst) {
        return (sd << 1) + (sqDst > sqSrc ? 0 : 1);
    }

    public static int SIDE_TAG(int sd) {
        return 8 + (sd << 3);
    }

    public static int OPP_SIDE_TAG(int sd) {
        return 16 - (sd << 3);
    }

    public static int SRC(int mv) {
        return mv & 127;
    }

    public static int DST(int mv) {
        return mv >> 7;
    }

    public static int MOVE(int sqSrc, int sqDst) {
        return sqSrc + (sqDst << 7);
    }

    public static int PIECE_TYPE(int pc) {
        return pc & 7;
    }

    public static int MVV_LVA(int pc, int lva) {
        return MMV_VALUE[PIECE_TYPE(pc)] - lva;
    }

    public static int PARSE_COORD(String str, int index) {
        int sq = 0;
        if (index == str.length()) {
            return 0;
        }
        int c = str.charAt(index);
        if (c >= 'a' && c <= 'h') {
            if (index + 1 == str.length()) {
                return 0;
            }
            char c2 = str.charAt(index + 1);
            if (c2 >= '1' && c2 <= '8') {
                sq = COORD_XY(c - 'a' + FILE_LEFT, '8' - c2 + RANK_TOP);
            }
        }
        return sq;
    }

    public static int PARSE_MOVE(String str) {
        return PARSE_MOVE(str, 0);
    }

    public static int PARSE_MOVE(String str, int index) {
        return MOVE(PARSE_COORD(str, index), PARSE_COORD(str, index + 2));
    }

    public static String SQUARE_STR(int sq) {
        return "" + (char) ('a' + FILE_X(sq) - FILE_LEFT) + (char) ('8' - RANK_Y(sq) + RANK_TOP);
    }

    public static String MOVE_STR(int mv) {
        return SQUARE_STR(SRC(mv)) + SQUARE_STR(DST(mv));
    }

    public static int PreGen_zobristKeyPlayer;
    public static int PreGen_zobristLockPlayer;
    public static int[][] PreGen_zobristKeyTable = new int[12][128];
    public static int[][] PreGen_zobristLockTable = new int[12][128];

    public static Random random = new Random();

    public static int bookSize = 0;
    public static int[] bookLock = new int[MAX_BOOK_SIZE];
    public static short[] bookMove = new short[MAX_BOOK_SIZE];
    public static short[] bookValue = new short[MAX_BOOK_SIZE];

    static {
        Util.RC4 rc4 = new Util.RC4(new byte[]{0});
        PreGen_zobristKeyPlayer = rc4.nextLong();
        rc4.nextLong(); // Skip ZobristLock0
        PreGen_zobristLockPlayer = rc4.nextLong();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 128; j++) {
                PreGen_zobristKeyTable[i][j] = rc4.nextLong();
                rc4.nextLong(); // Skip ZobristLock0
                PreGen_zobristLockTable[i][j] = rc4.nextLong();
            }
        }

        InputStream in = rc4.getClass().getResourceAsStream("/assets/touhou_little_maid/book/wchess/BOOK.DAT");
        if (in != null) {
            try {
                while (bookSize < MAX_BOOK_SIZE) {
                    bookLock[bookSize] = Util.readInt(in) >>> 1;
                    bookMove[bookSize] = (short) Util.readShort(in);
                    bookValue[bookSize] = (short) Util.readShort(in);
                    bookSize++;
                }
            } catch (Exception e) {
                // Exit "while" when IOException occurs
            }
            try {
                in.close();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public int sdPlayer;
    public byte[] squares = new byte[128];

    public int zobristKey;
    public int zobristLock;
    public int vlWhite, vlBlack;
    public int moveNum, distance;

    public int[] mvList = new int[MAX_MOVE_NUM];
    public int[] pcList = new int[MAX_MOVE_NUM];
    public int[] keyList = new int[MAX_MOVE_NUM];
    public boolean[] chkList = new boolean[MAX_MOVE_NUM];
    public boolean[] specialMoveList = new boolean[MAX_MOVE_NUM];
    public int[] castlingBitsList = new int[MAX_MOVE_NUM];
    public int[] sqEnPassantList = new int[MAX_MOVE_NUM];

    public int[] brWhitePawn = new int[8]; // br = Bit-Rank
    public int[] brBlackPawn = new int[8]; // br = Bit-Rank
    public short[][] vlWhitePiecePos = new short[6][128];
    public short[][] vlBlackPiecePos = new short[6][128];

    public void clearBoard() {
        sdPlayer = 0;
        for (int sq = 0; sq < 128; sq++) {
            squares[sq] = 0;
        }
        for (int i = 0; i < 8; i++) {
            brWhitePawn[i] = brBlackPawn[i] = 0;
        }
        zobristKey = zobristLock = 0;
        vlWhite = vlBlack = 0;
    }

    public boolean captured() {
        return pcList[moveNum - 1] > 0;
    }

    public boolean inCheck() {
        return chkList[moveNum - 1];
    }

    public boolean specialMove() {
        return specialMoveList[moveNum - 1];
    }

    public int castlingBits() {
        return castlingBitsList[moveNum - 1];
    }

    public int enPassantSquare() {
        return sqEnPassantList[moveNum - 1];
    }

    public boolean canCastling(int castling) {
        if (!inCheck() && (castlingBits() & (1 << castling)) != 0) {
            int delta = CASTLING_DIRECTION[castling];
            int sqSrc = CASTLING_KING_SRC[castling] + delta;
            int sqDst = CASTLING_ROOK_SRC[castling];
            while (sqSrc != sqDst) {
                if (squares[sqSrc] > 0) {
                    return false;
                }
                sqSrc += delta;
            }
            return !checked(CASTLING_ROOK_DST[castling]);
        }
        return false;
    }

    public void setIrrev() {
        setIrrev(castlingBits(), enPassantSquare());
    }

    public void setIrrev(int castlingBits, int sqEnPassant) {
        mvList[0] = pcList[0] = 0;
        castlingBitsList[0] = castlingBits;
        sqEnPassantList[0] = sqEnPassant;
        chkList[0] = checked();
        moveNum = 1;
        distance = 0;
    }

    public void addPiece(int sq, int pc, boolean del) {
        int pcAdjust;
        squares[sq] = (byte) (del ? 0 : pc);
        if (pc < 16) {
            if (pc == 8 + PIECE_PAWN) {
                brWhitePawn[RANK_Y(sq)] ^= 1 << FILE_X(sq);
            }
            pcAdjust = pc - 8;
            vlWhite += del ? -vlWhitePiecePos[pcAdjust][sq] : vlWhitePiecePos[pcAdjust][sq];
        } else {
            if (pc == 16 + PIECE_PAWN) {
                brBlackPawn[RANK_Y(sq)] ^= 1 << FILE_X(sq);
            }
            pcAdjust = pc - 16;
            vlBlack += del ? -vlBlackPiecePos[pcAdjust][sq] : vlBlackPiecePos[pcAdjust][sq];
            pcAdjust += 6;
        }
        zobristKey ^= PreGen_zobristKeyTable[pcAdjust][sq];
        zobristLock ^= PreGen_zobristLockTable[pcAdjust][sq];
    }

    public void addPiece(int sq, int pc) {
        addPiece(sq, pc, false);
    }

    public void delPiece(int sq, int pc) {
        addPiece(sq, pc, true);
    }

    /* A - Take out Captured Piece (Different in En-Passant, see A-EP)
     * B - Remove Source
     * C - Add into Destination (Different in Promotion, see C-P)
     * D - for Castling only
     */
    public void movePiece() {
        int sqSrc = SRC(mvList[moveNum]);
        int sqDst = DST(mvList[moveNum]);
        int pcCaptured = squares[sqDst];
        if (pcCaptured > 0) {
            delPiece(sqDst, pcCaptured); // A
        }
        int pc = squares[sqSrc];
        // __ASSERT((pcCaptured & SIDE_TAG(sdPlayer)) == 0);
        // __ASSERT((pc & SIDE_TAG(sdPlayer)) != 0);
        delPiece(sqSrc, pc); // B
        addPiece(sqDst, pc); // C
        pcList[moveNum] = pcCaptured;
        specialMoveList[moveNum] = false;
        castlingBitsList[moveNum] = castlingBits();
        sqEnPassantList[moveNum] = 0;
        // CASTLING -> Set Castling Bits for Rook's Capture
        if (PIECE_TYPE(pcCaptured) == PIECE_ROOK) {
            int castling = (1 - sdPlayer) << 1;
            if (sqDst == CASTLING_ROOK_SRC[castling]) {
                castlingBitsList[moveNum] &= ~(1 << castling);
            } else if (sqDst == CASTLING_ROOK_SRC[castling + 1]) {
                castlingBitsList[moveNum] &= ~(1 << (castling + 1));
            }
        }
        if (PIECE_TYPE(pc) == PIECE_KING) {
            // CASTLING -> Move both King and Rook
            if (!KING_SPAN(sqSrc, sqDst)) {
                int castling = CASTLING_TYPE(sdPlayer, sqSrc, sqDst);
                delPiece(CASTLING_ROOK_SRC[castling], pc - PIECE_KING + PIECE_ROOK); // D
                addPiece(CASTLING_ROOK_DST[castling], pc - PIECE_KING + PIECE_ROOK); // D
                specialMoveList[moveNum] = true;
            }
            // CASTLING -> Set Castling Bits for King's Move
            castlingBitsList[moveNum] &= ~(3 << (sdPlayer << 1));
        } else if (PIECE_TYPE(pc) == PIECE_PAWN) {
            if (PAWN_PROMOTION(sqDst, sdPlayer)) {
                // PROMOTION -> Add a Queen instead of a Pawn
                delPiece(sqDst, pc); // C-P
                addPiece(sqDst, pc - PIECE_PAWN + PIECE_QUEEN); // C-P
                specialMoveList[moveNum] = true;
            } else if (sqDst == enPassantSquare()) {
                // EN-PASSANT -> Reset the Captured Piece for En-Passant Move
                int sqCaptured = sqDst - FORWARD_DELTA(sdPlayer);
                pcCaptured = squares[sqCaptured];
                // __ASSERT(sqSrc == sqCaptured + 1 || sqSrc == sqCaptured - 1);
                // __ASSERT(pcCaptured == OPP_SIDE_TAG(sdPlayer) + PIECE_PAWN);
                delPiece(sqCaptured, pcCaptured); // A-EP
                pcList[moveNum] = pcCaptured;
                specialMoveList[moveNum] = true;
            } else {
                // EN-PASSANT -> Set En-Passant Square for Pawn's Double-Move
                int delta = FORWARD_DELTA(sdPlayer);
                if (sqDst == sqSrc + (delta << 1)) {
                    sqEnPassantList[moveNum] = sqSrc + delta;
                }
            }
        } else if (PIECE_TYPE(pc) == PIECE_ROOK) {
            // CASTLING -> Set Castling Bits for Rook's Move
            int castling = sdPlayer << 1;
            if (sqSrc == CASTLING_ROOK_SRC[castling]) {
                castlingBitsList[moveNum] &= ~(1 << castling);
            } else if (sqSrc == CASTLING_ROOK_SRC[castling + 1]) {
                castlingBitsList[moveNum] &= ~(1 << (castling + 1));
            }
        }
    }

    /* A - Return Captured Piece (Different in En-Passant, see A-EP)
     * B - Add into Source (Different in Promotion, see B-P)
     * C - Remove Destination
     * D - for Castling only
     */
    public void undoMovePiece() {
        int sqSrc = SRC(mvList[moveNum]);
        int sqDst = DST(mvList[moveNum]);
        int pc = squares[sqDst];
        // __ASSERT((pcList[moveNum] & SIDE_TAG(sdPlayer)) == 0);
        // __ASSERT((pc & SIDE_TAG(sdPlayer)) != 0);
        delPiece(sqDst, pc); // C
        addPiece(sqSrc, pc); // B
        if (pcList[moveNum] > 0) {
            addPiece(sqDst, pcList[moveNum]); // A
        }
        if (specialMoveList[moveNum]) {
            if (PIECE_TYPE(pc) == PIECE_KING) {
                // CASTLING -> Move both King and Rook
                int castling = CASTLING_TYPE(sdPlayer, sqSrc, sqDst);
                // __ASSERT((castlingBits() & (1 << castling)) != 0);
                // __ASSERT(squares[CASTLING_ROOK_DST[castling]] == SIDE_TAG(sdPlayer) + PIECE_ROOK);
                // __ASSERT(squares[CASTLING_ROOK_SRC[castling]] == 0);
                delPiece(CASTLING_ROOK_DST[castling], pc - PIECE_KING + PIECE_ROOK); // D
                addPiece(CASTLING_ROOK_SRC[castling], pc - PIECE_KING + PIECE_ROOK); // D
            } else if (PAWN_PROMOTION(sqDst, sdPlayer)) {
                // PROMOTION -> Add a Pawn instead of a Queen
                // __ASSERT(pc == SIDE_TAG(sdPlayer) + PIECE_QUEEN);
                delPiece(sqSrc, pc); // B-P
                addPiece(sqSrc, pc - PIECE_QUEEN + PIECE_PAWN); // B-P
            } else {
                // __ASSERT(sqDst == enPassantSquare());
                // EN-PASSANT -> Adjust the Captured Pawn
                // __ASSERT(pcList[moveNum] == OPP_SIDE_TAG(sdPlayer) + PIECE_PAWN);
                delPiece(sqDst, pcList[moveNum]); // A-EP
                addPiece(sqDst - FORWARD_DELTA(sdPlayer), pcList[moveNum]); // A-EP
            }
        }
    }

    public void changeSide() {
        sdPlayer = 1 - sdPlayer;
        zobristKey ^= PreGen_zobristKeyPlayer;
        zobristLock ^= PreGen_zobristLockPlayer;
    }

    public boolean makeMove(int mv) {
        keyList[moveNum] = zobristKey;
        mvList[moveNum] = mv;
        movePiece();
        if (checked()) {
            undoMovePiece();
            return false;
        }
        changeSide();
        chkList[moveNum] = checked();
        moveNum++;
        distance++;
        return true;
    }

    public void undoMakeMove() {
        moveNum--;
        distance--;
        changeSide();
        undoMovePiece();
    }

    public void nullMove() {
        keyList[moveNum] = zobristKey;
        changeSide();
        mvList[moveNum] = pcList[moveNum] = sqEnPassantList[moveNum] = 0;
        chkList[moveNum] = specialMoveList[moveNum] = false;
        castlingBitsList[moveNum] = castlingBits();
        moveNum++;
        distance++;
    }

    public void undoNullMove() {
        moveNum--;
        distance--;
        changeSide();
    }

    public int fenPiece(char c) {
        switch (c) {
            case 'K':
                return PIECE_KING;
            case 'Q':
                return PIECE_QUEEN;
            case 'R':
                return PIECE_ROOK;
            case 'B':
                return PIECE_BISHOP;
            case 'N':
                return PIECE_KNIGHT;
            case 'P':
                return PIECE_PAWN;
            default:
                return -1;
        }
    }

    public void fromFen(String fen) {
        clearBoard();
        int y = RANK_TOP;
        int x = FILE_LEFT;
        int index = 0;
        if (index == fen.length()) {
            setIrrev(0, 0);
            return;
        }
        char c = fen.charAt(index);
        while (c != ' ') {
            if (c == '/') {
                x = FILE_LEFT;
                y++;
                if (y > RANK_BOTTOM) {
                    break;
                }
            } else if (c >= '1' && c <= '8') {
                x += (c - '0');
            } else if (c >= 'A' && c <= 'Z') {
                if (x <= FILE_RIGHT) {
                    if (c != 'P' || (y != RANK_TOP && y != RANK_BOTTOM)) {
                        int pt = fenPiece(c);
                        if (pt >= 0) {
                            addPiece(COORD_XY(x, y), pt + 8);
                        }
                    }
                    x++;
                }
            } else if (c >= 'a' && c <= 'z') {
                if (x <= FILE_RIGHT) {
                    if (c != 'p' || (y != RANK_TOP && y != RANK_BOTTOM)) {
                        int pt = fenPiece((char) (c + 'A' - 'a'));
                        if (pt >= 0) {
                            addPiece(COORD_XY(x, y), pt + 16);
                        }
                    }
                    x++;
                }
            }
            index++;
            if (index == fen.length()) {
                setIrrev(0, 0);
                return;
            }
            c = fen.charAt(index);
        }
        index++;
        if (index == fen.length()) {
            setIrrev(0, 0);
            return;
        }
        if (sdPlayer == (fen.charAt(index) == 'b' ? 0 : 1)) {
            changeSide();
        }
        index++; // Skip a ' '
        if (index == fen.length()) {
            setIrrev(0, 0);
            return;
        }
        int castlingBits = 0;
        index++;
        if (index == fen.length()) {
            setIrrev(0, 0);
            return;
        }
        c = fen.charAt(index);
        while (c != ' ') {
            switch (c) {
                case 'K':
                    if (squares[0x78] == 8 && squares[0x7b] == 10) {
                        castlingBits += 1;
                    }
                    break;
                case 'Q':
                    if (squares[0x78] == 8 && squares[0x74] == 10) {
                        castlingBits += 2;
                    }
                    break;
                case 'k':
                    if (squares[0x08] == 16 && squares[0x0b] == 18) {
                        castlingBits += 4;
                    }
                    break;
                case 'q':
                    if (squares[0x08] == 16 && squares[0x04] == 18) {
                        castlingBits += 8;
                    }
                    break;
            }
            index++;
            if (index == fen.length()) {
                setIrrev(castlingBits, 0);
                return;
            }
            c = fen.charAt(index);
        }
        int sqEnPassant = PARSE_COORD(fen, index + 1);
        if (sqEnPassant > 0 && PAWN_EN_PASSANT(sqEnPassant, sdPlayer) &&
            squares[sqEnPassant - FORWARD_DELTA(sdPlayer)] > 0) {
            setIrrev(castlingBits, sqEnPassant);
        } else {
            setIrrev(castlingBits, 0);
        }
    }

    public static final String FEN_PIECE = "        KQRBNP  kqrbnp  ";
    public static final String CASTLING_CHAR = "KQkq";

    public String toFen() {
        StringBuffer fen = new StringBuffer();
        for (int y = RANK_TOP; y <= RANK_BOTTOM; y++) {
            int k = 0;
            for (int x = FILE_LEFT; x <= FILE_RIGHT; x++) {
                int pc = squares[COORD_XY(x, y)];
                if (pc > 0) {
                    if (k > 0) {
                        fen.append((char) ('0' + k));
                        k = 0;
                    }
                    fen.append(FEN_PIECE.charAt(pc));
                } else {
                    k++;
                }
            }
            if (k > 0) {
                fen.append((char) ('0' + k));
            }
            fen.append('/');
        }
        fen.setCharAt(fen.length() - 1, ' ');
        fen.append(sdPlayer == 0 ? 'w' : 'b');
        fen.append(' ');
        int castlingBits = castlingBits();
        if (castlingBits == 0) {
            fen.append('-');
        } else {
            for (int castling = 0; castling < 4; castling++) {
                if ((castlingBits & (1 << castling)) != 0) {
                    fen.append(CASTLING_CHAR.charAt(castling));
                }
            }
        }
        fen.append(' ');
        fen.append(enPassantSquare() > 0 ? SQUARE_STR(enPassantSquare()) : "-");
        return fen.toString();
    }

    public int generateAllMoves(int[] mvs) {
        return generateMoves(mvs, null);
    }

    public int generateMoves(int[] mvs, int[] vls) {
        int moves = 0;
        int pcSelfSide = SIDE_TAG(sdPlayer);
        int pcOppSide = OPP_SIDE_TAG(sdPlayer);
        // CASTLING -> Begin Generating Castling Moves
        if (vls == null) {
            for (int i = 0; i < 2; i++) {
                int castling = (sdPlayer << 1) + i;
                if (canCastling(castling)) {
                    mvs[moves] = MOVE(CASTLING_KING_SRC[castling], CASTLING_KING_DST[castling]);
                    moves++;
                }
            }
        }
        // CASTLING -> End Generating Castling Moves
        for (int sqSrc = 0; sqSrc < 128; sqSrc++) {
            int pcSrc = squares[sqSrc];
            if ((pcSrc & pcSelfSide) == 0) {
                continue;
            }
            switch (pcSrc - pcSelfSide) {
                case PIECE_KING:
                    for (int i = 0; i < 8; i++) {
                        int sqDst = sqSrc + KING_DELTA[i];
                        if (!IN_BOARD(sqDst)) {
                            continue;
                        }
                        int pcDst = squares[sqDst];
                        if (vls == null) {
                            if ((pcDst & pcSelfSide) == 0) {
                                mvs[moves] = MOVE(sqSrc, sqDst);
                                moves++;
                            }
                        } else if ((pcDst & pcOppSide) != 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            vls[moves] = MVV_LVA(pcDst, 99);
                            moves++;
                        }
                    }
                    break;
                case PIECE_QUEEN:
                    for (int i = 0; i < 8; i++) {
                        int delta = KING_DELTA[i];
                        int sqDst = sqSrc + delta;
                        while (IN_BOARD(sqDst)) {
                            int pcDst = squares[sqDst];
                            if (pcDst == 0) {
                                if (vls == null) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    moves++;
                                }
                            } else {
                                if ((pcDst & pcOppSide) != 0) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    if (vls != null) {
                                        vls[moves] = MVV_LVA(pcDst, 9);
                                    }
                                    moves++;
                                }
                                break;
                            }
                            sqDst += delta;
                        }
                    }
                    break;
                case PIECE_ROOK:
                    for (int i = 0; i < 4; i++) {
                        int delta = ROOK_DELTA[i];
                        int sqDst = sqSrc + delta;
                        while (IN_BOARD(sqDst)) {
                            int pcDst = squares[sqDst];
                            if (pcDst == 0) {
                                if (vls == null) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    moves++;
                                }
                            } else {
                                if ((pcDst & pcOppSide) != 0) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    if (vls != null) {
                                        vls[moves] = MVV_LVA(pcDst, 5);
                                    }
                                    moves++;
                                }
                                break;
                            }
                            sqDst += delta;
                        }
                    }
                    break;
                case PIECE_BISHOP:
                    for (int i = 0; i < 4; i++) {
                        int delta = BISHOP_DELTA[i];
                        int sqDst = sqSrc + delta;
                        while (IN_BOARD(sqDst)) {
                            int pcDst = squares[sqDst];
                            if (pcDst == 0) {
                                if (vls == null) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    moves++;
                                }
                            } else {
                                if ((pcDst & pcOppSide) != 0) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    if (vls != null) {
                                        vls[moves] = MVV_LVA(pcDst, 3);
                                    }
                                    moves++;
                                }
                                break;
                            }
                            sqDst += delta;
                        }
                    }
                    break;
                case PIECE_KNIGHT:
                    for (int i = 0; i < 8; i++) {
                        int sqDst = sqSrc + KNIGHT_DELTA[i];
                        if (!IN_BOARD(sqDst)) {
                            continue;
                        }
                        int pcDst = squares[sqDst];
                        if (vls == null) {
                            if ((pcDst & pcSelfSide) == 0) {
                                mvs[moves] = MOVE(sqSrc, sqDst);
                                moves++;
                            }
                        } else if ((pcDst & pcOppSide) != 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            vls[moves] = MVV_LVA(pcDst, 3);
                            moves++;
                        }
                    }
                    break;
                case PIECE_PAWN:
                    int delta = FORWARD_DELTA(sdPlayer);
                    int sqDst = sqSrc + delta;
                    if (vls == null) {
                        if (IN_BOARD(sqDst) && squares[sqDst] == 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            moves++;
                            if (PAWN_INIT(sqSrc, sdPlayer)) {
                                sqDst += delta;
                                if (squares[sqDst] == 0) {
                                    mvs[moves] = MOVE(sqSrc, sqDst);
                                    moves++;
                                }
                            }
                        }
                    } else {
                        // PROMOTION -> Promotions are regarded as Capture Moves
                        if (PAWN_PROMOTION(sqDst, sdPlayer) && squares[sqDst] == 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            vls[moves] = MVV_LVA(PIECE_QUEEN, 1);
                            moves++;
                        }
                    }
                    int sqTmp = sqSrc + delta;
                    for (int i = -1; i <= 1; i += 2) {
                        sqDst = sqTmp + i;
                        if (!IN_BOARD(sqDst)) {
                            continue;
                        }
                        int pcDst = squares[sqDst];
                        // EN-PASSANT -> En-passant considered
                        if (sqDst == enPassantSquare()) {
                            pcDst = squares[sqDst - delta];
                        }
                        if ((pcDst & pcOppSide) != 0) {
                            mvs[moves] = MOVE(sqSrc, sqDst);
                            if (vls != null) {
                                vls[moves] = MVV_LVA(pcDst, 1);
                            }
                            moves++;
                        }
                    }
                    break;
            }
        }
        return moves;
    }

    public boolean legalMove(int mv) {
        int sqSrc = SRC(mv);
        int pcSrc = squares[sqSrc];
        int pcSelfSide = SIDE_TAG(sdPlayer);
        if ((pcSrc & pcSelfSide) == 0) {
            return false;
        }
        int sqDst = DST(mv);
        int pcDst = squares[sqDst];
        if ((pcDst & pcSelfSide) != 0) {
            return false;
        }
        int pieceType = pcSrc - pcSelfSide;
        switch (pieceType) {
            case PIECE_KING:
                if (KING_SPAN(sqSrc, sqDst)) {
                    return true;
                }
                // CASTLING -> Castling considered
                int castling = CASTLING_TYPE(sdPlayer, sqSrc, sqDst);
                return (CASTLING_KING_DST[castling] == sqDst && canCastling(castling));
            case PIECE_KNIGHT:
                return KNIGHT_SPAN(sqSrc, sqDst);
            case PIECE_QUEEN:
            case PIECE_ROOK:
            case PIECE_BISHOP:
                int delta;
                switch (SAME_LINE(sqSrc, sqDst)) {
                    case DIFF_LINE:
                        return false;
                    case SAME_RANK:
                        if (pieceType == PIECE_BISHOP) {
                            return false;
                        }
                        delta = (sqDst < sqSrc ? -1 : 1);
                        break;
                    case SAME_FILE:
                        if (pieceType == PIECE_BISHOP) {
                            return false;
                        }
                        delta = (sqDst < sqSrc ? -16 : 16);
                        break;
                    case SAME_DIAG_A1H8:
                        if (pieceType == PIECE_ROOK) {
                            return false;
                        }
                        delta = (sqDst < sqSrc ? -15 : 15);
                        break;
                    case SAME_DIAG_A8H1:
                        if (pieceType == PIECE_ROOK) {
                            return false;
                        }
                        delta = (sqDst < sqSrc ? -17 : 17);
                        break;
                    default: // Never Occurs
                        throw new RuntimeException();
                }
                int sqTmp = sqSrc + delta;
                while (sqTmp != sqDst) {
                    if (squares[sqTmp] > 0) {
                        return false;
                    }
                    sqTmp += delta;
                }
                return true;
            case PIECE_PAWN:
                delta = FORWARD_DELTA(sdPlayer);
                sqTmp = sqSrc + delta;
                // EN-PASSANT -> En-passant is a capture move but "pcDst != 0"
                if (pcDst != 0 || sqDst == enPassantSquare()) {
                    return (sqDst == sqTmp - 1 || sqDst == sqTmp + 1);
                }
                return (sqDst == sqTmp || (sqDst == sqTmp + delta &&
                                           PAWN_INIT(sqSrc, sdPlayer) && squares[sqTmp] == 0));
            default:
                return false;
        }
    }

    public boolean checked() {
        int pcSelfSide = SIDE_TAG(sdPlayer);
        for (int sqSrc = 0; sqSrc < 128; sqSrc++) {
            if (squares[sqSrc] == pcSelfSide + PIECE_KING) {
                return checked(sqSrc);
            }
        }
        return false;
    }

    public boolean checked(int sqSrc) {
        int pcOppSide = OPP_SIDE_TAG(sdPlayer);
        int sqTmp = sqSrc + FORWARD_DELTA(sdPlayer);
        for (int i = -1; i <= 1; i += 2) {
            int sqDst = sqTmp + i;
            if (IN_BOARD(sqDst) && squares[sqDst] == pcOppSide + PIECE_PAWN) {
                return true;
            }
        }
        for (int i = 0; i < 8; i++) {
            int sqDst = sqSrc + KING_DELTA[i];
            if (IN_BOARD(sqDst) && squares[sqDst] == pcOppSide + PIECE_KING) {
                return true;
            }
        }
        for (int i = 0; i < 8; i++) {
            int sqDst = sqSrc + KNIGHT_DELTA[i];
            if (IN_BOARD(sqDst) && squares[sqDst] == pcOppSide + PIECE_KNIGHT) {
                return true;
            }
        }
        for (int i = 0; i < 4; i++) {
            int delta = BISHOP_DELTA[i];
            int sqDst = sqSrc + delta;
            while (IN_BOARD(sqDst)) {
                int pcDst = squares[sqDst];
                if (pcDst > 0) {
                    if (pcDst == pcOppSide + PIECE_BISHOP || pcDst == pcOppSide + PIECE_QUEEN) {
                        return true;
                    }
                    break;
                }
                sqDst += delta;
            }
        }
        for (int i = 0; i < 4; i++) {
            int delta = ROOK_DELTA[i];
            int sqDst = sqSrc + delta;
            while (IN_BOARD(sqDst)) {
                int pcDst = squares[sqDst];
                if (pcDst > 0) {
                    if (pcDst == pcOppSide + PIECE_ROOK || pcDst == pcOppSide + PIECE_QUEEN) {
                        return true;
                    }
                    break;
                }
                sqDst += delta;
            }
        }
        return false;
    }

    public boolean isMate() {
        int[] mvs = new int[MAX_GEN_MOVES];
        int moves = generateAllMoves(mvs);
        for (int i = 0; i < moves; i++) {
            if (makeMove(mvs[i])) {
                undoMakeMove();
                return false;
            }
        }
        return true;
    }

    public int drawValue() {
        return (distance & 1) == 0 ? -DRAW_VALUE : DRAW_VALUE;
    }

    public int checkmateValue() {
        return distance - MATE_VALUE;
    }

    public int mateValue() {
        return inCheck() ? checkmateValue() : drawValue();
    }

    public int material() {
        return (sdPlayer == 0 ? vlWhite - vlBlack : vlBlack - vlWhite) + ADVANCED_VALUE;
    }

    public boolean nullOkay() {
        return (sdPlayer == 0 ? vlWhite : vlBlack) > NULL_OKAY_MARGIN;
    }

    public boolean nullSafe() {
        return (sdPlayer == 0 ? vlWhite : vlBlack) > NULL_SAFE_MARGIN;
    }

    public boolean isRep() {
        return isRep(1);
    }

    public boolean isRep(int recur_) {
        int recur = recur_;
        boolean selfSide = false;
        int index = moveNum - 1;
        while (mvList[index] > 0 && pcList[index] == 0) {
            if (selfSide) {
                if (keyList[index] == zobristKey) {
                    recur--;
                    if (recur == 0) {
                        return true;
                    }
                }
            }
            selfSide = !selfSide;
            index--;
        }
        return false;
    }

    public int bookMove() {
        if (bookSize == 0) {
            return 0;
        }
        int lock = zobristLock >>> 1; // Convert into Unsigned
        int index = Util.binarySearch(lock, bookLock, 0, bookSize);
        if (index < 0) {
            return 0;
        }
        index--;
        while (index >= 0 && bookLock[index] == lock) {
            index--;
        }
        int[] mvs = new int[MAX_GEN_MOVES];
        int[] vls = new int[MAX_GEN_MOVES];
        int value = 0;
        int moves = 0;
        index++;
        while (index < bookSize && bookLock[index] == lock) {
            int mv = 0xffff & bookMove[index];
            if (legalMove(mv)) {
                mvs[moves] = mv;
                vls[moves] = bookValue[index];
                value += vls[moves];
                moves++;
                if (moves == MAX_GEN_MOVES) {
                    break;
                }
            }
            index++;
        }
        if (value == 0) {
            return 0;
        }
        value = Math.abs(random.nextInt()) % value;
        for (index = 0; index < moves; index++) {
            value -= vls[index];
            if (value < 0) {
                break;
            }
        }
        return mvs[index];
    }

    public int historyIndex(int mv) {
        return ((squares[SRC(mv)] - 8) << 7) + DST(mv);
    }

    public void printBoard() {
        printBoard(System.out);
    }

    public void printBoard(PrintStream out) {
        for (int y = Position.RANK_TOP; y <= Position.RANK_BOTTOM; y++) {
            out.print((char) ('8' - y));
            out.print('|');
            for (int x = Position.FILE_LEFT; x <= Position.FILE_RIGHT; x++) {
                int pc = squares[Position.COORD_XY(x, y)];
                if (pc > 0) {
                    if (pc < 16) {
                        out.print(PIECE_STRING.charAt(pc - 8));
                    } else {
                        out.print((char) (PIECE_STRING.charAt(pc - 16) - 'A' + 'a'));
                    }
                } else {
                    out.print('.');
                }
                out.print(' ');
            }
            out.println();
        }
        out.println(" +----------------");
        out.println("  a b c d e f g h");
    }
}