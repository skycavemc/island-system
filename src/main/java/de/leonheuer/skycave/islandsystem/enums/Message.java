package de.leonheuer.skycave.islandsystem.enums;

import de.leonheuer.skycave.islandsystem.models.FormattableString;

public enum Message {

    /**
     * Global
     */
    NO_PERMISSION("&cFehlende Berechtigung!"),
    NO_NUMMER("&cBitte gib eibe gültige Zahl an!"),
    CMD_CONSOLE("&cDieser Command ist nur über die Console verfügbar!"),
    COMING_SOON("&cDiese Funktion ist erst in Zukunft verfügbar!"),
    PLAYER_NOFOUND("&cDer Spieler {player} existiert nicht!"),
    PLAYER_NOONLINE("&cDer Spieler {player} ist nicht online!"),

    /**
     * Command_sbadmin
     */
    CMD_SBADMIN_HELP_TITEL("\n &cSkyBeeInsel Admin Hilfe:"),
    CMD_SBADMIN_HELP_CREATE("\n&e/sbadmin create <Spieler> <250,500> <Variante>&8- &7&oErstellt eine Insel"),
    CMD_SBADMIN_HELP_SETOWNER("&e/sbadmin setowner <Spieler> &8- &7&oÄndert den Besitzer der Insel"),
    CMD_SBADMIN_HELP_SETRADIUS("&e/sbadmin setradius <Radius> &8- &7&oÄndert die Größe der Insel"),
    CMD_SBADMIN_HELP_TRUST("&e/sbadmin trust <Spieler> &8- &7&oFügt einen Spieler zur Insel hinzu"),
    CMD_SBADMIN_HELP_UNTRUST("&e/sbadmin untrust <Spieler> &8- &7&oEntfernt einen Spieler von der Insel"),
    CMD_SBADMIN_HELP_SETSPAWN("&e/sbadmin setspawn &8- &7&oSetzt den Spawnpunkt der Insel um"),
    CMD_SBADMIN_HELP_TP("&e/sbadmin tp <Inselnummer> [Zentrum] &8- &7&oTeleportiert dich zu einer Insel"),
    CMD_SBADMIN_HELP_SETWARP("&e/sbadmin setwarp <Name> &8- &7&oSetzt einen Warp Punkt"),
    CMD_SBADMIN_HELP_DELWARP("&e/sbadmin delwarp <Name> &8- &7&oEntfernt einen Warp Punkt"),
    CMD_SBADMIN_HELP_INFO("&e/sbadmin info &8- &7&oLiefert Infos zur Insel"),
    CMD_SBADMIN_HELP_ENDTITEL("\n"),

    /**
     * Command_sc
     */
    CMD_SB_HELP_TITEL("\n &bSkyBeeInsel Hilfe:"),
    CMD_SB_HELP_BUY("\n&e/sb buy &8- &7&oZeigt Kaufinfos für eine Insel"),
    CMD_SB_HELP_TRUST("&e/sb trust <Spieler> &8- &7&oFügt einen Spieler zur Insel hinzu"),
    CMD_SB_HELP_UNTRUST("&e/sb untrust <Spieler> &8- &7&oEntfernt einen Spieler von der Insel"),
    CMD_SB_HELP_LIST("&e/sb list &8- &7&oListe deiner Inseln + Inseln, auf denen du hinzugefügt bist"),
    CMD_SB_HELP_TEAM("&e/sb team &8- &7&oZeigt den Besitzer und die Mitglieder einer Insel an"),
    CMD_SB_HELP_INFO("&e/sb info &8- &7&oLiefert Infos zur Insel"),
    CMD_SB_HELP_SETSPAWN("&e/sb setspawn &8- &7&oSetzt den Spawnpunkt der Insel um"),
    CMD_SB_HELP_TP("&e/sb tp <Inselnummer> &8- &7&oTeleportiert dich zur einer Insel"),
    CMD_SB_HELP_SPAWN("&e/sb spawn &8- &7&oTeleportiert dich zur Spawn-Insel"),
    CMD_SB_HELP_WARP("&e/sb warp <Name> &8- &7&oTeleportiert dich zur einem Warp"),
    CMD_SB_HELP_LIMITS("&e/sb limits &8- &7&oZeigt dir die Limits für deine Insel an"),
    CMD_SB_HELP_ENDTITEL("\n"),
    MISC_NOOWNER("&cDu bist nicht der Besitzer dieser Insel!"),
    MISC_NOINWORLD("&cDu befindest dich nicht in der korrekten Welt!"),

    /**
     * SB_Subcommand_trust
     */
    SB_SUBCOMMAND_TRUST_SYNTAX("&e/sb trust <Spieler>"),
    SB_SUBCOMMAND_TRUST_ERFOLG("&aDer Spieler {player} wurde erfolgreich hinzugefügt!"),
    SB_SUBCOMMAND_TRUST_NOYOU("&cDu kannst dich nicht selbst hinzufügen!"),
    SB_SUBCOMMAND_TRUST_BEREITS("&cDieser Spieler ist bereits Mitglied!"),

    /**
     * SB_Subcommand_untrust
     */
    SB_SUBCOMMAND_UNTRUST_SYNTAX("&e/sb untrust <Spieler>"),
    SB_SUBCOMMAND_UNTRUST_ERFOLG("&aDer Spieler {player} wurde erfolgreich entfernt!"),
    SB_SUBCOMMAND_UNTRUST_NOYOU("&cDu kannst dich nicht selbst entfernen!"),
    SB_SUBCOMMAND_UNTRUST_BEREITS("&cDieser Spieler ist kein Mitglied!"),

    /**
     * SBAdmin_Subcommand_create
     */
    SBADMIN_SUBCOMMAND_CREATE_SYNTAX("&e/sbadmin create <Spieler> <250,500> <Variante>"),
    SBADMIN_SUBCOMMAND_CREATE_WAIT("&aBeginne Inselerstellung! Bitte habe einen Moment Geduld..."),
    SBADMIN_SUBCOMMAND_CREATE_FERTIG_SPAWN("&aDie Spawninsel wurde erfolgreich erstellt!"),
    SBADMIN_SUBCOMMAND_CREATE_FERTIG("&aDie Insel {isid} wurde erfolgreich erstellt!"),
    SBADMIN_SUBCOMMAND_CREATE_OUTOFRANGE("&cDie Größe geht über den erlaubten Bereich hinaus!"),
    SBADMIN_SUBCOMMAND_CREATE_MISSING_SCHEMATIC("&cInsel Schematic wurde nicht gefunden!"),
    SBADMIN_SUBCOMMAND_CREATE_ERROR("&cBei der Schematic liegt ein Fehler vor!"),
    SBADMIN_SUBCOMMAND_CREATE_TYPEERROR("&cUnbekannter Inseltyp! Wähle zwischen: Blume, Eis, Pilz, Wüste"),

    /**
     * SBAdmin_Subcommand_setowner
     */
    SBADMIN_SUBCOMMAND_SETOWNER_SYNTAX("&e/sbadmin setowner <Spieler>"),
    SBADMIN_SUBCOMMAND_SETOWNER_ERFOLG("&aDer neue Besitzer ist nun {player}!"),

    /**
     * SBAdmin_Subcommand_tp
     */
    SBADMIN_SUBCOMMAND_TP_SYNTAX("&e/sc tp <Inselnummer> [Zentrum]"),
    SBADMIN_SUBCOMMAND_TP_NOEXIST("&cDie Inselnummer wurde nicht gefunden!"),
    SBADMIN_SUBCOMMAND_TP_ERFOLG("&aDu hast dich erfolgreich teleportiert!"),

    /**
     * SB_Subcommand_setspawn
     */
    SBADMIN_SUBCOMMAND_SETSPAWN_ERFOLG("&aDer Insel-Spawn wurde erfolgreich umgesetzt!"),

    /**
     * SB_Subcommand_tp
     */
    SB_SUBCOMMAND_TP_SYNTAX("&e/sb tp <Inselnummer>"),
    SB_SUBCOMMAND_TP_NOEXIST("&cDie Inselnummer wurde nicht gefunden!"),
    SB_SUBCOMMAND_TP_NOMEMBER("&cDu bist kein Mitglied auf dieser Insel!"),
    SB_SUBCOMMAND_TP_ERFOLG("&aDu hast dich erfolgreich teleportiert!"),

    /**
     * SBAdmin_Subcommand_setradius
     */
    SBADMIN_SUBCOMMAND_SETRADIUS_SYNTAX("&e/sbadmin setradius <Radius>"),
    SBADMIN_SUBCOMMAND_SETRADIUS_ERFOLG("&aDie Größe der Insel wurde erfolgreich geändert!"),
    SBADMIN_SUBCOMMAND_SETRADIUS_OUTOFRANGE("&cDie Insel kann nicht verkleinert werden! &8(&7'/scadmin setradius <Radius> true', um den Schutz zu umgehen&8)"),

    /**
     * SB_Subcommand_list
     */
    SB_SUBCOMMAND_LIST_OWNER("&7Deine Inseln: &b{nummer}"),
    SB_SUBCOMMAND_LIST_OWNER_NO("&7Deine Inseln: &cDu besitzt keine Insel!"),
    SB_SUBCOMMAND_LIST_MEMBER("&7Du bist auf folgenden Inseln Mitglied: &b{nummer}"),
    SB_SUBCOMMAND_LIST_MEMBER_NO("&7Du bist auf folgenden Inseln Mitglied: &cDu bist auf keiner Insel hinzugefügt!"),

    /**
     * SB_Subcommand_info
     */
    SB_SUBCOMMAND_INFO("&7Insel &b{nummer} &8- &7Besitzer: &b{player}"),
    SB_SUBCOMMAND_INFO_SPAWN("&cDu befindest dich auf der Spawn-Insel!"),

    /**
     * SB_Subcommand_kick
     */
    SB_SUBCOMMAND_KICK_SYNTAX("&e/sb kick <Spieler>"),
    SB_SUBCOMMAND_KICK_ERFOLG("&aDer Spieler &2{player} &awurde von der Insel gekickt!"),
    SB_SUBCOMMAND_KICK_TOOTHER("&cDu wurdest von der Insel gekickt!"),
    SB_SUBCOMMAND_KICK_NOYOU("&cDu kannst dich nicht selbst kicken!"),
    SB_SUBCOMMAND_KICK_NOMEMBER("&cDieser Spieler ist ein Mitglied!"),
    SB_SUBCOMMAND_KICK_NOONISLAND("&cDer Spieler muss sich auf deiner Insel befinden!"),

    /**
     * SBAdmin_Subcommand_info
     */
    SBADMIN_SUBCOMMAND_INFO_TITEL("&8~~~~ &2Info für Insel Nummer {nummer} &8~~~~"),
    SBADMIN_SUBCOMMAND_INFO_OWNER("&7Besitzer: &b{player}"),
    SBADMIN_SUBCOMMAND_INFO_MEMBER("&7Mitglieder: &b{member}"),
    SBADMIN_SUBCOMMAND_INFO_SPAWN("&cDu befindest dich auf der Spawn-Insel!"),

    /**
     * SB_Subcommand_team
     */
    SB_SUBCOMMAND_TEAM_TITEL("\n&8~~~~ &2Team von Insel Nummer {nummer} &8~~~~"),
    SB_SUBCOMMAND_TEAM_OWNER("&6Besitzer:"),
    SB_SUBCOMMAND_TEAM_OWNER_LIST_ON("&a&lo &7{player}"),
    SB_SUBCOMMAND_TEAM_OWNER_LIST_OFF("&c&lo &7{player}"),
    SB_SUBCOMMAND_TEAM_MEMBER("&6Mitglieder:"),
    SB_SUBCOMMAND_TEAM_MEMBER_LIST_ON("&a&lo &7{player}"),
    SB_SUBCOMMAND_TEAM_MEMBER_LIST_OFF("&c&lo &7{player}"),
    SB_SUBCOMMAND_TEAM_NOMEMBER("&cNur Mitglieder können das Insel-Team sehen!"),

    /**
     * SB_Subcommand_warp
     */
    SB_SUBCOMMAND_WARP_NOEXIST("&cDer Warp {warp} existiert nicht."),
    SB_SUBCOMMAND_WARP_SUCCESS("&aDu wurdest zu {warp} teleportiert."),
    SB_SUBCOMMAND_WARP_MISSING("&7Verfügbare Warps: &a{warps}"),

    /**
     * SBAdmin_Subcommand_setwarp
     */
    SBADMIN_SUBCOMMAND_SETWARP_OVERRIDE("&aDer Warp {warp} wurde überschrieben."),
    SBADMIN_SUBCOMMAND_SETWARP_SUCCESS("&aDer Warp {warp} wurde erstellt."),
    SBADMIN_SUBCOMMAND_SETWARP_MISSING("&cBitte gib einen Name an."),

    /**
     * SBAdmin_Subcommand_delwarp
     */
    SBADMIN_SUBCOMMAND_DELWARP_NOEXIST("&cDer Warp {warp} existiert nicht."),
    SBADMIN_SUBCOMMAND_DELWARP_SUCCESS("&aDer Warp {warp} wurde entfernt."),
    SBADMIN_SUBCOMMAND_DELWARP_MISSING("&cBitte gib den Warp an: §7{warps}"),

    /**
     * PLUGIN
     */
    PLUGIN_MANUAL_WARNUNG("&4Achtung! Jeder Eingriff kann die Funktion des Plugins beeinträchtigen!"),
    ;

    private final String string;

    Message(String string) {
        this.string = string;
    }

    public FormattableString getString() {
        return new FormattableString(string);
    }

}