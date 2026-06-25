package keystrokesmod.module.impl.other;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.NoEventPacketEvent;
import keystrokesmod.event.ReceivePacketEvent;
import keystrokesmod.event.SendPacketEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Component;

public class ViewPackets extends Module {
    private ButtonSetting includeCancelled;
    private ButtonSetting sent;
    private ButtonSetting received;
    private ButtonSetting ignoreMove;
    private ButtonSetting compactMove;
    private Packet<?> lastPacket;
    public static long tick;

    public ViewPackets() {
        super("View Packets", category.other);
        this.registerSetting(includeCancelled = new ButtonSetting("Include cancelled", true));
        this.registerSetting(sent = new ButtonSetting("Sent", false));
        this.registerSetting(received = new ButtonSetting("Received", false));
        this.registerSetting(ignoreMove = new ButtonSetting("Ignore move", false));
        this.registerSetting(compactMove = new ButtonSetting("Compact move", false));
    }

    public void onDisable() {
        lastPacket = null;
        tick = 0;
    }

    @SubscribeEvent
    public void onSend(SendPacketEvent e) {
        handle(e.getPacket(), false, e.isCanceled());
    }

    @SubscribeEvent
    public void onSendNoEvent(NoEventPacketEvent e) {
        handle(e.getPacket(), false, e.isCanceled());
    }

    @SubscribeEvent
    public void onReceive(ReceivePacketEvent e) {
        if (!received.isToggled()) {
            return;
        }
        sendMessage(e.getPacket(), true);
    }

    private void handle(Packet<?> packet, boolean incoming, boolean cancelled) {
        if (!sent.isToggled() || incoming) {
            return;
        }
        if (cancelled && !includeCancelled.isToggled()) {
            return;
        }
        if (ignoreMove.isToggled() && packet instanceof ServerboundMovePlayerPacket) {
            return;
        }
        if (compactMove.isToggled() && packet instanceof ServerboundMovePlayerPacket && lastPacket instanceof ServerboundMovePlayerPacket) {
            return;
        }
        sendMessage(lastPacket = packet, false);
    }

    private void sendMessage(Packet<?> packet, boolean incoming) {
        if (!Mc.nullCheck()) {
            return;
        }
        String name = packet.getClass().getSimpleName();
        Component line = net.minecraft.network.chat.Component.literal(Utils.formatColor("&7[&dR&7] &7" + (incoming ? "Received" : "Sent") + " &b" + name + " &7(t:" + tick + ")"))
                .withStyle(s -> s.withHoverEvent(new HoverEvent.ShowText(net.minecraft.network.chat.Component.literal(name))));
        mc.player.displayClientMessage(line, false);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase == ClientTickEvent.Phase.START) {
            tick++;
        }
    }
}
