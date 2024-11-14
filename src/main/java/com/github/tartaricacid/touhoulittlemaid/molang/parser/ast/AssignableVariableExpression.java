package com.github.tartaricacid.touhoulittlemaid.molang.parser.ast;

import com.github.tartaricacid.touhoulittlemaid.molang.runtime.AssignableVariable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AssignableVariableExpression implements Expression {
    private final AssignableVariable target;

    public AssignableVariableExpression(AssignableVariable target) {
        Objects.requireNonNull(target, "target");
        this.target = target;
    }

    public AssignableVariable target() {
        return target;
    }

    @Override
    public <R> R visit(final @NotNull ExpressionVisitor<R> visitor) {
        return visitor.visitAssignableVariable(this);
    }

    @Override
    public String toString() {
        return target.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignableVariableExpression that = (AssignableVariableExpression) o;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }
}
