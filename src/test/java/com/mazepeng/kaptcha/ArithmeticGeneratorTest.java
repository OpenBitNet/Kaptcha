package com.mazepeng.kaptcha;

import com.mazepeng.kaptcha.api.CaptchaContent;
import com.mazepeng.kaptcha.generator.ArithmeticGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import static org.assertj.core.api.Assertions.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

class ArithmeticGeneratorTest {

    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    @RepeatedTest(100) // 重复测试100次，以覆盖更多随机情况
    @DisplayName("生成的算术题答案应该与计算结果一致")
    void generatedAnswerShouldMatchCalculatedResult() throws ScriptException {
        // Arrange
        ArithmeticGenerator generator = new ArithmeticGenerator(20); // 使用稍大范围

        // Act
        CaptchaContent content = generator.generate();
        String answer = content.getAnswer();
        String question = content.getDrawText();

        // Assert
        assertThat(answer).isNotNull();
        assertThat(question).isNotNull().endsWith("= ?");

        // 使用JavaScript引擎来动态计算表达式的结果，这是一个很好的验证技巧
        String expression = question.replace("= ?", "")
                .replace("×", "*")
                .replace("÷", "/");

        Object result = engine.eval(expression);
        String calculatedAnswer = String.valueOf(Math.round(Double.parseDouble(result.toString())));

        assertThat(answer).isEqualTo(calculatedAnswer);
    }

    @RepeatedTest(50)
    @DisplayName("减法结果不应为负数")
    void subtractionResultShouldNotBeNegative() {
        // Arrange
        ArithmeticGenerator generator = new ArithmeticGenerator(100);

        // Act & Assert
        for (int i = 0; i < 100; i++) { // 多次生成来找到减法
            CaptchaContent content = generator.generate();
            if (content.getDrawText().contains("-")) {
                int answer = Integer.parseInt(content.getAnswer());
                assertThat(answer).isGreaterThanOrEqualTo(0);
            }
        }
    }
}