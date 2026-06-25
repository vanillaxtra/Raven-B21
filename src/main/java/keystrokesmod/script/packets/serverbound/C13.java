package keystrokesmod.script.packets.serverbound;

import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;

public class C13 extends CPacket {
    public boolean invulnerable;
    public boolean flying;
    public boolean allowFlying;
    public boolean creativeMode;
    public float flySpeed;
    public float walkSpeed;

    public C13(boolean invulnerable, boolean flying, boolean allowFlying, boolean creativeMode, float flySpeed, float walkSpeed) {
        super(null);
        this.invulnerable = invulnerable;
        this.flying = flying;
        this.allowFlying = allowFlying;
        this.creativeMode = creativeMode;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    public C13(ServerboundPlayerAbilitiesPacket packet) {
        super(packet);
        this.invulnerable = false;
        this.flying = false;
        this.allowFlying = false;
        this.creativeMode = false;
        this.flySpeed = 0;
        this.walkSpeed = 0;
    }

    @Override
    public ServerboundPlayerAbilitiesPacket convert() {
        if (this.packet instanceof ServerboundPlayerAbilitiesPacket abilitiesPacket) {
            return abilitiesPacket;
        }
        return new ServerboundPlayerAbilitiesPacket(new net.minecraft.world.entity.player.Abilities());
    }
}
