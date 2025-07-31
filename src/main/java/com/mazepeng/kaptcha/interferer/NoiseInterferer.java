package com.mazepeng.kaptcha.interferer;

import com.mazepeng.kaptcha.api.IInterferer;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 噪点干扰器
 * 在图片上随机画N个像素点
 */
public class NoiseInterferer implements IInterferer {

    private final float density; // 噪点密度，例如 0.7f

    /**
     * 默认构造函数，使用中等密度
     */
    public NoiseInterferer() {
        this.density = 0.7f;
    }

    /**
     * 构造函数
     * @param density 噪点密度，取值范围建议在 0.1 - 1.0 之间
     */
    public NoiseInterferer(float density) {
        this.density = density;
    }

    @Override
    public void draw(Graphics2D g, int width, int height) {
        // 计算要画的噪点数量 = 图片面积 * 密度 / 100
        int area = width * height;
        int noisePoints = (int) (area * density / 100.0f);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < noisePoints; i++) {
            // 在随机位置画一个 1x1 的小方块
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.fillRect(x, y, 1, 1);
        }
    }
}