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

package com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding;

import com.github.tartaricacid.touhoulittlemaid.molang.parser.ast.*;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.AssignableVariable;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Function;

import java.util.Arrays;

/**
 * Class holding some default bindings and
 * static utility methods for ease working
 * with bindings
 */
public final class StandardBindings {
    private static final int MAX_LOOP_ROUND = 1024;

    public static final Function LOOP_FUNC = (ctx, args) -> {
        // Parameters:
        // - double:           How many times should we loop
        // - CallableBinding:  The looped expressions

        if (args.size() < 2) {
            return null;
        }

        int n = Math.min((int) Math.round(args.getAsDouble(ctx, 0)), MAX_LOOP_ROUND);
        Object expr = args.getExpression(1);

        if (expr instanceof ExecutionScopeExpression) {
            Function callable = ((ExecutionScopeExpression) expr).buildFunction((ExpressionVisitor<?>) ctx);
            if (callable != null) {
                for (int i = 0; i < n; i++) {
                    Object value = callable.evaluate(ctx, Function.EMPTY_ARGUMENT);
                    if (value == StatementExpression.Op.BREAK) {
                        break;
                    }
                    // (not necessary, callable already exits when returnValue
                    //  is set to any non-null value)
                    // if (value == StatementExpression.Op.CONTINUE) continue;
                }
            }
        }
        return null;
    };

    public static final Function FOR_EACH_FUNC = (ctx, args) -> {
        // Parameters:
        // - any:              Variable
        // - array:            Any array
        // - CallableBinding:  The looped expressions

        if (args.size() < 3) {
            return null;
        }

        final Expression variableExpr = args.getExpression(0);
        if (!(variableExpr instanceof AssignableVariableExpression)) {
            // first argument must be an access expression,
            // e.g. 'variable.test', 'v.pig', 't.entity' or
            // 't.entity.location.world'
            return null;
        }
        final AssignableVariable variableAccess = ((AssignableVariableExpression) variableExpr).target();

        final Object array = args.getValue(ctx, 1);
        final Iterable<?> arrayIterable;
        if (array instanceof Object[]) {
            arrayIterable = Arrays.asList((Object[]) array);
        } else if (array instanceof Iterable<?>) {
            arrayIterable = (Iterable<?>) array;
        } else {
            // second argument must be an array or iterable
            return null;
        }

        final Expression expr = args.getExpression(2);

        if (expr instanceof ExecutionScopeExpression) {
            Function callable = ((ExecutionScopeExpression) expr).buildFunction((ExpressionVisitor<?>) ctx);
            if (callable != null) {
                for (final Object val : arrayIterable) {
                    // set 'val' as current value
                    // eval (objectExpr.propertyName = val)
                    variableAccess.assign(ctx, val);
                    final Object returnValue = callable.evaluate(ctx, Function.EMPTY_ARGUMENT);

                    if (returnValue == StatementExpression.Op.BREAK) {
                        break;
                    }
                }
            }
        }
        return null;
    };
}
