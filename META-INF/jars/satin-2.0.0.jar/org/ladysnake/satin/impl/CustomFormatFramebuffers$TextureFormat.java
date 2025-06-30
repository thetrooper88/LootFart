package org.ladysnake.satin.impl;

public static enum CustomFormatFramebuffers.TextureFormat {
    RGBA8(32856),
    RGBA16(32859),
    RGBA16F(34842),
    RGBA32F(34836);

    public final int value;

    /*
     * Exception decompiling
     */
    public static CustomFormatFramebuffers.TextureFormat decode(String formatString) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private CustomFormatFramebuffers.TextureFormat(int value) {
        this.value = value;
    }
}
