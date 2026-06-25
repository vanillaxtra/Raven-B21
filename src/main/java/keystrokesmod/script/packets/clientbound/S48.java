package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;

import java.util.UUID;

public class S48 extends SPacket {
    public String url;
    public String hash;

    public S48(ClientboundResourcePackPushPacket packet) {
        super(packet);
        this.url = packet.url();
        this.hash = packet.hash();
    }

    public S48(String url, String hash) {
        super(new ClientboundResourcePackPushPacket(UUID.randomUUID(), url, hash, false, net.minecraft.network.chat.Component.empty(), 0));
        this.url = url;
        this.hash = hash;
    }
}
