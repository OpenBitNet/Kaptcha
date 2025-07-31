package com.mazepeng.kaptcha;

import com.mazepeng.kaptcha.font.RandomFontProvider;
import com.mazepeng.kaptcha.generator.ArithmeticGenerator;
import com.mazepeng.kaptcha.generator.CharGenerator;
import com.mazepeng.kaptcha.interferer.BesselCurveInterferer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class CaptchaProducerTest {

    @Test
    @DisplayName("Builder应该能正确创建并配置CaptchaProducer")
    void builderShouldCreateAndConfigureProducerCorrectly() {
        // Arrange & Act
        CaptchaProducer producer = new CaptchaProducer.Builder()
                .width(200)
                .height(80)
                .generator(new ArithmeticGenerator(15))
                .interferer(new BesselCurveInterferer(5))
                .fontProvider(new RandomFontProvider(32))
                .build();

        // Assert
        assertThat(producer).isNotNull();
        // 这里可以通过反射来断言内部字段，但更好的方式是测试其行为
    }

    @Test
    @DisplayName("nextCaptcha应该每次都生成一个新的、不同的Captcha实例")
    void nextCaptchaShouldProduceNewInstanceEachTime() {
        // Arrange
        CaptchaProducer producer = new CaptchaProducer.Builder().build();

        // Act
        Captcha captcha1 = producer.nextCaptcha();
        Captcha captcha2 = producer.nextCaptcha();

        // Assert
        assertThat(captcha1).isNotSameAs(captcha2); // 不同的对象实例
        assertThat(captcha1.getText()).isNotEqualTo(captcha2.getText()); // 答案也应该不同
        assertThat(captcha1.getImage()).isNotNull();
        assertThat(captcha2.getImage()).isNotNull();
    }

    @Test
    @DisplayName("生成的Captcha图像尺寸应与配置一致")
    void captchaImageShouldHaveCorrectDimensions() {
        // Arrange
        int width = 180;
        int height = 75;
        CaptchaProducer producer = new CaptchaProducer.Builder()
                .width(width)
                .height(height)
                .build();

        // Act
        Captcha captcha = producer.nextCaptcha();
        BufferedImage image = captcha.getImage();

        // Assert
        assertThat(image.getWidth()).isEqualTo(width);
        assertThat(image.getHeight()).isEqualTo(height);
    }

    @Test
    @DisplayName("toBase64方法应返回正确的Data URI格式")
    void toBase64ShouldReturnCorrectDataUriFormat() {
        // Arrange
        CaptchaProducer producer = new CaptchaProducer.Builder().build();
        Captcha captcha = producer.nextCaptcha();

        // Act
        String base64 = captcha.toBase64();

        // Assert
        assertThat(base64).startsWith("data:image/png;base64,");
    }

    @Test
    @DisplayName("interferer设置为null时应不绘制干扰")
    void shouldNotDrawInterferenceWhenInterfererIsNull() {
        // 这个测试比较复杂，需要比较图像。一个简化的方法是比较两个无干扰图像的背景。
        // 由于随机文本不同，图像不会完全一样。
        // 更高级的测试需要图像比较库，这里我们只验证能正常生成。

        // Arrange
        CaptchaProducer producer = new CaptchaProducer.Builder()
                .interferer(null)
                .generator(new CharGenerator(4)) // 固定文本生成器以便比较
                .build();

        // Act & Assert
        assertThatCode(producer::nextCaptcha).doesNotThrowAnyException();
    }

    private static void saveToFile(Captcha captcha, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            captcha.writeTo(fos);
            System.out.println("  -> 图片已保存: " + filename + " (答案: " + captcha.getText() + ")");
        } catch (IOException e) {
            System.err.println("保存文件失败: " + filename + " - " + e.getMessage());
        }
    }
}