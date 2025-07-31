package com.mazepeng.kaptcha.api;

import java.awt.Font;

/**
 * 字体提供器接口
 */
public interface IFontProvider {

    /**
     * 获取用于绘制验证码文本的字体
     * @return Font 对象
     */
    Font getFont();
}
