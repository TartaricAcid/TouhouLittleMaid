package com.github.tartaricacid.touhoulittlemaid.molang.runtime;

public interface Struct {
    Object getProperty(int name);

    void putProperty(int name, Object value);

    // FIXME: 作为 foreign 变量被访问时有线程安全问题
    Struct copy();
}
