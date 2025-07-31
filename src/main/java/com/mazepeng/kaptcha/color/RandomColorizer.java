package com.mazepeng.kaptcha.color;

import com.mazepeng.kaptcha.api.IColorizer;

import java.awt.Color;
import java.util.Random;

/**
 * 随机颜色选择器 (用于文字)
 */
public class RandomColorizer implements IColorizer {

    private final Random random = new Random();

    @Override
    public Color nextColor() {
        // 返回较深的颜色以保证可读性
        return new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150));
    }
}