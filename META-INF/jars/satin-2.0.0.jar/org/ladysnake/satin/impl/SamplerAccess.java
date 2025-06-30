package org.ladysnake.satin.impl;

import java.util.List;

public interface SamplerAccess {
    public void satin$removeSampler(String var1);

    public boolean satin$hasSampler(String var1);

    public List<String> satin$getSamplerNames();

    public List<Integer> satin$getSamplerShaderLocs();
}
