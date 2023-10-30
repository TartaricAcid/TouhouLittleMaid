package com.github.tartaricacid.touhoulittlemaid.mclib.math;

import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.Function;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.classic.*;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.limit.Clamp;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.limit.Max;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.limit.Min;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.rounding.Ceil;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.rounding.Floor;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.rounding.Round;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.rounding.Trunc;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.functions.utility.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathBuilder {
    public Map<String, Variable> variables = new HashMap<String, Variable>();

    public Map<String, Class<? extends Function>> functions = new HashMap<String, Class<? extends Function>>();

    public MathBuilder() {
        /* 默认值 */
        this.register(new Variable("PI", Math.PI));
        this.register(new Variable("E", Math.E));

        /* 取整函数 */
        this.functions.put("floor", Floor.class);
        this.functions.put("round", Round.class);
        this.functions.put("ceil", Ceil.class);
        this.functions.put("trunc", Trunc.class);

        /* 比较函数 */
        this.functions.put("clamp", Clamp.class);
        this.functions.put("max", Max.class);
        this.functions.put("min", Min.class);

        /* 经典数学函数 */
        this.functions.put("abs", Abs.class);
        this.functions.put("exp", Exp.class);
        this.functions.put("ln", Ln.class);
        this.functions.put("sqrt", Sqrt.class);
        this.functions.put("mod", Mod.class);
        this.functions.put("pow", Pow.class);

        /* 三角函数 */
        this.functions.put("cos", Cos.class);
        this.functions.put("sin", Sin.class);
        this.functions.put("acos", ACos.class);
        this.functions.put("asin", ASin.class);
        this.functions.put("atan", ATan.class);
        this.functions.put("atan2", ATan2.class);

        /* 实用工具 */
        this.functions.put("lerp", Lerp.class);
        this.functions.put("lerprotate", LerpRotate.class);
        this.functions.put("random", Random.class);
        this.functions.put("randomi", RandomInteger.class);
        this.functions.put("roll", DieRoll.class);
        this.functions.put("rolli", DieRollInteger.class);
        this.functions.put("hermite", HermiteBlend.class);
    }

    /**
     * 注册变量
     */
    public void register(Variable variable) {
        this.variables.put(variable.getName(), variable);
    }

    /**
     * 将给定的数学表达式解析为可用于执行数学运算的 {@link IValue}
     */
    public IValue parse(String expression) throws Exception {
        return this.parseSymbols(this.breakdownChars(this.breakdown(expression)));
    }

    /**
     * 分解表达式
     */
    public String[] breakdown(String expression) throws Exception {
        // 如果给定的字符串包含非法字符，则无法解析
        if (!expression.matches("^[\\w\\d\\s_+-/*%^&|<>=!?:.,()]+$")) {
            throw new Exception("Given expression '" + expression + "' contains illegal characters!");
        }
        // 删除所有空格以及前导和尾随括号
        expression = expression.replaceAll("\\s+", "");
        String[] chars = expression.split("(?!^)");
        int left = 0;
        int right = 0;
        for (String s : chars) {
            if ("(".equals(s)) {
                left++;
            } else if (")".equals(s)) {
                right++;
            }
        }
        // 左括号和右括号的数量应相同
        if (left != right) {
            throw new Exception("Given expression '" + expression + "' has more uneven amount of parenthesis, there are " + left + " open and " + right + " closed!");
        }
        return chars;
    }

    /**
     * 将分解字符转换为数学表达式符号列表
     */
    public List<Object> breakdownChars(String[] chars) {
        List<Object> symbols = new ArrayList<>();
        String buffer = "";
        int len = chars.length;
        for (int i = 0; i < len; i++) {
            String s = chars[i];
            boolean longOperator = i > 0 && this.isOperator(chars[i - 1] + s);
            if (this.isOperator(s) || longOperator || ",".equals(s)) {
                // 使用负号翻转数值的用法判定
                if ("-".equals(s)) {
                    int size = symbols.size();
                    boolean isFirst = size == 0 && buffer.isEmpty();
                    boolean isOperatorBehind = size > 0 && (this.isOperator(symbols.get(size - 1)) || ",".equals(symbols.get(size - 1))) && buffer.isEmpty();
                    if (isFirst || isOperatorBehind) {
                        buffer += s;
                        continue;
                    }
                }
                if (longOperator) {
                    s = chars[i - 1] + s;
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                // 推送 buffer 和操作符
                if (!buffer.isEmpty()) {
                    symbols.add(buffer);
                    buffer = "";
                }
                symbols.add(s);
            } else if ("(".equals(s)) {
                if (!buffer.isEmpty()) {
                    symbols.add(buffer);
                    buffer = "";
                }
                int counter = 1;
                for (int j = i + 1; j < len; j++) {
                    String c = chars[j];
                    if ("(".equals(c)) {
                        counter++;
                    } else if (")".equals(c)) {
                        counter--;
                    }
                    if (counter == 0) {
                        symbols.add(this.breakdownChars(buffer.split("(?!^)")));
                        i = j;
                        buffer = "";
                        break;
                    } else {
                        buffer += c;
                    }
                }
            } else {
                buffer += s;
            }
        }
        if (!buffer.isEmpty()) {
            symbols.add(buffer);
        }
        return symbols;
    }

    @SuppressWarnings("unchecked")
    public IValue parseSymbols(List<Object> symbols) throws Exception {
        IValue ternary = this.tryTernary(symbols);
        if (ternary != null) {
            return ternary;
        }
        int size = symbols.size();
        // 常量、变量或组（括号）
        if (size == 1) {
            return this.valueFromObject(symbols.get(0));
        }
        // 函数
        if (size == 2) {
            Object first = symbols.get(0);
            Object second = symbols.get(1);
            if ((this.isVariable(first) || "-".equals(first)) && second instanceof List) {
                return this.createFunction((String) first, (List<Object>) second);
            }
        }
        // 其他数学表达式
        int lastOp = this.seekLastOperator(symbols);
        int op = lastOp;

        while (op != -1) {
            int leftOp = this.seekLastOperator(symbols, op - 1);
            if (leftOp != -1) {
                Operation left = this.operationForOperator((String) symbols.get(leftOp));
                Operation right = this.operationForOperator((String) symbols.get(op));
                if (right.value > left.value) {
                    IValue leftValue = this.parseSymbols(symbols.subList(0, leftOp));
                    IValue rightValue = this.parseSymbols(symbols.subList(leftOp + 1, size));
                    return new Operator(left, leftValue, rightValue);
                } else if (left.value > right.value) {
                    Operation initial = this.operationForOperator((String) symbols.get(lastOp));
                    if (initial.value < left.value) {
                        IValue leftValue = this.parseSymbols(symbols.subList(0, lastOp));
                        IValue rightValue = this.parseSymbols(symbols.subList(lastOp + 1, size));
                        return new Operator(initial, leftValue, rightValue);
                    }
                    IValue leftValue = this.parseSymbols(symbols.subList(0, op));
                    IValue rightValue = this.parseSymbols(symbols.subList(op + 1, size));
                    return new Operator(right, leftValue, rightValue);
                }
            }
            op = leftOp;
        }
        Operation operation = this.operationForOperator((String) symbols.get(lastOp));
        return new Operator(operation, this.parseSymbols(symbols.subList(0, lastOp)), this.parseSymbols(symbols.subList(lastOp + 1, size)));
    }

    protected int seekLastOperator(List<Object> symbols) {
        return this.seekLastOperator(symbols, symbols.size() - 1);
    }

    protected int seekLastOperator(List<Object> symbols, int offset) {
        for (int i = offset; i >= 0; i--) {
            Object o = symbols.get(i);
            if (this.isOperator(o)) {
                return i;
            }
        }
        return -1;
    }

    protected int seekFirstOperator(List<Object> symbols) {
        return this.seekFirstOperator(symbols, 0);
    }

    protected int seekFirstOperator(List<Object> symbols, int offset) {
        for (int i = offset, size = symbols.size(); i < size; i++) {
            Object o = symbols.get(i);
            if (this.isOperator(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 尝试解析三元表达式
     */
    protected IValue tryTernary(List<Object> symbols) throws Exception {
        int question = -1;
        int questions = 0;
        int colon = -1;
        int colons = 0;
        int size = symbols.size();
        for (int i = 0; i < size; i++) {
            Object object = symbols.get(i);
            if (object instanceof String) {
                if ("?".equals(object)) {
                    if (question == -1) {
                        question = i;
                    }
                    questions++;
                } else if (":".equals(object)) {
                    if (colons + 1 == questions && colon == -1) {
                        colon = i;
                    }
                    colons++;
                }
            }
        }
        if (questions == colons && question > 0 && question + 1 < colon && colon < size - 1) {
            return new Ternary(
                    this.parseSymbols(symbols.subList(0, question)),
                    this.parseSymbols(symbols.subList(question + 1, colon)),
                    this.parseSymbols(symbols.subList(colon + 1, size))
            );
        }

        return null;
    }

    /**
     * 创建函数值
     */
    protected IValue createFunction(String first, List<Object> args) throws Exception {
        // 取非
        if ("!".equals(first)) {
            return new Negate(this.parseSymbols(args));
        }
        if (first.startsWith("!") && first.length() > 1) {
            return new Negate(this.createFunction(first.substring(1), args));
        }
        // 取负
        if ("-".equals(first)) {
            return new Negative(new Group(this.parseSymbols(args)));
        }
        if (first.startsWith("-") && first.length() > 1) {
            return new Negative(this.createFunction(first.substring(1), args));
        }
        if (!this.functions.containsKey(first)) {
            throw new Exception("Function '" + first + "' couldn't be found!");
        }
        List<IValue> values = new ArrayList<>();
        List<Object> buffer = new ArrayList<>();
        for (Object o : args) {
            if (",".equals(o)) {
                values.add(this.parseSymbols(buffer));
                buffer.clear();
            } else {
                buffer.add(o);
            }
        }
        if (!buffer.isEmpty()) {
            values.add(this.parseSymbols(buffer));
        }
        Class<? extends Function> function = this.functions.get(first);
        Constructor<? extends Function> constructor = function.getConstructor(IValue[].class, String.class);
        return constructor.newInstance(values.toArray(new IValue[0]), first);
    }

    /**
     * 从对象中获取值
     */
    @SuppressWarnings("unchecked")
    public IValue valueFromObject(Object object) throws Exception {
        if (object instanceof String) {
            String symbol = (String) object;
            // 取非
            if (symbol.startsWith("!")) {
                return new Negate(this.valueFromObject(symbol.substring(1)));
            }
            if (this.isDecimal(symbol)) {
                return new Constant(Double.parseDouble(symbol));
            } else if (this.isVariable(symbol)) {
                // 取负
                if (symbol.startsWith("-")) {
                    symbol = symbol.substring(1);
                    Variable value = this.getVariable(symbol);

                    if (value != null) {
                        return new Negative(value);
                    }
                } else {
                    IValue value = this.getVariable(symbol);
                    // 避免 NPE
                    if (value != null) {
                        return value;
                    }
                }
            }
        } else if (object instanceof List) {
            return new Group(this.parseSymbols((List<Object>) object));
        }
        throw new Exception("Given object couldn't be converted to value! " + object);
    }

    protected Variable getVariable(String name) {
        return this.variables.get(name);
    }

    protected Operation operationForOperator(String op) throws Exception {
        for (Operation operation : Operation.values()) {
            if (operation.sign.equals(op)) {
                return operation;
            }
        }
        throw new Exception("There is no such operator '" + op + "'!");
    }

    protected boolean isVariable(Object o) {
        return o instanceof String && !this.isDecimal((String) o) && !this.isOperator((String) o);
    }

    protected boolean isOperator(Object o) {
        return o instanceof String && this.isOperator((String) o);
    }

    protected boolean isOperator(String s) {
        return Operation.OPERATORS.contains(s) || "?".equals(s) || ":".equals(s);
    }

    protected boolean isDecimal(String s) {
        return s.matches("^-?\\d+(\\.\\d+)?$");
    }
}
