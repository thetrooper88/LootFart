package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueNumber;

@RegisterModule(name="ViewModel", tag="ViewModel", description="", category=Module.Category.VISUALS)
public class ViewModel
extends Module {
    public ValueCategory translation = new ValueCategory("Translate", "The category for model translation.");
    public ValueNumber translateX = new ValueNumber("TranslateX", "X", "", this.translation, (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)-2.0f), (Number)Float.valueOf((float)2.0f));
    public ValueNumber translateY = new ValueNumber("TranslateY", "Y", "", this.translation, (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)-2.0f), (Number)Float.valueOf((float)2.0f));
    public ValueNumber translateZ = new ValueNumber("TranslateZ", "Z", "", this.translation, (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)-2.0f), (Number)Float.valueOf((float)2.0f));
    public ValueCategory rotation = new ValueCategory("Rotation", "The category for model translation.");
    public ValueNumber rotateX = new ValueNumber("RotateX", "X", "", this.rotation, (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)-180), (Number)Integer.valueOf((int)180));
    public ValueNumber rotateY = new ValueNumber("RotateY", "Y", "", this.rotation, (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)-180), (Number)Integer.valueOf((int)180));
    public ValueNumber rotateZ = new ValueNumber("RotateZ", "Z", "", this.rotation, (Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)-180), (Number)Integer.valueOf((int)180));
    public ValueCategory scale = new ValueCategory("Scale", "The category for model translation.");
    public ValueNumber scaleX = new ValueNumber("ScaleX", "X", "", this.scale, (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)3.0f));
    public ValueNumber scaleY = new ValueNumber("ScaleY", "Y", "", this.scale, (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)3.0f));
    public ValueNumber scaleZ = new ValueNumber("ScaleZ", "Z", "", this.scale, (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.0f), (Number)Float.valueOf((float)3.0f));
}
