package keystrokesmod.script.packets.serverbound;

import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;

public class C16 extends CPacket {
    public String status;

    public C16(String status) {
        super(null);
        this.status = status;
    }

    public C16(ServerboundClientCommandPacket packet) {
        super(packet);
        this.status = "";
    }

    @Override
    public ServerboundClientCommandPacket convert() {
        if (this.packet instanceof ServerboundClientCommandPacket commandPacket) {
            return commandPacket;
        }
        return new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.RESPAWN);
    }
}
