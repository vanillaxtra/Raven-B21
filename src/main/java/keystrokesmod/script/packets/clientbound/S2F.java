package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.ItemStack;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;

public class S2F extends SPacket {
    public int windowId;
    public int slot;
    public ItemStack itemStack;

    public S2F(ClientboundContainerSetSlotPacket packet) {
        super(packet);
        this.windowId = packet.getContainerId();
        this.slot = packet.getSlot();
        this.itemStack = new ItemStack(packet.getItem(), (byte) 0);
    }

    public S2F(int windowId, int slot, ItemStack itemStack) {
        super(new ClientboundContainerSetSlotPacket(windowId, 0, slot, itemStack.itemStack));
        this.windowId = windowId;
        this.slot = slot;
        this.itemStack = itemStack;
    }
}
