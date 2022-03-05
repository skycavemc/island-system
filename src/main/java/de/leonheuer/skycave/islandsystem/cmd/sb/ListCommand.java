package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.util.IslandUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.StringJoiner;

public class ListCommand {

    public ListCommand(Player player, IslandSystem main) {
        File dir = new File(main.getDataFolder(), "islands/");
        if (!dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        StringJoiner ownerMessage = new StringJoiner(", ");
        StringJoiner memberMessage = new StringJoiner(", ");

        for (File f : files) {
            String name = f.getName().replace(".yml", "");
            if (!IslandUtils.isValidName(name)) {
                continue;
            }
            Island island = Island.load(IslandUtils.nameToId(name));
            if (island == null) {
                continue;
            }
            ProtectedRegion region = island.getRegion();
            if (region == null) {
                continue;
            }

            if (region.getOwners().contains(player.getUniqueId())) {
                ownerMessage.add("" + island.getId());
            }
            if (region.getMembers().contains(player.getUniqueId())) {
                memberMessage.add("" + island.getId());
            }
        }

        if (ownerMessage.length() == 0) {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_OWNER_NO.getString().get());
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_OWNER.getString().replace("{nummer}", ownerMessage.toString()).get());
        }
        if (memberMessage.length() == 0) {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_MEMBER_NO.getString().get());
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_MEMBER.getString().replace("{nummer}", memberMessage.toString()).get());
        }
    }
}
