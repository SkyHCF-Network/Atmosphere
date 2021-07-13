package net.skyhcf.atmosphere.shared.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemType {

    BUKKIT("Bukkit"),
    BUNGEE("BungeeCord Proxy"),
    UNKNOWN("Unknown System Type");

    private final String simpleName;

}
