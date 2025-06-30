package dev.loottech.api.utilities;

public class AnimationUtils {
    public static float interpolate(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    public static float getProgress(long startTime, long duration) {
        float elapsed = (float)(System.currentTimeMillis() - startTime) / 1000.0f;
        float deltaAdjusted = elapsed / ((float)duration / 1000.0f);
        return Math.min((float)1.0f, (float)deltaAdjusted);
    }

    public static float animate(float start, float end, long startTime, long duration) {
        float progress = AnimationUtils.getProgress(startTime, duration);
        return AnimationUtils.interpolate(start, end, progress);
    }
}
