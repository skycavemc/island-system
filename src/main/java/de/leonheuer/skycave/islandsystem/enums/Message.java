package de.leonheuer.skycave.islandsystem.enums;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.models.ColoredString;
import org.bukkit.plugin.java.JavaPlugin;


public enum Message {

    // General
    INVALID_NUMBER("&cBitte gib eine gültige Zahl an!"),
    PLAYER_UNKNOWN("&cDer Spieler {player} existiert nicht!"),
    PLAYER_OFFLINE("&cDer Spieler {player} ist nicht online!"),
    NOT_ON_ISLAND("&cDu befindest dich auf keiner SB-Insel."),
    NOT_IN_WORLD("&cDu befindest dich nicht in der korrekten Welt!"),
    NO_OWNER("&cDu bist nicht der Besitzer dieser Insel!"),
    NO_MEMBER("&cDu bist kein Mitglied auf dieser Insel!"),
    ISLAND_UNKNOWN("&cDie Inselnummer wurde nicht gefunden!"),
    ISLAND_WORLD_UNLOADED("&cDie Inselwelt ist nicht geladen."),
    REGION_ACCESS("&4Achtung! Jeder Eingriff kann die Funktion des Plugins beeinträchtigen!"),
    TEMP_DISABLED("&cDiese Funktion ist deaktiviert."),



    // Admin help
    ADMIN_HELP_HEADER("&8~~~~ &cSkyBeeInsel Admin Hilfe: &8~~~~"),
    ADMIN_HELP_CREATE("&e/sbadmin create <Spieler> <250,500> <Variante>&8- &7&oErstellt eine Insel"),
    ADMIN_HELP_SETOWNER("&e/sbadmin setowner <Spieler> &8- &7&oÄndert den Besitzer der Insel"),
    ADMIN_HELP_SETRADIUS("&e/sbadmin setradius <Radius> &8- &7&oÄndert die Größe der Insel"),
    ADMIN_HELP_TRUST("&e/sbadmin trust <Spieler> &8- &7&oFügt einen Spieler zur Insel hinzu"),
    ADMIN_HELP_UNTRUST("&e/sbadmin untrust <Spieler> &8- &7&oEntfernt einen Spieler von der Insel"),
    ADMIN_HELP_SETSPAWN("&e/sbadmin setspawn &8- &7&oSetzt den Spawnpunkt der Insel um"),
    ADMIN_HELP_TP("&e/sbadmin tp <Inselnummer> [center] &8- &7&oTeleportiert dich zu einer Insel, optional ins Zentrum"),
    ADMIN_HELP_SETWARP("&e/sbadmin setwarp <Name> &8- &7&oSetzt einen Warp Punkt"),
    ADMIN_HELP_DELWARP("&e/sbadmin delwarp <Name> &8- &7&oEntfernt einen Warp Punkt"),
    ADMIN_HELP_INFO("&e/sbadmin info [Nummer] &8- &7&oZeigt Infos zur Insel an"),
    ADMIN_HELP_IMPORT("&e/sbadmin import &8- &7&oImportiert alte SB-Inseln und Konfigurationen"),

    // Admin create
    ADMIN_CREATE_SYNTAX("&e/sbadmin create <Spieler> <250,500> <Variante>"),
    ADMIN_CREATE_WAIT("&aBeginne Inselerstellung! Bitte habe einen Moment Geduld..."),
    ADMIN_CREATE_FINISHED_SPAWN("&aDie Spawninsel wurde erfolgreich erstellt!"),
    ADMIN_CREATE_FINISHED("&aDie Insel {isid} wurde erfolgreich erstellt!"),
    ADMIN_CREATE_OUT_OF_RANGE("&cDer Radius darf nur zwischen 100 und 1500 liegen!"),
    ADMIN_CREATE_MISSING_SCHEMATIC("&cInsel Schematic wurde nicht gefunden!"),
    ADMIN_CREATE_TYPE_ERROR("&cUnbekannter Inseltyp! Wähle zwischen: &e{types}"),
    ADMIN_CREATE_ERROR("&cEin unbekannter Fehler ist aufgetreten."),

    // Admin setowner
    ADMIN_SETOWNER_SYNTAX("&e/sbadmin setowner <Spieler>"),
    ADMIN_SETOWNER_SUCCESS("&aDer neue Besitzer ist nun {player}!"),
    ADMIN_SETOWNER_ALREADY("&c{player} ist bereits der Besitzer!"),

    // Admin setradius
    ADMIN_SETRADIUS_SYNTAX("&e/sbadmin setradius <Radius>"),
    ADMIN_SETRADIUS_ERFOLG("&aDie Größe der Insel wurde erfolgreich geändert!"),
    ADMIN_SETRADIUS_OUT_OF_RANGE("&cDer Radius darf nur zwischen 100 und 1500 liegen!"),
    ADMIN_SETRADIUS_SMALLER("&cDu kannst keine Insel verkleinern."),

    // Admin tp
    ADMIN_TP_SYNTAX("&e/sc tp <Inselnummer> [Zentrum]"),
    ADMIN_TP_SUCCESS("&aDu hast dich erfolgreich teleportiert!"),

    // Admin setwarp
    ADMIN_SETWARP_OVERRIDE("&aDer Warp {warp} wurde überschrieben."),
    ADMIN_SETWARP_SUCCESS("&aDer Warp {warp} wurde erstellt."),
    ADMIN_SETWARP_MISSING("&cBitte gib einen Name an."),

    // Admin delwarp
    ADMIN_DELWARP_UNKNOWN("&cDer Warp {warp} existiert nicht."),
    ADMIN_DELWARP_SUCCESS("&aDer Warp {warp} wurde entfernt."),
    ADMIN_DELWARP_MISSING("&cBitte gib den Warp an: §7{warps}"),

    // Admin import
    ADMIN_IMPORT_ERROR("&cFolgende Komponenten konnten nicht importiert werden: &4{components}"),
    ADMIN_IMPORT_SUCCESS("&aFolgende Komponenten wurden erfolgreich importiert: &2{components}"),
    ADMIN_IMPORT_PERMISSION("&cDieser Vorgang erfordert Sonderberechtigungen."),



    // Help
    HELP_HEADER("&8~~~~ &bSkyBeeInsel Hilfe: &8~~~~"),
    HELP_BUY("&e/sb buy &8- &7&oKaufe dir eine Insel"),
    HELP_TRUST("&e/sb trust <Spieler> &8- &7&oFügt einen Spieler zur Insel hinzu"),
    HELP_UNTRUST("&e/sb untrust <Spieler> &8- &7&oEntfernt einen Spieler von der Insel"),
    HELP_LIST("&e/sb list &8- &7&oListe deiner Inseln + Inseln, auf denen du hinzugefügt bist"),
    HELP_INFO("&e/sb info &8- &7&oZeigt Infos zur Insel an"),
    HELP_SETSPAWN("&e/sb setspawn &8- &7&oSetzt den Spawnpunkt der Insel um"),
    HELP_TP("&e/sb tp <Nummer> &8- &7&oTeleportiert dich zur einer Insel"),
    HELP_SPAWN("&e/sb spawn &8- &7&oTeleportiert dich zur Spawn-Insel"),
    HELP_WARP("&e/sb warp <Name> &8- &7&oTeleportiert dich zur einem Warp"),
    HELP_LIMITS("&e/sb limits &8- &7&oZeigt dir die Limits für deine Insel an"),

    // Trust
    TRUST_SYNTAX("&e/sb trust <Spieler>"),
    TRUST_SUCCESS("&aDer Spieler {player} wurde erfolgreich hinzugefügt!"),
    TRUST_SELF("&cDu kannst dich nicht selbst hinzufügen!"),
    TRUST_ALREADY("&cDieser Spieler ist bereits Mitglied!"),

    // Untrust
    UNTRUST_SYNTAX("&e/sb untrust <Spieler>"),
    UNTRUST_SUCCESS("&aDer Spieler {player} wurde erfolgreich entfernt!"),
    UNTRUST_SELF("&cDu kannst dich nicht selbst entfernen!"),
    UNTRUST_ALREADY("&cDieser Spieler ist kein Mitglied!"),

    // Setspawn
    ADMIN_SETSPAWN_SUCCESS("&aDer Insel-Spawn wurde erfolgreich bei deiner Position gesetzt."),
    SETSPAWN_SUCCESS("&aDer Insel-Spawn wurde erfolgreich bei deiner Position gesetzt."),

    // TP
    TP_SYNTAX("&e/sb tp <Inselnummer>"),
    TP_SUCCESS("&aDu hast dich erfolgreich teleportiert!"),

    // List
    LIST_OWNER("&7Deine Inseln: &b{nummer}"),
    LIST_OWNER_NO("&7Deine Inseln: &cDu besitzt keine Insel!"),
    LIST_MEMBER("&7Du bist auf folgenden Inseln Mitglied: &b{nummer}"),
    LIST_MEMBER_NO("&7Du bist auf folgenden Inseln Mitglied: &cDu bist auf keiner Insel hinzugefügt!"),

    // Kick
    KICK_SYNTAX("&e/sb kick <Spieler>"),
    KICK_SUCCESS("&aDer Spieler &2{player} &awurde von der Insel gekickt!"),
    KICK_ALERT("&c{player} hat dich von seiner Insel gekickt!"),
    KICK_SELF("&cDu kannst dich nicht selbst kicken!"),
    KICK_MEMBER("&cDieser Spieler ist ein Mitglied!"),
    KICK_NOT_ON_ISLAND("&cDer Spieler muss sich auf deiner Insel befinden!"),

    // Warp
    WARP_UNKNOWN("&cDer Warp {warp} existiert nicht."),
    WARP_SUCCESS("&aDu wurdest zu {warp} teleportiert."),
    WARP_MISSING("&7Verfügbare Warps: &a{warps}"),

    // Spawn
    SPAWN("&aDu hast dich zum SB Spawn teleportiert."),

    // Info
    INFO_HEADER("&8~~~~ &2Info für Insel Nummer {nummer} &8~~~~"),
    INFO_OWNER("&eBesitzer: &b{owner}"),
    INFO_MEMBER("&eMitglieder: &b{member}"),
    INFO_CREATION("&eGründungsdatum: &b{creation}"),
    INFO_RADIUS("&2Radius: &b{radius}"),
    INFO_SPAWN("&aSpawn: &b{spawn}"),
    INFO_CENTER("&aZentrum: &b{center}"),
    INFO_START("&aAnfang: &b{start}"),
    INFO_END("&aEnde: &b{end}"),

    // Buy
    BUY_NOT_ENOUGH("&cDu hast nicht genug Geld. Dir fehlen noch {diff}"),
    BUY_SUCCESS("&aDu hast dir erfolgreich eine SB-Insel gekauft."),
    BUY_ERROR("&cDas Geld konnte nicht eingezogen werden."),
    ;

    private final String string;

    Message(String string) {
        this.string = string;
    }

    public ColoredString getString() {
        return new ColoredString(JavaPlugin.getPlugin(IslandSystem.class), string);
    }

}