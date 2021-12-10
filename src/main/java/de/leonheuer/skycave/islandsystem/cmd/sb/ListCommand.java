package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ListCommand {

    public ListCommand(Player player, String[] args) {
        File f = new File("plugins/SkyBeeIslandSystem/insel/");
        StringBuilder sbo = new StringBuilder();
        StringBuilder sbm = new StringBuilder();
        for (File fa : f.listFiles()) {
            FileConfiguration faa = YamlConfiguration.loadConfiguration(fa);
            String name = fa.getName().replace("sc_", "").replace(".yml", "");
            if (player.getUniqueId().toString().equalsIgnoreCase(faa.getString("owner"))) {
                sbo.append(name + ", ");
            }
            for (String l : faa.getStringList("member")) {
                if (player.getUniqueId().toString().equalsIgnoreCase(l)) {
                    sbm.append(name + ", ");
                }
            }
        }
        if (sbo.length() <= 3) {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_OWNER_NO.getString().get());
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_OWNER.getString().replace("{nummer}", sbo.substring(0, (sbo.length() - 2))).get());
        }
        if (sbm.length() <= 3) {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_MEMBER_NO.getString().get());
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_LIST_MEMBER.getString().replace("{nummer}", sbm.substring(0, (sbm.length() - 2))).get());
        }
    }
}
