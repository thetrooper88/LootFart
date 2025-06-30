package dev.loottech.client.modules.visuals;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

private static class ActivatedSpawnerESP.LoggedSpawner {
    public int x;
    public int y;
    public int z;

    public ActivatedSpawnerESP.LoggedSpawner(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void write(Writer writer) throws IOException {
        writer.write(this.x + "," + this.y + "," + this.z + "\n");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivatedSpawnerESP.LoggedSpawner)) {
            return false;
        }
        ActivatedSpawnerESP.LoggedSpawner that = (ActivatedSpawnerESP.LoggedSpawner)o;
        return this.x == that.x && this.y == that.y && this.z == that.z;
    }

    public int hashCode() {
        return Objects.hash((Object[])new Object[]{this.x, this.y, this.z});
    }
}
