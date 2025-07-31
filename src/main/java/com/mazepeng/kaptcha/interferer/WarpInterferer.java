package com.mazepeng.kaptcha.interferer;

import com.mazepeng.kaptcha.api.IInterferer;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 扭曲干扰器 (修正版)
 * 通过绘制平滑的、随机的波浪线来制造干扰
 */
public class WarpInterferer implements IInterferer {

    private final int lineCount;
    private final float strokeWidth;

    /**
     * 默认构造函数，画3条线
     */
    public WarpInterferer() {
        this(3, 1.8f);
    }

    /**
     * 构造函数
     * @param lineCount   要画的扭曲线条数量
     * @param strokeWidth 线条粗细
     */
    public WarpInterferer(int lineCount, float strokeWidth) {
        this.lineCount = lineCount;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Graphics2D g, int width, int height) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        g.setStroke(new BasicStroke(this.strokeWidth));

        for (int i = 0; i < this.lineCount; i++) {
            // 设置随机颜色
            g.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));

            // 定义波浪线的参数
            double amplitude = random.nextDouble() * (height / 4.0) + (height / 8.0); // 振幅
            double period = random.nextDouble() * (width / 2.0) + (width / 4.0);    // 周期
            double phase = random.nextDouble() * Math.PI * 2;                       // 相位

            // 使用 Path2D 来构建平滑的曲线
            Path2D.Double path = new Path2D.Double();

            // 计算起始点
            double startY = height / 2.0 + Math.sin(phase) * amplitude;
            path.moveTo(0, startY);

            // 逐点绘制波浪线
            for (int x = 1; x < width; x++) {
                double y = height / 2.0 + Math.sin((x / period) * 2 * Math.PI + phase) * amplitude;
                path.lineTo(x, y);
            }

            // 将构建好的路径绘制到画布上
            g.draw(path);
        }
    }
}
