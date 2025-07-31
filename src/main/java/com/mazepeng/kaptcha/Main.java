package com.mazepeng.kaptcha;

import com.mazepeng.kaptcha.api.IInterferer;
import com.mazepeng.kaptcha.font.RandomFontProvider;
import com.mazepeng.kaptcha.generator.ArithmeticGenerator;
import com.mazepeng.kaptcha.interferer.*;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("--- 1. 配置一个可复用的算术验证码生产者 ---");
// 同时使用直线、贝塞尔曲线和噪点！
        IInterferer composite = new CompositeInterferer(
                new LineInterferer(5),
                new BesselCurveInterferer(2),
                new NoiseInterferer(0.6f)
        );
        // 只配置一次，生成一个生产者
        CaptchaProducer mathProducer = new CaptchaProducer.Builder()
                .width(160)
                .height(60)
                .generator(new ArithmeticGenerator())
                .interferer(composite)
                .backgroundColor(new Color(240, 240, 240))
                .fontProvider(new RandomFontProvider(36))
                .build();

        System.out.println("生产者已创建，现在可以反复使用它来生成新的验证码。\n");


        System.out.println("--- 2. 第一次生成 ---");
        Captcha captcha1 = mathProducer.nextCaptcha();
        saveToFile(captcha1, "math_captcha_1.png");
        System.out.println("第一次生成的答案: " + captcha1.getText());



        System.out.println("\n--- 3. 第二次生成 (使用同一个生产者) ---");
        Captcha captcha2 = mathProducer.nextCaptcha();
        saveToFile(captcha2, "math_captcha_2.png");
        System.out.println("第二次生成的答案: " + captcha2.getText());


        System.out.println("\n--- 4. 第三次生成 (还是用同一个生产者) ---");
        Captcha captcha3 = mathProducer.nextCaptcha();
        saveToFile(captcha3, "math_captcha_3.png");
        System.out.println("第三次生成的答案: " + captcha3.getText());
    }

    private static void saveToFile(Captcha captcha, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            captcha.writeTo(fos);
        }
        System.out.println("验证码图片已保存到: " + filename);
    }
}
