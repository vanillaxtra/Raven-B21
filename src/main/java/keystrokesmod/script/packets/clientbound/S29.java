package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.ScriptVec3;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;

public class S29 extends SPacket {
    public String sound;
    public ScriptVec3 position;
    public float volume;
    public float pitch;

    public S29(ClientboundSoundPacket packet) {
        super(packet);
        this.sound = "";
        this.position = new ScriptVec3(0, 0, 0);
        this.volume = 1;
        this.pitch = 1;
    }

    public S29(String sound, ScriptVec3 position, float volume, float pitch) {
        super(new ClientboundSoundPacket(net.minecraft.core.Holder.direct(net.minecraft.sounds.SoundEvents.EMPTY), net.minecraft.sounds.SoundSource.MASTER, position.x, position.y, position.z, volume, pitch, 0L));
        this.sound = sound;
        this.position = position;
        this.volume = volume;
        this.pitch = pitch;
    }
}
