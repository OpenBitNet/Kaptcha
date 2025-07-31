package com.mazepeng.kaptcha.api;
import java.awt.Color;

/**
 * 颜色选择器接口
 */
public interface IColorizer {

    /**
     * 获取下一个颜色
     * @return Color 对象
     */
    Color nextColor();
}
