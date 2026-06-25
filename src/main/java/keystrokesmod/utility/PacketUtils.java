package keystrokesmod.utility;

import keystrokesmod.utility.Mc;
import net.minecraft.network.protocol.Packet;

import java.util.ArrayList;
import java.util.List;

public class PacketUtils {
    public static List<Packet<?>> skipSendEvent = new ArrayList<>();
    public static List<Packet<?>> skipReceiveEvent = new ArrayList<>();

    public static void sendPacketNoEvent(Packet<?> packet) {
        if (packet == null || Mc.player() == null || Mc.player().connection == null) {
            return;
        }
        skipSendEvent.add(packet);
        Mc.player().connection.send(packet);
    }

    public static void receivePacketNoEvent(Packet<?> packet) {
        if (packet == null || Mc.player() == null || Mc.player().connection == null) {
            return;
        }
        skipReceiveEvent.add(packet);
    }
}
