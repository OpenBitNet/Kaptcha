package com.mazepeng.kaptcha;

import com.mazepeng.kaptcha.api.IColorizer;
import com.mazepeng.kaptcha.api.IFontProvider;
import com.mazepeng.kaptcha.api.IGenerator;
import com.mazepeng.kaptcha.api.IInterferer;
import com.mazepeng.kaptcha.color.RandomColorizer;
import com.mazepeng.kaptcha.font.DefaultFontProvider;
import com.mazepeng.kaptcha.generator.CharGenerator;
import com.mazepeng.kaptcha.interferer.LineInterferer;

import java.awt.*;
import java.util.Objects;

public class CaptchaProducer {
    // 所有配置信息都保存在这里
    private final int width;
    private final int height;
    private final IGenerator generator;
    private final IInterferer interferer;
    private final IFontProvider fontProvider;
    private final IColorizer colorizer;
    private final Color backgroundColor;

    // 构造函数由 Builder 调用
    private CaptchaProducer(CaptchaProducer.Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.generator = builder.generator;
        this.interferer = builder.interferer;
        this.fontProvider = builder.fontProvider;
        this.colorizer = builder.colorizer;
        this.backgroundColor = builder.backgroundColor;
    }

    /**
     * 生成一个新的、随机的验证码实例。
     * 这是核心方法，可以被反复调用。
     * @return 一个新的 Captcha 对象
     */
    public Captcha nextCaptcha() {
        return new Captcha(this);
    }

    // 内部 getter，供 Captcha 类访问配置
    int getWidth() { return width; }
    int getHeight() { return height; }
    IGenerator getGenerator() { return generator; }
    IInterferer getInterferer() { return interferer; }
    IFontProvider getFontProvider() { return fontProvider; }
    IColorizer getColorizer() { return colorizer; }
    Color getBackgroundColor() { return backgroundColor; }


    /**
     * Builder 类，现在它的 build() 方法返回一个 CaptchaProducer
     */
    public static class Builder {

         int width = 120;
         int height = 40;
         IGenerator generator = new CharGenerator(4);
         IInterferer interferer = new LineInterferer(10);
         IFontProvider fontProvider = new DefaultFontProvider(32);
         IColorizer colorizer = new RandomColorizer();
         Color backgroundColor = Color.WHITE;

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder generator(IGenerator generator) {
            this.generator = Objects.requireNonNull(generator);
            return this;
        }

        public Builder interferer(IInterferer interferer) {
            this.interferer = interferer;
            return this;
        }

        public Builder fontProvider(IFontProvider fontProvider) {
            this.fontProvider = Objects.requireNonNull(fontProvider);
            return this;
        }

        public Builder colorizer(IColorizer colorizer) {
            this.colorizer = Objects.requireNonNull(colorizer);
            return this;
        }

        public Builder backgroundColor(Color color) {
            this.backgroundColor = Objects.requireNonNull(color);
            return this;
        }

        /**
         * 构建最终的 CaptchaProducer 对象
         * @return 一个配置好的、可复用的 CaptchaProducer 实例
         */
        public CaptchaProducer build() {
            return new CaptchaProducer(this);
        }
    }
}
