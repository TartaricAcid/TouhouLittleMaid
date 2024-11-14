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

import com.github.tartaricacid.touhoulittlemaid.molang.runtime.AssignableVariable;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Identifier expression implementation for Molang.
 *
 * <p>Note that, identifiers in Molang are always
 * <b>case-insensitive</b></p>
 *
 * <p>Example identifier expressions: {@code math},
 * {@code name}, {@code this}, {@code print}</p>
 *
 * @since 3.0.0
 */
public final class IdentifierExpression implements Expression {

    private final String name;
    private final Object target;

    private IdentifierExpression(final @NotNull String name, Object target) {
        Objects.requireNonNull(name, "name");

        this.name = name.toLowerCase(); // case-insensitive
        this.target = target;
    }

    public static Expression get(String name, Object target) {
        if(target instanceof Number) {
            return new DoubleExpression(((Number) target).doubleValue());
        } else if(target instanceof String) {
            return new StringExpression((String) target);
        } else if(target instanceof AssignableVariable) {
            return new AssignableVariableExpression((AssignableVariable) target);
        } else if(target instanceof Variable) {
            return new VariableExpression((Variable) target);
        }
        return new IdentifierExpression(name, target);
    }

    /**
     * Gets the identifier name.
     *
     * @return The identifier name.
     * @since 3.0.0
     */
    public @NotNull String name() {
        return name;
    }

    public Object target() {
        return target;
    }

    @Override
    public <R> R visit(final @NotNull ExpressionVisitor<R> visitor) {
        return visitor.visitIdentifier(this);
    }

    @Override
    public String toString() {
        return "Identifier(" + name + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentifierExpression that = (IdentifierExpression) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}