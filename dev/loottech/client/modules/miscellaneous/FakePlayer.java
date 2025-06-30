package dev.loottech.client.modules.miscellaneous;

import com.google.common.eventbus.Subscribe;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.entity.FakePlayerEntity;
import dev.loottech.client.events.LogoutEvent;
import net.minecraft.class_1657;

@RegisterModule(name="FakePlayer", tag="FakePlayer", description="Summon lightning at player deaths.", category=Module.Category.MISCELLANEOUS)
public class FakePlayer
extends Module {
    private FakePlayerEntity fakePlayer;

    @Override
    public void onEnable() {
        if (FakePlayer.mc.field_1724 != null && FakePlayer.mc.field_1687 != null) {
            this.fakePlayer = new FakePlayerEntity((class_1657)FakePlayer.mc.field_1724, "FakePlayer");
            this.fakePlayer.spawnPlayer();
        }
    }

    @Override
    public void onDisable() {
        if (this.fakePlayer != null) {
            this.fakePlayer.despawnPlayer();
            this.fakePlayer = null;
        }
    }

    @Override
    @Subscribe
    public void onLogout(LogoutEvent event) {
        this.fakePlayer = null;
        this.toggle(false);
    }
}
