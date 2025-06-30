package dev.loottech.api.manager.miscellaneous;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.manager.friend.Friend;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.Timer;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.modules.client.Notifications;
import dev.loottech.client.values.Value;
import dev.loottech.client.values.impl.ValueBind;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueColor;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import dev.loottech.client.values.impl.ValueString;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;

public class ConfigManager
implements EventListener {
    Timer autoSaveTimer = new Timer();

    @Override
    public void onTick(TickEvent event) {
        if (this.autoSaveTimer.passedS(60.0)) {
            this.saveFullConfig("autoSave");
            this.autoSaveTimer.reset();
            if (Managers.MODULE.getInstance(Notifications.class).autoSave.getValue()) {
                Managers.NOTIFICATION.send("[+] Automatically Saved Client Configuration.", Color.GREEN.getRGB());
            }
        }
    }

    public void saveFullConfig(String name) {
        try {
            Path file;
            Path clientDir = Paths.get((String)"LootTech", (String[])new String[0]);
            if (!Files.exists((Path)clientDir, (LinkOption[])new LinkOption[0])) {
                Files.createDirectories((Path)clientDir, (FileAttribute[])new FileAttribute[0]);
            }
            if (Files.exists((Path)(file = clientDir.resolve(name + ".json")), (LinkOption[])new LinkOption[0])) {
                Files.delete((Path)file);
            }
            Files.createFile((Path)file, (FileAttribute[])new FileAttribute[0]);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject root = new JsonObject();
            JsonObject mods = new JsonObject();
            for (Module m : Managers.MODULE.getModules()) {
                JsonObject mj = new JsonObject();
                mj.add("Status", (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)m.isEnabled())));
                JsonObject vals = new JsonObject();
                this.saveValues(vals, m.getValues());
                mj.add("Values", (JsonElement)vals);
                mods.add(m.getName(), (JsonElement)mj);
            }
            root.add("Modules", (JsonElement)mods);
            JsonObject elems = new JsonObject();
            for (Element e : Managers.ELEMENT.getElements()) {
                JsonObject ej = new JsonObject();
                ej.add("Status", (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)e.isEnabled())));
                JsonObject vals = new JsonObject();
                this.saveValues(vals, e.getValues());
                ej.add("Values", (JsonElement)vals);
                JsonObject pos = new JsonObject();
                pos.add("X", (JsonElement)new JsonPrimitive((Number)Float.valueOf((float)e.frame.getX())));
                pos.add("Y", (JsonElement)new JsonPrimitive((Number)Float.valueOf((float)e.frame.getY())));
                ej.add("Positions", (JsonElement)pos);
                elems.add(e.getName(), (JsonElement)ej);
            }
            root.add("Elements", (JsonElement)elems);
            root.add("Prefix", (JsonElement)new JsonPrimitive(Managers.COMMAND.getPrefix()));
            JsonArray fr = new JsonArray();
            for (Friend f : Managers.FRIEND.getFriends()) {
                fr.add(f.getName());
            }
            root.add("Friends", (JsonElement)fr);
            try (OutputStreamWriter w = new OutputStreamWriter((OutputStream)new FileOutputStream(file.toFile()), StandardCharsets.UTF_8);){
                w.write(gson.toJson((JsonElement)root));
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveModuleConfig(String name) {
        try {
            Path file;
            Path cfgDir = Paths.get((String)"LootTech/Configs", (String[])new String[0]);
            if (!Files.exists((Path)cfgDir, (LinkOption[])new LinkOption[0])) {
                Files.createDirectories((Path)cfgDir, (FileAttribute[])new FileAttribute[0]);
            }
            if (Files.exists((Path)(file = cfgDir.resolve(name + ".json")), (LinkOption[])new LinkOption[0])) {
                Files.delete((Path)file);
            }
            Files.createFile((Path)file, (FileAttribute[])new FileAttribute[0]);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject root = new JsonObject();
            for (Module m : Managers.MODULE.getModules()) {
                JsonObject moduleJson = new JsonObject();
                moduleJson.add("Enabled", (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)m.isEnabled())));
                JsonObject valuesJson = new JsonObject();
                for (Value v : m.getValues()) {
                    if (v instanceof ValueBind) continue;
                    if (v instanceof ValueBoolean) {
                        valuesJson.add(v.getName(), (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)((ValueBoolean)v).getValue())));
                        continue;
                    }
                    if (v instanceof ValueNumber) {
                        valuesJson.add(v.getName(), (JsonElement)new JsonPrimitive(((ValueNumber)v).getValue()));
                        continue;
                    }
                    if (v instanceof ValueEnum) {
                        valuesJson.add(v.getName(), (JsonElement)new JsonPrimitive(((ValueEnum)v).getValue().name()));
                        continue;
                    }
                    if (v instanceof ValueString) {
                        valuesJson.add(v.getName(), (JsonElement)new JsonPrimitive(((ValueString)v).getValue()));
                        continue;
                    }
                    if (!(v instanceof ValueColor)) continue;
                    ValueColor c = (ValueColor)v;
                    valuesJson.add(c.getName(), (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)c.getValue().getRGB())));
                    valuesJson.add(c.getName() + "-Alpha", (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)c.getValue().getAlpha())));
                    valuesJson.add(c.getName() + "-Rainbow", (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)c.isRainbow())));
                    valuesJson.add(c.getName() + "-Sync", (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)c.isSync())));
                }
                moduleJson.add("Values", (JsonElement)valuesJson);
                root.add(m.getName(), (JsonElement)moduleJson);
            }
            try (OutputStreamWriter w = new OutputStreamWriter((OutputStream)new FileOutputStream(file.toFile()), StandardCharsets.UTF_8);){
                w.write(gson.toJson((JsonElement)root));
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadModuleConfig(String name) {
        Path cfgDir = Paths.get((String)"LootTech/Configs", (String[])new String[0]);
        Path file = cfgDir.resolve(name + ".json");
        if (!Files.exists((Path)file, (LinkOption[])new LinkOption[0])) {
            return;
        }
        try (BufferedReader r = Files.newBufferedReader((Path)file, (Charset)StandardCharsets.UTF_8);){
            JsonObject root = JsonParser.parseReader((Reader)r).getAsJsonObject();
            for (Module m : Managers.MODULE.getModules()) {
                if (!root.has(m.getName())) continue;
                JsonObject moduleJson = root.getAsJsonObject(m.getName());
                if (moduleJson.has("Enabled")) {
                    boolean shouldBeEnabled = moduleJson.get("Enabled").getAsBoolean();
                    if (shouldBeEnabled && !m.isEnabled()) {
                        m.enable(false);
                    } else if (!shouldBeEnabled && m.isEnabled()) {
                        m.disable(false);
                    }
                }
                if (!moduleJson.has("Values")) continue;
                JsonObject valuesJson = moduleJson.getAsJsonObject("Values");
                ArrayList toLoad = new ArrayList();
                for (Value v : m.getValues()) {
                    if (v instanceof ValueBind) continue;
                    toLoad.add((Object)v);
                }
                this.loadValues(valuesJson, (ArrayList<Value>)toLoad);
            }
        }
        catch (IOException | IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void loadFullConfig(String name) {
        Path clientDir = Paths.get((String)"LootTech", (String[])new String[0]);
        Path file = clientDir.resolve(name + ".json");
        if (!Files.exists((Path)file, (LinkOption[])new LinkOption[0])) {
            return;
        }
        try (BufferedReader r = Files.newBufferedReader((Path)file, (Charset)StandardCharsets.UTF_8);){
            JsonObject root = JsonParser.parseReader((Reader)r).getAsJsonObject();
            JsonObject mods = root.getAsJsonObject("Modules");
            for (Module m : Managers.MODULE.getModules()) {
                if (!mods.has(m.getName())) continue;
                JsonObject mj = mods.getAsJsonObject(m.getName());
                if (mj.get("Status").getAsBoolean()) {
                    m.enable(false);
                }
                this.loadValues(mj.getAsJsonObject("Values"), m.getValues());
            }
            JsonObject elems = root.getAsJsonObject("Elements");
            for (Element e : Managers.ELEMENT.getElements()) {
                if (!elems.has(e.getName())) continue;
                JsonObject ej = elems.getAsJsonObject(e.getName());
                if (ej.get("Status").getAsBoolean()) {
                    e.enable(false);
                }
                this.loadValues(ej.getAsJsonObject("Values"), e.getValues());
                JsonObject pos = ej.getAsJsonObject("Positions");
                e.frame.setX(pos.get("X").getAsFloat());
                e.frame.setY(pos.get("Y").getAsFloat());
            }
            Managers.COMMAND.setPrefix(root.get("Prefix").getAsString());
            Managers.FRIEND.getFriends().clear();
            for (JsonElement fr : root.getAsJsonArray("Friends")) {
                Managers.FRIEND.addFriend(fr.getAsString());
            }
            LoggerFactory.getLogger((String)"LootTech").info("Loaded autoSave config successfully!");
        }
        catch (IOException | IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void loadAutoSaveConfig() {
        this.loadFullConfig("autoSave");
    }

    public void init() {
        Path path = Paths.get((String)"LootTech", (String[])new String[]{"Configs"});
        try {
            Files.createDirectories((Path)path, (FileAttribute[])new FileAttribute[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook((Thread)new SaveThread());
    }

    private void loadValues(JsonObject valueJson, ArrayList<Value> values) {
        for (Value value : values) {
            JsonElement dataObject = valueJson.get(value.getName());
            if (dataObject == null || !dataObject.isJsonPrimitive()) continue;
            if (value instanceof ValueBoolean) {
                ((ValueBoolean)value).setValue(dataObject.getAsBoolean());
                continue;
            }
            if (value instanceof ValueNumber) {
                if (((ValueNumber)value).getType() == 1) {
                    ((ValueNumber)value).setValue((Number)Integer.valueOf((int)dataObject.getAsInt()));
                    continue;
                }
                if (((ValueNumber)value).getType() == 2) {
                    ((ValueNumber)value).setValue((Number)Double.valueOf((double)dataObject.getAsDouble()));
                    continue;
                }
                if (((ValueNumber)value).getType() != 3) continue;
                ((ValueNumber)value).setValue((Number)Float.valueOf((float)dataObject.getAsFloat()));
                continue;
            }
            if (value instanceof ValueEnum) {
                ((ValueEnum)value).setValue(((ValueEnum)value).getEnum(dataObject.getAsString()));
                continue;
            }
            if (value instanceof ValueString) {
                ((ValueString)value).setValue(dataObject.getAsString());
                continue;
            }
            if (value instanceof ValueColor) {
                ((ValueColor)value).setValue(new Color(dataObject.getAsInt()));
                if (valueJson.get(value.getName() + "-Rainbow") != null) {
                    ((ValueColor)value).setRainbow(valueJson.get(value.getName() + "-Rainbow").getAsBoolean());
                }
                if (valueJson.get(value.getName() + "-Alpha") != null) {
                    ((ValueColor)value).setValue(new Color(((ValueColor)value).getValue().getRed(), ((ValueColor)value).getValue().getGreen(), ((ValueColor)value).getValue().getBlue(), valueJson.get(value.getName() + "-Alpha").getAsInt()));
                }
                if (valueJson.get(value.getName() + "-Sync") == null) continue;
                ((ValueColor)value).setSync(valueJson.get(value.getName() + "-Sync").getAsBoolean());
                continue;
            }
            if (!(value instanceof ValueBind)) continue;
            ((ValueBind)value).setValue(dataObject.getAsInt());
        }
    }

    private void saveValues(JsonObject valueJson, ArrayList<Value> values) {
        for (Value value : values) {
            if (value instanceof ValueBoolean) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)((ValueBoolean)value).getValue())));
                continue;
            }
            if (value instanceof ValueNumber) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive(((ValueNumber)value).getValue()));
                continue;
            }
            if (value instanceof ValueEnum) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive(((ValueEnum)value).getValue().name()));
                continue;
            }
            if (value instanceof ValueString) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive(((ValueString)value).getValue()));
                continue;
            }
            if (value instanceof ValueColor) {
                valueJson.add(value.getName(), (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)((ValueColor)value).getValue().getRGB())));
                valueJson.add(value.getName() + "-Alpha", (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)((ValueColor)value).getValue().getAlpha())));
                valueJson.add(value.getName() + "-Rainbow", (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)((ValueColor)value).isRainbow())));
                valueJson.add(value.getName() + "-Sync", (JsonElement)new JsonPrimitive(Boolean.valueOf((boolean)((ValueColor)value).isSync())));
                continue;
            }
            if (!(value instanceof ValueBind)) continue;
            valueJson.add(value.getName(), (JsonElement)new JsonPrimitive((Number)Integer.valueOf((int)((ValueBind)value).getValue())));
        }
    }

    public static class SaveThread
    extends Thread {
        public void run() {
            Managers.CONFIG.saveFullConfig("autoSave");
        }
    }
}
