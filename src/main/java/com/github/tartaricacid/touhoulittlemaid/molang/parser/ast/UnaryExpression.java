/*
 * This file is part of molang, licensed under the MIT license
 *
 * Copyright (c) 2021-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.tartaricacid.touhoulittlemaid.molang.parser.ast;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Unary expression implementation, performs a single operation
 * to a single expression, like logical negation, arithmetical
 * negation, or "return expr;".
 *
 * <p>Example unary expressions: {@code -hello}, {@code !p},
 * {@code !q}, {@code -(10 * 5)}, {@code return this},
 * {@code return 5}</p>
 *
 * @since 3.0.0
 */
public final class UnaryExpression implements Expression {

    private final Op op;
    private final Expression expression;

    public UnaryExpression(
            final @NotNull Op op,
            final @NotNull Expression expression
    ) {
        this.op = requireNonNull(op, "op");
        this.expression = requireNonNull(expression, "expression");
    }

    /**
     * Gets the unary expression operation.
     *
     * @return The unary expression operation.
     * @since 3.0.0
     */
    public @NotNull Op op() {
        return op;
    }

    /**
     * Gets the operated expression.
     *
     * @return The operated expression.
     * @since 3.0.0
     */
    public @NotNull Expression expression() {
        return expression;
    }

    @Override
    public <R> R visit(final @NotNull ExpressionVisitor<R> visitor) {
        return visitor.visitUnary(this);
    }

    @Override
    public String toString() {
        return "Unary(" + op + ")(" + expression + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryExpression that = (UnaryExpression) o;
        if (op != that.op) return false;
        return expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        int result = op.hashCode();
        result = 31 * result + expression.hashCode();
        return result;
    }

    public enum Op {
        LOGICAL_NEGATION(2800),
        ARITHMETICAL_NEGATION(2800),
        PLUS(2800),
        RETURN(-1);

        final int precedence;

        Op(int precedence) {
            this.precedence = precedence;
        }

        public int precedence() {
            return precedence;
        }
    }

}
