package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ListCommand {

    public ListCommand(Player player) {
        File dir = new File("plugins/SkyBeeIslandSystem/insel/");
        File[] files = dir.listFiles();
        if (!dir.isDirectory() || files == null) {
            return;
        }
        StringBuilder ownerMessage = new StringBuilder();
        StringBuilder memberMessage = new StringBuilder();
        for (File f : files) {
            FileConfiguration islandConfig = YamlConfiguration.loadConfiguration(f);
            String name = f.getName().replace("sc_", "").replace(".yml", "");
            if (player.getUniqueId().toString().equalsIgnoreCase(islandConfig.getString("owner"))) {
                ownerMessage.append(name).append(", ");
            }
            for (String member : islandConfig.getStringList("member")) {
                if (player.getUniqueId().toString().equalsIgnoreCase(member)) {
                    memberMessage.append(name).append(", ");
                }
            }
        }
        if (ownerMessage.length() <= 3) {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_OWNER_NO.getString().get());
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_OWNER.getString().replace("{nummer}", ownerMessage.substring(0, (ownerMessage.length() - 2))).get());
        }
        if (memberMessage.length() <= 3) {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_MEMBER_NO.getString().get());
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_MEMBER.getString().replace("{nummer}", ownerMessage.substring(0, (ownerMessage.length() - 2))).get());
        }
    }
}
