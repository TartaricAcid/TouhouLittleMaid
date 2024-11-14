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

import java.util.*;

/**
 * An enum of token kinds. Represents a single token kind.
 *
 * <p>Tokens are, commonly, a sequence of one or more continuous
 * characters, like "??", "->", "!", "true", "1.0", "2.0", ...</p>
 *
 * <p>Tokens do not have an specific behavior, they just group
 * certain characters that can be used by the parser.</p>
 *
 * @since 3.0.0
 */
public enum TokenKind {

    /** End-of-file token, means that the end was reached */
    EOF,

    /** Error token, means that there was an error there */
    ERROR(Tag.HAS_VALUE),

    /** Identifier token, has a string value of the identifier name */
    IDENTIFIER(Tag.HAS_VALUE),

    /** String literal token, has a string value of its content */
    STRING(Tag.HAS_VALUE),

    /**
     * Float literal token, has a string value of its content,
     * which can be parsed to a floating-point number
     */
    FLOAT(Tag.HAS_VALUE),

    /** 'True' literal boolean token */
    TRUE,

    /** 'False' literal boolean token */
    FALSE,

    /** The "break" keyword */
    BREAK,

    /** The "continue" keyword */
    CONTINUE,

    /** The "return" keyword */
    RETURN,

    /** The dot symbol (.) */
    DOT,

    /** The bang or exclamation symbol (!) */
    BANG,

    /** Double ampersand token (&&) */
    AMPAMP,

    /** Double bar token (||) */
    BARBAR,

    /** Less-than token (<) */
    LT,

    /** Less-than-or-equal token (<=) */
    LTE,

    /** Greater-than token (>) */
    GT,

    /** Greater-than-or-equal token (>=) */
    GTE,

    /** Equal symbol (=) */
    EQ,

    /** Equal-equal token (==) */
    EQEQ,

    /** Bang-eq token (!=) */
    BANGEQ,

    /** Star symbol (*) */
    STAR,

    /** Slash symbol (/) */
    SLASH,

    /** Plus symbol (+) */
    PLUS,

    /** Hyphen/sub symbol (-) */
    SUB,

    /** Left-parenthesis symbol "(" */
    LPAREN,

    /** Right-parenthesis symbol ")" */
    RPAREN,

    /** Left-brace symbol "{" */
    LBRACE,

    /** Right-brace symbol "}" */
    RBRACE,

    /** Question-question token (??) */
    QUESQUES,

    /** Question symbol (?) */
    QUES,

    /** Colon symbol (:) */
    COLON,

    /** Arrow token (->) */
    ARROW,

    /** Left-bracket token "[" */
    LBRACKET,

    /** Right-bracket "] */
    RBRACKET,

    /** Comma symbol (,) */
    COMMA,

    /** Semicolon symbol (;) */
    SEMICOLON;

    private final Set<Tag> tags;

    TokenKind(final Tag...tags) {
        this.tags = EnumSet.copyOf(Arrays.asList(tags));
    }

    TokenKind() {
        this.tags = Collections.emptySet();
    }

    /**
     * Determines if this token kind has a certain
     * tag.
     *
     * @param tag The tag to check.
     * @return True if this token kind is tagged with
     * the given tag
     * @since 3.0.0
     */
    public boolean hasTag(final @NotNull Tag tag) {
        Objects.requireNonNull(tag, "tag");
        return tags.contains(tag);
    }

    /**
     * An enum of tags for token kinds. Tags specify
     * certain features of token kinds.
     *
     * @since 3.0.0
     */
    public enum Tag {

        /**
         * A token kind with HAS_VALUE tag will have a variable value,
         * for example, double or string literal tokens have variable
         * values, but they are still parsed with the same token kind.
         *
         * @since 3.0.0
         */
        HAS_VALUE

    }

}
