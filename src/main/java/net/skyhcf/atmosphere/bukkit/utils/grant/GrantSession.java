package net.skyhcf.atmosphere.bukkit.utils.grant;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.rank.Rank;
import net.skyhcf.atmosphere.shared.server.Server;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GrantSession {

    private Profile profile;
    private Rank rank;
    private long duration;
    private String reason;
    private List<String> scopes;
    private UUID addedBy;
    private Server addedServer;

}
