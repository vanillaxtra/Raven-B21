package keystrokesmod.script.packets.serverbound;

import keystrokesmod.script.classes.ItemStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;

public class C0E extends CPacket {
    public int windowId;
    public int slot;
    public int button;
    public int mode;
    public short action;
    public ItemStack itemStack;

    public C0E(int windowId, int slot, int button, int mode, ItemStack itemStack) {
        super(null);
        this.windowId = windowId;
        this.slot = slot;
        this.button = button;
        this.mode = mode;
        this.itemStack = itemStack;
    }

    public C0E(ServerboundContainerClickPacket packet) {
        super(packet);
        this.windowId = packet.containerId();
        this.slot = packet.slotNum();
        this.button = packet.buttonNum();
        this.mode = 0;
        this.action = 0;
        this.itemStack = new ItemStack(net.minecraft.world.item.ItemStack.EMPTY, (byte) 0);
    }

    @Override
    public ServerboundContainerClickPacket convert() {
        return this.packet instanceof ServerboundContainerClickPacket clickPacket ? clickPacket : null;
    }
}
