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

import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Function;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Execution scope expression implementation. Execution
 * scopes define a new scope and a new sequence of
 * expressions to evaluate.
 *
 * <p>Execution scope expression examples: {@code { print('a'); print('b'); }},
 * {@code { doThisFirst(); thenDoThis(); }}, {@code { v.x = v.x + 1; }}</p>
 *
 * @since 3.0.0
 */
public final class ExecutionScopeExpression implements Expression {

    private final List<Expression> expressions;

    public ExecutionScopeExpression(final @NotNull List<Expression> expressions) {
        this.expressions = Objects.requireNonNull(expressions, "expressions");
    }

    /**
     * Returns the expressions inside this
     * execution scope, never null
     */
    public @NotNull List<Expression> expressions() {
        return expressions;
    }

    @Override
    public <R> R visit(final @NotNull ExpressionVisitor<R> visitor) {
        return visitor.visitExecutionScope(this);
    }

    public Function buildFunction(final @NotNull ExpressionVisitor<?> visitor) {
        return visitor.buildExecutionScopeFunction(this);
    }

    @Override
    public String toString() {
        return "ExecutionScope(" + this.expressions + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionScopeExpression that = (ExecutionScopeExpression) o;
        return expressions.equals(that.expressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions);
    }

}