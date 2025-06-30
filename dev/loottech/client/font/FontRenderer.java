package dev.loottech.client.font;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.utilities.render.RenderUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_2561;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_5348;
import net.minecraft.class_5944;
import net.minecraft.class_757;
import net.minecraft.class_9801;
import org.apache.commons.codec.binary.Base64;
import org.joml.Matrix4f;

public class FontRenderer {
    private Font theFont;
    private Graphics2D theGraphics;
    private FontMetrics theMetrics;
    public float fontSize;
    private int startChar;
    private int endChar;
    private float[] xPos;
    private float[] yPos;
    public BufferedImage bufferedImage;
    public class_2960 resourceLocation;
    private final Pattern patternControlCode = Pattern.compile((String)"(?i)\\u00A7[0-9A-FK-OG]");
    private final Pattern patternUnsupported = Pattern.compile((String)"(?i)\\u00A7[L-O]");
    public boolean mcFont = false;
    private static class_310 mc = class_310.method_1551();

    public FontRenderer(Object font, float size) {
        this(font, size, 0.0f);
    }

    public FontRenderer(Object font) {
        this(font, 18.0f, 0.0f);
    }

    public FontRenderer(Object font, float size, float spacing) {
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        this.setupGraphics2D();
        this.createFont(font, size);
    }

    public FontRenderer(Object font, float size, float spacing, boolean mcFont) {
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        this.setupGraphics2D();
        this.createFont(font, size);
        this.mcFont = mcFont;
    }

    public void setFontSize(float size) {
        this.fontSize = size;
    }

    public float getFontSize() {
        return this.fontSize;
    }

    private final void setupGraphics2D() {
        this.bufferedImage = new BufferedImage(256, 256, 2);
        this.theGraphics = (Graphics2D)this.bufferedImage.getGraphics();
        this.theGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private final void createFont(Object font, float size) {
        try {
            String name;
            this.theFont = font instanceof Font ? (Font)font : (font instanceof File ? Font.createFont((int)0, (File)((File)font)).deriveFont(size) : (font instanceof InputStream ? Font.createFont((int)0, (InputStream)((InputStream)font)).deriveFont(size) : (font instanceof String ? (((String)font).toLowerCase().endsWith("ttf") || ((String)font).toLowerCase().endsWith("otf") ? Font.createFont((int)0, (File)new File(FontRenderer.mc.field_1697.getPath() + File.separator + "loottech" + File.separator + "assets/loottech/font", (String)font)).deriveFont(size) : new Font((String)font, 0, Math.round((float)size))) : (font instanceof class_2960 ? ((name = ((class_2960)font).method_12832()).toLowerCase().endsWith("ttf") || name.endsWith("otf") ? Font.createFont((int)0, (File)new File(FontRenderer.mc.field_1697.getPath() + File.separator + "loottech" + File.separator + "assets/loottech/font", name)).deriveFont(size) : new Font(name, 0, Math.round((float)size))) : new Font("Arial", 0, Math.round((float)size))))));
            this.theGraphics.setFont(this.theFont);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            this.theFont = new Font("Arial", 0, Math.round((float)size));
            this.theGraphics.setFont(this.theFont);
        }
        this.theGraphics.setColor(new Color(255, 255, 255, 0));
        this.theGraphics.fillRect(0, 0, 256, 256);
        this.theGraphics.setColor(Color.white);
        this.theMetrics = this.theGraphics.getFontMetrics();
        float x = 5.0f;
        float y = 5.0f;
        for (int i = this.startChar; i < this.endChar; ++i) {
            this.theGraphics.drawString(Character.toString((char)((char)i)), x, y + (float)this.theMetrics.getAscent());
            this.xPos[i - this.startChar] = x;
            this.yPos[i - this.startChar] = y - (float)this.theMetrics.getMaxDescent();
            x += (float)this.theMetrics.stringWidth(Character.toString((char)((char)i))) + 2.0f;
            if (!(x >= (float)(250 - this.theMetrics.getMaxAdvance()))) continue;
            x = 5.0f;
            y += (float)(this.theMetrics.getMaxAscent() + this.theMetrics.getMaxDescent()) + this.fontSize / 2.0f;
        }
        String base64 = this.imageToBase64String(this.bufferedImage, "png");
        this.setResourceLocation(base64, this.theFont, size);
    }

    private String imageToBase64String(BufferedImage image, String type) {
        String ret = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage)image, (String)type, (OutputStream)bos);
            byte[] bytes = bos.toByteArray();
            Base64 encoder = new Base64();
            ret = encoder.encodeAsString(bytes);
            ret = ret.replace((CharSequence)System.lineSeparator(), (CharSequence)"");
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
        return ret;
    }

    public void setResourceLocation(String base64, Object font, float size) {
        class_1011 image = FontRenderer.readTexture(base64);
        int imageWidth = image.method_4307();
        int imageHeight = image.method_4323();
        class_1011 imgNew = new class_1011(imageWidth, imageHeight, true);
        for (int x = 0; x < image.method_4307(); ++x) {
            for (int y = 0; y < image.method_4323(); ++y) {
                imgNew.method_4305(x, y, image.method_4315(x, y));
            }
        }
        image.close();
        this.resourceLocation = class_2960.method_60655((String)"loottech", (String)("post" + this.getFont().getFontName().toLowerCase().replace((CharSequence)" ", (CharSequence)"-") + size));
        this.applyTexture(this.resourceLocation, imgNew);
    }

    private static class_1011 readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64((String)textureBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
            return class_1011.method_4309((InputStream)bais);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void applyTexture(class_2960 identifier, class_1011 nativeImage) {
        class_310.method_1551().execute(() -> class_310.method_1551().method_1531().method_4616(identifier, (class_1044)new class_1043(nativeImage)));
    }

    public final void drawString(class_4587 matrixStack, String text, float x, float y, FontType fontType, int color, int color2) {
        text = this.stripUnsupported(text);
        matrixStack.method_22903();
        RenderUtils.setup2DRender(true);
        String text2 = this.stripControlCodes(text);
        switch (fontType.ordinal()) {
            case 1: {
                this.drawer(matrixStack, text2, x + 0.5f, y, color2);
                this.drawer(matrixStack, text2, x - 0.5f, y, color2);
                this.drawer(matrixStack, text2, x, y + 0.5f, color2);
                this.drawer(matrixStack, text2, x, y - 0.5f, color2);
                break;
            }
            case 2: {
                this.drawer(matrixStack, text2, x + 0.7f, y + 0.7f, color2);
                break;
            }
            case 3: {
                this.drawer(matrixStack, text2, x + 0.5f, y + 1.0f, color2);
                break;
            }
            case 4: {
                this.drawer(matrixStack, text2, x, y + 0.5f, color2);
                break;
            }
            case 5: {
                this.drawer(matrixStack, text2, x, y - 0.5f, color2);
                break;
            }
        }
        this.drawer(matrixStack, text, x, y, color);
        RenderUtils.end2DRender();
        matrixStack.method_22909();
    }

    public void drawCenteredString(class_4587 matrixStack, String text, float x, float y, int color) {
        if (!this.mcFont) {
            this.drawString(matrixStack, text, x - this.getStringWidth(text) / 2.0f, y, FontType.SHADOW_THIN, color);
        } else {
            FontRenderer.mc.field_1772.method_27522(text, x, y, color, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
        }
    }

    public final void drawString(class_4587 matrixStack, String text, float x, float y, FontType fontType, int color) {
        matrixStack.method_22903();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrixStack.method_22905(0.5f, 0.5f, 1.0f);
        this.drawString(matrixStack, text, x, y, fontType, color, -1157627904);
        matrixStack.method_22905(2.0f, 2.0f, 1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        matrixStack.method_22909();
    }

    private final void drawer(class_4587 matrixStack, String text, float x, float y, int color) {
        x *= 2.0f;
        y *= 2.0f;
        RenderUtils.setup2DRender(true);
        RenderUtils.bindTexture(this.resourceLocation);
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        int newColor = color;
        float startX = x;
        boolean scramble = false;
        class_289 tessellator = class_289.method_1348();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27377, class_290.field_1575);
        class_5944 prevShader = RenderSystem.getShader();
        RenderSystem.setShader(class_757::method_34543);
        for (int i = 0; i < text.length(); ++i) {
            char c;
            if (text.charAt(i) == '\u00a7' && i + 1 < text.length()) {
                char oneMore = Character.toLowerCase((char)text.charAt(i + 1));
                if (oneMore == 'n') {
                    y += (float)(this.theMetrics.getAscent() + 2);
                    x = startX;
                } else if (oneMore == 'k') {
                    scramble = true;
                } else {
                    newColor = oneMore == 'r' ? color : this.getColorFromCode(oneMore);
                }
                ++i;
                continue;
            }
            try {
                String obfText = "\\:><&%$@!/?";
                c = scramble ? obfText.charAt((int)(new Random().nextFloat() * (float)(obfText.length() - 1))) : text.charAt(i);
                this.drawChar(matrixStack, c, x, y, newColor);
                x += this.getStringWidth(Character.toString((char)c)) * 2.0f;
                continue;
            }
            catch (ArrayIndexOutOfBoundsException indexException) {
                c = text.charAt(i);
                System.err.println("Can't draw character: " + c + " (" + Character.getNumericValue((char)c) + ")");
            }
        }
        RenderSystem.setShader(() -> prevShader);
        RenderUtils.end2DRender();
    }

    private final Rectangle2D getBounds(String text) {
        return this.theMetrics.getStringBounds(text, (Graphics)this.theGraphics);
    }

    private final void drawChar(class_4587 matrixStack, char character, float x, float y, int color) throws ArrayIndexOutOfBoundsException {
        Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString((char)character), (Graphics)this.theGraphics);
        this.drawTexturedModalRect(matrixStack, x, y, this.xPos[character - this.startChar], this.yPos[character - this.startChar], (float)bounds.getWidth(), (float)bounds.getHeight() + (float)this.theMetrics.getMaxDescent() + 1.0f, color);
    }

    private final List<String> listFormattedStringToWidth(String s, int width) {
        return Arrays.asList((Object[])this.wrapFormattedStringToWidth(s, width).split("\n"));
    }

    private final String wrapFormattedStringToWidth(String s, float width) {
        int wrapWidth = this.sizeStringToWidth(s, width);
        if (s.length() <= wrapWidth) {
            return s;
        }
        String split = s.substring(0, wrapWidth);
        String split2 = this.getFormatFromString(split) + s.substring(wrapWidth + (s.charAt(wrapWidth) == ' ' || s.charAt(wrapWidth) == '\n' ? 1 : 0));
        try {
            return split + "\n" + this.wrapFormattedStringToWidth(split2, width);
        }
        catch (Exception e) {
            System.err.println("Cannot wrap string to width.");
            return "";
        }
    }

    private final int sizeStringToWidth(String par1Str, float par2) {
        int var5;
        int var3 = par1Str.length();
        float var4 = 0.0f;
        int var6 = -1;
        boolean var7 = false;
        for (var5 = 0; var5 < var3; ++var5) {
            char var8 = par1Str.charAt(var5);
            switch (var8) {
                case '\n': {
                    --var5;
                    break;
                }
                case '\u00a7': {
                    char var9;
                    if (var5 >= var3 - 1) break;
                    if ((var9 = par1Str.charAt(++var5)) != 'l' && var9 != 'L') {
                        if (var9 != 'r' && var9 != 'R' && !this.isFormatColor(var9)) break;
                        var7 = false;
                        break;
                    }
                    var7 = true;
                    break;
                }
                case ' ': {
                    var6 = var5;
                }
                case '-': {
                    var6 = var5;
                }
                case '_': {
                    var6 = var5;
                }
                case ':': {
                    var6 = var5;
                }
                default: {
                    String text = String.valueOf((char)var8);
                    var4 += this.getStringWidth(text);
                    if (!var7) break;
                    var4 += 1.0f;
                }
            }
            if (var8 == '\n') {
                var6 = ++var5;
                continue;
            }
            if (var4 > par2) break;
        }
        return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
    }

    private final String getFormatFromString(String par0Str) {
        String var1 = "";
        int var2 = -1;
        int var3 = par0Str.length();
        while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
            if (var2 >= var3 - 1) continue;
            char var4 = par0Str.charAt(var2 + 1);
            if (this.isFormatColor(var4)) {
                var1 = "\u00a7" + var4;
                continue;
            }
            if (!this.isFormatSpecial(var4)) continue;
            var1 = var1 + "\u00a7" + var4;
        }
        return var1;
    }

    private final boolean isFormatColor(char par0) {
        return par0 >= '0' && par0 <= '9' || par0 >= 'a' && par0 <= 'f' || par0 >= 'A' && par0 <= 'F';
    }

    private final boolean isFormatSpecial(char par0) {
        return par0 >= 'k' && par0 <= 'o' || par0 >= 'K' && par0 <= 'O' || par0 == 'r' || par0 == 'R';
    }

    private final void drawTexturedModalRect(class_4587 matrixStack, float x, float y, float u, float v, float width, float height, int color) {
        Matrix4f matrix4f = matrixStack.method_23760().method_23761();
        float scale = 0.0039063f;
        class_289 tessellator = class_289.method_1348();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        RenderSystem.disableDepthTest();
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        bufferBuilder.method_22918(matrix4f, x + 0.0f, y + height, 0.0f).method_22913((u + 0.0f) * scale, (v + height) * scale).method_22915(f1, f2, f3, f);
        bufferBuilder.method_22918(matrix4f, x + width, y + height, 0.0f).method_22913((u + width) * scale, (v + height) * scale).method_22915(f1, f2, f3, f);
        bufferBuilder.method_22918(matrix4f, x + width, y + 0.0f, 0.0f).method_22913((u + width) * scale, (v + 0.0f) * scale).method_22915(f1, f2, f3, f);
        bufferBuilder.method_22918(matrix4f, x + 0.0f, y + 0.0f, 0.0f).method_22913((u + 0.0f) * scale, (v + 0.0f) * scale).method_22915(f1, f2, f3, f);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.enableDepthTest();
    }

    public final String stripControlCodes(String s) {
        return this.patternControlCode.matcher((CharSequence)s).replaceAll("");
    }

    public final String stripUnsupported(String s) {
        return this.patternUnsupported.matcher((CharSequence)s).replaceAll("");
    }

    public final Graphics2D getGraphics() {
        return this.theGraphics;
    }

    public final Font getFont() {
        return this.theFont;
    }

    private int getColorFromCode(char code) {
        switch (code) {
            case '0': {
                return Color.BLACK.getRGB();
            }
            case '1': {
                return -16777046;
            }
            case '2': {
                return -16733696;
            }
            case '3': {
                return -16733526;
            }
            case '4': {
                return -5636096;
            }
            case '5': {
                return -5635926;
            }
            case '6': {
                return -22016;
            }
            case '7': {
                return -5592406;
            }
            case '8': {
                return -11184811;
            }
            case '9': {
                return -11184641;
            }
            case 'a': {
                return -11141291;
            }
            case 'b': {
                return -11141121;
            }
            case 'c': {
                return -43691;
            }
            case 'd': {
                return -43521;
            }
            case 'e': {
                return -171;
            }
            case 'f': {
                return -1;
            }
            case 'g': {
                return -2238971;
            }
        }
        return -1;
    }

    public float getStringWidth(String string) {
        if (!this.mcFont) {
            return (float)this.getBounds(this.stripControlCodes(string)).getWidth() / 2.0f;
        }
        return FontRenderer.mc.field_1772.method_1727(string);
    }

    public float getStringHeight(String string) {
        if (!this.mcFont) {
            return (float)(this.getBounds(this.stripControlCodes(string)).getHeight() / 2.0);
        }
        Objects.requireNonNull((Object)FontRenderer.mc.field_1772);
        return 9.0f;
    }

    public float getStringWidth(class_2561 string) {
        return FontRenderer.mc.field_1772.method_27525((class_5348)string);
    }

    public void drawWithShadow(class_4587 matrixStack, String text, float x, float y, int color) {
        if (!this.mcFont) {
            this.drawString(matrixStack, text, x, y - 3.0f, FontType.SHADOW_THIN, color);
        } else {
            class_4597.class_4598 vertexConsumers = mc.method_22940().method_23000();
            FontRenderer.mc.field_1772.method_27522(text, x, y + 2.0f, color, true, matrixStack.method_23760().method_23761(), (class_4597)vertexConsumers, class_327.class_6415.field_33994, 0, -1, false);
        }
    }

    public float getWidth(String string) {
        if (!this.mcFont) {
            return (float)(this.getBounds(this.stripControlCodes(string)).getWidth() / 2.0);
        }
        return FontRenderer.mc.field_1772.method_1727(string);
    }

    public void draw(class_4587 matrixStack, String text, float x, float y, int color) {
        if (!this.mcFont) {
            this.drawString(matrixStack, text, x, y - 3.0f, FontType.NORMAL, color);
        } else {
            FontRenderer.mc.field_1772.method_27522(text, x, y + 2.0f, color, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
        }
    }

    public void drawCenteredString(class_4587 matrixStack, String string, float x, float y, int color, boolean shadow) {
        float newX = x - this.getStringWidth(string) / 2.0f;
        if (!this.mcFont) {
            this.drawString(matrixStack, string, newX, y, shadow ? FontType.SHADOW_THIN : FontType.NORMAL, color);
        } else {
            FontRenderer.mc.field_1772.method_27522(string, x, y + 4.0f, color, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
        }
    }

    public void drawWithShadow(class_4587 matrixStack, class_2561 text, float x, float y, int color) {
        String s = text.getString();
        this.draw(matrixStack, s, x + 0.5f, y + 0.5f + 4.0f, -16777216);
        this.draw(matrixStack, s, x, y, color);
    }

    public void draw(class_4587 matrixStack, class_2561 text, float x, float y, int color) {
        FontRenderer.mc.field_1772.method_27522(String.valueOf((Object)text), x, y + 4.0f, color, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
    }

    public void drawCenteredString(class_4587 matrixStack, class_2561 string, float x, float y, int color) {
        float newX = x - this.getStringWidth(string) / 2.0f;
        this.drawWithShadow(matrixStack, string, newX, y, color);
    }

    public String fix(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        for (int i = 0; i < 9; ++i) {
            if (!s.contains((CharSequence)("\u00a7" + i))) continue;
            s = s.replace((CharSequence)("\u00a7" + i), (CharSequence)"");
        }
        return s.replace((CharSequence)"\u00a7a", (CharSequence)"").replace((CharSequence)"\u00a7b", (CharSequence)"").replace((CharSequence)"\u00a7c", (CharSequence)"").replace((CharSequence)"\u00a7d", (CharSequence)"").replace((CharSequence)"\u00a7e", (CharSequence)"").replace((CharSequence)"\u00a7f", (CharSequence)"").replace((CharSequence)"\u00a7g", (CharSequence)"");
    }

    public String trimToWidth(String string, int width) {
        try {
            return string.substring(0, width);
        }
        catch (Exception e) {
            return string;
        }
    }

    public String trimToWidth(String string, int width, boolean backwards) {
        try {
            return backwards ? string.substring(width) : string.substring(0, width);
        }
        catch (Exception e) {
            return string;
        }
    }

    public static enum FontType {
        NORMAL,
        SHADOW_THICK,
        SHADOW_THIN,
        OUTLINE_THIN,
        EMBOSS_TOP,
        EMBOSS_BOTTOM;

    }
}
