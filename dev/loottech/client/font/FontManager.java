package dev.loottech.client.font;

import dev.loottech.client.font.FontRenderer;
import java.io.InputStream;

public class FontManager {
    public static FontManager INSTANCE = new FontManager();
    public static String font = "arial.ttf";
    public static final String assets = "assets/loottech/font/";
    public static int size = 20;
    public static boolean mcFont = false;
    public static FontRenderer arial = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/" + font), 20.0f, 1.0f, mcFont);
    public static FontRenderer times = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/times.ttf"), 20.0f, 1.25f, mcFont);
    public static FontRenderer verdana = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/verdana.ttf"), 18.0f, 1.0f, mcFont);
    public static FontRenderer bahnscrift = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/bahnscrift.ttf"), 20.0f, 1.0f, mcFont);
    public static FontRenderer comicsans = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/comicsans.ttf"), 20.0f, 1.0f, mcFont);
    public static FontRenderer robotoSmaller = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/" + font), 14.0f, 1.0f, mcFont);
    public static FontRenderer robotoSmall = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/" + font), 16.0f, 1.0f, mcFont);
    public static FontRenderer roboto = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/" + font), 18.0f, 1.0f, mcFont);
    public static FontRenderer robotoMed = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/" + font), 20.0f, 1.0f, mcFont);
    public static FontRenderer robotoMed2 = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/" + font), 22.0f, 1.0f, mcFont);
    public static FontRenderer robotoBig = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/" + font), 25.0f, 1.0f, mcFont);
    public static FontRenderer robotoCustomSize = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/" + font), size, 1.0f, mcFont);
    public static FontRenderer greycliff = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/greycliff.ttf"), 20.0f, 1.0f, mcFont);
    public static FontRenderer icons = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/icons.ttf"), 21.0f, 1.0f, false);
    public static FontRenderer iconsSmall = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/icons.ttf"), 18.0f, 1.0f, false);

    public static void setMcFont(boolean mcFont) {
        FontManager.mcFont = mcFont;
        arial = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), size, 1.0f, mcFont);
        verdana = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/verdana.ttf"), 18.0f, 1.0f, mcFont);
        bahnscrift = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/bahnscrift.ttf"), 20.0f, 1.0f, mcFont);
        robotoSmaller = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), 14.0f, 1.0f, mcFont);
        robotoSmall = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), 16.0f, 1.0f, mcFont);
        roboto = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), 18.0f, 1.0f, mcFont);
        robotoMed = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), 20.0f, 1.0f, mcFont);
        robotoMed2 = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), 22.0f, 1.0f, mcFont);
        robotoBig = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), 25.0f, 1.0f, mcFont);
        robotoCustomSize = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), size, 1.0f, mcFont);
        icons = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/icons.ttf"), 21.0f, 1.0f, false);
        iconsSmall = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/icons.ttf"), 18.0f, 1.0f, false);
        times = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), "assets/loottech/font/times.ttf"), 20.0f, 1.0f, mcFont);
    }

    public static void setSize(int size) {
        FontManager.size = size;
        robotoCustomSize = new FontRenderer(FontManager.getFileFromJar(INSTANCE.getClass().getClassLoader(), assets + font), size, 1.0f, mcFont);
    }

    public static InputStream getFileFromJar(ClassLoader classLoader, String path) {
        return classLoader.getResourceAsStream(path);
    }
}
