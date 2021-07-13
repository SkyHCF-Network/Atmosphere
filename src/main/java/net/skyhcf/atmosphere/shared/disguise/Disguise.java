package net.skyhcf.atmosphere.shared.disguise;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.skyhcf.atmosphere.shared.rank.Rank;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Disguise {

    private UUID uuid;
    private String name;
    private Skin skin;
    private Rank rank;

}
