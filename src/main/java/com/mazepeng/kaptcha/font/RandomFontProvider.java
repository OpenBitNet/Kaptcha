package com.mazepeng.kaptcha.font;

import com.mazepeng.kaptcha.api.IFontProvider;
import com.mazepeng.kaptcha.exception.CaptchaGenerationException;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机字体提供器
 * 从项目的 resources/fonts 目录中加载所有字体，并随机选择一个使用。
 */
public class RandomFontProvider implements IFontProvider {

    private final List<Font> fonts;
    private final int fontSize;

    /**
     * 默认构造函数，字体大小为32
     */
    public RandomFontProvider() {
        this(32);
    }

    /**
     * 构造函数
     * @param fontSize 字体大小
     */
    public RandomFontProvider(int fontSize) {
        this.fontSize = fontSize;
        this.fonts = loadFontsFromResources();
    }

    /**
     * 从 resources/fonts 目录加载字体文件
     * @return 加载好的字体列表
     */
    private List<Font> loadFontsFromResources() {
        List<Font> loadedFonts = new ArrayList<>();
        String fontDir = "/fonts";

        try {
            URI uri = RandomFontProvider.class.getResource(fontDir).toURI();
            Path fontPath;

            // 处理在 jar 包内和在普通文件系统中的不同情况
            if ("jar".equals(uri.getScheme())) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    fontPath = fileSystem.getPath(fontDir);
                }
            } else {
                fontPath = Paths.get(uri);
            }

            // 遍历目录下的所有 .ttf 和 .otf 文件
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(fontPath, "*.{ttf,otf}")) {
                for (Path entry : stream) {
                    try (InputStream is = Files.newInputStream(entry)) {
                        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                        loadedFonts.add(font);
                    } catch (FontFormatException | IOException e) {
                        System.err.println("加载字体失败: " + entry.getFileName() + " - " + e.getMessage());
                    }
                }
            }
        } catch (URISyntaxException | IOException | NullPointerException e) {
            throw new CaptchaGenerationException("无法读取字体目录: " + fontDir, e);
        }

        if (loadedFonts.isEmpty()) {
            System.err.println("警告: 字体目录 '" + fontDir + "' 中没有找到任何字体文件，将回退到默认字体。");
            // 添加一个默认字体作为后备
            loadedFonts.add(new Font("Arial", Font.BOLD, this.fontSize));
        }

        return loadedFonts;
    }

    @Override
    public Font getFont() {
        if (fonts.isEmpty()) {
            return new Font("Arial", Font.BOLD, this.fontSize);
        }
        Font baseFont = fonts.get(ThreadLocalRandom.current().nextInt(fonts.size()));
        return baseFont.deriveFont(Font.BOLD, this.fontSize);
    }
}
