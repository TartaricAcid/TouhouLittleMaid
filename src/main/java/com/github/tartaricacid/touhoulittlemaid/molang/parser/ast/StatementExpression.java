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

import java.util.Objects;

/**
 * Statement expression implementation. Statement expressions
 * do not have children expressions, they just have a single
 * operation type.
 *
 * <p>Example statement expressions: {@code break}, {@code continue}</p>
 *
 * @since 3.0.0
 */
public final class StatementExpression implements Expression {

    private final Op op;

    public StatementExpression(final @NotNull Op op) {
        this.op = Objects.requireNonNull(op, "op");
    }

    /**
     * Gets the operation/type of this statement.
     *
     * @return The statement operation/type.
     * @since 3.0.0
     */
    public @NotNull Op op() {
        return op;
    }

    @Override
    public <R> R visit(final @NotNull ExpressionVisitor<R> visitor) {
        return visitor.visitStatement(this);
    }


    /**
     * Enum containing all the possible operations/types
     * of statement expressions.
     *
     * @since 3.0.0
     */
    public enum Op {
        /**
         * The break statement type
         *
         * @since 3.0.0
         */
        BREAK,

        /**
         * The continue statement type
         *
         * @since 3.0.0
         */
        CONTINUE
    }

}
