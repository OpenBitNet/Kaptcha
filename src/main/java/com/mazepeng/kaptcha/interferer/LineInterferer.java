package com.mazepeng.kaptcha.interferer;

import com.mazepeng.kaptcha.api.IInterferer;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机直线干扰器
 */
public class LineInterferer implements IInterferer {

    private final int count;

    public LineInterferer(int count) {
        this.count = count;
    }

    @Override
    public void draw(Graphics2D g, int width, int height) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        g.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < count; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
    }
}