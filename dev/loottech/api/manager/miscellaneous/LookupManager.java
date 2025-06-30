package dev.loottech.api.manager.miscellaneous;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.Util;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_640;
import org.apache.commons.io.IOUtils;

public class LookupManager
implements Util,
EventListener {
    private static final Map<String, UUID> LOOKUPS_UUID = new HashMap();
    private static final Map<UUID, String> LOOKUPS_NAME = new HashMap();
    private static final Set<UUID> FAILED_LOOKUPS = new HashSet();

    public UUID getUUIDFromName(String name) {
        ArrayList playerListEntries;
        class_640 profile;
        UUID uuid = (UUID)LOOKUPS_UUID.get((Object)name);
        if (uuid != null) {
            return uuid;
        }
        if (mc.method_1562() != null && (profile = (class_640)(playerListEntries = new ArrayList(mc.method_1562().method_2880())).stream().filter(info -> info.method_2966().getName().equalsIgnoreCase(name)).findFirst().orElse(null)) != null) {
            UUID result = profile.method_2966().getId();
            LOOKUPS_UUID.put((Object)name, (Object)result);
            return result;
        }
        return null;
    }

    public String getNameFromUUID(UUID uuid) {
        if (FAILED_LOOKUPS.contains((Object)uuid)) {
            return null;
        }
        if (LOOKUPS_NAME.containsKey((Object)uuid)) {
            return (String)LOOKUPS_NAME.get((Object)uuid);
        }
        String url = String.format((String)"https://laby.net/api/v2/user/%s/get-profile", (Object[])new Object[]{uuid.toString()});
        try {
            String name = IOUtils.toString((URL)new URL(url), (Charset)StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString((String)name).getAsJsonObject();
            String result = jsonObject.get("username").toString();
            LOOKUPS_NAME.put((Object)uuid, (Object)result.replace((CharSequence)"\"", (CharSequence)""));
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            FAILED_LOOKUPS.add((Object)uuid);
            return null;
        }
    }
}
