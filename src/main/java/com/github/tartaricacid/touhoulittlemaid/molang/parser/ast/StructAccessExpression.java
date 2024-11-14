package com.github.tartaricacid.touhoulittlemaid.molang.parser.ast;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.util.StringPool;
import org.jetbrains.annotations.NotNull;

public class StructAccessExpression implements Expression {
    private final Expression left;
    private final int path;

    public StructAccessExpression(Expression left, String path) {
        this.left = left;
        this.path = StringPool.computeIfAbsent(path);
    }

    @Override
    public <R> R visit(@NotNull ExpressionVisitor<R> visitor) {
        return visitor.visitStruct(this);
    }

    public Expression left() {
        return left;
    }

    public int path() {
        return path;
    }
}
