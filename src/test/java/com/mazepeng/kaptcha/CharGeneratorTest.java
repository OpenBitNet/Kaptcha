package com.mazepeng.kaptcha;


import com.mazepeng.kaptcha.generator.CharGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.*;

class CharGeneratorTest {

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6})
    @DisplayName("生成的文本长度应该与指定的长度一致")
    void shouldGenerateTextWithCorrectLength(int length) {
        // Arrange
        CharGenerator generator = new CharGenerator(length);

        // Act
        String text = generator.generate().getAnswer();

        // Assert
        assertThat(text).isNotNull();
        assertThat(text.length()).isEqualTo(length);
    }

    @Test
    @DisplayName("生成的文本应该只包含字母和数字")
    void shouldGenerateTextWithAlphanumericChars() {
        // Arrange
        CharGenerator generator = new CharGenerator(100); // 生成长字符串以增加覆盖率

        // Act
        String text = generator.generate().getAnswer();

        // Assert
        assertThat(text).matches("^[a-zA-Z0-9]+$");
    }

    @Test
    @DisplayName("构造函数传入无效长度时应该抛出异常")
    void shouldThrowExceptionForInvalidLength() {
        // Arrange, Act & Assert
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new CharGenerator(0))
                .withMessage("Length must be greater than 0.");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new CharGenerator(-1))
                .withMessage("Length must be greater than 0.");
    }
}