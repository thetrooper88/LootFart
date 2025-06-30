package dev.loottech.client.gui.click.manage;

import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import java.util.function.Supplier;

public abstract class AnimatedComponent
extends Component {
    protected float animationProgress = 1.0f;
    protected long lastAnimationTime = System.currentTimeMillis();
    protected static final float ANIMATION_SPEED = 5.0f;

    public AnimatedComponent(int offset, Frame parent, Supplier<Boolean> visible) {
        super(offset, parent, visible);
    }

    protected void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        float delta = (float)(currentTime - this.lastAnimationTime) / 1000.0f;
        this.lastAnimationTime = currentTime;
        if (this.isVisible() && this.animationProgress < 1.0f) {
            this.animationProgress = Math.min((float)1.0f, (float)(this.animationProgress + delta * 5.0f));
        } else if (!this.isVisible() && this.animationProgress > 0.0f) {
            this.animationProgress = Math.max((float)0.0f, (float)(this.animationProgress - delta * 5.0f));
        }
    }

    @Override
    public float getEasedProgress() {
        return 1.0f - (float)Math.pow((double)(1.0f - this.animationProgress), (double)3.0);
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
    }
}
