package dev.loottech.client.modules.visuals;

import dev.loottech.api.utilities.entity.FakePlayerEntity;
import dev.loottech.api.utilities.render.StaticBipedEntityModel;
import net.minecraft.class_1657;
import net.minecraft.class_742;

public static class Chams.PopChamEntity
extends FakePlayerEntity {
    private final StaticBipedEntityModel<class_742> model;

    public Chams.PopChamEntity(class_1657 player, float tickDelta) {
        super(player);
        this.model = new StaticBipedEntityModel<class_742>((class_742)player, false, tickDelta);
        this.field_6243 = player.field_6243;
        this.field_6264 = player.field_6243;
        this.method_18380(player.method_18376());
    }

    public StaticBipedEntityModel<class_742> getModel() {
        return this.model;
    }
}
