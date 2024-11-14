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

package com.github.tartaricacid.touhoulittlemaid.molang.runtime;

import com.github.tartaricacid.touhoulittlemaid.molang.parser.ast.Expression;
import com.github.tartaricacid.touhoulittlemaid.molang.parser.ast.ExpressionVisitor;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ObjectBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link ExpressionVisitor} implementation that evaluates
 * (interprets) the expressions it visits and returns a single
 * value, commonly, a double value.
 *
 * @since 3.0.0
 */
@SuppressWarnings("rawtypes")
public /* sealed */ interface ExpressionEvaluator<TEntity> /* permits ExpressionEvaluatorImpl */ extends ExecutionContext<TEntity>, ExpressionVisitor<Object> {
    /**
     * Creates a new {@link ExpressionEvaluator} instance with
     * the given bindings.
     *
     * @param entity   The entity object
     * @return The created expression evaluator.
     * @since 3.0.0
     */
    static @NotNull <TEntity> ExpressionEvaluator<TEntity> evaluator(final @Nullable TEntity entity) {
        return new ExpressionEvaluatorImpl<>(entity);
    }

    /**
     * Creates a new {@link ExpressionEvaluator} instance
     * without bindings.
     *
     * @return The created expression evaluator.
     * @since 3.0.0
     */
    static @NotNull ExpressionEvaluator evaluator() {
        return evaluator(ObjectBinding.EMPTY);
    }

    @Override
    default @Nullable Object eval(final @NotNull Expression expression) {
        return expression.visit(this);
    }

    /**
     * Creates a new, child, expression evaluator.
     *
     * <p>Child evaluators have all the bindings of
     * their parents and may have extra bindings.</p>
     *
     * <p>Child evaluators have their own stack.</p>
     *
     * @return The child expression evaluator.
     * @since 3.0.0
     */
    @NotNull ExpressionEvaluator<TEntity> createChild();

    /**
     * Creates a new, child, expression evaluator.
     *
     * <p>Child evaluators have all the bindings of
     * their parents and may have extra bindings.</p>
     *
     * <p>Child evaluators have their own stack.</p>
     *
     * @param entity The new entity value
     * @return The child expression evaluator.
     * @since 3.0.0
     */
    @NotNull <TNewEntity> ExpressionEvaluator<TNewEntity> createChild(final @Nullable TNewEntity entity);

    /**
     * Pops the return value, set by the last "return"
     * expression.
     *
     * @return The return value, null if no "return"
     * expression is found.
     * @since 3.0.0
     */
    @Nullable Object popReturnValue();

}