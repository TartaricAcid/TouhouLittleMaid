package com.github.tartaricacid.touhoulittlemaid.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record BoardStateTooltip(String type, String stateData) implements TooltipComponent {
    public static final String GOMOKU = "gomoku";
    public static final String XIANGQI = "xiangqi";
    public static final String CHESS = "chess";

    public static BoardStateTooltip ofGomoku(String stateData) {
        return new BoardStateTooltip(GOMOKU, stateData);
    }

    public static BoardStateTooltip ofXiangqi(String stateData) {
        return new BoardStateTooltip(XIANGQI, stateData);
    }

    public static BoardStateTooltip ofChess(String stateData) {
        return new BoardStateTooltip(CHESS, stateData);
    }
}
