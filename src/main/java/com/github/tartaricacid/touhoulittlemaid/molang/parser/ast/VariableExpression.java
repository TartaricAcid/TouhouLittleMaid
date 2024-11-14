package com.github.tartaricacid.touhoulittlemaid.molang.parser.ast;

import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VariableExpression  implements Expression {
    private final Variable target;

    public VariableExpression(Variable target) {
        Objects.requireNonNull(target, "target");
        this.target = target;
    }

    public Variable target() {
        return target;
    }

    @Override
    public <R> R visit(final @NotNull ExpressionVisitor<R> visitor) {
        return visitor.visitVariable(this);
    }

    @Override
    public String toString() {
        return target.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableExpression that = (VariableExpression) o;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }
}
