package net.skyhcf.atmosphere.shared.punishment;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.packet.PunishmentPacket;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.atmosphere.shared.utils.SystemType;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class PunishmentManager {

    @Getter private List<Punishment> punishments = Lists.newArrayList();

    public PunishmentManager(){
        this.refresh();
    }

    public Punishment getPunishment(UUID uuid){
        for(Punishment punishment : punishments){
            if(String.valueOf(punishment.getUuid()).equalsIgnoreCase(String.valueOf(uuid))) return punishment;
        }
        return null;
    }

    public void createPunishment(Punishment punishment, boolean silent){
        Document document = new Document("uuid", punishment.getUuid().toString())
                .append("target", punishment.getTarget().toString())
                .append("addedBy", punishment.getAddedBy().toString())
                .append("removedBy", (punishment.getRemovedBy() == null ? null : punishment.getRemovedBy()))
                .append("punishmentType", punishment.getPunishmentType().name())
                .append("addedServer", punishment.getAddedServer().getName())
                .append("addedAt", punishment.getAddedAt())
                .append("duration", punishment.getDuration())
                .append("removedAt", punishment.getRemovedAt())
                .append("reason", punishment.getReason())
                .append("internalReason", punishment.getInternalReason())
                .append("removedReason", (punishment.getRemovedReason() == null ? null : punishment.getRemovedReason()))
                .append("proof", punishment.getProof());
        AtmosphereShared.getInstance().getRedisHandler().sendPacket(new PunishmentPacket(punishment, silent));
        AtmosphereShared.getInstance().getMongoManager().getPunishments().insertOne(document);
    }

    public void refresh(){
        List<Punishment> punishments = Lists.newArrayList();
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getPunishments().find()){
            punishments.add(new Punishment(
                    UUID.fromString(document.getString("uuid")),
                    UUID.fromString(document.getString("target")),
                    UUID.fromString(document.getString("addedBy")),
                    (document.getString("removedBy") == null ? null : UUID.fromString(document.getString("removedBy"))),
                    PunishmentType.valueOf(document.getString("punishmentType")),
                    AtmosphereShared.getInstance().getServerManager().getServer(document.getString("addedServer")),
                    document.getLong("addedAt"),
                    document.getLong("duration"),
                    document.getLong("removedAt"),
                    document.getString("reason"),
                    document.getString("internalReason"),
                    document.getString("removedReason"),
                    (List<String>) document.get("proof")
            ));
        }
        for(Punishment punishment : punishments){
            if(punishment.getRemainingTime() <= 0L && punishment.isActive()){
                punishment.setRemovedAt(System.currentTimeMillis());
                punishment.setRemovedBy(Profile.getConsoleUUID());
                punishment.setRemovedReason("Punishment Expired");
                punishment.save();
            }
        }
        /*for(Profile profile : AtmosphereShared.getInstance().getProfileManager().getProfiles()){
            if (profile.hasActivePunishment(PunishmentType.BLACKLIST)) {
                if(AtmosphereShared.getInstance().getSystemType() == SystemType.BUKKIT){
                    for(Profile alt : profile.getAlts()){
                        if(!profile.hasActivePunishment(PunishmentType.BLACKLIST)) {
                            if (!alt.getUuid().toString().equals(profile.getUuid().toString())) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "blacklist " + alt.getUsername() + " Botting");
                            }
                        }
                    }
                }
            }
        }*/
        this.punishments = punishments;
    }

}
