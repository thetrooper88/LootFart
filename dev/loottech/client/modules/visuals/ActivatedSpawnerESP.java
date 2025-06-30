package dev.loottech.client.modules.visuals;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.asm.mixins.accessor.MinecraftServerAccessor;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.OpenScreenEvent;
import dev.loottech.client.events.PreTickEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueCategory;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.regex.Pattern;
import net.minecraft.class_1694;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2586;
import net.minecraft.class_2636;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_2837;
import net.minecraft.class_2841;
import net.minecraft.class_419;
import net.minecraft.class_434;
import net.minecraft.class_8961;
import net.minecraft.class_8967;

@RegisterModule(name="ActivatedSpawnerESP", tag="ActivatedSpawnerESP", description="Highlight spawners that players have walked nearby.", category=Module.Category.VISUALS)
public class ActivatedSpawnerESP
extends Module {
    public static final Pattern FILE_NAME_INVALID_CHARS_PATTERN = Pattern.compile((String)"[\\s\\\\/:*?\"<>|]");
    private final ValueBoolean trialSpawner = new ValueBoolean("Trial Spawners", "Detect activated trial spawners.", true);
    private final ValueCategory showMoreMenu = new ValueCategory("More Settings", "");
    private final ValueBoolean enableDungeon = new ValueBoolean("Dungeons", "Enables dungeon spawner esp", this.showMoreMenu, true);
    private final ValueBoolean enableMineshafts = new ValueBoolean("Mineshafts", "Enables mineshaft spawner esp", this.showMoreMenu, true);
    private final ValueBoolean enableBastion = new ValueBoolean("Bastions", "Enables bastion spawner esp", this.showMoreMenu, true);
    private final ValueBoolean enableWoodlandMansion = new ValueBoolean("Woodland Mansion", "Enables woodland mansion spawner esp", this.showMoreMenu, true);
    private final ValueBoolean enableStronghold = new ValueBoolean("Stronghold", "Enables stronghold spawner esp", this.showMoreMenu, true);
    private final ValueBoolean enableFortress = new ValueBoolean("Fortress", "Enables fortress spawner esp", this.showMoreMenu, true);
    private final ValueBoolean chatFeedback = new ValueBoolean("Chat Feedback", "Enables chat feedback", true);
    private final ValueBoolean displayCoords = new ValueBoolean("Display Coords", "Shows coordinates in chat feedback", false);
    private final ValueBoolean extraMessage = new ValueBoolean("Stash Message", "Send a message when there are chests nearby.", true);
    private final ValueBoolean lessSpam = new ValueBoolean("Less Spam", "Enables less spam.", true);
    private final ValueBoolean airChecker = new ValueBoolean("Check Air Disturbances", "", false);
    private final ValueBoolean ignoreGeodes = new ValueBoolean("Ignore Geodes", "", true);
    private final List<class_2248> blocks = List.of((Object)class_2246.field_10034, (Object)class_2246.field_16328, (Object)class_2246.field_10312, (Object)class_2246.field_10200);
    private final ValueBoolean deactivatedSpawner = new ValueBoolean("De-Activated Spawners", "Detect spawners with torches", true);
    private final ValueNumber deactivatedSpawnerdistance = new ValueNumber("Deactivated Spawner Distance", "How many blocks from the spawner to look torches", (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)10));
    private final ValueBoolean lessRenderSpam = new ValueBoolean("Less Render Spam", "Dont render big box if no chests in range.", true);
    private final ValueNumber renderDistance = new ValueNumber("RenderDistance (Chunks)", "How many chunks from the character to render the detected spawners.", (Number)Integer.valueOf((int)32), (Number)Integer.valueOf((int)6), (Number)Integer.valueOf((int)100));
    private final ValueBoolean removerenderdist = new ValueBoolean("Remove Render Distance", "Remove spawners out of render distance", true);
    private final Color spawnerSideColor = new Color(251, 5, 5, 70);
    private final Color spawnerLineColor = new Color(251, 5, 5, 235);
    private final Color despawnerSideColor = new Color(251, 5, 251, 70);
    private final Color despawnerLineColor = new Color(251, 5, 251, 235);
    private final Color rangeSideColor = new Color(5, 178, 251, 30);
    private final Color rangeLineColor = new Color(5, 178, 251, 155);
    private final List<LoggedSpawner> loggedSpawners = new ArrayList();
    private final Set<class_2338> loggedSpawnerPositions = new HashSet();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Set<class_2338> scannedPositions = Collections.synchronizedSet((Set)new HashSet());
    private final Set<class_2338> spawnerPositions = Collections.synchronizedSet((Set)new HashSet());
    private final Set<class_2338> trialspawnerPositions = Collections.synchronizedSet((Set)new HashSet());
    private final Set<class_2338> deactivatedSpawnerPositions = Collections.synchronizedSet((Set)new HashSet());
    private final Set<class_2338> noRenderPositions = Collections.synchronizedSet((Set)new HashSet());
    private int closestSpawnerX = 2000000000;
    private int closestSpawnerY = 2000000000;
    private int closestSpawnerZ = 2000000000;
    private double SpawnerDistance = 2.0E9;
    private boolean activatedSpawnerFound = false;
    private static final Set<class_2248> GEODE_BLOCKS = Set.of((Object)class_2246.field_27159, (Object)class_2246.field_27160, (Object)class_2246.field_27114, (Object)class_2246.field_29032, (Object)class_2246.field_27161, (Object)class_2246.field_27162, (Object)class_2246.field_27163, (Object)class_2246.field_27164);

    @Override
    public void onEnable() {
        this.clearChunkData();
        this.loadLogs();
    }

    @Override
    public void onDisable() {
        this.clearChunkData();
    }

    @Override
    public void onScreenOpen(OpenScreenEvent event) {
        if (event.screen instanceof class_419 || event.screen instanceof class_434) {
            this.clearChunkData();
        }
    }

    @Override
    public void onLogout(LogoutEvent event) {
        this.clearChunkData();
    }

    private void clearChunkData() {
        this.scannedPositions.clear();
        this.spawnerPositions.clear();
        this.deactivatedSpawnerPositions.clear();
        this.noRenderPositions.clear();
        this.trialspawnerPositions.clear();
        this.closestSpawnerX = 2000000000;
        this.closestSpawnerY = 2000000000;
        this.closestSpawnerZ = 2000000000;
        this.SpawnerDistance = 2.0E9;
    }

    private boolean chunkContainsGeodeBlocks(class_2818 chunk, int sectionsToCheck) {
        class_2826[] sections = chunk.method_12006();
        for (int i = 0; i < sectionsToCheck; ++i) {
            class_2826 section = sections[i];
            if (section.method_38292()) continue;
            class_2841 blockStatesContainer = section.method_12265();
            class_2837 blockStatePalette = blockStatesContainer.field_34560.comp_119();
            int blockPaletteLength = blockStatePalette.method_12197();
            for (int i2 = 0; i2 < blockPaletteLength; ++i2) {
                class_2680 blockPaletteEntry = (class_2680)blockStatePalette.method_12288(i2);
                if (!GEODE_BLOCKS.contains((Object)blockPaletteEntry.method_26204())) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPreTick(PreTickEvent event) {
        if (ActivatedSpawnerESP.mc.field_1687 == null || ActivatedSpawnerESP.mc.field_1724 == null) {
            return;
        }
        AtomicReferenceArray chunks = ActivatedSpawnerESP.mc.field_1687.method_2935().field_16246.field_16251;
        HashSet chunkSet = new HashSet();
        for (int i = 0; i < chunks.length(); ++i) {
            class_2818 chunk2 = (class_2818)chunks.get(i);
            if (chunk2 == null) continue;
            chunkSet.add((Object)chunk2);
        }
        chunkSet.forEach(chunk -> {
            ArrayList blockEntities = new ArrayList(chunk.method_12214().values());
            for (class_2586 blockEntity : blockEntities) {
                if (blockEntity instanceof class_2636) {
                    this.activatedSpawnerFound = false;
                    class_2636 spawner = (class_2636)blockEntity;
                    class_2338 pos = spawner.method_11016();
                    String monster = null;
                    if (spawner.method_11390().field_9155 != null && spawner.method_11390().field_9155.method_38093().method_10580("id") != null) {
                        monster = spawner.method_11390().field_9155.method_38093().method_10580("id").toString();
                    }
                    if (!(this.trialspawnerPositions.contains((Object)pos) || this.noRenderPositions.contains((Object)pos) || this.deactivatedSpawnerPositions.contains((Object)pos) || this.spawnerPositions.contains((Object)pos))) {
                        if (this.airChecker.getValue() && (spawner.method_11390().field_9154 == 20 || spawner.method_11390().field_9154 == 0)) {
                            class_2338 bpos;
                            int z;
                            int y;
                            int x;
                            boolean airFound = false;
                            boolean caveAirFound = false;
                            boolean geodeNearby = false;
                            if (this.ignoreGeodes.getValue() && this.chunkContainsGeodeBlocks((class_2818)chunk, Math.min((int)chunk.method_12006().length, (int)20))) {
                                for (x = -5; x <= 5; ++x) {
                                    for (y = -5; y <= 5; ++y) {
                                        for (z = -5; z <= 5; ++z) {
                                            bpos = pos.method_10069(x, y, z);
                                            if (!GEODE_BLOCKS.contains((Object)ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204())) continue;
                                            geodeNearby = true;
                                            break;
                                        }
                                        if (geodeNearby) break;
                                    }
                                    if (geodeNearby) break;
                                }
                            }
                            if (monster != null && !this.scannedPositions.contains((Object)pos)) {
                                if (monster.contains((CharSequence)"zombie") || monster.contains((CharSequence)"skeleton") || monster.contains((CharSequence)":spider")) {
                                    for (x = -2; x < 2; ++x) {
                                        block5: for (y = -1; y < 3; ++y) {
                                            for (z = -2; z < 2; ++z) {
                                                bpos = new class_2338(pos.method_10263() + x, pos.method_10264() + y, pos.method_10260() + z);
                                                if (ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() == class_2246.field_10124) {
                                                    airFound = true;
                                                }
                                                if (ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() == class_2246.field_10543) {
                                                    caveAirFound = true;
                                                }
                                                if (caveAirFound && airFound) continue block5;
                                            }
                                        }
                                    }
                                    if (caveAirFound && airFound) {
                                        if (monster == ":spider") {
                                            this.displayMessage("dungeon", pos, ":spider");
                                        } else {
                                            this.displayMessage("dungeon", pos, "null");
                                        }
                                    }
                                } else if (monster.contains((CharSequence)"cave_spider")) {
                                    for (x = -1; x < 2; ++x) {
                                        block8: for (y = 0; y < 2; ++y) {
                                            for (z = -1; z < 2; ++z) {
                                                bpos = new class_2338(pos.method_10263() + x, pos.method_10264() + y, pos.method_10260() + z);
                                                if (ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() == class_2246.field_10124) {
                                                    airFound = true;
                                                }
                                                if (ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() == class_2246.field_10543) {
                                                    caveAirFound = true;
                                                }
                                                if (caveAirFound && airFound) continue block8;
                                            }
                                        }
                                    }
                                    if (caveAirFound && airFound) {
                                        this.displayMessage("cave_spider", pos, "null");
                                    }
                                } else if (monster.contains((CharSequence)"silverfish")) {
                                    for (x = -3; x < 4; ++x) {
                                        block11: for (y = -2; y < 4; ++y) {
                                            for (z = -3; z < 4; ++z) {
                                                bpos = new class_2338(pos.method_10263() + x, pos.method_10264() + y, pos.method_10260() + z);
                                                if (ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() == class_2246.field_10124) {
                                                    airFound = true;
                                                }
                                                if (ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() == class_2246.field_10543) {
                                                    caveAirFound = true;
                                                }
                                                if (caveAirFound && airFound) continue block11;
                                            }
                                        }
                                    }
                                    if (caveAirFound && airFound) {
                                        this.displayMessage("silverfish", pos, "null");
                                    }
                                }
                            }
                            this.scannedPositions.add((Object)pos);
                        } else if (spawner.method_11390().field_9154 != 20) {
                            if (ActivatedSpawnerESP.mc.field_1687.method_27983() == class_1937.field_25180 && spawner.method_11390().field_9154 == 0) {
                                return;
                            }
                            if (this.chatFeedback.getValue()) {
                                if (monster != null) {
                                    if (monster.contains((CharSequence)"zombie") || monster.contains((CharSequence)"skeleton") || monster.contains((CharSequence)":spider")) {
                                        if (monster == ":spider") {
                                            this.displayMessage("dungeon", pos, ":spider");
                                        } else {
                                            this.displayMessage("dungeon", pos, "null");
                                        }
                                    } else if (monster.contains((CharSequence)"cave_spider")) {
                                        this.displayMessage("cave_spider", pos, "null");
                                    } else if (monster.contains((CharSequence)"silverfish")) {
                                        this.displayMessage("silverfish", pos, "null");
                                    } else if (monster.contains((CharSequence)"blaze")) {
                                        this.displayMessage("blaze", pos, "null");
                                    } else if (monster.contains((CharSequence)"magma")) {
                                        this.displayMessage("magma", pos, "null");
                                    } else {
                                        if (this.displayCoords.getValue()) {
                                            ChatUtils.sendMessage(String.valueOf((Object)class_2561.method_30163((String)("\u00a7cActivatedSpawnerESP\u00a7r | Detected Activated Spawner! Block Position: " + String.valueOf((Object)pos)))));
                                        } else {
                                            ChatUtils.sendMessage(String.valueOf((Object)class_2561.method_30163((String)"\u00a7cActivatedSpawnerESP\u00a7r | Detected Activated Spawner!")));
                                        }
                                        this.spawnerPositions.add((Object)pos);
                                        this.activatedSpawnerFound = true;
                                    }
                                } else {
                                    if (this.displayCoords.getValue()) {
                                        ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cActivatedSpawnerESP\u00a7r | Detected Activated Spawner! Block Position: " + String.valueOf((Object)pos))));
                                    } else {
                                        ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cActivatedSpawnerESP\u00a7r | Detected Activated Spawner!"));
                                    }
                                    this.spawnerPositions.add((Object)pos);
                                    this.activatedSpawnerFound = true;
                                }
                            }
                        }
                        if (this.activatedSpawnerFound) {
                            int z;
                            int y;
                            int x;
                            if (this.deactivatedSpawner.getValue()) {
                                boolean lightsFound = false;
                                for (x = -this.deactivatedSpawnerdistance.getValue().intValue(); x < this.deactivatedSpawnerdistance.getValue().intValue() + 1; ++x) {
                                    block14: for (y = -this.deactivatedSpawnerdistance.getValue().intValue(); y < this.deactivatedSpawnerdistance.getValue().intValue() + 1; ++y) {
                                        for (z = -this.deactivatedSpawnerdistance.getValue().intValue(); z < this.deactivatedSpawnerdistance.getValue().intValue() + 1; ++z) {
                                            class_2338 bpos = new class_2338(pos.method_10263() + x, pos.method_10264() + y, pos.method_10260() + z);
                                            if (ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_10336 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_22092 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_10523 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_10009 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_10171 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_22122 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_37572 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_37574 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_37574 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_10174 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_16541 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_22110 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_17350 && ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204() != class_2246.field_23860) continue;
                                            lightsFound = true;
                                            this.deactivatedSpawnerPositions.add((Object)pos);
                                            continue block14;
                                        }
                                    }
                                }
                                if (this.chatFeedback.getValue() && lightsFound) {
                                    ChatUtils.sendMessage(class_2561.method_30163((String)"The Spawner has torches or other light blocks!"));
                                }
                            }
                            boolean chestfound = false;
                            for (x = -16; x < 17; ++x) {
                                for (y = -16; y < 17; ++y) {
                                    for (z = -16; z < 17; ++z) {
                                        class_2338 bpos = new class_2338(pos.method_10263() + x, pos.method_10264() + y, pos.method_10260() + z);
                                        if (this.blocks.contains((Object)ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204())) {
                                            chestfound = true;
                                            break;
                                        }
                                        class_238 box = new class_238(bpos);
                                        List minecarts = ActivatedSpawnerESP.mc.field_1687.method_8390(class_1694.class, box, entity -> true);
                                        if (minecarts.isEmpty()) continue;
                                        chestfound = true;
                                        break;
                                    }
                                    if (chestfound) break;
                                }
                                if (chestfound) break;
                            }
                            if (!chestfound && this.lessRenderSpam.getValue()) {
                                this.noRenderPositions.add((Object)pos);
                            }
                            if (this.chatFeedback.getValue()) {
                                if (this.lessSpam.getValue() && chestfound && this.extraMessage.getValue()) {
                                    ChatUtils.sendMessage("There may be stashed items in the storage near the spawners!");
                                } else if (!this.lessSpam.getValue() && this.extraMessage.getValue()) {
                                    ChatUtils.sendMessage("There may be stashed items in the storage near the spawners!");
                                }
                            }
                        }
                    }
                }
                if (!(blockEntity instanceof class_8961)) continue;
                class_8961 trialspawner = (class_8961)blockEntity;
                class_2338 tPos = trialspawner.method_11016();
                if (!this.trialSpawner.getValue() || this.trialspawnerPositions.contains((Object)tPos) || this.noRenderPositions.contains((Object)tPos) || this.deactivatedSpawnerPositions.contains((Object)tPos) || this.spawnerPositions.contains((Object)tPos) || trialspawner.method_55151() == class_8967.field_47384) continue;
                if (this.chatFeedback.getValue()) {
                    if (this.displayCoords.getValue()) {
                        ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cActivatedSpawnerESP\u00a7r | Detected Activated \u00a7cTRIAL\u00a7r Spawner! Block Position: " + String.valueOf((Object)tPos))));
                    } else {
                        ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cActivatedSpawnerESP\u00a7r | Detected Activated \u00a7cTRIAL\u00a7r Spawner!"));
                    }
                }
                this.trialspawnerPositions.add((Object)tPos);
                boolean chestfound = false;
                for (int x = -14; x < 15; ++x) {
                    for (int y = -14; y < 15; ++y) {
                        for (int z = -14; z < 15; ++z) {
                            class_2338 bpos = new class_2338(tPos.method_10263() + x, tPos.method_10264() + y, tPos.method_10260() + z);
                            if (this.blocks.contains((Object)ActivatedSpawnerESP.mc.field_1687.method_8320(bpos).method_26204())) {
                                chestfound = true;
                                break;
                            }
                            class_238 box = new class_238(bpos);
                            List minecarts = ActivatedSpawnerESP.mc.field_1687.method_8390(class_1694.class, box, entity -> true);
                            if (minecarts.isEmpty()) continue;
                            chestfound = true;
                            break;
                        }
                        if (chestfound) break;
                    }
                    if (chestfound) break;
                }
                if (!chestfound && this.lessRenderSpam.getValue()) {
                    this.noRenderPositions.add((Object)tPos);
                }
                if (!this.chatFeedback.getValue()) continue;
                if (this.lessSpam.getValue() && chestfound && this.extraMessage.getValue()) {
                    ChatUtils.sendMessage("There may be stashed items in the storage near the spawners!");
                    continue;
                }
                if (this.lessSpam.getValue() || !this.extraMessage.getValue()) continue;
                ChatUtils.sendMessage("There may be stashed items in the storage near the spawners!");
            }
        });
        if (this.removerenderdist.getValue()) {
            this.removeChunksOutsideRenderDistance((Set<class_2818>)chunkSet);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onRender3D(Render3DEvent event) {
        RenderBuffers.preRender();
        if (this.spawnerSideColor.getAlpha() > 5 || this.spawnerLineColor.getAlpha() > 5 || this.rangeSideColor.getAlpha() > 5 || this.rangeLineColor.getAlpha() > 5) {
            Iterator iterator = this.spawnerPositions;
            synchronized (iterator) {
                for (class_2338 pos : this.spawnerPositions) {
                    class_2338 playerPos = new class_2338(ActivatedSpawnerESP.mc.field_1724.method_31477(), pos.method_10264(), ActivatedSpawnerESP.mc.field_1724.method_31479());
                    if (pos == null || !playerPos.method_19771((class_2382)pos, (double)(this.renderDistance.getValue().intValue() * 16))) continue;
                    int startX = pos.method_10263();
                    int startY = pos.method_10264();
                    int startZ = pos.method_10260();
                    int endX = pos.method_10263();
                    int endY = pos.method_10264();
                    int endZ = pos.method_10260();
                    if (!this.lessRenderSpam.getValue()) {
                        this.renderRange(new class_238(new class_243((double)(startX + 17), (double)(startY + 17), (double)(startZ + 17)), new class_243((double)(endX - 16), (double)(endY - 16), (double)(endZ - 16))), this.rangeSideColor, this.rangeLineColor, event);
                    } else if (this.lessRenderSpam.getValue() && !this.noRenderPositions.contains((Object)pos)) {
                        this.renderRange(new class_238(new class_243((double)(startX + 17), (double)(startY + 17), (double)(startZ + 17)), new class_243((double)(endX - 16), (double)(endY - 16), (double)(endZ - 16))), this.rangeSideColor, this.rangeLineColor, event);
                    }
                    if (this.deactivatedSpawnerPositions.contains((Object)pos)) {
                        this.render(new class_238(new class_243((double)(startX + 1), (double)(startY + 1), (double)(startZ + 1)), new class_243((double)endX, (double)endY, (double)endZ)), this.despawnerSideColor, this.despawnerLineColor, event);
                    } else {
                        this.render(new class_238(new class_243((double)(startX + 1), (double)(startY + 1), (double)(startZ + 1)), new class_243((double)endX, (double)endY, (double)endZ)), this.spawnerSideColor, this.spawnerLineColor, event);
                    }
                    this.render2(new class_238(new class_243((double)this.closestSpawnerX, (double)this.closestSpawnerY, (double)this.closestSpawnerZ), new class_243((double)this.closestSpawnerX, (double)this.closestSpawnerY, (double)this.closestSpawnerZ)), this.spawnerSideColor, this.spawnerLineColor, event);
                }
            }
            for (class_2338 pos : this.trialspawnerPositions) {
                class_2338 playerPos = new class_2338(ActivatedSpawnerESP.mc.field_1724.method_31477(), pos.method_10264(), ActivatedSpawnerESP.mc.field_1724.method_31479());
                if (pos == null || !playerPos.method_19771((class_2382)pos, (double)(this.renderDistance.getValue().intValue() * 16))) continue;
                int startX = pos.method_10263();
                int startY = pos.method_10264();
                int startZ = pos.method_10260();
                int endX = pos.method_10263();
                int endY = pos.method_10264();
                int endZ = pos.method_10260();
                if (!this.lessRenderSpam.getValue()) {
                    this.renderRange(new class_238(new class_243((double)(startX + 17), (double)(startY + 17), (double)(startZ + 17)), new class_243((double)(endX - 16), (double)(endY - 16), (double)(endZ - 16))), this.rangeSideColor, this.rangeLineColor, event);
                } else if (this.lessRenderSpam.getValue() && !this.noRenderPositions.contains((Object)pos)) {
                    this.renderRange(new class_238(new class_243((double)(startX + 17), (double)(startY + 17), (double)(startZ + 17)), new class_243((double)(endX - 16), (double)(endY - 16), (double)(endZ - 16))), this.rangeSideColor, this.rangeLineColor, event);
                }
                if (this.deactivatedSpawnerPositions.contains((Object)pos)) {
                    this.render(new class_238(new class_243((double)(startX + 1), (double)(startY + 1), (double)(startZ + 1)), new class_243((double)endX, (double)endY, (double)endZ)), this.despawnerSideColor, this.despawnerLineColor, event);
                } else {
                    this.render(new class_238(new class_243((double)(startX + 1), (double)(startY + 1), (double)(startZ + 1)), new class_243((double)endX, (double)endY, (double)endZ)), this.spawnerSideColor, this.spawnerLineColor, event);
                }
                this.render2(new class_238(new class_243((double)this.closestSpawnerX, (double)this.closestSpawnerY, (double)this.closestSpawnerZ), new class_243((double)this.closestSpawnerX, (double)this.closestSpawnerY, (double)this.closestSpawnerZ)), this.spawnerSideColor, this.spawnerLineColor, event);
            }
        }
        RenderBuffers.postRender();
    }

    private void displayMessage(String key, class_2338 pos, String key2) {
        if (this.chatFeedback.getValue()) {
            if (Objects.equals((Object)key, (Object)"dungeon")) {
                if (Objects.equals((Object)key2, (Object)":spider")) {
                    if (ActivatedSpawnerESP.mc.field_1687.method_8320(pos.method_10074()).method_26204() == class_2246.field_10148 && this.enableWoodlandMansion.getValue()) {
                        this.activatedSpawnerFound = true;
                        this.spawnerPositions.add((Object)pos);
                        if (this.displayCoords.getValue()) {
                            ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cASD\u00a7r | Detected Activated \u00a7cWOODLAND MANSION\u00a7r Spawner! Block Position: " + String.valueOf((Object)pos))));
                        } else {
                            ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cASD\u00a7r | Detected Activated \u00a7cWOODLAND MANSION\u00a7r Spawner!"));
                        }
                    } else if (this.enableDungeon.getValue()) {
                        this.activatedSpawnerFound = true;
                        this.spawnerPositions.add((Object)pos);
                        if (this.displayCoords.getValue()) {
                            ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cASD\u00a7r | Detected Activated \u00a7cDUNGEON\u00a7r Spawner! Block Position: " + String.valueOf((Object)pos))));
                        } else {
                            ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cASD\u00a7r | Detected Activated \u00a7cDUNGEON\u00a7r Spawner!"));
                        }
                    }
                } else if (this.enableDungeon.getValue()) {
                    this.activatedSpawnerFound = true;
                    this.spawnerPositions.add((Object)pos);
                    if (this.displayCoords.getValue()) {
                        ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cASD\u00a7r | Detected Activated \u00a7cDUNGEON\u00a7r Spawner! Block Position: " + String.valueOf((Object)pos))));
                    } else {
                        ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cASD\u00a7r | Detected Activated \u00a7cDUNGEON\u00a7r Spawner!"));
                    }
                }
            } else if (key == "cave_spider" && this.enableMineshafts.getValue()) {
                this.activatedSpawnerFound = true;
                this.spawnerPositions.add((Object)pos);
                if (this.displayCoords.getValue()) {
                    ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cASD\u00a7r | Detected Activated \u00a7cMINESHAFT\u00a7r Spawner! Block Position: " + String.valueOf((Object)pos))));
                } else {
                    ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cASD\u00a7r | Detected Activated \u00a7cMINESHAFT\u00a7r Spawner!"));
                }
            } else if (key == "silverfish" && this.enableStronghold.getValue()) {
                this.activatedSpawnerFound = true;
                this.spawnerPositions.add((Object)pos);
                if (this.displayCoords.getValue()) {
                    ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cASD\u00a7r | Detected Activated \u00a7cSTRONGHOLD\u00a7r Spawner! Block Position: " + String.valueOf((Object)pos))));
                } else {
                    ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cASD\u00a7r | Detected Activated \u00a7cSTRONGHOLD\u00a7r Spawner!"));
                }
            } else if (key == "blaze" && this.enableFortress.getValue()) {
                this.activatedSpawnerFound = true;
                this.spawnerPositions.add((Object)pos);
                if (this.displayCoords.getValue()) {
                    ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cASD\u00a7r | Detected Activated \u00a7cFORTRESS\u00a7r Spawner! Block Position: " + String.valueOf((Object)pos))));
                } else {
                    ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cASD\u00a7r | Detected Activated \u00a7cFORTRESS\u00a7r Spawner!"));
                }
            } else if (key == "magma" && this.enableBastion.getValue()) {
                this.activatedSpawnerFound = true;
                this.spawnerPositions.add((Object)pos);
                if (this.displayCoords.getValue()) {
                    ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cASD\u00a7r | Detected Activated \u00a7cBASTION\u00a7r Spawner! Block Position: " + String.valueOf((Object)pos))));
                } else {
                    ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cASD\u00a7r | Detected Activated \u00a7cBASTION\u00a7r Spawner!"));
                }
            } else {
                this.activatedSpawnerFound = true;
                this.spawnerPositions.add((Object)pos);
                if (this.displayCoords.getValue()) {
                    ChatUtils.sendMessage(class_2561.method_30163((String)("\u00a7cASD\u00a7r | Detected Activated Spawner! Block Position: " + String.valueOf((Object)pos))));
                } else {
                    ChatUtils.sendMessage(class_2561.method_30163((String)"\u00a7cASD\u00a7r | Detected Activated Spawner!"));
                }
            }
        }
    }

    private void render(class_238 box, Color sides, Color lines, Render3DEvent event) {
        RenderManager.renderBoundingBox(event.getMatrices(), box, 1.0f, lines.getRGB());
        RenderManager.renderBox(event.getMatrices(), box, sides.getRGB());
    }

    private void render2(class_238 box, Color sides, Color lines, Render3DEvent event) {
        RenderManager.renderBoundingBox(event.getMatrices(), box, 1.0f, lines.getRGB());
        RenderManager.renderBox(event.getMatrices(), box, sides.getRGB());
    }

    private void renderRange(class_238 box, Color sides, Color lines, Render3DEvent event) {
        RenderManager.renderBoundingBox(event.getMatrices(), box, 1.0f, lines.getRGB());
        RenderManager.renderBox(event.getMatrices(), box, sides.getRGB());
    }

    private void removeChunksOutsideRenderDistance(Set<class_2818> chunks) {
        this.removeBlockPosOutsideRenderDistance(this.scannedPositions, chunks);
        this.removeBlockPosOutsideRenderDistance(this.spawnerPositions, chunks);
        this.removeBlockPosOutsideRenderDistance(this.deactivatedSpawnerPositions, chunks);
        this.removeBlockPosOutsideRenderDistance(this.trialspawnerPositions, chunks);
        this.removeBlockPosOutsideRenderDistance(this.noRenderPositions, chunks);
    }

    private void removeBlockPosOutsideRenderDistance(Set<class_2338> blockSet, Set<class_2818> worldChunks) {
        blockSet.removeIf(blockpos -> {
            class_2338 boxPos = new class_2338((int)Math.floor((double)blockpos.method_10263()), (int)Math.floor((double)blockpos.method_10264()), (int)Math.floor((double)blockpos.method_10260()));
            assert (ActivatedSpawnerESP.mc.field_1687 != null);
            return !worldChunks.contains((Object)ActivatedSpawnerESP.mc.field_1687.method_22350(boxPos));
        });
    }

    private void logSpawner(class_2338 pos) {
        if (!this.loggedSpawnerPositions.contains((Object)pos)) {
            this.loggedSpawnerPositions.add((Object)pos);
            this.loggedSpawners.add((Object)new LoggedSpawner(pos.method_10263(), pos.method_10264(), pos.method_10260()));
            this.saveJson();
            this.saveCsv();
        }
    }

    private void saveCsv() {
        try {
            File file = this.getCsvFile();
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write("X,Y,Z\n");
            for (LoggedSpawner ls : this.loggedSpawners) {
                ls.write((Writer)writer);
            }
            writer.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private void saveJson() {
        try {
            File file = this.getJsonFile();
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            GSON.toJson(this.loggedSpawners, (Appendable)writer);
            writer.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private File getJsonFile() {
        return new File(new File(new File("TrouserStreak", "ActivatedSpawners"), ActivatedSpawnerESP.getFileWorldName()), "spawners.json");
    }

    private File getCsvFile() {
        return new File(new File(new File("TrouserStreak", "ActivatedSpawners"), ActivatedSpawnerESP.getFileWorldName()), "spawners.csv");
    }

    private void loadLogs() {
        FileReader reader2;
        File file = this.getJsonFile();
        boolean loaded = false;
        if (file.exists()) {
            try {
                reader2 = new FileReader(file);
                List data = (List)GSON.fromJson((Reader)reader2, new TypeToken<List<LoggedSpawner>>(){}.getType());
                reader2.close();
                if (data != null) {
                    this.loggedSpawners.addAll((Collection)data);
                    for (LoggedSpawner ls : data) {
                        this.loggedSpawnerPositions.add((Object)new class_2338(ls.x, ls.y, ls.z));
                    }
                    loaded = true;
                }
            }
            catch (Exception reader2) {
                // empty catch block
            }
        }
        if (!loaded && (file = this.getCsvFile()).exists()) {
            try {
                String line;
                reader2 = new BufferedReader((Reader)new FileReader(file));
                reader2.readLine();
                while ((line = reader2.readLine()) != null) {
                    LoggedSpawner ls;
                    String[] values = line.split(",");
                    ls = new LoggedSpawner(Integer.parseInt((String)values[0]), Integer.parseInt((String)values[1]), Integer.parseInt((String)values[2]));
                    this.loggedSpawners.add((Object)ls);
                    this.loggedSpawnerPositions.add((Object)new class_2338(ls.x, ls.y, ls.z));
                }
                reader2.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static String getFileWorldName() {
        return FILE_NAME_INVALID_CHARS_PATTERN.matcher((CharSequence)ActivatedSpawnerESP.getWorldName()).replaceAll("_");
    }

    public static String getWorldName() {
        if (mc.method_1542()) {
            if (ActivatedSpawnerESP.mc.field_1687 == null) {
                return "";
            }
            File folder = ((MinecraftServerAccessor)mc.method_1576()).getSession().method_27424(ActivatedSpawnerESP.mc.field_1687.method_27983()).toFile();
            if (folder.toPath().relativize(ActivatedSpawnerESP.mc.field_1697.toPath()).getNameCount() != 2) {
                folder = folder.getParentFile();
            }
            return folder.getName();
        }
        if (mc.method_1558() != null) {
            return mc.method_1558().method_52811() ? "realms" : ActivatedSpawnerESP.mc.method_1558().field_3761;
        }
        return "";
    }

    private static class LoggedSpawner {
        public int x;
        public int y;
        public int z;

        public LoggedSpawner(int x, int y, int z) {
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
            if (!(o instanceof LoggedSpawner)) {
                return false;
            }
            LoggedSpawner that = (LoggedSpawner)o;
            return this.x == that.x && this.y == that.y && this.z == that.z;
        }

        public int hashCode() {
            return Objects.hash((Object[])new Object[]{this.x, this.y, this.z});
        }
    }
}
