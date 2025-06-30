package dev.loottech.api.manager.element;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.client.elements.Armor;
import dev.loottech.client.elements.Chat;
import dev.loottech.client.elements.Direction;
import dev.loottech.client.elements.Logo;
import dev.loottech.client.elements.ModuleList;
import dev.loottech.client.elements.Music;
import dev.loottech.client.elements.Position;
import dev.loottech.client.elements.Statistics;
import dev.loottech.client.elements.Totems;
import dev.loottech.client.elements.Watermark;
import dev.loottech.client.values.Value;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import net.minecraft.class_310;

public class ElementManager
implements EventListener {
    protected static final class_310 mc = class_310.method_1551();
    private final ArrayList<Element> elements;
    private final Map<Class<? extends Element>, Element> elementInstances = new Reference2ReferenceOpenHashMap();

    public ElementManager() {
        Managers.EVENT.register(this);
        this.elements = new ArrayList();
        this.register(new Watermark());
        this.register(new Position());
        this.register(new Direction());
        this.register(new Totems());
        this.register(new ModuleList());
        this.register(new Armor());
        this.register(new Logo());
        this.register(new Music());
        this.register(new Chat());
        this.register(new Statistics());
    }

    public void register(Element element) {
        try {
            for (Field field : element.getClass().getDeclaredFields()) {
                if (!Value.class.isAssignableFrom(field.getType())) continue;
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                element.getValues().add((Object)((Value)field.get((Object)element)));
            }
            this.elements.add((Object)element);
            this.elementInstances.put((Object)element.getClass(), (Object)element);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Element> getElements() {
        return this.elements;
    }

    public Element getElement(String name) {
        for (Element module : this.elements) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Element> T getInstance(Class<T> klass) {
        return (T)((Element)this.elementInstances.get(klass));
    }

    public boolean isElementEnabled(String name) {
        Element module = (Element)this.elements.stream().filter(m -> m.getName().equals((Object)name)).findFirst().orElse(null);
        if (module != null) {
            return module.isEnabled();
        }
        return false;
    }
}
