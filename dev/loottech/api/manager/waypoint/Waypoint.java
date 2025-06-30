package dev.loottech.api.manager.waypoint;

import net.minecraft.class_2338;

public class Waypoint {
    class_2338 pos;
    String name;
    String ip;
    int dimension;

    public Waypoint(String name, class_2338 pos, int dimension, String ip) {
        this.pos = pos;
        this.name = name;
        this.dimension = dimension;
        this.ip = ip;
    }

    public class_2338 getPos() {
        return this.pos;
    }

    public String getName() {
        return this.name;
    }

    public String getIp() {
        return this.ip;
    }

    public int getDimension() {
        return this.dimension;
    }
}
