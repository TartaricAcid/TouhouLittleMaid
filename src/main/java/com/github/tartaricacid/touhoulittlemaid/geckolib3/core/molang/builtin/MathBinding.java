package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.binding.ContextBinding;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.math.*;

public class MathBinding extends ContextBinding {
    public static final MathBinding INSTANCE = new MathBinding();

    private MathBinding() {
        /* 常量 */
        constValue("pi", Math.PI);
        constValue("e", Math.E);

        /* 取整函数 */
        function("floor", new Floor());
        function("round", new Round());
        function("ceil", new Ceil());
        function("trunc", new Trunc());

        /* 比较函数 */
        function("clamp", new Clamp());
        function("max", new Max());
        function("min", new Min());

        /* 经典数学函数 */
        function("abs", new Abs());
        function("exp", new Exp());
        function("ln", new Ln());
        function("sqrt", new Sqrt());
        function("mod", new Mod());
        function("pow", new Pow());

        /* 三角函数 */
        function("sin", new Sin());     // degree
        function("cos", new Cos());     // degree
        function("acos", new ACos());
        function("asin", new ASin());
        function("atan", new Atan());
        function("atan2", new ATan2());

        /* 实用工具 */
        function("lerp", new Lerp());
        function("lerprotate", new LerpRotate());
        function("random", new Random());
        function("random_integer", new RandomInteger());
        function("die_roll", new DieRoll());
        function("die_roll_integer", new DieRollInteger());
        function("hermite_blend", new HermitBlend());

        /* 其它 */
        function("min_angle", new MinAngle());

        /* 非标准命名，兼容原 geckolib */
        function("randomi", new RandomInteger());
        function("roll", new DieRoll());
        function("rolli", new DieRollInteger());
        function("hermite", new HermitBlend());
    }
}
