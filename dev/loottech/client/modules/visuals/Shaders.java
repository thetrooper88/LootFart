package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.manager.shader.ShaderManager;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.asm.mixins.accessor.AccessorWorldRenderer;
import dev.loottech.asm.mixins.accessor.IGameRenderer;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1311;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1683;
import net.minecraft.class_1684;

@RegisterModule(name="Shaders", tag="Shaders", description="Draw cool shader over shit.", category=Module.Category.VISUALS)
public class Shaders
extends Module {
    private final ValueCategory select = new ValueCategory("Select", "What to render");
    public final ValueBoolean hands = new ValueBoolean("Hands", "Hands", "", this.select, true);
    private final ValueBoolean players = new ValueBoolean("Players", "Players", "", this.select, true);
    private final ValueBoolean self = new ValueBoolean("Self", "Self", "", this.select, true);
    private final ValueBoolean friends = new ValueBoolean("Friends", "Friends", "", this.select, true);
    private final ValueBoolean crystals = new ValueBoolean("Crystals", "Crystals", "", this.select, true);
    private final ValueBoolean creatures = new ValueBoolean("Creatures", "Creatures", "", this.select, true);
    private final ValueBoolean monsters = new ValueBoolean("Monsters", "Monsters", "", this.select, true);
    private final ValueBoolean ambients = new ValueBoolean("Ambients", "Ambients", "", this.select, true);
    private final ValueBoolean xp = new ValueBoolean("XP Bottles", "XP Bottles", "", this.select, true);
    private final ValueBoolean pearls = new ValueBoolean("Ender Pearls", "Ender Pearls", "", this.select, true);
    private final ValueBoolean items = new ValueBoolean("Items", "Items", "", this.select, true);
    public final ValueNumber maxRange = new ValueNumber("MaxRange", "MaxRange", "", (Number)Integer.valueOf((int)64), (Number)Integer.valueOf((int)16), (Number)Integer.valueOf((int)256));
    public final ValueNumber lineWidth = new ValueNumber("LineWidth", "LineWidth", "", (Number)Integer.valueOf((int)5), (Number)Float.valueOf((float)0.0f), (Number)Integer.valueOf((int)20));
    public final ValueBoolean glow = new ValueBoolean("Glow", "Glow", "", true);
    public final ValueNumber quality = new ValueNumber("Quality", "Quality", "", (Number)Integer.valueOf((int)5), (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)20));
    private final ValueCategory colors = new ValueCategory("Colors", "");
    public final ValueBoolean friendColor = new ValueBoolean("FriendColor", "FriendColor", "", this.select, true);
    public final ValueColor outlineColor = new ValueColor("Outline", "Outline", "", this.colors, ModuleColor.getColor(), false, true);
    public final ValueColor fillColor1 = new ValueColor("Fill", "Fill", "", this.colors, ModuleColor.getColor(50), false, true);

    public boolean shouldRender(class_1297 entity) {
        if (((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum() == null) {
            return false;
        }
        if (entity == null) {
            return false;
        }
        if (Shaders.mc.field_1724 == null) {
            return false;
        }
        if (Shaders.mc.field_1724.method_5707(entity.method_19538()) > this.maxRange.getValue().doubleValue() * this.maxRange.getValue().doubleValue()) {
            return false;
        }
        if (entity instanceof class_1657) {
            if (entity == Shaders.mc.field_1724 && !this.self.getValue()) {
                return false;
            }
            if (Managers.FRIEND.isFriend((class_1657)entity)) {
                return this.friends.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829());
            }
            return this.players.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829());
        }
        if (entity instanceof class_1511) {
            return this.crystals.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829());
        }
        if (entity instanceof class_1303 || entity instanceof class_1683) {
            return this.xp.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829());
        }
        if (entity instanceof class_1684) {
            return this.pearls.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829());
        }
        if (entity instanceof class_1542) {
            return this.items.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829());
        }
        return switch (entity.method_5864().method_5891()) {
            case class_1311.field_6294, class_1311.field_6300 -> {
                if (this.creatures.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829())) {
                    yield true;
                }
                yield false;
            }
            case class_1311.field_6302 -> {
                if (this.monsters.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829())) {
                    yield true;
                }
                yield false;
            }
            case class_1311.field_6303, class_1311.field_24460 -> {
                if (this.ambients.getValue() && ((AccessorWorldRenderer)Shaders.mc.field_1769).getFrustum().method_23093(entity.method_5829())) {
                    yield true;
                }
                yield false;
            }
            default -> false;
        };
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.hands.getValue()) {
            Managers.SHADER.renderShader(() -> ((IGameRenderer)Shaders.mc.field_1773).hookRenderHand(Shaders.mc.field_1773.method_19418(), RenderUtils.getTickDelta(), event.getMatrices().method_23760().method_23761()), this.getHandMode());
        }
    }

    private ShaderManager.Shader getHandMode() {
        return ShaderManager.Shader.Default;
    }

    public ShaderManager.Shader getMode() {
        return ShaderManager.Shader.Default;
    }

    @Override
    public void onDisable() {
        Managers.SHADER.reloadShaders();
    }
}
