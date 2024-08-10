package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

import java.util.List;

public class EasingManager {
    public static double ease(double number, EasingType easingType, List<Double> easingArgs) {
        Double firstArg = easingArgs == null || easingArgs.isEmpty() ? null : easingArgs.get(0);
        return ease(number, easingType, firstArg);
    }

    private static double ease(double number, EasingType easingType, Double arg0) {
        return switch (easingType) {
            default -> number;
            case STEP -> step(arg0).apply(number);
        };
    }

    private static Double2DoubleFunction getEasingFuncImpl(EasingFunctionArgs args) {
        return switch (args.easingType()) {
            default -> in(EasingManager::linear);
            case STEP -> in(step(args.arg0()));
        };
    }

    private static Double2DoubleFunction in(Double2DoubleFunction easing) {
        return easing;
    }

    private static double linear(double t) {
        return t;
    }


    private static Double2DoubleFunction step(Double stepArg) {
        int steps = stepArg != null ? stepArg.intValue() : 2;
        double[] intervals = stepRange(steps);
        return t -> intervals[findIntervalBorderIndex(t, intervals, false)];
    }

    /**
     * The MIT License (MIT)
     * <p>
     * Copyright (c) 2015 Boris Chumichev
     * <p>
     * Permission is hereby granted, free of charge, to any person obtaining a copy
     * of this software and associated documentation files (the "Software"), to deal
     * in the Software without restriction, including without limitation the rights
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     * copies of the Software, and to permit persons to whom the Software is
     * furnished to do so, subject to the following conditions:
     * <p>
     * The above copyright notice and this permission notice shall be included in
     * all copies or substantial portions of the Software.
     * <p>
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
     * SOFTWARE.
     * <p>
     * /**
     * <p>
     * Utilizes bisection method to search an interval to which point belongs to,
     * then returns an index of left or right border of the interval
     */
    private static int findIntervalBorderIndex(double point, double[] intervals, boolean useRightBorder) {
        // 如果点超出给定间隔
        if (point < intervals[0]) {
            return 0;
        }
        if (point > intervals[intervals.length - 1]) {
            return intervals.length - 1;
        }
        // 如果点在区间内
        // 开始搜索全区间
        int indexOfNumberToCompare = 0;
        int leftBorderIndex = 0;
        int rightBorderIndex = intervals.length - 1;
        //  使用二分搜索，缩小搜索范围
        while (rightBorderIndex - leftBorderIndex != 1) {
            indexOfNumberToCompare = leftBorderIndex + (rightBorderIndex - leftBorderIndex) / 2;
            if (point >= intervals[indexOfNumberToCompare]) {
                leftBorderIndex = indexOfNumberToCompare;
            } else {
                rightBorderIndex = indexOfNumberToCompare;
            }
        }
        return useRightBorder ? rightBorderIndex : leftBorderIndex;
    }

    private static double[] stepRange(int steps) {
        final double stop = 1;
        if (steps < 2) {
            throw new IllegalArgumentException("steps must be > 2, got:" + steps);
        }
        double stepLength = stop / (double) steps;
        double[] stepArray = new double[steps];
        for (int i = 0; i < steps; i++) {
            stepArray[i] = i * stepLength;
        }
        return stepArray;
    }
}
