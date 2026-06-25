package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;

public class S06 extends SPacket {
    public float health;
    public float saturation;
    public int food;

    public S06(ClientboundSetHealthPacket packet) {
        super(packet);
        this.health = packet.getHealth();
        this.saturation = packet.getSaturation();
        this.food = packet.getFood();
    }

    public S06(float health, float saturation, int food) {
        super(new ClientboundSetHealthPacket(health, food, saturation));
        this.health = health;
        this.saturation = saturation;
        this.food = food;
    }
}
