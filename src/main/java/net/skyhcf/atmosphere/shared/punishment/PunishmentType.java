package net.skyhcf.atmosphere.shared.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PunishmentType {

    WARN("warn", "warned", "unwarned", "Warnings"),
    KICK("kick", "kicked", "unkicked", "Kicks"),
    MUTE("mute", "muted", "unmuted", "Mutes"),
    BAN("ban", "banned", "unbanned", "Bans"),
    BLACKLIST("blacklist", "blacklisted", "unblacklisted", "Blacklists");

    private final String display;
    private final String addedDisplay;
    private final String removedDisplay;
    private final String menuDisplay;

}
