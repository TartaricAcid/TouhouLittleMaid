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

package com.github.tartaricacid.touhoulittlemaid.molang.parser;

import com.github.tartaricacid.touhoulittlemaid.molang.lexer.MolangLexer;
import com.github.tartaricacid.touhoulittlemaid.molang.lexer.Token;
import com.github.tartaricacid.touhoulittlemaid.molang.lexer.TokenKind;
import com.github.tartaricacid.touhoulittlemaid.molang.parser.ast.*;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Function;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ObjectBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

final class MolangParserImpl implements MolangParser {
    private static final int PRECEDENCE_QUES = 1400;   // 三元条件运算符优先级的值必须与其他运算符不同
    private static final Object UNSET_FLAG = new Object();

    private final MolangLexer lexer;
    private final ObjectBinding binding;

    // the last parsed expression, returned by next()
    // we have to use Object and a flag since null is a valid value too
    private @Nullable Object current = UNSET_FLAG;

    MolangParserImpl(final @NotNull MolangLexer lexer, @NotNull ObjectBinding binding) {
        this.lexer = requireNonNull(lexer, "lexer");
        this.binding = requireNonNull(binding, "binding");
    }

    //
    // Parses a single expression.
    // Single expressions don't require a left-hand expression
    // to be parsed, e.g. literals, statements, identifiers,
    // wrapped expressions and execution scopes
    //
    @NotNull
    Expression parseSingle(final @NotNull MolangLexer lexer) throws IOException {
        Token token = lexer.current();
        switch (token.kind()) {
            case FLOAT:
                lexer.next();
                return new DoubleExpression(Double.parseDouble(token.value()));
            case STRING:
                lexer.next();
                return new StringExpression(token.value());
            case TRUE:
                lexer.next();
                return DoubleExpression.ONE;
            case FALSE:
                lexer.next();
                return DoubleExpression.ZERO;
            case LPAREN:
                lexer.next();
                // wrapped expression: (expression)
                Expression expression = parseCompoundExpression(lexer, 0);
                token = lexer.current();
                if (token.kind() != TokenKind.RPAREN) {
                    throw new ParseException("Non closed expression", lexer.cursor());
                }
                lexer.next();
                return expression;
            case LBRACE:
                lexer.next();
                List<Expression> expressions = new ArrayList<>();
                while (true) {
                    expressions.add(parseCompoundExpression(lexer, 0));
                    token = lexer.current();
                    if (token.kind() == TokenKind.RBRACE) {
                        lexer.next();
                        break;
                    } else if (token.kind() == TokenKind.EOF) {
                        // end reached but not closed yet, huh?
                        throw new ParseException(
                                "Found the end before the execution scope closing token",
                                lexer.cursor()
                        );
                    } else if (token.kind() == TokenKind.ERROR) {
                        throw new ParseException("Found an invalid token (error): " + token.value(), lexer.cursor());
                    } else {
                        if (token.kind() != TokenKind.SEMICOLON) {
                            throw new ParseException("Missing semicolon", lexer.cursor());
                        }
                        lexer.next();
                    }
                }
                return new ExecutionScopeExpression(expressions);
            case BREAK:
                lexer.next();
                return new StatementExpression(StatementExpression.Op.BREAK);
            case CONTINUE:
                lexer.next();
                return new StatementExpression(StatementExpression.Op.CONTINUE);
            case IDENTIFIER:
                Object lastTarget = binding.getProperty(token.value());
                if (lastTarget == null) {
                    throw new ParseException("Failed to get property: " + token.value(), lexer.cursor());
                }
                Expression expr = IdentifierExpression.get(token.value(), lastTarget);
                token = lexer.next();
                if (token.kind() == TokenKind.DOT) {
                    token = lexer.next();

                    if (token.kind() != TokenKind.IDENTIFIER) {
                        throw new ParseException("Unexpected token, expected a valid field token", lexer.cursor());
                    }

                    if (lastTarget instanceof ObjectBinding) {
                        lastTarget = ((ObjectBinding) lastTarget).getProperty(token.value());
                    } else {
                        throw new ParseException("Illegal access to : " + token.value(), lexer.cursor());
                    }
                    if (lastTarget == null) {
                        throw new ParseException("Failed to get property: " + token.value(), lexer.cursor());
                    }

                    expr = IdentifierExpression.get(token.value(), lastTarget);
                    lexer.next();
                }

                return expr;
            case PLUS:
                lexer.next();
                return parseCompoundExpression(lexer, UnaryExpression.Op.PLUS.precedence());
            case SUB:
                lexer.next();
                return new UnaryExpression(UnaryExpression.Op.ARITHMETICAL_NEGATION, parseCompoundExpression(lexer, UnaryExpression.Op.ARITHMETICAL_NEGATION.precedence()));
            case BANG:
                lexer.next();
                return new UnaryExpression(UnaryExpression.Op.LOGICAL_NEGATION, parseCompoundExpression(lexer, UnaryExpression.Op.LOGICAL_NEGATION.precedence()));
            case RETURN:
                lexer.next();
                return new UnaryExpression(UnaryExpression.Op.RETURN, parseCompoundExpression(lexer, UnaryExpression.Op.RETURN.precedence()));
        }

        throw new ParseException("Expected an expression.", lexer.cursor());
    }

    @NotNull
    Expression parseCompoundExpression(
            final @NotNull MolangLexer lexer,
            final int lastPrecedence
    ) throws IOException {
        Expression expr = parseSingle(lexer);
        while (true) {
            final Expression compoundExpr = parseCompound(lexer, expr, lastPrecedence);

            // current token
            final Token current = lexer.current();
            if (current.kind() == TokenKind.EOF || current.kind() == TokenKind.SEMICOLON) {
                // found eof, stop parsing, return expr
                return compoundExpr;
            } else if (compoundExpr == expr) {
                return expr;
            }

            expr = compoundExpr;
        }
    }

    @NotNull
    Expression parseCompound(
            final @NotNull MolangLexer lexer,
            final @NotNull Expression left,
            final int lastPrecedence
    ) throws IOException {
        Token current = lexer.current();

        switch (current.kind()) {
            case RPAREN:
            case EOF:
                return left;
            case LPAREN: { // CALL EXPRESSION: "left("
                if (left instanceof IdentifierExpression) {
                    lexer.next();
                    final List<Expression> arguments = new ArrayList<>();

                    // start reading the arguments
                    while (true) {
                        arguments.add(parseCompoundExpression(lexer, 0));
                        // update current character
                        current = lexer.current();
                        if (current.kind() == TokenKind.EOF) {
                            throw new ParseException("Found EOF before closing RPAREN", null);
                        } else if (current.kind() == TokenKind.RPAREN) {
                            lexer.next();
                            break;
                        } else {
                            if (current.kind() != TokenKind.COMMA) {
                                throw new ParseException("Expected a comma", lexer.cursor());
                            }
                            lexer.next();
                        }
                    }

                    Object target = ((IdentifierExpression) left).target();
                    String name = ((IdentifierExpression) left).name();
                    if (target instanceof Function) {
                        Function func = (Function) target;
                        if (!func.validateArgumentSize(arguments.size())) {
                            throw new ParseException("Function call to \"" + name + "\" has illegal parameter size", null);
                        }
                        return new CallExpression(func, new Function.ArgumentCollection(arguments));
                    }
                    throw new ParseException("\"" + name + "\" is not a function", null);
                } else {
                    if (lastPrecedence >= BinaryExpression.Op.MUL.precedence()) {
                        return left;
                    }
                    Expression right = parseCompoundExpression(lexer, BinaryExpression.Op.MUL.precedence());
                    return new BinaryExpression(BinaryExpression.Op.MUL, left, right);
                }
            }
            case QUES: {
                // 嵌套的三元条件表达式从右往左执行
                if (lastPrecedence > PRECEDENCE_QUES) {
                    return left;
                }

                lexer.next();
                final Expression trueValue = parseCompoundExpression(lexer, PRECEDENCE_QUES);

                if (lexer.current().kind() == TokenKind.COLON) {
                    // then it's a ternary expression, since there is a ':', indicating the next expression
                    lexer.next();
                    return new TernaryConditionalExpression(left, trueValue, parseCompoundExpression(lexer, PRECEDENCE_QUES));
                } else {
                    return new BinaryExpression(BinaryExpression.Op.CONDITIONAL, left, trueValue);
                }
            }
        }

        if (current.kind() == TokenKind.DOT) {
            current = lexer.next();
            if (current.kind() == TokenKind.IDENTIFIER) {
                lexer.next();
                return new StructAccessExpression(left, current.value());
            } else {
                throw new ParseException("Expect a identifier after struct access operator", lexer.cursor());
            }
        }

        // check for binary expressions
        final BinaryExpression.Op op;

        // @formatter:off
        // I wish this was java 17
        switch (current.kind()) {
            case AMPAMP: op = BinaryExpression.Op.AND; break;
            case BARBAR: op = BinaryExpression.Op.OR; break;
            case LT: op = BinaryExpression.Op.LT; break;
            case LTE: op = BinaryExpression.Op.LTE; break;
            case GT: op = BinaryExpression.Op.GT; break;
            case GTE: op = BinaryExpression.Op.GTE; break;
            case PLUS: op = BinaryExpression.Op.ADD; break;
            case SUB: op = BinaryExpression.Op.SUB; break;
            case STAR: op = BinaryExpression.Op.MUL; break;
            case SLASH: op = BinaryExpression.Op.DIV; break;
            case QUESQUES: op = BinaryExpression.Op.NULL_COALESCE; break;
            case EQ: op = BinaryExpression.Op.ASSIGN; break;
            case EQEQ: op = BinaryExpression.Op.EQ; break;
            case BANGEQ: op = BinaryExpression.Op.NEQ; break;
            case ARROW: op = BinaryExpression.Op.ARROW; break;
            default: return left;
        }
        // @formatter:on

        final int precedence = op.precedence();
        if (lastPrecedence >= precedence) {
            return left;
        }

        lexer.next();
        return new BinaryExpression(op, left, parseCompoundExpression(lexer, precedence));
    }

    @Override
    public @NotNull MolangLexer lexer() {
        return lexer;
    }

    @Override
    public @Nullable Expression current() {
        if (current == UNSET_FLAG) {
            throw new IllegalStateException("No current parsed expression, call next() at least once!");
        }
        return (Expression) current;
    }

    @Override
    public @Nullable Expression next() throws IOException {
        final Expression expr = next0();
        current = expr;
        return expr;
    }

    //
    // Parses an expression until it finds an unexpected token,
    // a semicolon, or an end-of-file token.
    //
    private @Nullable Expression next0() throws IOException {
        Token token = lexer.next();

        if (token.kind() == TokenKind.EOF) {
            // reached end-of-file!
            return null;
        }

        if (token.kind() == TokenKind.ERROR) {
            // tokenization error!
            throw new ParseException("Found an invalid token (error): " + token.value(), cursor());
        }

        final Expression expression = parseCompoundExpression(lexer, -10);

        // check current token, should be a semicolon or an eof
        token = lexer.current();
        if (token.kind() != TokenKind.EOF && token.kind() != TokenKind.SEMICOLON) {
            throw new ParseException("Expected a semicolon, but was " + token, lexer.cursor());
        }

        return expression;
    }

    @Override
    public void close() throws IOException {
        this.lexer.close();
    }
}