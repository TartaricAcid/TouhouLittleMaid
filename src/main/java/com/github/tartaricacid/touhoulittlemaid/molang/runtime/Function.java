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
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ValueConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Molang function. Receives a certain amount of
 * parameters and (optionally) returns a value. Can be called
 * from Molang code using call expressions: {@code my_function(1, 2, 3)}
 *
 * <p>This is a very low-level function that is "expression-sensitive",
 * this means, it takes the raw expression arguments instead of the
 * evaluated expression argument values.</p>
 *
 * @since 3.0.0
 */
@FunctionalInterface
public interface Function {
    /**
     * Executes this function with the given arguments.
     *
     * @param context   The execution context
     * @param arguments The arguments
     * @return The function result
     * @since 3.0.0
     */
    @Nullable Object evaluate(final @NotNull ExecutionContext<?> context, final @NotNull ArgumentCollection arguments);

    default boolean validateArgumentSize(int size) {
        return true;
    }

    ArgumentCollection EMPTY_ARGUMENT = new ArgumentCollection(new ArrayList<>());

    class ArgumentCollection {
        private final List<Expression> arguments;

        public ArgumentCollection(List<Expression> arguments) {
            this.arguments = arguments;
        }

        public int size() {
            return arguments.size();
        }

        public String getAsString(@NotNull ExecutionContext<?> ctx, final int index) {
            return ValueConversions.asString(ctx.eval(arguments.get(index)));
        }

        public double getAsDouble(@NotNull ExecutionContext<?> ctx, final int index) {
            return ValueConversions.asDouble(ctx.eval(arguments.get(index)));
        }

        public int getAsInt(@NotNull ExecutionContext<?> ctx, final int index) {
            return ValueConversions.asInt(ctx.eval(arguments.get(index)));
        }

        public float getAsFloat(@NotNull ExecutionContext<?> ctx, final int index) {
            return ValueConversions.asFloat(ctx.eval(arguments.get(index)));
        }

        public boolean getAsBoolean(@NotNull ExecutionContext<?> ctx, final int index) {
            return ValueConversions.asBoolean(ctx.eval(arguments.get(index)));
        }

        public Object getValue(@NotNull ExecutionContext<?> ctx, final int index) {
            return ctx.eval(arguments.get(index));
        }

        public Expression getExpression(final int index) {
            return arguments.get(index);
        }
    }
}
