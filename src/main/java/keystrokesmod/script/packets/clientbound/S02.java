package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;

public class S02 extends SPacket {
    public byte type;
    public String message;

    public S02(ClientboundSystemChatPacket packet) {
        super(packet);
        this.type = 0;
        this.message = packet.content().getString();
    }

    public S02(ClientboundPlayerChatPacket packet) {
        super(packet);
        this.type = 0;
        this.message = packet.unsignedContent().getString();
    }

    public S02(byte type, String message) {
        super(new ClientboundSystemChatPacket(net.minecraft.network.chat.Component.literal(message), false));
        this.type = type;
        this.message = message;
    }
}
