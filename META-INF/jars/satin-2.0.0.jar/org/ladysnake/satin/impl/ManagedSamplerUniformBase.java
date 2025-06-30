package org.ladysnake.satin.impl;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_280;
import net.minecraft.class_283;
import net.minecraft.class_284;
import net.minecraft.class_5944;
import org.ladysnake.satin.api.managed.uniform.SamplerUniform;
import org.ladysnake.satin.impl.ManagedUniformBase;
import org.ladysnake.satin.impl.SamplerAccess;

public abstract class ManagedSamplerUniformBase
extends ManagedUniformBase
implements SamplerUniform {
    protected SamplerAccess[] targets = new SamplerAccess[0];
    protected int[] locations = new int[0];
    protected Object cachedValue;

    public ManagedSamplerUniformBase(String name) {
        super(name);
    }

    @Override
    public boolean findUniformTargets(List<class_283> shaders) {
        ArrayList targets = new ArrayList(shaders.size());
        IntArrayList rawTargets = new IntArrayList(shaders.size());
        for (class_283 shader : shaders) {
            class_280 program = shader.method_1295();
            SamplerAccess access = (SamplerAccess)program;
            if (!access.satin$hasSampler(this.name)) continue;
            targets.add((Object)access);
            rawTargets.add(this.getSamplerLoc(access));
        }
        this.targets = (SamplerAccess[])targets.toArray((Object[])new SamplerAccess[0]);
        this.locations = rawTargets.toArray(new int[0]);
        this.syncCurrentValues();
        return this.targets.length > 0;
    }

    private int getSamplerLoc(SamplerAccess access) {
        return (Integer)access.satin$getSamplerShaderLocs().get(access.satin$getSamplerNames().indexOf((Object)this.name));
    }

    @Override
    public boolean findUniformTarget(class_5944 shader) {
        return this.findUniformTarget((SamplerAccess)shader);
    }

    private boolean findUniformTarget(SamplerAccess access) {
        if (access.satin$hasSampler(this.name)) {
            this.targets = new SamplerAccess[]{access};
            this.locations = new int[]{this.getSamplerLoc(access)};
            this.syncCurrentValues();
            return true;
        }
        return false;
    }

    private void syncCurrentValues() {
        Object value = this.cachedValue;
        if (value != null) {
            this.cachedValue = null;
            this.set(value);
        }
    }

    protected abstract void set(Object var1);

    @Override
    public void setDirect(int activeTexture) {
        int length = this.locations.length;
        for (int i = 0; i < length; ++i) {
            this.targets[i].satin$removeSampler(this.name);
            class_284.method_22095((int)this.locations[i], (int)activeTexture);
        }
    }
}
