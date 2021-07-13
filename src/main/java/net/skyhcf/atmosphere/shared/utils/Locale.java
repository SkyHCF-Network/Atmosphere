package net.skyhcf.atmosphere.shared.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Locale {

    VERTICAL_STRAIGHT_LINE("⎜"),
    DOUBLE_RIGHT_ARROW("»"),
    DOUBLE_LEFT_ARROW("«");

    private final String character;

}
