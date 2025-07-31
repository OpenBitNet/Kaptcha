package com.mazepeng.kaptcha.generator;

import com.mazepeng.kaptcha.api.CaptchaContent;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持四则运算的算术题生成器
 * <p>
 * 通过逻辑约束保证生成的题目易于计算：
 * - 减法：确保被减数大于减数，避免负数结果。
 * - 乘法：操作数限制在较小范围内（例如1-10），避免结果过大。
 * - 除法：通过“反向生成”（先生成结果和除数，再计算被除数）来确保能够整除。
 */
public class ArithmeticGenerator extends AbstractGenerator {

    private final int maxOperand;

    /**
     * 默认构造函数，操作数范围为 1-10
     */
    public ArithmeticGenerator() {
        this(10);
    }

    /**
     * 构造函数，可以指定操作数的最大值
     *
     * @param maxOperand 参与运算的数字的最大值（例如10，则数字范围为1-10）
     */
    public ArithmeticGenerator(int maxOperand) {
        if (maxOperand < 2) {
            throw new IllegalArgumentException("Max operand must be at least 2.");
        }
        this.maxOperand = maxOperand;
    }

    @Override
    public CaptchaContent generate() {
        char op = "+-*/".charAt(getRandom().nextInt(4));
        switch (op) {
            case '+':
                return generateAddition();
            case '-':
                return generateSubtraction();
            case '*':
                return generateMultiplication();
            case '/':
                return generateDivision();
            default:
                throw new IllegalStateException("Unreachable code");
        }
    }


    private CaptchaContent generateAddition() {
        int num1 = getRandom().nextInt(maxOperand) + 1;
        int num2 = getRandom().nextInt(maxOperand) + 1;
        String text = String.valueOf(num1 + num2);
        String drawText = num1 + " + " + num2 + " = ?";
        return new CaptchaContent(text, drawText);
    }

    private CaptchaContent generateSubtraction() {
        int num1 = getRandom().nextInt(maxOperand) + 1;
        int num2 = getRandom().nextInt(maxOperand) + 1;

        // 确保被减数大于等于减数
        if (num1 < num2) {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }
        String text = String.valueOf(num1 - num2);
        String drawText = num1 + " - " + num2 + " = ?";
        return new CaptchaContent(text, drawText);
    }

    private CaptchaContent generateMultiplication() {
        int num1 = getRandom().nextInt(maxOperand / 2 + 2);
        int num2 = getRandom().nextInt(maxOperand / 2 + 2);
        String text = String.valueOf(num1 * num2);
        String drawText = num1 + " × " + num2 + " = ?";
        return new CaptchaContent(text, drawText);
    }

    private CaptchaContent generateDivision() {
        int dividend;
        int divisor;
        int quotient;

        do {
            // 1. 先随机生成除数和商（结果）
            divisor = getRandom().nextInt(maxOperand - 1) + 2; // 除数不为0或1，从2开始
            quotient = getRandom().nextInt(maxOperand / 2 + 2); // 商也不宜过大

            // 2. 计算出被除数
            dividend = divisor * quotient;

            // 3. 如果被除数在合理范围内，则跳出循环
        } while (dividend > (maxOperand * 2));

        String text = String.valueOf(quotient);
        String drawText = dividend + " ÷ " + divisor + " = ?";
        return new CaptchaContent(text, drawText);
    }
}