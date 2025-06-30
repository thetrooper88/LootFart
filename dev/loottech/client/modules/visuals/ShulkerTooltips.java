package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.InventoryUtils;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.client.events.RenderTooltipEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import java.awt.Color;
import java.util.List;
import net.minecraft.class_1747;
import net.minecraft.class_1767;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1806;
import net.minecraft.class_1937;
import net.minecraft.class_22;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2371;
import net.minecraft.class_2480;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_4597;
import net.minecraft.class_7225;
import net.minecraft.class_9209;
import net.minecraft.class_9279;
import net.minecraft.class_9288;
import net.minecraft.class_9334;

@RegisterModule(name="ShulkerTooltips", tag="ShulkerTooltips", description="Shows you whats inside a shulker box without opening it.", category=Module.Category.VISUALS)
public class ShulkerTooltips
extends Module {
    public final ValueBoolean majorityItemConfig = new ValueBoolean("Majority Item", "Show a item over shulker boxes in the inventory displaying the item most prevalent in the shulker.", this.isChatNotify());

    @Override
    public void onRenderTooltip(RenderTooltipEvent event) {
        class_1799 stack = event.getStack();
        if (stack.method_7960()) {
            return;
        }
        class_9279 nbtComponent = (class_9279)stack.method_57824(class_9334.field_49611);
        if (nbtComponent != null && InventoryUtils.isShulker(stack.method_7909())) {
            event.cancel();
            event.context.method_51448().method_22903();
            event.context.method_51448().method_46416(0.0f, 0.0f, 600.0f);
            class_2371 defaultedList = class_2371.method_10213((int)27, (Object)class_1799.field_8037);
            class_9288 container = (class_9288)stack.method_57824(class_9334.field_49622);
            if (container != null) {
                List stacks = container.method_57489().toList();
                for (int i = 0; i < stacks.size() && i < defaultedList.size(); ++i) {
                    defaultedList.set(i, (Object)((class_1799)stacks.get(i)));
                }
            } else {
                class_2487 nbtCompound = nbtComponent.method_57461();
                if (nbtCompound.method_10573("Items", 9)) {
                    class_2499 nbtList = nbtCompound.method_10554("Items", 10);
                    for (int i = 0; i < nbtList.size(); ++i) {
                        class_2487 nbtCompound1 = nbtList.method_10602(i);
                        int slot = nbtCompound1.method_10571("Slot") & 0xFF;
                        if (slot < 0 || slot >= defaultedList.size()) continue;
                        defaultedList.set(slot, (Object)class_1799.method_57359((class_7225.class_7874)ShulkerTooltips.mc.field_1724.method_56673(), (class_2487)nbtCompound1));
                    }
                }
            }
            int x = event.getX();
            int y = event.getY();
            int width = 150;
            int height = 67;
            int gridX = x + 8;
            int gridY = y - 8;
            int titleHeight = 15;
            Color shulkerColor = ShulkerTooltips.getShulkerColor(stack);
            RenderManager.rect(event.context.method_51448(), gridX - 2, gridY - titleHeight, width, height, shulkerColor.getRGB() & 0xFFFFFF | 0xCC000000);
            RenderManager.rect(event.context.method_51448(), gridX - 2, gridY - titleHeight, width, 1.0, -12303292);
            RenderManager.rect(event.context.method_51448(), gridX - 2, gridY - titleHeight + height - 1, width, 1.0, -12303292);
            RenderManager.rect(event.context.method_51448(), gridX - 2, gridY - titleHeight, 1.0, height, -12303292);
            RenderManager.rect(event.context.method_51448(), gridX - 2 + width - 1, gridY - titleHeight, 1.0, height, -12303292);
            RenderManager.rect(event.context.method_51448(), gridX, gridY - titleHeight + 2, width - 4, 13.0, shulkerColor.getRGB());
            RenderManager.enableScissor(gridX, gridY - titleHeight + 2, gridX + width - 4, gridY - titleHeight + 15);
            RenderManager.renderText(event.getContext(), stack.method_7964().getString(), gridX + 3, gridY - titleHeight + 5, 0xFFFFFF);
            RenderManager.disableScissor();
            RenderManager.rect(event.context.method_51448(), gridX, gridY, width - 4, 49.0, 0x77000000);
            for (int i = 0; i < defaultedList.size(); ++i) {
                int slotX = gridX + i % 9 * 16 + 1;
                int slotY = gridY + i / 9 * 16 + 1;
                RenderManager.rect(event.context.method_51448(), slotX - 1, slotY - 1, 18.0, 18.0, 0x33000000);
                event.context.method_51427((class_1799)defaultedList.get(i), slotX, slotY);
                event.context.method_51431(ShulkerTooltips.mc.field_1772, (class_1799)defaultedList.get(i), slotX, slotY);
            }
            event.context.method_51448().method_22909();
        } else if (stack.method_7909() instanceof class_1806) {
            event.cancel();
            event.context.method_51448().method_22903();
            event.context.method_51448().method_46416(0.0f, 0.0f, 600.0f);
            RenderManager.rect(event.context.method_51448(), (double)event.getX() + 8.0, (double)event.getY() - 21.0, 128.0, 13.0, Managers.MODULE.getInstance(ModuleColor.class).color.getValue(170).getRGB());
            RenderManager.enableScissor((double)event.getX() + 8.0, (double)event.getY() - 21.0, (double)event.getX() + 132.0, (double)event.getY() - 8.0);
            RenderManager.renderText(event.getContext(), stack.method_7964().getString(), (float)event.getX() + 11.0f, (float)event.getY() - 18.0f, -1);
            RenderManager.disableScissor();
            event.getContext().method_51448().method_46416((float)event.getX() + 8.0f, (float)event.getY() - 8.0f, 0.0f);
            class_9209 mapIdComponent = (class_9209)stack.method_57824(class_9334.field_49646);
            class_22 mapState = class_1806.method_7997((class_9209)mapIdComponent, (class_1937)ShulkerTooltips.mc.field_1687);
            if (mapState != null) {
                ShulkerTooltips.mc.field_1773.method_3194().method_1773(event.getContext().method_51448(), (class_4597)event.getContext().method_51450(), mapIdComponent, mapState, true, 1);
            }
            event.context.method_51448().method_22909();
        }
    }

    public static Color getShulkerColor(class_1799 shulkerItem) {
        class_1792 class_17922 = shulkerItem.method_7909();
        if (class_17922 instanceof class_1747) {
            class_1747 blockItem = (class_1747)class_17922;
            class_2248 block = blockItem.method_7711();
            if (block == class_2246.field_10443) {
                return new Color(0, 50, 50, 100);
            }
            if (block instanceof class_2480) {
                class_2480 shulkerBlock = (class_2480)block;
                class_1767 dye = shulkerBlock.method_10528();
                if (dye == null) {
                    return new Color(135, 60, 200, 100);
                }
                int color = dye.method_7787();
                return new Color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 100);
            }
        }
        return new Color(135, 60, 200, 100);
    }
}
