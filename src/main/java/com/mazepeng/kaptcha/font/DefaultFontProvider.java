package com.mazepeng.kaptcha.font;

import com.mazepeng.kaptcha.api.IFontProvider;

import java.awt.Font;

/**
 * 默认字体提供器
 */
public class DefaultFontProvider implements IFontProvider {

    private final int fontSize;

    public DefaultFontProvider() {
        this.fontSize = 32;
    }

    public DefaultFontProvider(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public Font getFont() {
        return new Font("Arial", Font.BOLD, this.fontSize);
    }
}