package dev.loottech.api.manager.shader;

import dev.loottech.api.manager.shader.ManagedUniformBase;
import dev.loottech.api.manager.shader.uniform.Uniform1f;
import dev.loottech.api.manager.shader.uniform.Uniform1i;
import dev.loottech.api.manager.shader.uniform.Uniform2f;
import dev.loottech.api.manager.shader.uniform.Uniform2i;
import dev.loottech.api.manager.shader.uniform.Uniform3f;
import dev.loottech.api.manager.shader.uniform.Uniform3i;
import dev.loottech.api.manager.shader.uniform.Uniform4f;
import dev.loottech.api.manager.shader.uniform.Uniform4i;
import dev.loottech.api.manager.shader.uniform.UniformMat4;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_283;
import net.minecraft.class_284;
import net.minecraft.class_5944;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class ManagedUniform
extends ManagedUniformBase
implements Uniform1i,
Uniform2i,
Uniform3i,
Uniform4i,
Uniform1f,
Uniform2f,
Uniform3f,
Uniform4f,
UniformMat4 {
    private static final class_284[] NO_TARGETS = new class_284[0];
    private final int count;
    private class_284[] targets = NO_TARGETS;
    private int i0;
    private int i1;
    private int i2;
    private int i3;
    private float f0;
    private float f1;
    private float f2;
    private float f3;
    private boolean firstUpload = true;

    public ManagedUniform(String name, int count) {
        super(name);
        this.count = count;
    }

    @Override
    public boolean findUniformTargets(List<class_283> shaders) {
        ArrayList list = new ArrayList();
        for (class_283 shader : shaders) {
            class_284 uniform = shader.method_1295().method_1271(this.name);
            if (uniform == null) continue;
            if (uniform.method_35661() != this.count) {
                throw new IllegalStateException("Mismatched number of values, expected " + this.count + " but JSON definition declares " + uniform.method_35661());
            }
            list.add((Object)uniform);
        }
        if (list.size() > 0) {
            this.targets = (class_284[])list.toArray((Object[])new class_284[0]);
            this.syncCurrentValues();
            return true;
        }
        this.targets = NO_TARGETS;
        return false;
    }

    @Override
    public boolean findUniformTarget(class_5944 shader) {
        class_284 uniform = shader.method_34582(this.name);
        if (uniform != null) {
            this.targets = new class_284[]{uniform};
            this.syncCurrentValues();
            return true;
        }
        this.targets = NO_TARGETS;
        return false;
    }

    private void syncCurrentValues() {
        if (!this.firstUpload) {
            for (class_284 target : this.targets) {
                if (target.method_35663() != null) {
                    target.method_1248(this.i0, this.i1, this.i2, this.i3);
                    continue;
                }
                assert (target.method_35664() != null);
                target.method_1252(this.f0, this.f1, this.f2, this.f3);
            }
        }
    }

    @Override
    public void set(int value) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0 && (this.firstUpload || this.i0 != value)) {
            for (class_284 target : targets) {
                target.method_35649(value);
            }
            this.i0 = value;
            this.firstUpload = false;
        }
    }

    @Override
    public void set(int value0, int value1) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0 && (this.firstUpload || this.i0 != value0 || this.i1 != value1)) {
            for (class_284 target : targets) {
                target.method_35650(value0, value1);
            }
            this.i0 = value0;
            this.i1 = value1;
            this.firstUpload = false;
        }
    }

    @Override
    public void set(int value0, int value1, int value2) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0 && (this.firstUpload || this.i0 != value0 || this.i1 != value1 || this.i2 != value2)) {
            for (class_284 target : targets) {
                target.method_35651(value0, value1, value2);
            }
            this.i0 = value0;
            this.i1 = value1;
            this.i2 = value2;
            this.firstUpload = false;
        }
    }

    @Override
    public void set(int value0, int value1, int value2, int value3) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0 && (this.firstUpload || this.i0 != value0 || this.i1 != value1 || this.i2 != value2 || this.i3 != value3)) {
            for (class_284 target : targets) {
                target.method_35656(value0, value1, value2, value3);
            }
            this.i0 = value0;
            this.i1 = value1;
            this.i2 = value2;
            this.i3 = value3;
            this.firstUpload = false;
        }
    }

    @Override
    public void set(float value) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0 && (this.firstUpload || this.f0 != value)) {
            for (class_284 target : targets) {
                target.method_1251(value);
            }
            this.f0 = value;
            this.firstUpload = false;
        }
    }

    @Override
    public void set(float value0, float value1) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0 && (this.firstUpload || this.f0 != value0 || this.f1 != value1)) {
            for (class_284 target : targets) {
                target.method_1255(value0, value1);
            }
            this.f0 = value0;
            this.f1 = value1;
            this.firstUpload = false;
        }
    }

    @Override
    public void set(Vector2f value) {
        this.set(value.x(), value.y());
    }

    @Override
    public void set(float value0, float value1, float value2) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0 && (this.firstUpload || this.f0 != value0 || this.f1 != value1 || this.f2 != value2)) {
            for (class_284 target : targets) {
                target.method_1249(value0, value1, value2);
            }
            this.f0 = value0;
            this.f1 = value1;
            this.f2 = value2;
            this.firstUpload = false;
        }
    }

    @Override
    public void set(Vector3f value) {
        this.set(value.x(), value.y(), value.z());
    }

    @Override
    public void set(float value0, float value1, float value2, float value3) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0 && (this.firstUpload || this.f0 != value0 || this.f1 != value1 || this.f2 != value2 || this.f3 != value3)) {
            for (class_284 target : targets) {
                target.method_35657(value0, value1, value2, value3);
            }
            this.f0 = value0;
            this.f1 = value1;
            this.f2 = value2;
            this.f3 = value3;
            this.firstUpload = false;
        }
    }

    @Override
    public void set(Vector4f value) {
        this.set(value.x(), value.y(), value.z(), value.w());
    }

    @Override
    public void set(Matrix4f value) {
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            for (class_284 target : targets) {
                target.method_1250(value);
            }
        }
    }

    @Override
    public void setFromArray(float[] values) {
        if (this.count != values.length) {
            throw new IllegalArgumentException("Mismatched values size, expected " + this.count + " but got " + values.length);
        }
        class_284[] targets = this.targets;
        int nbTargets = targets.length;
        if (nbTargets > 0) {
            for (class_284 target : targets) {
                target.method_1253(values);
            }
        }
    }
}
