package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class KickCommand {

    public KickCommand(Player player, String[] args, IslandSystem main) {
        if (args.length < 2) {
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_SYNTAX.getString().get());
            return;
        }
        if (!Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            return;
        }

        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
        if (rm == null) {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            return;
        }

        ProtectedRegion r = Utils.getIslandRegionAt(player.getLocation());
        if (r == null) {
            player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
            return;
        }

        Insel insel = new Insel(r.getId());
        if (!insel.isOwner(player.getUniqueId())) {
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
        if (insel.isMember(other.getUniqueId())) {
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOMEMBER.getString().get());
            return;
        }
        if (!player.getLocation().getWorld().getName().equals(Utils.getInselWorld().getName())) {
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOONISLAND.getString().get());
            return;
        }

        if (r.contains(BukkitAdapter.asBlockVector(other.getLocation()))) {
            Location spawn = main.getMultiverse().getMVWorldManager()
                    .getMVWorld(main.getIslandWorld()).getSpawnLocation();
            other.teleport(spawn);
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_TOOTHER.getString()
                    .replace("{player}", player.getName()).get());
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_ERFOLG.getString().replace("{player}", args[1]).get());
            return;
        }
        player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOONISLAND.getString().get());
    }

}
