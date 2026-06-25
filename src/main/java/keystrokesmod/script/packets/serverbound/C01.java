package keystrokesmod.script.packets.serverbound;

import net.minecraft.network.protocol.game.ServerboundChatPacket;

public class C01 extends CPacket {
    public String message;

    public C01(String message) {
        super(null);
        this.message = message;
    }

    public C01(ServerboundChatPacket packet, byte f) {
        super(packet);
        this.message = "";
    }

    @Override
    public ServerboundChatPacket convert() {
        return this.packet instanceof ServerboundChatPacket chatPacket ? chatPacket : null;
    }
}
