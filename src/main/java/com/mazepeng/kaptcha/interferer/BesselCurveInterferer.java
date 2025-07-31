package com.mazepeng.kaptcha.interferer;
import com.mazepeng.kaptcha.api.IInterferer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 贝塞尔曲线干扰器
 */
public class BesselCurveInterferer implements IInterferer {

    private final int count;

    public BesselCurveInterferer(int count) {
        this.count = count;
    }

    @Override
    public void draw(Graphics2D g, int width, int height) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        g.setStroke(new BasicStroke(2.0f));
        for (int i = 0; i < count; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            int x1 = 5, y1 = random.nextInt(height);
            int x2 = width - 5, y2 = random.nextInt(height);

            int ctrlx1 = random.nextInt(width / 4) + width / 4;
            int ctrly1 = random.nextInt(height);
            int ctrlx2 = random.nextInt(width / 4) + width / 2;
            int ctrly2 = random.nextInt(height);

            g.draw(new CubicCurve2D.Double(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2));
        }
    }
}