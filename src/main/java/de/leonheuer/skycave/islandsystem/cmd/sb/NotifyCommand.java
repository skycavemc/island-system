package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.mongodb.client.model.Filters;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.models.Islands;
import de.leonheuer.skycave.islandsystem.models.User;
import de.leonheuer.skycave.islandsystem.util.IslandUtils;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NotifyCommand {
    
    public NotifyCommand(@NotNull Player player, String @NotNull [] args, @NotNull IslandSystem main) {
        UUID uuid = player.getUniqueId();
        Bson filter = Filters.eq("uuid", uuid.toString());
        User user = main.getUsers().find(filter).first();
        if (user == null) {
            user = new User(uuid.toString());
            main.getUsers().insertOne(user);
            main.getLogger().info("Created new user profile for " + player.getName() + " (UUID: " + uuid + ")");
        }
        
        if (args.length < 2) {
            player.sendMessage(Message.NOTIFY_STATUS.getString().replace("{notify}", listNotifications(user)).get());
            return;
        }

        switch (args[1].toLowerCase()) {
            case "on" -> {
                if (args.length == 2) {
                    user.getIgnoredEntityLimits().clear();
                    main.getUsers().replaceOne(filter, user);
                    player.sendMessage(Message.NOTIFY_SUCCESS.getString().replace("{notify}", "&aalle &8-> &7alle").get());
                    return;
                }
                if (args.length == 3) {
                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(Message.INVALID_NUMBER.getString().get());
                        return;
                    }
                    Island target = Islands.load(id);
                    if (target == null) {
                        player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                        return;
                    }
                    ProtectedRegion region = target.getRegion();
                    if (region == null) {
                        player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                        return;
                    }
                    if (!region.getOwners().contains(uuid) && !region.getMembers().contains(uuid)) {
                        player.sendMessage(Message.NO_MEMBER.getString().get());
                    }

                    user.getIgnoredEntityLimits().remove(id);
                    main.getUsers().replaceOne(filter, user);
                    player.sendMessage(Message.NOTIFY_SUCCESS.getString().replace("{notify}", listNotifications(user)).get());
                    return;
                }

                int id;
                try {
                    id = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(Message.INVALID_NUMBER.getString().get());
                    return;
                }
                Island target = Islands.load(id);
                if (target == null) {
                    player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                    return;
                }
                ProtectedRegion region = target.getRegion();
                if (region == null) {
                    player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                    return;
                }
                if (!region.getOwners().contains(uuid) && !region.getMembers().contains(uuid)) {
                    player.sendMessage(Message.NO_MEMBER.getString().get());
                }

                EntityLimit limit;
                try {
                    limit = EntityLimit.valueOf(args[3]);
                } catch (IllegalArgumentException e) {
                    player.sendMessage(Message.LIMIT_UNKNOWN.getString().get());
                    return;
                }

                List<String> ignored = user.getIgnoredEntityLimits().get(id);
                if (ignored != null) {
                    ignored.remove(limit.toString());
                }
                user.getIgnoredEntityLimits().put(id, ignored);
                main.getUsers().replaceOne(filter, user);
                player.sendMessage(Message.NOTIFY_SUCCESS.getString().replace("{notify}", listNotifications(user)).get());
            }
            case "off" -> {
                if (args.length == 2) {
                    for (Island island : Islands.listAll()) {
                        ProtectedRegion region = island.getRegion();
                        if (region == null) {
                            continue;
                        }
                        if (!region.getOwners().contains(uuid) && !region.getMembers().contains(uuid)) {
                            continue;
                        }
                        user.getIgnoredEntityLimits().put(island.getId(),
                                Arrays.stream(EntityLimit.values()).map(Enum::toString).toList());
                    }
                    main.getUsers().replaceOne(filter, user);
                    player.sendMessage(Message.NOTIFY_SUCCESS.getString().replace("{notify}", "&akeine &8-> &7keine").get());
                    return;
                }
                if (args.length == 3) {
                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(Message.INVALID_NUMBER.getString().get());
                        return;
                    }
                    Island target = Islands.load(id);
                    if (target == null) {
                        player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                        return;
                    }
                    ProtectedRegion region = target.getRegion();
                    if (region == null) {
                        player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                        return;
                    }
                    if (!region.getOwners().contains(uuid) && !region.getMembers().contains(uuid)) {
                        player.sendMessage(Message.NO_MEMBER.getString().get());
                    }

                    user.getIgnoredEntityLimits().put(id, Arrays.stream(EntityLimit.values()).map(Enum::toString).toList());
                    main.getUsers().replaceOne(filter, user);
                    player.sendMessage(Message.NOTIFY_SUCCESS.getString().replace("{notify}", listNotifications(user)).get());
                    return;
                }

                int id;
                try {
                    id = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(Message.INVALID_NUMBER.getString().get());
                    return;
                }
                Island target = Islands.load(id);
                if (target == null) {
                    player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                    return;
                }
                ProtectedRegion region = target.getRegion();
                if (region == null) {
                    player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                    return;
                }
                if (!region.getOwners().contains(uuid) && !region.getMembers().contains(uuid)) {
                    player.sendMessage(Message.NO_MEMBER.getString().get());
                }

                EntityLimit limit;
                try {
                    limit = EntityLimit.valueOf(args[3]);
                } catch (IllegalArgumentException e) {
                    player.sendMessage(Message.LIMIT_UNKNOWN.getString().get());
                    return;
                }

                List<String> ignored = user.getIgnoredEntityLimits().get(id);
                if (ignored == null) {
                    ignored = new ArrayList<>();
                }
                if (!ignored.contains(limit.toString())) {
                    ignored.add(limit.toString());
                }
                user.getIgnoredEntityLimits().put(id, ignored);
                main.getUsers().replaceOne(filter, user);
                player.sendMessage(Message.NOTIFY_SUCCESS.getString().replace("{notify}", listNotifications(user)).get());
            }
            default -> player.sendMessage(Message.NOTIFY_OPTIONS.getString().get());
        }
    }

    @NotNull
    private String listNotifications(@NotNull User user) {
        if (user.getIgnoredEntityLimits().isEmpty()) {
            return "&7alle &8-> &7alle";
        }
        StringJoiner sj = new StringJoiner("&8, ");
        for (Map.Entry<Integer, List<String>> list : user.getIgnoredEntityLimits().entrySet()) {
            StringJoiner listSj = new StringJoiner("&8, ");
            for (String limit : list.getValue()) {
                listSj.add("&7" + limit);
            }
            sj.add("&a" + list.getKey() + " &8-> (" + listSj + "&8)");
        }
        return sj.toString();
    }
    
}
