package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ColorUtil;
import dev.loottech.api.utilities.entity.FakePlayerEntity;
import dev.loottech.api.utilities.render.Interpolation;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderLayersClient;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.asm.mixins.accessor.AccessorItemRenderer;
import dev.loottech.asm.mixins.accessor.AccessorTextRenderer;
import dev.loottech.client.events.PlaySoundEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.Font;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.visuals.Freecam;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.class_1087;
import net.minecraft.class_1091;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1321;
import net.minecraft.class_1496;
import net.minecraft.class_1657;
import net.minecraft.class_1684;
import net.minecraft.class_1738;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1921;
import net.minecraft.class_1937;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_290;
import net.minecraft.class_327;
import net.minecraft.class_3414;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_4696;
import net.minecraft.class_4720;
import net.minecraft.class_5819;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_777;
import net.minecraft.class_7833;
import net.minecraft.class_811;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

@RegisterModule(name="Nametags", tag="Nametags", description="Better player nametags.", category=Module.Category.VISUALS)
public class Nametags
extends Module {
    private final Map<SoundRender, Long> sounds = new HashMap();
    ValueBoolean armorConfig = new ValueBoolean("Armor", "Displays the player's armor", true);
    ValueBoolean itemsConfig = new ValueBoolean("Items", "Displays the player's held items", true);
    ValueBoolean enchantmentsConfig = new ValueBoolean("Enchantments", "Displays a list of the item's enchantments", true);
    ValueBoolean durabilityConfig = new ValueBoolean("Durability", "Displays item durability", true);
    ValueBoolean itemNameConfig = new ValueBoolean("ItemName", "Displays the player's current held item name", false);
    ValueBoolean entityIdConfig = new ValueBoolean("EntityId", "Displays the player's entity id", false);
    ValueBoolean gamemodeConfig = new ValueBoolean("Gamemode", "Displays the player's gamemode", false);
    ValueBoolean pingConfig = new ValueBoolean("Ping", "Displays the player's server connection ping", true);
    ValueBoolean healthConfig = new ValueBoolean("Health", "Displays the player's current health", true);
    ValueBoolean totemsConfig = new ValueBoolean("Totems", "Displays the player's popped totem count", false);
    float scalingConfig = 0.003f;
    ValueBoolean invisiblesConfig = new ValueBoolean("Invisibles", "Renders nametags on invisible players", true);
    ValueColor backgroundConfig = new ValueColor("Fill", "Fill", "Renders a background behind the nametag", ModuleColor.getColor(30));
    ValueColor borderedConfig = new ValueColor("Outline", "Outline", "Renders a border around the nametag", ModuleColor.getColor(255));
    ValueBoolean tamedConfig = new ValueBoolean("MobOwner", "Renders nametags on tamed mobs", false);
    ValueBoolean pearlsConfig = new ValueBoolean("Pearls", "Renders nametags on thrown ender pearls", false);
    ValueBoolean soundsConfig = new ValueBoolean("Sounds", "Renders nametags on sounds", false);

    public static class_4588 getItemGlintConsumer(class_4597 vertexConsumers, class_1921 layer, boolean glint) {
        if (glint) {
            return class_4720.method_24037((class_4588)vertexConsumers.getBuffer(RenderLayersClient.GLINT), (class_4588)vertexConsumers.getBuffer(layer));
        }
        return vertexConsumers.getBuffer(layer);
    }

    @Override
    public void onDisable() {
        this.sounds.clear();
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (Nametags.mc.field_1773 == null || mc.method_1560() == null) {
            return;
        }
        RenderBuffers.preRender();
        class_243 interpolate = Interpolation.getRenderPosition(mc.method_1560(), mc.method_60646().method_60637(true));
        class_4184 camera = Nametags.mc.field_1773.method_19418();
        class_243 pos = camera.method_19326();
        for (class_1297 entity : Nametags.mc.field_1687.method_18112()) {
            class_1321 tameable;
            class_243 tamePos;
            String lookup;
            class_1496 tameable2;
            double rz;
            double ry;
            double rx;
            if (!RenderManager.isBoxVisible(entity.method_5829())) continue;
            if (entity instanceof class_1657) {
                double dz;
                double dy;
                class_1657 player = (class_1657)entity;
                if (player == Nametags.mc.field_1724 && Nametags.mc.field_1690.method_31044().method_31034() && (!Managers.MODULE.getInstance(Freecam.class).isEnabled() || !Managers.MODULE.getInstance(Freecam.class).renderPlayer.getValue()) || !player.method_5805() || !this.invisiblesConfig.getValue() && player.method_5767()) continue;
                String info = this.getNametagInfo(player);
                class_243 pinterpolate = Interpolation.getRenderPosition((class_1297)player, mc.method_60646().method_60637(true));
                rx = player.method_23317() - pinterpolate.method_10216();
                ry = player.method_23318() + 0.06 - pinterpolate.method_10214() + 0.06;
                rz = player.method_23321() - pinterpolate.method_10215();
                float w1 = Managers.FONT.getWidth(info);
                int width = (int)(w1 + (float)(this.isOnlineUser(player) ? 10 : 0));
                float hwidth = (float)width / 2.0f;
                double dx = pos.method_10216() - interpolate.method_10216() - rx;
                double dist = Math.sqrt((double)(dx * dx + (dy = pos.method_10214() - interpolate.method_10214() - ry) * dy + (dz = pos.method_10215() - interpolate.method_10215() - rz) * dz));
                if (dist > 4096.0) continue;
                float scaling = 0.0018f + this.scalingConfig * (float)dist;
                if (dist <= 8.0) {
                    scaling = 0.0245f;
                }
                this.renderInfo(info, hwidth, player, rx, ry, rz, camera, scaling);
                continue;
            }
            if (entity instanceof class_1496 && (tameable2 = (class_1496)entity).method_6139() != null && this.tamedConfig.getValue()) {
                lookup = Managers.LOOKUP.getNameFromUUID(tameable2.method_6139());
                if (lookup == null) continue;
                tamePos = Interpolation.getRenderPosition(entity, mc.method_60646().method_60637(true));
                rx = entity.method_23317() - tamePos.method_10216();
                ry = entity.method_23318() + (double)entity.method_17682() + (double)0.43f - tamePos.method_10214();
                rz = entity.method_23321() - tamePos.method_10215();
                RenderManager.renderSign("Owner: " + lookup, rx, ry, rz, 0.0f, 0.0f, -1);
                continue;
            }
            if (entity instanceof class_1321 && (tameable = (class_1321)entity).method_6139() != null && this.tamedConfig.getValue()) {
                lookup = Managers.LOOKUP.getNameFromUUID(tameable.method_6139());
                if (lookup == null) continue;
                tamePos = Interpolation.getRenderPosition(entity, mc.method_60646().method_60637(true));
                rx = entity.method_23317() - tamePos.method_10216();
                ry = entity.method_23318() + (double)entity.method_17682() + (double)0.43f - tamePos.method_10214();
                rz = entity.method_23321() - tamePos.method_10215();
                RenderManager.renderSign("Owner: " + lookup, rx, ry, rz, 0.0f, 0.0f, -1);
                continue;
            }
            if (!(entity instanceof class_1684)) continue;
            class_1684 pearlEntity = (class_1684)entity;
            if (!this.pearlsConfig.getValue() || pearlEntity.method_24921() == null) continue;
            class_243 itemPos = Interpolation.getRenderPosition((class_1297)pearlEntity, mc.method_60646().method_60637(true));
            double rx2 = pearlEntity.method_23317() - itemPos.method_10216();
            double ry2 = pearlEntity.method_23318() - itemPos.method_10214();
            double rz2 = pearlEntity.method_23321() - itemPos.method_10215();
            class_1297 thrower = pearlEntity.method_24921();
            if (!(thrower instanceof class_1657)) continue;
            class_1657 player = (class_1657)thrower;
            RenderManager.renderSign(player.method_5477().getString(), rx2, ry2, rz2, 0.0f, 0.0f, this.getNametagColor(player));
        }
        if (this.soundsConfig.getValue()) {
            this.sounds.entrySet().removeIf(e -> System.currentTimeMillis() - (Long)e.getValue() > 1000L);
            for (Map.Entry entry : this.sounds.entrySet()) {
                class_3414 soundEvent = ((SoundRender)((Object)entry.getKey())).soundEvent();
                class_243 renderPos = ((SoundRender)((Object)entry.getKey())).pos();
                RenderManager.renderSign(soundEvent.method_14833().method_43903(), renderPos.field_1352, renderPos.field_1351, renderPos.field_1350, 0.0f, 0.0f, -1);
            }
        }
        RenderBuffers.postRender();
    }

    @Override
    public void onPlaySound(PlaySoundEvent event) {
        this.sounds.put((Object)new SoundRender(event.getPos(), event.getSoundEvent()), (Object)System.currentTimeMillis());
    }

    private void renderInfo(String info, float width, class_1657 entity, double x, double y, double z, class_4184 camera, float scaling) {
        GL11.glDepthFunc((int)519);
        class_243 pos = camera.method_19326();
        class_4587 matrices = new class_4587();
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        matrices.method_22904(x - pos.method_10216(), y + (double)entity.method_17682() + (double)(entity.method_5715() ? 0.4f : 0.43f) - pos.method_10214(), z - pos.method_10215());
        matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22905(-scaling, -scaling, 1.0f);
        double d = this.isOnlineUser(entity) ? (double)(-width - 3.0f) : (double)(-width - 1.0f);
        double d2 = width * 2.0f + (this.isOnlineUser(entity) ? 5.0f : 2.5f);
        Objects.requireNonNull((Object)Nametags.mc.field_1772);
        RenderManager.rect(matrices, d, -1.0, d2, 9.0f + 1.0f, 0.0, this.backgroundConfig.getValue().getRGB());
        double d3 = this.isOnlineUser(entity) ? (double)(-width - 3.0f) : (double)(-width - 1.0f);
        double d4 = width * 2.0f + (this.isOnlineUser(entity) ? 5.0f : 2.5f);
        Objects.requireNonNull((Object)Nametags.mc.field_1772);
        RenderManager.borderedRectLine(matrices, d3, -1.0, d4, 9.0f + 1.0f, this.borderedConfig.getValue().getRGB());
        int color = this.getNametagColor(entity);
        this.renderItems(matrices, entity);
        this.drawText(matrices, info, this.isOnlineUser(entity) ? -width + 10.0f : -width, 0.0f, color);
        GL11.glDepthFunc((int)515);
    }

    private boolean isOnlineUser(class_1657 entity) {
        return false;
    }

    private void drawText(class_4587 matrices, String text, float x, float y, int color) {
        if (Managers.MODULE.isModuleEnabled(Font.class)) {
            Managers.FONT.drawWithShadow(matrices, text, x, y - 1.0f, color);
        } else {
            class_4597.class_4598 vertexConsumers = mc.method_22940().method_23000();
            ((AccessorTextRenderer)Nametags.mc.field_1772).hookDrawLayer(text, x, y, class_327.method_27515((int)color), true, matrices.method_23760().method_23761(), (class_4597)vertexConsumers, class_327.class_6415.field_33994, 0, 0xF000F0);
            vertexConsumers.method_22993();
            ((AccessorTextRenderer)Nametags.mc.field_1772).hookDrawLayer(text, x, y, class_327.method_27515((int)color), false, matrices.method_23760().method_23761(), (class_4597)vertexConsumers, class_327.class_6415.field_33994, 0, 0xF000F0);
            vertexConsumers.method_22993();
        }
    }

    private void renderItems(class_4587 matrixStack, class_1657 player) {
        ArrayList displayItems = new ArrayList();
        if (!player.method_6079().method_7960()) {
            displayItems.add((Object)player.method_6079());
        }
        player.method_31548().field_7548.forEach(arg_0 -> Nametags.lambda$renderItems$1((List)displayItems, arg_0));
        if (!player.method_6047().method_7960()) {
            displayItems.add((Object)player.method_6047());
        }
        Collections.reverse((List)displayItems);
        float xOffset = 0.0f;
        int maxEnchantments = 0;
        boolean gapple = true;
        for (class_1799 stack : displayItems) {
            xOffset -= gapple ? 9.0f : 8.0f;
            gapple = false;
            if (stack.method_58657().method_57534().size() <= maxEnchantments) continue;
            maxEnchantments = stack.method_58657().method_57534().size();
        }
        float yOffset = this.enchantOffset(maxEnchantments);
        for (class_1799 stack : displayItems) {
            boolean isArmor = stack.method_7909() instanceof class_1738;
            if (this.armorConfig.getValue() && isArmor || this.itemsConfig.getValue() && !isArmor) {
                float yPos = !isArmor && !this.enchantmentsConfig.getValue() ? -18.5f : yOffset;
                matrixStack.method_22903();
                matrixStack.method_46416(xOffset, yPos, 0.0f);
                matrixStack.method_46416(8.0f, 8.0f, 0.0f);
                matrixStack.method_22905(16.0f, -16.0f, 0.0f);
                class_4597.class_4598 vertexConsumers = mc.method_22940().method_23000();
                mc.method_1480().method_23179(stack, class_811.field_4317, false, matrixStack, (class_4597)vertexConsumers, 0xF000F0, class_4608.field_21444, mc.method_1480().method_4019(stack, null, null, 0));
                vertexConsumers.method_22993();
                matrixStack.method_22909();
                this.renderItemOverlay(matrixStack, stack, (int)xOffset, (int)yPos);
                matrixStack.method_22903();
                matrixStack.method_22905(0.5f, 0.5f, 0.5f);
                if (stack.method_7909() == class_1802.field_8367) {
                    float yPosScaled = (yOffset + (this.armorConfig.getValue() ? 1.0f : -13.5f)) * 2.0f;
                    this.drawText(matrixStack, "God", (xOffset + 2.0f) * 2.0f, yPosScaled, -3977663);
                } else if (this.enchantmentsConfig.getValue()) {
                    this.renderEnchants(matrixStack, stack, xOffset + 2.0f, yOffset);
                }
                matrixStack.method_22909();
            }
            if (this.durabilityConfig.getValue()) {
                matrixStack.method_22903();
                matrixStack.method_22905(0.5f, 0.5f, 0.5f);
                this.renderDurability(matrixStack, stack, xOffset + 2.0f, yOffset - 4.5f);
                matrixStack.method_22909();
            }
            xOffset += 16.0f;
        }
        class_1799 heldItem = player.method_6047();
        if (!heldItem.method_7960() && this.itemNameConfig.getValue()) {
            matrixStack.method_22903();
            matrixStack.method_22905(0.5f, 0.5f, 0.5f);
            this.renderItemName(matrixStack, heldItem, 0.0f, this.durabilityConfig.getValue() ? yOffset - 10.0f : yOffset - 5.5f);
            matrixStack.method_22909();
        }
    }

    private void renderItemOverlay(class_4587 matrixStack, class_1799 stack, int x, int y) {
        matrixStack.method_22903();
        if (stack.method_7947() != 1) {
            String countText = String.valueOf((int)stack.method_7947());
            class_4597.class_4598 vertexConsumers = mc.method_22940().method_23000();
            ((AccessorTextRenderer)Nametags.mc.field_1772).hookDrawLayer(countText, x + 17 - Nametags.mc.field_1772.method_1727(countText), (float)y + 9.0f, class_327.method_27515((int)Color.WHITE.getRGB()), false, matrixStack.method_23760().method_23761(), (class_4597)vertexConsumers, class_327.class_6415.field_33994, 0, Integer.MAX_VALUE);
            vertexConsumers.method_22993();
        }
        if (stack.method_31578()) {
            int barWidth = (int)((double)stack.method_31579() * 0.923076923);
            int barColor = stack.method_31580();
            RenderManager.rect(matrixStack, x + 3, y + 13, 12.0, 1.0, -16777216);
            RenderManager.rect(matrixStack, x + 3, y + 13, barWidth, 1.0, barColor | 0xFF000000);
        }
        matrixStack.method_22909();
    }

    private void renderItem(class_1799 stack, class_811 renderMode, int light, int overlay, class_4587 matrices, class_4597 vertexConsumers, class_1937 world, int seed) {
        boolean bl;
        class_1087 bakedModel = mc.method_1480().method_4019(stack, world, null, seed);
        if (stack.method_7960()) {
            return;
        }
        boolean bl2 = bl = renderMode == class_811.field_4317 || renderMode == class_811.field_4318 || renderMode == class_811.field_4319;
        if (bl) {
            if (stack.method_31574(class_1802.field_8547)) {
                bakedModel = mc.method_1480().method_4012().method_3303().method_4742(class_1091.method_45910((String)"trident", (String)"inventory"));
            } else if (stack.method_31574(class_1802.field_27070)) {
                bakedModel = mc.method_1480().method_4012().method_3303().method_4742(class_1091.method_45910((String)"spyglass", (String)"inventory"));
            }
        }
        bakedModel.method_4709().method_3503(renderMode).method_23075(false, matrices);
        matrices.method_46416(-0.5f, -0.5f, 0.0f);
        if (bakedModel.method_4713() || stack.method_31574(class_1802.field_8547) && !bl) {
            ((AccessorItemRenderer)mc.method_1480()).hookGetBuiltinModelItemRenderer().method_3166(stack, renderMode, matrices, vertexConsumers, light, overlay);
        } else {
            this.renderBakedItemModel(bakedModel, stack, light, overlay, matrices, Nametags.getItemGlintConsumer(vertexConsumers, class_4696.method_23678((class_1799)stack, (boolean)true), stack.method_7958()));
        }
    }

    private void renderBakedItemModel(class_1087 model, class_1799 stack, int light, int overlay, class_4587 matrices, class_4588 vertices) {
        class_5819 random = class_5819.method_43047();
        long l = 42L;
        for (class_2350 direction : class_2350.values()) {
            random.method_43052(42L);
            this.renderBakedItemQuads(matrices, vertices, (List<class_777>)model.method_4707(null, direction, random), stack, light, overlay);
        }
        random.method_43052(42L);
        this.renderBakedItemQuads(matrices, vertices, (List<class_777>)model.method_4707(null, null, random), stack, light, overlay);
    }

    private void renderBakedItemQuads(class_4587 matrices, class_4588 vertices, List<class_777> quads, class_1799 stack, int light, int overlay) {
        boolean bl = !stack.method_7960();
        class_4587.class_4665 entry = matrices.method_23760();
        for (class_777 bakedQuad : quads) {
            int i = -1;
            if (bl && bakedQuad.method_3360()) {
                i = ((AccessorItemRenderer)mc.method_1480()).hookGetItemColors().method_1704(stack, bakedQuad.method_3359());
            }
            float f = (float)(i >> 16 & 0xFF) / 255.0f;
            float g = (float)(i >> 8 & 0xFF) / 255.0f;
            float h = (float)(i & 0xFF) / 255.0f;
            this.quad(vertices, entry, bakedQuad, f, g, h, light, overlay);
        }
    }

    public void quad(class_4588 vertexConsumer, class_4587.class_4665 matrixEntry, class_777 quad, float red, float green, float blue, int light, int overlay) {
        float[] fs = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        int[] is = new int[]{light, light, light, light};
        int[] js = quad.method_3357();
        Matrix4f matrix4f = matrixEntry.method_23761();
        int i = 8;
        int j = js.length / 8;
        try (MemoryStack memoryStack = MemoryStack.stackPush();){
            ByteBuffer byteBuffer = memoryStack.malloc(class_290.field_1590.method_1362());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();
            for (int k = 0; k < j; ++k) {
                intBuffer.clear();
                intBuffer.put(js, k * 8, 8);
                float f = byteBuffer.getFloat(0);
                float g = byteBuffer.getFloat(4);
                float h = byteBuffer.getFloat(8);
                float o = fs[k] * red;
                float p = fs[k] * green;
                float q = fs[k] * blue;
                int r = is[k];
                float m = byteBuffer.getFloat(16);
                float n = byteBuffer.getFloat(20);
                Vector4f vector4f = matrix4f.transform(new Vector4f(f, g, h, 1.0f));
                vertexConsumer.method_22912(vector4f.x(), vector4f.y(), vector4f.z());
                vertexConsumer.method_39415(new Color(o, p, q).getRGB());
                vertexConsumer.method_22913(m, n);
                vertexConsumer.method_22922(overlay);
                vertexConsumer.method_60803(r);
                vertexConsumer.method_22914(1.0f, 1.0f, 1.0f);
            }
        }
    }

    private void renderDurability(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
        if (!itemStack.method_7963()) {
            return;
        }
        int n = itemStack.method_7936();
        int n2 = itemStack.method_7919();
        int durability = (int)((float)(n - n2) / (float)n * 100.0f);
        this.drawText(matrixStack, durability + "%", x * 2.0f, y * 2.0f, ColorUtil.hslToColor((float)(n - n2) / (float)n * 120.0f, 100.0f, 50.0f, 1.0f).getRGB());
    }

    private void renderEnchants(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
        if (!itemStack.method_7942()) {
            return;
        }
        Set enchants = class_1890.method_57532((class_1799)itemStack).method_57539();
        float n2 = 0.0f;
        for (Object2IntMap.Entry e : enchants) {
            int lvl = e.getIntValue();
            StringBuilder enchantString = new StringBuilder();
            String translatedName = class_1887.method_8179((class_6880)((class_6880)e.getKey()), (int)lvl).getString();
            if (translatedName.contains((CharSequence)"Vanish")) {
                enchantString.append("Van");
            } else if (translatedName.contains((CharSequence)"Bind")) {
                enchantString.append("Bind");
            } else {
                int maxLen;
                int n = maxLen = lvl > 1 ? 2 : 3;
                if (translatedName.length() > maxLen) {
                    translatedName = translatedName.substring(0, maxLen);
                }
                enchantString.append(translatedName);
                enchantString.append(lvl);
            }
            this.drawText(matrixStack, enchantString.toString(), x * 2.0f, (y + n2) * 2.0f, -1);
            n2 += 4.5f;
        }
    }

    private float enchantOffset(int n) {
        if (!this.enchantmentsConfig.getValue() || n <= 3) {
            return this.armorConfig.getValue() ? -18.0f : -3.5f;
        }
        float n2 = -14.0f;
        return n2 -= (float)(n - 3) * 4.5f;
    }

    private void renderItemName(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
        String itemName = itemStack.method_7964().getString();
        float width = (float)Nametags.mc.field_1772.method_1727(itemName) / 4.0f;
        this.drawText(matrixStack, itemName, (x - width) * 2.0f, y * 2.0f, -1);
    }

    private String getNametagInfo(class_1657 player) {
        int totems;
        class_640 playerEntry;
        StringBuilder info = new StringBuilder(player.method_5477().getString());
        info.append(" ");
        if (this.entityIdConfig.getValue()) {
            info.append("ID: ");
            info.append(player.method_5628());
            info.append(" ");
        }
        if (this.gamemodeConfig.getValue()) {
            if (player.method_7337()) {
                info.append("[C] ");
            } else if (player.method_7325()) {
                info.append("[I] ");
            } else {
                info.append("[S] ");
            }
        }
        if (this.pingConfig.getValue() && mc.method_1562() != null && (playerEntry = mc.method_1562().method_2871(player.method_7334().getId())) != null) {
            info.append(playerEntry.method_2959());
            info.append("ms ");
        }
        if (this.healthConfig.getValue()) {
            double health = player.method_6032() + player.method_6067();
            class_124 hcolor = health > 18.0 ? class_124.field_1060 : (health > 16.0 ? class_124.field_1077 : (health > 12.0 ? class_124.field_1054 : (health > 8.0 ? class_124.field_1065 : (health > 4.0 ? class_124.field_1061 : class_124.field_1079))));
            BigDecimal bigDecimal = new BigDecimal(health);
            bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
            info.append((Object)hcolor);
            info.append(bigDecimal.doubleValue());
            info.append(" ");
        }
        if (this.totemsConfig.getValue() && player != Nametags.mc.field_1724 && (totems = Managers.TOTEM.getTotems((class_1297)player)) > 0) {
            class_124 pcolor = class_124.field_1060;
            if (totems > 1) {
                pcolor = class_124.field_1077;
            }
            if (totems > 2) {
                pcolor = class_124.field_1054;
            }
            if (totems > 3) {
                pcolor = class_124.field_1065;
            }
            if (totems > 4) {
                pcolor = class_124.field_1061;
            }
            if (totems > 5) {
                pcolor = class_124.field_1079;
            }
            info.append((Object)pcolor);
            info.append(-totems);
            info.append(" ");
        }
        return info.toString().trim();
    }

    private int getNametagColor(class_1657 player) {
        if (player == Nametags.mc.field_1724) {
            return Color.WHITE.getRGB();
        }
        if (Managers.FRIEND.isFriend(player)) {
            return ModuleColor.getFriendColor().getRGB();
        }
        if (player.method_5767()) {
            return -56064;
        }
        if (player instanceof FakePlayerEntity) {
            return -1113785;
        }
        if (player.method_5715()) {
            return -26368;
        }
        return -1;
    }

    public float getScaling() {
        return this.scalingConfig;
    }

    private static /* synthetic */ void lambda$renderItems$1(List displayItems, class_1799 armorStack) {
        if (!armorStack.method_7960()) {
            displayItems.add((Object)armorStack);
        }
    }

    private record SoundRender(class_243 pos, class_3414 soundEvent) {
    }
}
