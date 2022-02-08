package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.StringJoiner;

public class ListCommand {

    public ListCommand(Player player) {
        File dir = new File("plugins/SkyBeeIslandSystem/insel/");
        File[] files = dir.listFiles();
        if (!dir.isDirectory() || files == null) {
            return;
        }
        StringJoiner ownerMessage = new StringJoiner(", ");
        StringJoiner memberMessage = new StringJoiner(", ");
        for (File f : files) {
            FileConfiguration islandConfig = YamlConfiguration.loadConfiguration(f);
            String name = f.getName().replace("sc_", "").replace(".yml", "");
            if (player.getUniqueId().toString().equalsIgnoreCase(islandConfig.getString("owner"))) {
                ownerMessage.add(name);
            }
            for (String member : islandConfig.getStringList("member")) {
                if (player.getUniqueId().toString().equalsIgnoreCase(member)) {
                    memberMessage.add(name);
                }
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
