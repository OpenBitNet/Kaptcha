package com.mazepeng.kaptcha.interferer;

import com.mazepeng.kaptcha.api.IInterferer;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

/**
 * 复合干扰器
 * 可以将多个干扰器组合在一起使用
 */
public class CompositeInterferer implements IInterferer {

    private final List<IInterferer> interferers;

    public CompositeInterferer(IInterferer... interferers) {
        this.interferers = Arrays.asList(interferers);
    }

    @Override
    public void draw(Graphics2D g, int width, int height) {
        for (IInterferer interferer : interferers) {
            if (interferer != null) {
                interferer.draw(g, width, height);
            }
        }
    }
}