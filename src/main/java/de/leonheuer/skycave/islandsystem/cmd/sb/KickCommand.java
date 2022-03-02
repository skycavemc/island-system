package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class KickCommand {

    public KickCommand(Player player, String[] args, IslandSystem main) {
        if (args.length < 2) {
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_SYNTAX.getString().get());
            return;
        }

        if (player.getLocation().getWorld() == main.getIslandWorld()) {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            return;
        }

        Island island = Island.at(player.getLocation());
        if (island == null) {
            player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
            return;
        }

        ProtectedRegion region = island.getRegion();
        if (region == null) {
            player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
            return;
        }

        if (!region.getOwners().contains(player.getUniqueId())) {
            player.sendMessage(Message.MISC_NOOWNER.getString().get());
            return;
        }

        Player other = Bukkit.getPlayerExact(args[1]);
        if (other == null || !player.canSee(other)) {
            player.sendMessage(Message.PLAYER_NOONLINE.getString().replace("{player}", args[1]).get());
            return;
        }
        if (player.getName().equalsIgnoreCase(other.getName())) {
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOYOU.getString().get());
            return;
        }
        if (region.getMembers().contains(other.getUniqueId())) {
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOMEMBER.getString().get());
            return;
        }
        if (!region.contains(BukkitAdapter.asBlockVector(other.getLocation()))) {
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOONISLAND.getString().get());
            return;
        }

        Location spawn = main.getMultiverse().getMVWorldManager()
                .getMVWorld("Builder").getSpawnLocation();
        other.teleport(spawn);
        other.sendMessage(Message.SB_SUBCOMMAND_KICK_TOOTHER.getString()
                .replace("{player}", player.getName()).get());
        player.sendMessage(Message.SB_SUBCOMMAND_KICK_ERFOLG.getString().replace("{player}", args[1]).get());
    }

}
