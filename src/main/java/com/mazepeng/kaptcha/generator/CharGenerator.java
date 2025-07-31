package com.mazepeng.kaptcha.generator;

import com.mazepeng.kaptcha.api.CaptchaContent;

/**
 * 默认的随机字符生成器
 */
public class CharGenerator extends AbstractGenerator {

    private final int len;
    private final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public CharGenerator(int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0.");
        }
        this.len = len;
    }

    @Override
    public CaptchaContent generate() {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(getRandom().nextInt(chars.length())));
        }
        String text = sb.toString();
        return new CaptchaContent(text, text);
    }
}
