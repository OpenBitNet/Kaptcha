package com.mazepeng.kaptcha;

import com.mazepeng.kaptcha.api.*;
import com.mazepeng.kaptcha.exception.CaptchaGenerationException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

/**
 * 高分辨率优化后的验证码核心类
 */
public final class Captcha {

    private final String text;
    private final BufferedImage image;

    Captcha(CaptchaProducer producer) {
        CaptchaContent content = producer.getGenerator().generate();
        this.text = content.getAnswer();
        String drawText = content.getDrawText();

        int scale = 3;
        int realWidth = producer.getWidth() * scale;
        int realHeight = producer.getHeight() * scale;

        BufferedImage highResImage = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = highResImage.createGraphics();

        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            g.setColor(producer.getBackgroundColor());
            g.fillRect(0, 0, realWidth, realHeight);

            if (producer.getInterferer() != null) {
                producer.getInterferer().draw(g, realWidth, realHeight);
            }

            Font baseFont = producer.getFontProvider().getFont();
            Font font = baseFont.deriveFont((float) (baseFont.getSize() * scale));
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();

            int x = (realWidth - fm.stringWidth(drawText)) / 2;
            int y = (realHeight - fm.getHeight()) / 2 + fm.getAscent();

            if (drawText.length() > 1 && drawText.length() == this.text.length()) {
                for (int i = 0; i < drawText.length(); i++) {
                    g.setColor(producer.getColorizer().nextColor());
                    String charToDraw = String.valueOf(drawText.charAt(i));
                    int charWidth = fm.stringWidth(charToDraw);
                    g.drawString(charToDraw, x, y);
                    x += charWidth;
                }
            } else {
                g.setColor(producer.getColorizer().nextColor());
                g.drawString(drawText, x, y);
            }

        } catch (Exception e) {
            throw new CaptchaGenerationException("Failed to generate captcha image.", e);
        } finally {
            g.dispose();
        }

        this.image = new BufferedImage(producer.getWidth(), producer.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = this.image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(highResImage, 0, 0, producer.getWidth(), producer.getHeight(), null);
        g2.dispose();
    }

    public String getText() {
        return text;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void writeTo(OutputStream os) throws IOException {
        ImageIO.write(this.image, "png", os);
    }

    public String toBase64() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(this.image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new CaptchaGenerationException("Failed to convert captcha image to Base64.", e);
        }
    }
}
