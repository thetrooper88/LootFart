package dev.loottech.api.utilities;

public interface ITimer {
    public static final long MAX_TIME = -255L;

    public boolean passed(Number var1);

    public void reset();

    public long getElapsedTime();

    public void setElapsedTime(Number var1);
}
