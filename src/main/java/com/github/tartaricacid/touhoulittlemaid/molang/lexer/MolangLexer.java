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

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Lexical analyzer for the Molang language.
 *
 * <p>The lexical analyzer converts character streams
 * to token streams</p>
 *
 * <p>Note that this is a stream-based lexer, this means
 * that it will not consume the entire reader if it doesn't
 * continue having next() calls</p>
 *
 * <p>See the following example on correctly lexing a string:</p>
 * <pre>{@code
 *     MolangLexer lexer = MolangLexer.lexer(new StringReader("1 + 1"));
 *     List<Token> tokens = new ArrayList<>();
 *     Token token;
 *     while ((token = lexer.next()).kind() != TokenKind.EOF) {
 *         tokens.add(token);
 *     }
 *     // tokens: [ Double, Plus, Double ]
 * }</pre>
 *
 * <p>Or using the shorter, convenience method:</p>
 * <pre>{@code
 *      List<Token> tokens = MolangLexer.tokenizeAll("1 + 1");
 *      // tokens: [ Double, Plus, Double ]
 * }</pre>
 *
 * @since 3.0.0
 */
public /* sealed */ interface MolangLexer /* permits MolangLexerImpl */ extends Closeable {

    /**
     * Returns the cursor for this lexer, the cursor maintains
     * track of the current line and column, it is used for
     * error reporting.
     *
     * @return The lexer cursor
     * @since 3.0.0
     */
    @NotNull Cursor cursor();

    /**
     * Returns the last emitted token (the last token value
     * returned when calling {@link MolangLexer#next()})
     *
     * <p>Requires the user to call {@link MolangLexer#next()}
     * at least once first.</p>
     *
     * @return The last emitted token
     * @throws IllegalStateException If there is no current token
     * @since 3.0.0
     */
    @NotNull Token current();

    /**
     * Reads the internal reader until it gets a token and
     * then returns it.
     *
     * <p>The returned token will never be null, but it can
     * be of kind {@link TokenKind#EOF} or {@link TokenKind#ERROR}.</p>
     *
     * <p>We can stop lexing when we find a {@link TokenKind#EOF} token
     * for the first time, since following tokens will be EOF too.</p>
     *
     * @return The emitted token after reading characters from the internal reader
     * @throws IOException If reading fails
     * @since 3.0.0
     */
    @NotNull Token next() throws IOException;

    /**
     * Reads all the tokens until it finds a {@link TokenKind#EOF}.
     *
     * <p>After this method is called, the lexer should be
     * done and all next tokens should be EOF</p>
     *
     * @return All the read tokens
     * @throws IOException If reading fails
     * @since 3.0.0
     */
    default @NotNull List<Token> tokenizeAll() throws IOException {
        List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = next()).kind() != TokenKind.EOF) {
            tokens.add(token);
        }
        return tokens;
    }

    /**
     * Closes this lexer and the internal {@link Reader}.
     *
     * @throws IOException If closing fails
     * @since 3.0.0
     */
    @Override
    void close() throws IOException;

    /**
     * Creates a new lexer that will read the characters from the
     * given reader.
     *
     * @param reader The reader to use.
     * @return The created lexer
     * @throws IOException If lexer initialization fails.
     * @since 3.0.0
     */
    static @NotNull MolangLexer lexer(final @NotNull Reader reader) throws IOException {
        return new MolangLexerImpl(reader);
    }

    /**
     * Creates a new lexer that will read the characters from
     * the given string.
     *
     * @param string The string to tokenize.
     * @return The created lexer
     * @throws IOException If lexer initialization fails.
     * @since 3.0.0
     */
    static @NotNull MolangLexer lexer(final @NotNull String string) throws IOException {
        return lexer(new StringReader(string));
    }

    /**
     * Tokenizes all the data from the given reader.
     *
     * @param reader The reader.
     * @return The emitted tokens.
     * @throws IOException If reading fails.
     * @since 3.0.0
     */
    static @NotNull List<Token> tokenizeAll(final @NotNull Reader reader) throws IOException {
        try (MolangLexer lexer = lexer(reader)) {
            return lexer.tokenizeAll();
        }
    }

    /**
     * Tokenizes the provided string.
     *
     * @param string The string.
     * @return The emitted tokens.
     * @throws IOException If reading fails.
     * @since 3.0.0
     */
    static @NotNull List<Token> tokenizeAll(final @NotNull String string) throws IOException {
        try (MolangLexer lexer = lexer(string)) {
            return lexer.tokenizeAll();
        }
    }

}