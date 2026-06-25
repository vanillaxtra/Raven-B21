package keystrokesmod.module.impl.player;

import keystrokesmod.event.SendPacketEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.network.protocol.Packet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FakeLag extends Module {
    private SliderSetting delay;
    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();

    public FakeLag() {
        super("FakeLag", category.player, 0);
        this.registerSetting(delay = new SliderSetting("Delay ms", 200, 0, 1000, 10));
    }

    @SubscribeEvent
    public void onSend(SendPacketEvent e) {
        if (!Mc.nullCheck()) {
            return;
        }
        packets.add(e.getPacket());
        e.setCanceled(true);
    }

    public void onUpdate() {
        if (!Mc.nullCheck() || packets.isEmpty()) {
            return;
        }
        Packet<?> packet = packets.peek();
        if (packet != null) {
            mc.getConnection().send(packets.poll());
        }
    }

    public void onDisable() {
        if (Mc.nullCheck()) {
            while (!packets.isEmpty()) {
                mc.getConnection().send(packets.poll());
            }
        }
        packets.clear();
    }
}
