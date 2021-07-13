package net.skyhcf.atmosphere.shared.disguise;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Skin {

    private UUID uuid;
    private String name;
    private String signature;
    private String value;

}
