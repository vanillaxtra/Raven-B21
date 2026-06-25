package keystrokesmod.utility;

import keystrokesmod.event.PostUpdateEvent;
import keystrokesmod.event.ReceivePacketEvent;
import keystrokesmod.event.SendPacketEvent;
import keystrokesmod.event.SubscribeEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketsHandler {
    public PacketData C0A = new PacketData();
    public PacketData C08 = new PacketData();
    public PacketData C07 = new PacketData();
    public PacketData C02 = new PacketData();
    public PacketData C02_INTERACT_AT = new PacketData();
    public PacketData C09 = new PacketData();

    public AtomicInteger playerSlot = new AtomicInteger(-1);
    public AtomicInteger serverSlot = new AtomicInteger(-1);
    private final boolean handleSlots = true;

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onSendPacket(SendPacketEvent event) {
        if (event.isCanceled()) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof ServerboundInteractPacket) {
            if (C07.sentCurrentTick.get()) {
                event.setCanceled(true);
                return;
            }
            C02.sentCurrentTick.set(true);
            C02_INTERACT_AT.sentCurrentTick.set(true);
        } else if (packet instanceof net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
                || packet instanceof net.minecraft.network.protocol.game.ServerboundUseItemPacket) {
            C08.sentCurrentTick.set(true);
        } else if (packet instanceof ServerboundPlayerActionPacket) {
            C07.sentCurrentTick.set(true);
        } else if (packet instanceof ServerboundSwingPacket) {
            if (C07.sentCurrentTick.get()) {
                event.setCanceled(true);
                return;
            }
            C0A.sentCurrentTick.set(true);
        } else if (packet instanceof ServerboundSetCarriedItemPacket slotPacket && handleSlots) {
            int slotId = slotPacket.getSlot();
            if (slotId == playerSlot.get() && slotId == serverSlot.get()) {
                event.setCanceled(true);
                return;
            }
            C09.sentCurrentTick.set(true);
            playerSlot.set(slotId);
            serverSlot.set(slotId);
        }
    }

    @SubscribeEvent
    public void onReceivePacket(ReceivePacketEvent event) {
        if (event.getPacket() instanceof ClientboundSetHeldSlotPacket packet && handleSlots) {
            int index = packet.slot();
            if (index >= 0 && index < 9) {
                serverSlot.set(index);
            }
        } else if (event.getPacket() instanceof ClientboundLoginPacket && Mc.player() != null && handleSlots) {
            playerSlot.set(-1);
        }
    }

    @SubscribeEvent(priority = Integer.MAX_VALUE)
    public void onPostUpdate(PostUpdateEvent event) {
        C08.updateStatesPostUpdate();
        C07.updateStatesPostUpdate();
        C02.updateStatesPostUpdate();
        C0A.updateStatesPostUpdate();
        C02_INTERACT_AT.updateStatesPostUpdate();
        C09.updateStatesPostUpdate();
    }

    public void handlePacket(Packet<?> packet) {
        if (packet instanceof ServerboundSetCarriedItemPacket slotPacket && handleSlots) {
            playerSlot.set(slotPacket.getSlot());
            C09.sentCurrentTick.set(true);
        } else if (packet instanceof ServerboundInteractPacket) {
            C02.sentCurrentTick.set(true);
            C02_INTERACT_AT.sentCurrentTick.set(true);
        } else if (packet instanceof ServerboundPlayerActionPacket) {
            C07.sentCurrentTick.set(true);
        } else if (packet instanceof net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
                || packet instanceof net.minecraft.network.protocol.game.ServerboundUseItemPacket) {
            C08.sentCurrentTick.set(true);
        } else if (packet instanceof ServerboundSwingPacket) {
            C0A.sentCurrentTick.set(true);
        }
    }

    public boolean sent() {
        return C02.sentCurrentTick.get() || C08.sentCurrentTick.get() || C09.sentCurrentTick.get()
                || C07.sentCurrentTick.get() || C0A.sentCurrentTick.get();
    }

    public boolean updateSlot(int slot) {
        if (!handleSlots) {
            Mc.player().connection.send(new ServerboundSetCarriedItemPacket(slot));
            return true;
        }
        if (playerSlot.get() == slot || slot == -1) {
            return false;
        }
        Mc.player().connection.send(new ServerboundSetCarriedItemPacket(slot));
        playerSlot.set(slot);
        return true;
    }

    public static class PacketData {
        public AtomicBoolean sentLastTick = new AtomicBoolean(false);
        public AtomicBoolean sentCurrentTick = new AtomicBoolean(false);

        public void updateStatesPostUpdate() {
            sentLastTick.set(sentCurrentTick.get());
            sentCurrentTick.set(false);
        }
    }
}
