package keystrokesmod.script.packets.serverbound;

import keystrokesmod.script.classes.Entity;
import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;

public class C02 extends CPacket {
    public Entity entity;
    public String action;
    public ScriptVec3 hitVec;

    public C02(Entity entity, String action, ScriptVec3 hitVec) {
        super(null);
        this.entity = entity;
        this.action = action;
        this.hitVec = hitVec;
    }

    public C02(ServerboundInteractPacket packet) {
        super(packet);
        this.entity = null;
        this.action = "ATTACK";
        this.hitVec = null;
    }

    @Override
    public ServerboundInteractPacket convert() {
        return this.packet instanceof ServerboundInteractPacket interactPacket ? interactPacket : null;
    }
}
