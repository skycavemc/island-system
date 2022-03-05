package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.util.LegacyAdapter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.StringJoiner;

public class ImportAdmin {

    public ImportAdmin(Player player) {
        if (!player.isOp()) {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_IMPORT_PERMISSION.getString().get());
            return;
        }

        StringJoiner error = new StringJoiner("&8, &4");
        StringJoiner successful = new StringJoiner("&8, &2");

        if (LegacyAdapter.importWarps(new File("plugins/SkyBeeIslandSystem", "warps.yml"))) {
            successful.add("Warps");
        } else {
            error.add("Warps");
        }
        if (LegacyAdapter.importIslands(new File("plugins/SkyBeeIslandSystem/insel/"))) {
            successful.add("Inseln");
        } else {
            error.add("Inseln");
        }
        if (LegacyAdapter.importCacheConfig(new File("plugins/SkyBeeIslandSystem", "cache.yml"))) {
            successful.add("Cache");
        } else {
            error.add("cache");
        }

        player.sendMessage(Message.SBADMIN_SUBCOMMAND_IMPORT_ERROR.getString()
                .replace("{components}", error.toString()).get(false));
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_IMPORT_SUCCESS.getString()
                .replace("{components}", successful.toString()).get(false));
    }

}
