package com.github.tartaricacid.touhoulittlemaid.mclib.math.functions;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;


public abstract class Function implements IValue {
    protected IValue[] args;
    protected String name;

    public Function(IValue[] values, String name) throws Exception {
        if (values.length < this.getRequiredArguments()) {
            String message = String.format("Function '%s' requires at least %s arguments. %s are given!", this.getName(), this.getRequiredArguments(), values.length);
            throw new Exception(message);
        }
        this.args = values;
        this.name = name;
    }

    /**
     * 获取第 N 个参数的值
     */
    public double getArg(int index) {
        if (index < 0 || index >= this.args.length) {
            return 0;
        }
        return this.args[index].get();
    }

    @Override
        public String toString() {
        StringBuilder args = new StringBuilder();
        for (int i = 0; i < this.args.length; i++) {
            args.append(this.args[i].toString());
            if (i < this.args.length - 1) {
                args.append(", ");
            }
        }
        return this.getName() + "(" + args + ")";
    }

    /**
     * 获取函数名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 获取此函数所需的最小参数量
     */
        public int getRequiredArguments() {
        return 0;
    }
}
