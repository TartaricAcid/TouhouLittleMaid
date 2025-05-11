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

import com.github.tartaricacid.touhoulittlemaid.molang.parser.ast.*;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ValueConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("rawtypes,unchecked")
public final class ExpressionEvaluatorImpl<TEntity> implements ExpressionEvaluator<TEntity> {
    private static final Evaluator[] BINARY_EVALUATORS = {
            bool((a, b) -> a.eval() && b.eval()),
            bool((a, b) -> a.eval() || b.eval()),
            compare((a, b) -> a.eval() < b.eval()),
            compare((a, b) -> a.eval() <= b.eval()),
            compare((a, b) -> a.eval() > b.eval()),
            compare((a, b) -> a.eval() >= b.eval()),
            (evaluator, a, b) -> {
                final Object aVal = a.visit(evaluator);
                final Object bVal = b.visit(evaluator);
                return ValueConversions.asFloat(aVal) + ValueConversions.asFloat(bVal);
            },
            arithmetic((a, b) -> a.eval() - b.eval()),
            arithmetic((a, b) -> a.eval() * b.eval()),
            arithmetic((a, b) -> {
                // Molang allows division by zero,
                // which is always equal to 0
                float dividend = a.eval();
                float divisor = b.eval();
                if (divisor == 0) return 0;
                else return dividend / divisor;
            }),
            (evaluator, a, b) -> { // arrow
                final Object val = a.visit(evaluator);
                if (val == null) {
                    return null;
                } else {
                    return b.visit(evaluator.createChild(val));
                }
            },
            (evaluator, a, b) -> { // null coalesce
                Object val = a.visit(evaluator);
                if (val == null) {
                    return b.visit(evaluator);
                } else {
                    return val;
                }
            },
            (evaluator, a, b) -> { // assignation
                Object val = b.visit(evaluator);
                if (a instanceof AssignableVariableExpression) {
                    AssignableVariable var = ((AssignableVariableExpression) a).target();
                    if (val instanceof Struct) {
                        val = ((Struct) val).copy();
                    }
                    var.assign(evaluator, val);
                } else if (a instanceof StructAccessExpression) {
                    if (val instanceof Struct) {
                        // 不允许结构体嵌套
                        return val;
                    }
                    StructAccessExpression exp = (StructAccessExpression) a;
                    Object value = exp.left().visit(evaluator);
                    if (value == null) {
                        if (exp.left() instanceof AssignableVariableExpression) {
                            AssignableVariable variable = ((AssignableVariableExpression) exp.left()).target();
                            Struct struct = new HashMapStruct();
                            struct.putProperty(exp.path(), val);
                            variable.assign(evaluator, struct);
                        }
                    } else if (value instanceof Struct) {
                        ((Struct) value).putProperty(exp.path(), val);
                    }
                }
                // TODO: (else case) This isn't fail-fast, we can only assign to access expressions
                return val;
            },
            (evaluator, a, b) -> { // conditional
                Object condition = a.visit(evaluator);
                if (ValueConversions.asBoolean(condition)) {
                    return b.visit(evaluator);
                }
                return null;
            },
            (evaluator, a, b) -> {
                Object left = a.visit(evaluator);
                Object right = b.visit(evaluator);
                if (left == right)
                    return true;
                if (right == null)
                    return false;
                if (right instanceof Number) {
                    return ValueConversions.asFloat(right) == ValueConversions.asFloat(left);
                } else if (right instanceof String) {
                    return right.equals(left);
                }
                return false;
            }, // eq
            (evaluator, a, b) -> {
                Object left = a.visit(evaluator);
                Object right = b.visit(evaluator);
                if (left == right)
                    return false;
                if (right == null)
                    return true;
                if (right instanceof Number) {
                    return ValueConversions.asFloat(right) != ValueConversions.asFloat(left);
                } else if (right instanceof String) {
                    return !right.equals(left);
                }
                return false;
            }
    };

    private final TEntity entity;
    private @Nullable Object returnValue;

    public ExpressionEvaluatorImpl(final @Nullable TEntity entity) {
        this.entity = entity;
    }

    private static Evaluator bool(BooleanOperator op) {
        return (evaluator, a, b) -> op.operate(
                () -> ValueConversions.asBoolean(a.visit(evaluator)),
                () -> ValueConversions.asBoolean(b.visit(evaluator))
        );
    }

    private static Evaluator compare(Comparator comp) {
        return (evaluator, a, b) -> comp.compare(
                () -> ValueConversions.asFloat(a.visit(evaluator)),
                () -> ValueConversions.asFloat(b.visit(evaluator))
        );
    }

    private static Evaluator arithmetic(ArithmeticOperator op) {
        return (evaluator, a, b) -> op.operate(
                () -> ValueConversions.asFloat(a.visit(evaluator)),
                () -> ValueConversions.asFloat(b.visit(evaluator))
        );
    }

    @Override
    public TEntity entity() {
        return entity;
    }

    @Override
    public @NotNull <TNewEntity> ExpressionEvaluator<TNewEntity> createChild(final @Nullable TNewEntity entity) {
        return new ExpressionEvaluatorImpl<>(entity);
    }

    @Override
    public @NotNull ExpressionEvaluator<TEntity> createChild() {
        // Note that it will have its own returnValue, but same bindings
        // (Should we create new bindings?)
        return new ExpressionEvaluatorImpl<>(this.entity);
    }

    @Override
    public @Nullable Object popReturnValue() {
        Object val = this.returnValue;
        this.returnValue = null;
        return val;
    }

    @Override
    public @Nullable Object visitCall(final @NotNull CallExpression expression) {
        final Function function = expression.function();
        return function.evaluate(this, expression.arguments());
    }

    @Override
    public Object visitDouble(@NotNull DoubleExpression expression) {
        return expression.value();
    }

    @Override
    public Object visitExecutionScope(@NotNull ExecutionScopeExpression executionScope) {
        return buildExecutionScopeFunction(executionScope).evaluate(this, Function.EMPTY_ARGUMENT);
    }

    @Override
    public Function buildExecutionScopeFunction(final @NotNull ExecutionScopeExpression executionScope) {
        List<Expression> expressions = executionScope.expressions();
        var evaluatorForThisScope = createChild();
        return (context, arguments) -> {
            Object lastResult = null;
            for (Expression expression : expressions) {
                // eval expression, ignore result
                lastResult = evaluatorForThisScope.eval(expression);
                // check for return values
                Object returnValue = evaluatorForThisScope.popReturnValue();
                if (returnValue != null) {
                    return returnValue;
                }
            }
            return lastResult;
        };
    }

    @Override
    public Object visitIdentifier(@NotNull IdentifierExpression expression) {
        throw new RuntimeException("Unknown identifier type");
    }

    @Override
    public Object visitVariable(final @NotNull VariableExpression expression) {
        return expression.target().evaluate(this);
    }

    public Object visitAssignableVariable(final @NotNull AssignableVariableExpression expression) {
        return expression.target().evaluate(this);
    }

    @Override
    public Object visitStruct(final @NotNull StructAccessExpression expression) {
        Object value = expression.left().visit(this);
        if (value instanceof Struct) {
            return ((Struct) value).getProperty(expression.path());
        } else {
            return null;
        }
    }

    @Override
    public Object visitBinary(@NotNull BinaryExpression expression) {
        return BINARY_EVALUATORS[expression.op().index()].eval(
                this,
                expression.left(),
                expression.right()
        );
    }

    @Override
    public Object visitUnary(@NotNull UnaryExpression expression) {
        Object value = expression.expression().visit(this);
        switch (expression.op()) {
            case LOGICAL_NEGATION:
                return !ValueConversions.asBoolean(value);
            case ARITHMETICAL_NEGATION:
                return -ValueConversions.asFloat(value);
            case RETURN: {
                this.returnValue = value;
                return 0D;
            }
            default:
                throw new IllegalStateException("Unknown operation");
        }
    }

    @Override
    public Object visitStatement(@NotNull StatementExpression expression) {
        switch (expression.op()) {
            case BREAK: {
                this.returnValue = StatementExpression.Op.BREAK;
                break;
            }
            case CONTINUE: {
                this.returnValue = StatementExpression.Op.CONTINUE;
                break;
            }
        }
        return null;
    }

    @Override
    public Object visitString(@NotNull StringExpression expression) {
        return expression.value();
    }

    @Override
    public Object visitTernaryConditional(@NotNull TernaryConditionalExpression expression) {
        Object obj = expression.condition().visit(this);
        obj = ValueConversions.asBoolean(obj)
                ? expression.trueExpression().visit(this)
                : expression.falseExpression().visit(this);
        return obj;
    }

    @Override
    public Object visit(@NotNull Expression expression) {
        throw new UnsupportedOperationException("Unsupported expression type: " + expression);
    }

    private interface Evaluator<TEntity> {
        Object eval(ExpressionEvaluator<TEntity> evaluator, Expression a, Expression b);
    }

    private interface BooleanOperator {
        boolean operate(LazyEvaluableBoolean a, LazyEvaluableBoolean b);
    }

    interface LazyEvaluableBoolean {
        boolean eval();
    }

    interface LazyEvaluableFloat {
        float eval();
    }

    private interface Comparator {
        boolean compare(LazyEvaluableFloat a, LazyEvaluableFloat b);

    }

    private interface ArithmeticOperator {
        float operate(LazyEvaluableFloat a, LazyEvaluableFloat b);
    }
}
