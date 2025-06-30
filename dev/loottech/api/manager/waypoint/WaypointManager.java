package dev.loottech.api.manager.waypoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.loottech.api.manager.waypoint.Waypoint;
import dev.loottech.api.utilities.Util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;

public class WaypointManager
implements Util {
    private final List<Waypoint> waypoints = new ArrayList();
    private final File waypointsFile = new File("LootTech/Waypoints.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public WaypointManager() {
        this.loadWaypoints();
    }

    public void addWaypoint(Waypoint waypoint) {
        this.waypoints.add((Object)waypoint);
        this.saveWaypoints();
    }

    public void removeWaypoint(String name) {
        this.waypoints.removeIf(w -> w.getName().equalsIgnoreCase(name));
        this.saveWaypoints();
    }

    public List<Waypoint> getWaypoints() {
        return new ArrayList(this.waypoints);
    }

    public List<Waypoint> getWaypointsForServer(String ip) {
        ArrayList serverWaypoints = new ArrayList();
        for (Waypoint waypoint : this.waypoints) {
            if (!waypoint.getIp().equalsIgnoreCase(ip)) continue;
            serverWaypoints.add((Object)waypoint);
        }
        return serverWaypoints;
    }

    public void saveWaypoints() {
        try {
            if (!this.waypointsFile.exists()) {
                Files.createDirectories((Path)Paths.get((String)"LootTech/", (String[])new String[0]), (FileAttribute[])new FileAttribute[0]);
                this.waypointsFile.createNewFile();
            }
            JsonArray waypointsArray = new JsonArray();
            for (Waypoint waypoint : this.waypoints) {
                JsonObject waypointJson = new JsonObject();
                waypointJson.add("name", (JsonElement)new JsonPrimitive(waypoint.getName()));
                JsonObject posJson = new JsonObject();
                posJson.add("x", (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)waypoint.getPos().method_10263())));
                posJson.add("y", (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)waypoint.getPos().method_10264())));
                posJson.add("z", (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)waypoint.getPos().method_10260())));
                waypointJson.add("pos", (JsonElement)posJson);
                waypointJson.add("dimension", (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)waypoint.getDimension())));
                waypointJson.add("ip", (JsonElement)new JsonPrimitive(waypoint.getIp()));
                waypointsArray.add((JsonElement)waypointJson);
            }
            JsonObject mainObject = new JsonObject();
            mainObject.add("waypoints", (JsonElement)waypointsArray);
            try (OutputStreamWriter writer = new OutputStreamWriter((OutputStream)new FileOutputStream(this.waypointsFile), StandardCharsets.UTF_8);){
                writer.write(this.gson.toJson((JsonElement)mainObject));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWaypoints() {
        if (!this.waypointsFile.exists()) {
            return;
        }
        try (InputStream stream = Files.newInputStream((Path)this.waypointsFile.toPath(), (OpenOption[])new OpenOption[0]);){
            JsonObject mainObject = new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject();
            JsonArray waypointsArray = mainObject.getAsJsonArray("waypoints");
            if (waypointsArray == null) {
                return;
            }
            this.waypoints.clear();
            for (JsonElement element : waypointsArray) {
                JsonObject waypointJson = element.getAsJsonObject();
                String name = waypointJson.get("name").getAsString();
                JsonObject posJson = waypointJson.getAsJsonObject("pos");
                class_2338 pos = new class_2338(posJson.get("x").getAsInt(), posJson.get("y").getAsInt(), posJson.get("z").getAsInt());
                int dimension = waypointJson.get("dimension").getAsInt();
                String ip = waypointJson.get("ip").getAsString();
                this.waypoints.add((Object)new Waypoint(name, pos, dimension, ip));
            }
        }
        catch (JsonParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void clearWaypoints() {
        this.waypoints.clear();
        this.saveWaypoints();
    }

    public void clearWaypointsForServer(String ip) {
        this.waypoints.removeIf(w -> w.getIp().equalsIgnoreCase(ip));
        this.saveWaypoints();
    }
}
