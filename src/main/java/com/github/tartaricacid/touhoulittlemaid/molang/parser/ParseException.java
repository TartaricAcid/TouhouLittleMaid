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

import java.io.IOException;

/**
 * Exception that can be thrown during the
 * parsing phase
 *
 * @since 3.0.0
 */
public class ParseException extends IOException {

    private final Cursor cursor;

    public ParseException(Cursor cursor) {
        this.cursor = cursor;
    }

    public ParseException(String message, Cursor cursor) {
        super(appendCursor(message, cursor));
        this.cursor = cursor;
    }

    public ParseException(Throwable cause, Cursor cursor) {
        super(cause);
        this.cursor = cursor;
    }

    public ParseException(String message, Throwable cause, Cursor cursor) {
        super(appendCursor(message, cursor), cause);
        this.cursor = cursor;
    }

    public Cursor cursor() {
        return cursor;
    }

    private static String appendCursor(String message, Cursor cursor) {
        if (cursor == null) return message; // todo
        // default format for exception messages, i.e.
        // "unexpected token: '%'"
        // "    at line 2, column 6"
        return message + "\n  at " + cursor.toString();
    }

}
