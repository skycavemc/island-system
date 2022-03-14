package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.models.Islands;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class ListCommand {

    public ListCommand(Player player, @NotNull IslandSystem main) {
        StringJoiner ownerMessage = new StringJoiner(", ");
        StringJoiner memberMessage = new StringJoiner(", ");

        for (Island island : Islands.listAll()) {
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
            player.sendMessage(Message.LIST_OWNER_NO.getString().get());
        } else {
            player.sendMessage(Message.LIST_OWNER.getString().replace("{nummer}", ownerMessage.toString()).get());
        }
        if (memberMessage.length() == 0) {
            player.sendMessage(Message.LIST_MEMBER_NO.getString().get());
        } else {
            player.sendMessage(Message.LIST_MEMBER.getString().replace("{nummer}", memberMessage.toString()).get());
        }
    }
}
