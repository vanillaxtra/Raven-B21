package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;

import java.util.Collection;
import java.util.List;

public class S3E extends SPacket {
    public String name;
    public String displayName;
    public String prefix;
    public String suffix;
    public String nametagVisibility;
    public Collection<String> playerList;
    public int action;
    public int friendlyFlags;
    public int color;

    public S3E(ClientboundSetPlayerTeamPacket packet) {
        super(packet);
        this.name = "";
        this.displayName = "";
        this.prefix = "";
        this.suffix = "";
        this.nametagVisibility = "";
        this.playerList = List.of();
        this.action = 0;
        this.friendlyFlags = 0;
        this.color = 0;
    }

    public S3E(net.minecraft.network.protocol.Packet<?> packet, String name, String displayName, String prefix, String suffix, String nametagVisibility, Collection<String> playerList, int action, int friendlyFlags, int color) {
        super(packet);
        this.name = name;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.nametagVisibility = nametagVisibility;
        this.playerList = playerList;
        this.action = action;
        this.friendlyFlags = friendlyFlags;
        this.color = color;
    }
}
