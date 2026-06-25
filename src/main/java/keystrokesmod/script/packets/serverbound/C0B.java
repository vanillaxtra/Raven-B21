package keystrokesmod.script.packets.serverbound;

import keystrokesmod.utility.Mc;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class C0B extends CPacket {
    public String action;
    public int horsePower;

    public C0B(String action, int horsePower) {
        super(null);
        this.action = action;
        this.horsePower = horsePower;
    }

    public C0B(ServerboundPlayerCommandPacket packet) {
        super(packet);
        this.action = "";
        this.horsePower = 0;
    }

    @Override
    public ServerboundPlayerCommandPacket convert() {
        return this.packet instanceof ServerboundPlayerCommandPacket commandPacket ? commandPacket : null;
    }
}
