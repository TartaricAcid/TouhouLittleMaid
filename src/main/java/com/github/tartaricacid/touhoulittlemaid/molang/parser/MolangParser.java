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

import com.github.tartaricacid.touhoulittlemaid.molang.lexer.Cursor;
import com.github.tartaricacid.touhoulittlemaid.molang.lexer.MolangLexer;
import com.github.tartaricacid.touhoulittlemaid.molang.lexer.TokenKind;
import com.github.tartaricacid.touhoulittlemaid.molang.parser.ast.Expression;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ObjectBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for the Molang language.
 *
 * <p>The parser converts token streams to expression
 * streams</p>
 *
 * <p>Note that this is a stream-based parser, this means
 * that it will not consume the entire lexer if it doesn't
 * continue having next() calls</p>
 *
 * @since 3.0.0
 */
public /* sealed */ interface MolangParser /* permits MolangParserImpl */ extends Closeable {

    /**
     * Returns the internal lexer being used.
     *
     * @return The lexer for this parser.
     * @since 3.0.0
     */
    @NotNull MolangLexer lexer();

    /**
     * Returns the cursor for this parser, the cursor maintains
     * track of the current line and column, it is used for
     * error reporting.
     *
     * @return The cursor.
     * @since 3.0.0
     */
    default @NotNull Cursor cursor() {
        //noinspection resource
        return lexer().cursor();
    }

    /**
     * Returns the last emitted expression (the last expression value
     * returned when calling {@link MolangParser#next()})
     *
     * <p>Requires the user to call {@link MolangParser#next()}
     * at least once first.</p>
     *
     * @return The last emitted expression
     * @throws IllegalStateException If there is no current expression
     * @since 3.0.0
     */
    @Nullable Expression current();

    /**
     * Parses the next expression.
     *
     * <p>This method returns {@code null} if it reaches
     * the end of file and throws a {@link ParseException}
     * if there is an error.</p>
     *
     * @return The parsed expression
     * @throws IOException If reading or parsing fails
     * @since 3.0.0
     */
    @Nullable Expression next() throws IOException;

    /**
     * Parses all the tokens until it finds a {@link TokenKind#EOF}.
     *
     * <p>After this method is called, the parser should be
     * done and all next expressions will be null</p>
     *
     * @return All the read expressions
     * @throws IOException If reading or parsing fails
     * @since 3.0.0
     */
    default @NotNull List<Expression> parseAll() throws IOException {
        List<Expression> tokens = new ArrayList<>();
        Expression expr;
        while ((expr = next()) != null) {
            tokens.add(expr);
        }
        return tokens;
    }

    /**
     * Closes this parser and the internal {@link MolangLexer}.
     *
     * @throws IOException If closing fails
     * @since 3.0.0
     */
    @Override
    void close() throws IOException;

    /**
     * Creates a new parser that will read the tokens from
     * the given lexer.
     *
     * @param lexer The lexer
     * @return The created parser
     * @throws IOException If parser initialization fails.
     * @since 3.0.0
     */
    static @NotNull MolangParser parser(final @NotNull MolangLexer lexer, @NotNull ObjectBinding binding) throws IOException {
        return new MolangParserImpl(lexer, binding);
    }

    /**
     * Creates a new parser that will read the tokens from
     * the given reader.
     *
     * @param reader The reader
     * @return The created parser
     * @throws IOException If parser initialization fails.
     * @since 3.0.0
     */
    static @NotNull MolangParser parser(final @NotNull Reader reader, @NotNull ObjectBinding binding) throws IOException {
        return parser(MolangLexer.lexer(reader), binding);
    }


    /**
     * Creates a new parser that will read the tokens from
     * the given string.
     *
     * @param string The string
     * @return The created parser
     * @throws IOException If parser initialization fails.
     * @since 3.0.0
     */
    static @NotNull MolangParser parser(final @NotNull String string, @NotNull ObjectBinding binding) throws IOException {
        return parser(MolangLexer.lexer(string), binding);
    }

    /**
     * Parses all the expressions from the given reader.
     *
     * @param reader The reader.
     * @return The emitted expressions.
     * @throws IOException If reading or parsing fails.
     * @since 3.0.0
     */
    static @NotNull List<Expression> parseAll(final @NotNull Reader reader, @NotNull ObjectBinding binding) throws IOException {
        try (MolangParser parser = parser(reader, binding)) {
            return parser.parseAll();
        }
    }

    /**
     * Parses the provided string.
     *
     * @param string The string.
     * @return The emitted expressions.
     * @throws IOException If reading or parsing fails.
     * @since 3.0.0
     */
    static @NotNull List<Expression> parseAll(final @NotNull String string, @NotNull ObjectBinding binding) throws IOException {
        try (MolangParser parser = parser(string, binding)) {
            return parser.parseAll();
        }
    }

}
