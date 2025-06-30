package org.ladysnake.satin.impl;

import net.minecraft.class_276;
import net.minecraft.class_6367;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

public class CustomFormatFramebuffers {
    public static final String FORMAT_KEY = "satin:format";
    private static final ThreadLocal<TextureFormat> FORMAT = new ThreadLocal();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @API(status=API.Status.EXPERIMENTAL)
    public static class_276 create(int width, int height, boolean useDepth, boolean getError, TextureFormat format) {
        try {
            FORMAT.set((Object)format);
            class_6367 class_63672 = new class_6367(width, height, useDepth, getError);
            return class_63672;
        }
        finally {
            FORMAT.remove();
        }
    }

    public static void prepareCustomFormat(String formatString) {
        FORMAT.set((Object)TextureFormat.decode(formatString));
    }

    @Nullable
    public static TextureFormat getCustomFormat() {
        return (TextureFormat)((Object)FORMAT.get());
    }

    public static void clearCustomFormat() {
        FORMAT.remove();
    }

    public static enum TextureFormat {
        RGBA8(32856),
        RGBA16(32859),
        RGBA16F(34842),
        RGBA32F(34836);

        public final int value;

        /*
         * Exception decompiling
         */
        public static TextureFormat decode(String formatString) {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
             */
            throw new IllegalStateException("Decompilation failed");
        }

        private TextureFormat(int value) {
            this.value = value;
        }
    }
}
