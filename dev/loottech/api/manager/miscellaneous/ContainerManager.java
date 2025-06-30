package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.miscellaneous.ContainerContentsType;
import dev.loottech.api.manager.miscellaneous.ContainerType;
import dev.loottech.api.manager.miscellaneous.ItemStackManager;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_9276;
import net.minecraft.class_9288;
import net.minecraft.class_9323;
import net.minecraft.class_9334;

public class ContainerManager {
    private class_1799 containerStack;
    private class_9323 containerComponents;
    private String containerId;
    private boolean isContainerSupported;
    private ContainerType containerType;
    private ContainerContentsType containerContentsType;

    public ContainerManager(class_1799 containerStack) {
        this.containerStack = containerStack;
        this.containerId = containerStack.method_41409().method_55840();
        this.containerComponents = containerStack.method_57353();
        this.setContainerContentsType();
        this.setContainerType();
        this.setContainerSupported();
    }

    public class_1799 getDisplayStack() {
        Iterable itemIterable;
        if (!this.isContainerSupported) {
            return null;
        }
        class_1799 displayStack = ItemStackManager.getItemFromCustomName(this.containerStack);
        if (displayStack != null) {
            return displayStack;
        }
        switch (this.containerContentsType) {
            case CONTAINER: {
                class_9288 containerComponent = (class_9288)this.containerStack.method_57824(class_9334.field_49622);
                itemIterable = containerComponent.method_59715();
                break;
            }
            case BUNDLE: {
                class_9276 bundleComponent = (class_9276)this.containerStack.method_57824(class_9334.field_49650);
                itemIterable = bundleComponent.method_59708();
                break;
            }
            default: {
                return null;
            }
        }
        return ItemStackManager.getDisplayStackFromIterable((Iterable<class_1799>)itemIterable);
    }

    public int getStackSize() {
        return this.containerStack.method_7947();
    }

    public float getCapacity() {
        switch (this.containerContentsType) {
            case CONTAINER: {
                return this.getContainerCapacity();
            }
            case BUNDLE: {
                return this.getBundleCapacity();
            }
        }
        return 0.0f;
    }

    public ContainerType getContainerType() {
        return this.containerType;
    }

    private float getContainerCapacity() {
        if (this.containerType != ContainerType.SHULKER_BOX) {
            return 0.0f;
        }
        return this.getShulkerCapacity();
    }

    private float getShulkerCapacity() {
        class_9288 containerComponent = (class_9288)this.containerStack.method_57824(class_9334.field_49622);
        Iterable itemIterable = containerComponent.method_59714();
        float sumCapacity = 0.0f;
        for (class_1799 itemStack : itemIterable) {
            sumCapacity += (float)itemStack.method_7947() / (float)itemStack.method_7909().method_7882();
        }
        return sumCapacity / 27.0f;
    }

    private float getBundleCapacity() {
        class_9276 bundleComponent = (class_9276)this.containerStack.method_57824(class_9334.field_49650);
        return bundleComponent.method_57428().floatValue();
    }

    private void setContainerContentsType() {
        this.containerContentsType = this.containerComponents.method_57832(class_9334.field_49622) ? ContainerContentsType.CONTAINER : (this.containerComponents.method_57832(class_9334.field_49650) ? ContainerContentsType.BUNDLE : ContainerContentsType.NONE);
    }

    private void setContainerType() {
        this.containerType = this.containerContentsType == ContainerContentsType.NONE ? ContainerType.NONE : (this.containerId.matches("^minecraft:(.*_)?shulker_box$") ? ContainerType.SHULKER_BOX : (this.containerStack.method_31574(class_1802.field_27023) ? ContainerType.BUNDLE : ContainerType.OTHER));
    }

    private void setContainerSupported() {
        switch (this.containerType) {
            case SHULKER_BOX: {
                this.isContainerSupported = true;
                return;
            }
            case BUNDLE: {
                this.isContainerSupported = false;
                return;
            }
            case OTHER: {
                this.isContainerSupported = false;
                return;
            }
        }
        this.isContainerSupported = false;
    }
}
