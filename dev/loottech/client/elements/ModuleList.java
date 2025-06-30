package dev.loottech.client.elements;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.AnimationUtils;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.ModuleHUDEditor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_124;

@RegisterElement(name="ModuleList", description="Show the drawn and enabled modules with animations.")
public class ModuleList
extends Element {
    private final Map<Module, Float> modulePositions = new ConcurrentHashMap();
    private final Map<Module, Long> animationStartTimes = new ConcurrentHashMap();
    private final Map<Module, Boolean> exitingModules = new ConcurrentHashMap();
    private static final long ANIMATION_DURATION_MS = 200L;
    private final class_124 gray = class_124.field_1080;

    @Override
    public void onRender2D(Render2DEvent event) {
        Color col = Managers.MODULE.getInstance(ModuleHUDEditor.class).color.getValue();
        float height = 0.0f;
        ArrayList currentModules = new ArrayList();
        for (Module module : Managers.MODULE.getModules()) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            currentModules.add((Object)module);
        }
        this.handleModuleAnimations((List<Module>)currentModules);
        if (!currentModules.isEmpty() || !this.modulePositions.isEmpty()) {
            ArrayList allModules = new ArrayList();
            allModules.addAll((Collection)currentModules);
            allModules.addAll((Collection)this.modulePositions.keySet().stream().filter(arg_0 -> ModuleList.lambda$onRender2D$0((List)currentModules, arg_0)).toList());
            List sortedModules = allModules.stream().sorted(Comparator.comparing(m -> Float.valueOf((float)(Managers.FONT.getWidth(this.getModuleText((Module)m)) * -1.0f)))).toList();
            float currentY = this.frame.getY();
            for (Module module : sortedModules) {
                float currentX;
                String text = this.getModuleText(module);
                float textWidth = Managers.FONT.getWidth(text);
                float textHeight = Managers.FONT.getStringHeight(text);
                if (!this.exitingModules.containsKey((Object)module) || System.currentTimeMillis() - (Long)this.animationStartTimes.getOrDefault((Object)module, (Object)0L) < 200L) {
                    height += textHeight + 2.0f;
                }
                float progress = AnimationUtils.getProgress((Long)this.animationStartTimes.getOrDefault((Object)module, (Object)0L), 200L);
                float targetX = this.isOnRight() ? this.frame.getX() + this.frame.getWidth() - textWidth : this.frame.getX();
                boolean isCurrentModule = currentModules.contains((Object)module);
                if (isCurrentModule) {
                    float startX = this.isOnRight() ? targetX + 100.0f : targetX - 100.0f;
                    currentX = AnimationUtils.interpolate(startX, targetX, progress);
                } else {
                    float endX = this.isOnRight() ? targetX + 100.0f : targetX - 100.0f;
                    currentX = AnimationUtils.interpolate(targetX, endX, progress);
                }
                this.modulePositions.put((Object)module, (Object)Float.valueOf((float)currentX));
                if (isCurrentModule || progress < 1.0f) {
                    int alpha = isCurrentModule ? (int)((float)col.getAlpha() * progress) : (int)((float)col.getAlpha() * (1.0f - progress));
                    Managers.FONT.drawWithShadow(event.getContext().method_51448(), text, currentX, currentY, new Color(col.getRed(), col.getGreen(), col.getBlue(), alpha).getRGB());
                }
                currentY += textHeight + 2.0f;
            }
        }
        this.frame.setWidth(ModuleList.mc.field_1772.method_1727("positioning loophole or wtv"));
        this.frame.setHeight(height);
    }

    private void handleModuleAnimations(List<Module> currentModules) {
        for (Module module : new ArrayList((Collection)this.modulePositions.keySet())) {
            if (currentModules.contains((Object)module) || this.exitingModules.containsKey((Object)module)) continue;
            this.exitingModules.put((Object)module, (Object)true);
            this.animationStartTimes.put((Object)module, (Object)System.currentTimeMillis());
        }
        for (Module module : currentModules) {
            if (this.modulePositions.containsKey((Object)module)) continue;
            this.modulePositions.put((Object)module, (Object)Float.valueOf((float)(this.isOnRight() ? this.frame.getX() + this.frame.getWidth() + 100.0f : this.frame.getX() - 100.0f)));
            this.animationStartTimes.put((Object)module, (Object)System.currentTimeMillis());
            this.exitingModules.remove((Object)module);
        }
        Iterator iterator = this.exitingModules.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            if (System.currentTimeMillis() - (Long)this.animationStartTimes.getOrDefault(entry.getKey(), (Object)0L) <= 200L) continue;
            this.modulePositions.remove(entry.getKey());
            this.animationStartTimes.remove(entry.getKey());
            iterator.remove();
        }
    }

    private String getModuleText(Module module) {
        return module.getTag() + (module.getHudInfo().isEmpty() ? "" : String.valueOf((Object)this.gray) + " [" + String.valueOf((Object)class_124.field_1068) + module.getHudInfo() + String.valueOf((Object)this.gray) + "]");
    }

    private static /* synthetic */ boolean lambda$onRender2D$0(List currentModules, Module m) {
        return !currentModules.contains((Object)m);
    }
}
