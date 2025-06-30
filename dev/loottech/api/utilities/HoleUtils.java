package dev.loottech.api.utilities;

import dev.loottech.api.utilities.BlockUtils;
import dev.loottech.api.utilities.IMinecraft;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;

public class HoleUtils
implements IMinecraft {
    public static boolean isBedrockHole(class_2338 pos) {
        boolean retVal = false;
        if (HoleUtils.mc.field_1687.method_8320(pos).method_26204().equals((Object)class_2246.field_10124) && HoleUtils.mc.field_1687.method_8320(pos.method_10084()).method_26204().equals((Object)class_2246.field_10124) && HoleUtils.mc.field_1687.method_8320(pos.method_10084().method_10084()).method_26204().equals((Object)class_2246.field_10124) && HoleUtils.mc.field_1687.method_8320(pos.method_10074()).method_26204().equals((Object)class_2246.field_9987) && HoleUtils.mc.field_1687.method_8320(pos.method_10078()).method_26204().equals((Object)class_2246.field_9987) && HoleUtils.mc.field_1687.method_8320(pos.method_10067()).method_26204().equals((Object)class_2246.field_9987) && HoleUtils.mc.field_1687.method_8320(pos.method_10072()).method_26204().equals((Object)class_2246.field_9987) && HoleUtils.mc.field_1687.method_8320(pos.method_10095()).method_26204().equals((Object)class_2246.field_9987)) {
            retVal = true;
        }
        return retVal;
    }

    public static boolean isObiHole(class_2338 pos) {
        boolean retVal = false;
        int obiCount = 0;
        if (HoleUtils.mc.field_1687.method_8320(pos).method_26204().equals((Object)class_2246.field_10124) && HoleUtils.mc.field_1687.method_8320(pos.method_10084()).method_26204().equals((Object)class_2246.field_10124) && HoleUtils.mc.field_1687.method_8320(pos.method_10084().method_10084()).method_26204().equals((Object)class_2246.field_10124) && (HoleUtils.mc.field_1687.method_8320(pos.method_10074()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10074()).method_26204().equals((Object)class_2246.field_10540))) {
            if (HoleUtils.mc.field_1687.method_8320(pos.method_10074()).method_26204().equals((Object)class_2246.field_10540)) {
                ++obiCount;
            }
            if (HoleUtils.mc.field_1687.method_8320(pos.method_10078()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10078()).method_26204().equals((Object)class_2246.field_10540)) {
                if (HoleUtils.mc.field_1687.method_8320(pos.method_10078()).method_26204().equals((Object)class_2246.field_10540)) {
                    ++obiCount;
                }
                if (HoleUtils.mc.field_1687.method_8320(pos.method_10067()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10067()).method_26204().equals((Object)class_2246.field_10540)) {
                    if (HoleUtils.mc.field_1687.method_8320(pos.method_10067()).method_26204().equals((Object)class_2246.field_10540)) {
                        ++obiCount;
                    }
                    if (HoleUtils.mc.field_1687.method_8320(pos.method_10072()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10072()).method_26204().equals((Object)class_2246.field_10540)) {
                        if (HoleUtils.mc.field_1687.method_8320(pos.method_10072()).method_26204().equals((Object)class_2246.field_10540)) {
                            ++obiCount;
                        }
                        if (HoleUtils.mc.field_1687.method_8320(pos.method_10095()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10095()).method_26204().equals((Object)class_2246.field_10540)) {
                            if (HoleUtils.mc.field_1687.method_8320(pos.method_10095()).method_26204().equals((Object)class_2246.field_10540)) {
                                ++obiCount;
                            }
                            if (obiCount >= 1) {
                                retVal = true;
                            }
                        }
                    }
                }
            }
        }
        return retVal;
    }

    public static boolean isDoubleHole(class_2338 pos) {
        for (class_2350 f : class_2350.class_2353.field_11062) {
            int offZ;
            int offX = f.method_10148();
            if (HoleUtils.mc.field_1687.method_8320(pos.method_10069(offX, 0, offZ = f.method_10165())).method_26204() != class_2246.field_10540 && HoleUtils.mc.field_1687.method_8320(pos.method_10069(offX, 0, offZ)).method_26204() != class_2246.field_9987 || HoleUtils.mc.field_1687.method_8320(pos.method_10069(offX * -2, 0, offZ * -2)).method_26204() != class_2246.field_10540 && HoleUtils.mc.field_1687.method_8320(pos.method_10069(offX * -2, 0, offZ * -2)).method_26204() != class_2246.field_9987 || HoleUtils.mc.field_1687.method_8320(pos.method_10069(offX * -1, 0, offZ * -1)).method_26204() != class_2246.field_10124 || !HoleUtils.isSafeBlock(pos.method_10069(0, -1, 0)) || !HoleUtils.isSafeBlock(pos.method_10069(offX * -1, -1, offZ * -1))) continue;
            if (offZ == 0 && HoleUtils.isSafeBlock(pos.method_10069(0, 0, 1)) && HoleUtils.isSafeBlock(pos.method_10069(0, 0, -1)) && HoleUtils.isSafeBlock(pos.method_10069(offX * -1, 0, 1)) && HoleUtils.isSafeBlock(pos.method_10069(offX * -1, 0, -1))) {
                return true;
            }
            if (offX != 0 || !HoleUtils.isSafeBlock(pos.method_10069(1, 0, 0)) || !HoleUtils.isSafeBlock(pos.method_10069(-1, 0, 0)) || !HoleUtils.isSafeBlock(pos.method_10069(1, 0, offZ * -1)) || !HoleUtils.isSafeBlock(pos.method_10069(-1, 0, offZ * -1))) continue;
            return true;
        }
        return false;
    }

    public static boolean isHole(class_2338 pos) {
        boolean retVal = false;
        if (HoleUtils.mc.field_1687.method_8320(pos).method_26204().equals((Object)class_2246.field_10124) && HoleUtils.mc.field_1687.method_8320(pos.method_10084()).method_26204().equals((Object)class_2246.field_10124) && HoleUtils.mc.field_1687.method_8320(pos.method_10084().method_10084()).method_26204().equals((Object)class_2246.field_10124) && (HoleUtils.mc.field_1687.method_8320(pos.method_10074()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10074()).method_26204().equals((Object)class_2246.field_10540)) && (HoleUtils.mc.field_1687.method_8320(pos.method_10078()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10078()).method_26204().equals((Object)class_2246.field_10540)) && (HoleUtils.mc.field_1687.method_8320(pos.method_10067()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10067()).method_26204().equals((Object)class_2246.field_10540)) && (HoleUtils.mc.field_1687.method_8320(pos.method_10072()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10072()).method_26204().equals((Object)class_2246.field_10540)) && (HoleUtils.mc.field_1687.method_8320(pos.method_10095()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10095()).method_26204().equals((Object)class_2246.field_10540))) {
            retVal = true;
        }
        return retVal;
    }

    static boolean isSafeBlock(class_2338 pos) {
        return HoleUtils.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10540 || HoleUtils.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_9987;
    }

    public static boolean isDoubleBedrockHoleX(class_2338 blockPos) {
        if (!HoleUtils.mc.field_1687.method_8320(blockPos).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(1, 0, 0)).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 1, 0)).method_26204().equals((Object)class_2246.field_10124) && !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(1, 1, 0)).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 2, 0)).method_26204().equals((Object)class_2246.field_10124) && !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(1, 2, 0)).method_26204().equals((Object)class_2246.field_10124)) {
            return false;
        }
        for (class_2338 blockPos2 : new class_2338[]{blockPos.method_10069(2, 0, 0), blockPos.method_10069(1, 0, 1), blockPos.method_10069(1, 0, -1), blockPos.method_10069(-1, 0, 0), blockPos.method_10069(0, 0, 1), blockPos.method_10069(0, 0, -1), blockPos.method_10069(0, -1, 0), blockPos.method_10069(1, -1, 0)}) {
            class_2680 iBlockState = HoleUtils.mc.field_1687.method_8320(blockPos2);
            if (iBlockState.method_26204() != class_2246.field_10124 && iBlockState.method_26204() == class_2246.field_9987) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleBedrockHoleZ(class_2338 blockPos) {
        if (!HoleUtils.mc.field_1687.method_8320(blockPos).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 0, 1)).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 1, 0)).method_26204().equals((Object)class_2246.field_10124) && !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 1, 1)).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 2, 0)).method_26204().equals((Object)class_2246.field_10124) && !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 2, 1)).method_26204().equals((Object)class_2246.field_10124)) {
            return false;
        }
        for (class_2338 blockPos2 : new class_2338[]{blockPos.method_10069(0, 0, 2), blockPos.method_10069(1, 0, 1), blockPos.method_10069(-1, 0, 1), blockPos.method_10069(0, 0, -1), blockPos.method_10069(1, 0, 0), blockPos.method_10069(-1, 0, 0), blockPos.method_10069(0, -1, 0), blockPos.method_10069(0, -1, 1)}) {
            class_2680 iBlockState = HoleUtils.mc.field_1687.method_8320(blockPos2);
            if (iBlockState.method_26204() != class_2246.field_10124 && iBlockState.method_26204() == class_2246.field_9987) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleObsidianHoleX(class_2338 blockPos) {
        if (!HoleUtils.mc.field_1687.method_8320(blockPos).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(1, 0, 0)).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 1, 0)).method_26204().equals((Object)class_2246.field_10124) && !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(1, 1, 0)).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 2, 0)).method_26204().equals((Object)class_2246.field_10124) && !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(1, 2, 0)).method_26204().equals((Object)class_2246.field_10124)) {
            return false;
        }
        for (class_2338 blockPos2 : new class_2338[]{blockPos.method_10069(2, 0, 0), blockPos.method_10069(1, 0, 1), blockPos.method_10069(1, 0, -1), blockPos.method_10069(-1, 0, 0), blockPos.method_10069(0, 0, 1), blockPos.method_10069(0, 0, -1), blockPos.method_10069(0, -1, 0), blockPos.method_10069(1, -1, 0)}) {
            if (BlockUtils.getBlockResistance(blockPos2) == BlockUtils.BlockResistance.Resistant || BlockUtils.getBlockResistance(blockPos2) == BlockUtils.BlockResistance.Unbreakable) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleObsidianHoleZ(class_2338 blockPos) {
        if (!HoleUtils.mc.field_1687.method_8320(blockPos).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 0, 1)).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 1, 0)).method_26204().equals((Object)class_2246.field_10124) && !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 1, 1)).method_26204().equals((Object)class_2246.field_10124) || !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 2, 0)).method_26204().equals((Object)class_2246.field_10124) && !HoleUtils.mc.field_1687.method_8320(blockPos.method_10069(0, 2, 1)).method_26204().equals((Object)class_2246.field_10124)) {
            return false;
        }
        for (class_2338 blockPos2 : new class_2338[]{blockPos.method_10069(0, 0, 2), blockPos.method_10069(1, 0, 1), blockPos.method_10069(-1, 0, 1), blockPos.method_10069(0, 0, -1), blockPos.method_10069(1, 0, 0), blockPos.method_10069(-1, 0, 0), blockPos.method_10069(0, -1, 0), blockPos.method_10069(0, -1, 1)}) {
            if (BlockUtils.getBlockResistance(blockPos2) == BlockUtils.BlockResistance.Resistant || BlockUtils.getBlockResistance(blockPos2) == BlockUtils.BlockResistance.Unbreakable) continue;
            return false;
        }
        return true;
    }

    public static boolean isInHole(class_1657 player) {
        boolean retVal = false;
        class_2338 pos = new class_2338((int)Math.floor((double)player.method_23317()), (int)player.method_23318(), (int)Math.floor((double)player.method_23321()));
        if (HoleUtils.mc.field_1687.method_8320(pos).method_26204().equals((Object)class_2246.field_10124) && HoleUtils.mc.field_1687.method_8320(pos.method_10084()).method_26204().equals((Object)class_2246.field_10124) && (HoleUtils.mc.field_1687.method_8320(pos.method_10074()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10074()).method_26204().equals((Object)class_2246.field_10540)) && (HoleUtils.mc.field_1687.method_8320(pos.method_10078()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10078()).method_26204().equals((Object)class_2246.field_10540)) && (HoleUtils.mc.field_1687.method_8320(pos.method_10067()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10067()).method_26204().equals((Object)class_2246.field_10540)) && (HoleUtils.mc.field_1687.method_8320(pos.method_10072()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10072()).method_26204().equals((Object)class_2246.field_10540)) && (HoleUtils.mc.field_1687.method_8320(pos.method_10095()).method_26204().equals((Object)class_2246.field_9987) || HoleUtils.mc.field_1687.method_8320(pos.method_10095()).method_26204().equals((Object)class_2246.field_10540))) {
            retVal = true;
        }
        return retVal;
    }
}
