package keystrokesmod.script.packets.clientbound;

import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;

public class S45 extends SPacket {
    public String type;
    public String message;
    private int fadeInTime;
    private int displayTime;
    private int fadeOutTime;

    public S45(ClientboundSetTitleTextPacket packet) {
        super(packet);
        this.type = "TITLE";
        this.message = packet.text().getString();
    }

    public S45(ClientboundSetSubtitleTextPacket packet) {
        super(packet);
        this.type = "SUBTITLE";
        this.message = packet.text().getString();
    }

    public S45(ClientboundSetTitlesAnimationPacket packet) {
        super(packet);
        this.type = "TIMES";
        this.message = "";
        this.fadeInTime = packet.getFadeIn();
        this.displayTime = packet.getStay();
        this.fadeOutTime = packet.getFadeOut();
    }

    public S45(String type, String message, int fadeInTime, int displayTime, int fadeOutTime) {
        super(new ClientboundSetTitleTextPacket(net.minecraft.network.chat.Component.literal(message)));
        this.type = type;
        this.message = message;
        this.fadeInTime = fadeInTime;
        this.displayTime = displayTime;
        this.fadeOutTime = fadeOutTime;
    }
}
