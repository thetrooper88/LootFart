package dev.loottech.api.utilities.interfaces;

import java.util.List;

public interface SamplerAccess {
    public boolean hasSampler(String var1);

    public List<String> getSamplerNames();

    public List<Integer> getSamplerShaderLocs();
}
