package dev.loottech.api.utilities.render;

import net.minecraft.class_1268;

private static enum ChamsModelRenderer.HandRenderType {
    RENDER_BOTH_HANDS(true, true),
    RENDER_MAIN_HAND_ONLY(true, false),
    RENDER_OFF_HAND_ONLY(false, true);

    final boolean renderMainHand;
    final boolean renderOffHand;

    private ChamsModelRenderer.HandRenderType(boolean renderMainHand, boolean renderOffHand) {
        this.renderMainHand = renderMainHand;
        this.renderOffHand = renderOffHand;
    }

    public static ChamsModelRenderer.HandRenderType shouldOnlyRender(class_1268 hand) {
        return hand == class_1268.field_5808 ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
    }
}
