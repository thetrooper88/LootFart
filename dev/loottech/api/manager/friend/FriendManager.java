package dev.loottech.api.manager.friend;

import dev.loottech.api.manager.friend.Friend;
import java.util.ArrayList;
import net.minecraft.class_1657;

public class FriendManager {
    private final ArrayList<Friend> friends = new ArrayList();

    public ArrayList<Friend> getFriends() {
        return this.friends;
    }

    public Friend getFriend(String name) {
        return (Friend)this.friends.stream().filter(f -> f.getName().equals((Object)name)).findFirst().orElse(null);
    }

    public boolean isFriend(class_1657 player) {
        return this.isFriend(player.method_5477().method_54160());
    }

    public boolean isFriend(String name) {
        for (Friend friend : this.getFriends()) {
            if (!friend.getName().equals((Object)name)) continue;
            return true;
        }
        return false;
    }

    public void addFriend(String name) {
        this.friends.add((Object)new Friend(name));
    }

    public void removeFriend(String name) {
        if (this.getFriend(name) != null) {
            this.friends.remove((Object)this.getFriend(name));
        }
    }

    public void clearFriends() {
        this.friends.clear();
    }
}
