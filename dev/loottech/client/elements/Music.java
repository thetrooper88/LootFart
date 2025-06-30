package dev.loottech.client.elements;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.element.RegisterElement;
import dev.loottech.api.utilities.SpotifyTracker;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.ModuleHUDEditor;
import net.minecraft.class_156;

@RegisterElement(name="Spotify", description="Show playing spotify track.")
public class Music
extends Element {
    private String currentTrack = "";
    private long lastUpdateTime = 0L;

    @Override
    public void onRender2D(Render2DEvent event) {
        if (class_156.method_658() - this.lastUpdateTime > 5000L) {
            this.currentTrack = SpotifyTracker.getCurrentTrack();
            this.lastUpdateTime = class_156.method_658();
        }
        Managers.FONT.drawWithShadow(event.getContext().method_51448(), this.currentTrack.isEmpty() || this.currentTrack.contains((CharSequence)"Spotify") ? "No music playing" : this.currentTrack, this.frame.getX(), this.frame.getY(), Managers.MODULE.getInstance(ModuleHUDEditor.class).color.getValue().getRGB());
        this.frame.setWidth(Managers.FONT.getWidth(this.currentTrack.isEmpty() ? "No music playing" : this.currentTrack));
        this.frame.setHeight(Managers.FONT.getStringHeight(this.currentTrack.isEmpty() ? "No music playing" : "<UNK> " + this.currentTrack));
        this.snap();
    }
}
