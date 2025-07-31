package com.mazepeng.kaptcha.api;

import java.awt.*;

/**
 * 干扰器接口
 * 定义了如何在画布上添加干扰元素
 */
public interface IInterferer {

    /**
     * 在给定的画布上绘制干扰
     * @param g      Graphics2D 画笔
     * @param width  图片宽度
     * @param height 图片高度
     */
    void draw(Graphics2D g, int width, int height);
}
