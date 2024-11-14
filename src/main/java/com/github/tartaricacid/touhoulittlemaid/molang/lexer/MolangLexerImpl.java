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

package com.github.tartaricacid.touhoulittlemaid.molang.lexer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

import static java.util.Objects.requireNonNull;

final class MolangLexerImpl implements MolangLexer {

    // the source reader
    private final Reader reader;

    // the current index
    private final Cursor cursor = new Cursor();

    // the next character to be checked
    private int next;

    // the current token
    private Token lastToken = null;
    private Token token = null;

    MolangLexerImpl(final @NotNull Reader reader) throws IOException {
        this.reader = requireNonNull(reader, "reader");
        this.next = reader.read();
    }

    @Override
    public @NotNull Cursor cursor() {
        return cursor;
    }

    @Override
    public @NotNull Token current() {
        if (token == null) {
            throw new IllegalStateException("No current token, please call next() at least once");
        }
        return token;
    }

    @Override
    public @NotNull Token next() throws IOException {
        lastToken = token;
        return token = next0();
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

    private @NotNull Token next0() throws IOException {
        int c = next;
        if (c == -1) {
            // EOF reached
            return new Token(TokenKind.EOF, null, cursor.index(), cursor.index() + 1);
        }

        // skip whitespace (including tabs and newlines)
        while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
            c = read();
        }

        // additional spaces, lines, etc. at the end?
        if (c == -1) {
            // EOF reached
            return new Token(TokenKind.EOF, null, cursor.index(), cursor.index() + 1);
        }

        int start = cursor.index();
        if (c == '.' && lastToken != null && lastToken.kind() == TokenKind.RPAREN) {
            read();
            return new Token(TokenKind.DOT, null, start, cursor.index());
        }
        boolean isLastIdentifier = (lastToken != null && lastToken.kind() == TokenKind.IDENTIFIER);
        if (Characters.isDigit(c) || (!isLastIdentifier && c == '.')) {
            StringBuilder builder = new StringBuilder(8);
            if(!isLastIdentifier) {
                builder.appendCodePoint(c);

                // first char is a digit, continue reading number
                while (Characters.isDigit(c = read())) {
                    builder.appendCodePoint(c);
                }
            } else {
                builder.append('0');
            }

            if (c == '.') {
                builder.append('.');
                while (Characters.isDigit(c = read())) {
                    builder.appendCodePoint(c);
                }
            }

            return new Token(TokenKind.FLOAT, builder.toString(), start, cursor.index());
        } else if (Characters.isValidForWordStart(c)) {
            // may be an identifier or a keyword
            StringBuilder builder = new StringBuilder();
            do {
                builder.appendCodePoint(c);
            } while (Characters.isValidForWordContinuation(c = read()));
            String word = builder.toString().toLowerCase();
            TokenKind kind;
            switch (word) {
                //@formatter:off
                case "break": kind = TokenKind.BREAK; break;
                case "continue": kind = TokenKind.CONTINUE; break;
                case "return": kind = TokenKind.RETURN; break;
                case "true": kind = TokenKind.TRUE; break;
                case "false": kind = TokenKind.FALSE; break;
                default: kind = TokenKind.IDENTIFIER; break;
                //@formatter:on
            }

            return new Token(
                    kind,
                    // keywords do not have values
                    kind == TokenKind.IDENTIFIER ? word : null,
                    start,
                    cursor.index()
            );
        } else if (c == '\'') { // single quote means string start
            StringBuilder value = new StringBuilder(16);
            while (true) {
                c = read();
                if (c == -1) {
                    // the heck? you didn't close the string
                    return new Token(TokenKind.ERROR, "Found end-of-file before closing quote", start, cursor.index());
                } else if (c == '\'') {
                    // string was closed!
                    break;
                } else {
                    // TODO: should we allow escaping quotes? should we disallow line breaks?
                    // not end of file nor quote, this is inside the string literal
                    value.appendCodePoint(c);
                }
            }
            // Here, "c" should be a quote, so skip it and give it to the next person
            read();
            return new Token(TokenKind.STRING, value.toString(), start, cursor.index());
        } else {
            // here we are sure that "c" is NOT:
            // - EOF
            // - Single Quote (')
            // - A-Za-z_
            // - 0-9
            // so it must be some sign like ?, *, +, -
            TokenKind tokenKind;
            String value = null; // only set of token kind = ERROR, value is error message
            int c1 = -2; // only set if "c" may have a continuation, for example "==", "!=", "??"
            switch (c) {
                case '!': {
                    c1 = read();
                    if (c1 == '=') {
                        read();
                        tokenKind = TokenKind.BANGEQ;
                    } else {
                        tokenKind = TokenKind.BANG;
                    }
                    break;
                }
                case '&': {
                    c1 = read();
                    if (c1 == '&') {
                        read();
                        tokenKind = TokenKind.AMPAMP;
                    } else {
                        tokenKind = TokenKind.ERROR;
                        value = "Unexpected token '" + ((char) c1) + "', expected '&' (Molang doesn't support bitwise operators)";
                    }
                    break;
                }
                case '|': {
                    c1 = read();
                    if (c1 == '|') {
                        read();
                        tokenKind = TokenKind.BARBAR;
                    } else {
                        tokenKind = TokenKind.ERROR;
                        value = "Unexpected token '" + ((char) c1) + "', expected '|' (Molang doesn't support bitwise operators)";
                    }
                    break;
                }
                case '<': {
                    c1 = read();
                    if (c1 == '=') {
                        read();
                        tokenKind = TokenKind.LTE;
                    } else {
                        tokenKind = TokenKind.LT;
                    }
                    break;
                }
                case '>': {
                    c1 = read();
                    if (c1 == '=') {
                        read();
                        tokenKind = TokenKind.GTE;
                    } else {
                        tokenKind = TokenKind.GT;
                    }
                    break;
                }
                case '=': {
                    c1 = read();
                    if (c1 == '=') {
                        read();
                        tokenKind = TokenKind.EQEQ;
                    } else {
                        tokenKind = TokenKind.EQ;
                    }
                    break;
                }
                case '-': {
                    c1 = read();
                    if (c1 == '>') {
                        read();
                        tokenKind = TokenKind.ARROW;
                    } else {
                        tokenKind = TokenKind.SUB;
                    }
                    break;
                }
                case '?': {
                    c1 = read();
                    if (c1 == '?') {
                        read();
                        tokenKind = TokenKind.QUESQUES;
                    } else {
                        tokenKind = TokenKind.QUES;
                    }
                    break;
                }
                //@formatter:off
                case '/': tokenKind = TokenKind.SLASH; break;
                case '*': tokenKind = TokenKind.STAR; break;
                case '+': tokenKind = TokenKind.PLUS; break;
                case ',': tokenKind = TokenKind.COMMA; break;
                case '.': tokenKind = TokenKind.DOT; break;
                case '(': tokenKind = TokenKind.LPAREN; break;
                case ')': tokenKind = TokenKind.RPAREN; break;
                case '{': tokenKind = TokenKind.LBRACE; break;
                case '}': tokenKind = TokenKind.RBRACE; break;
                case ':': tokenKind = TokenKind.COLON; break;
                case '[': tokenKind = TokenKind.LBRACKET; break;
                case ']': tokenKind = TokenKind.RBRACKET; break;
                case ';': tokenKind = TokenKind.SEMICOLON; break;
                //@formatter:on
                default: {
                    // "c" is something we don't know about!
                    tokenKind = TokenKind.ERROR;
                    value = "Unexpected token '" + ((char) c) + "': invalid token";
                    break;
                }
            }

            if (c1 == -2) {
                // if token kind was known and the token didn't
                // check for an extra character
                read();
            }

            return new Token(tokenKind, value, start, cursor.index());
        }
    }

    private int read() throws IOException {
        int c = reader.read();
        cursor.push(c);
        next = c;
        return c;
    }

}
