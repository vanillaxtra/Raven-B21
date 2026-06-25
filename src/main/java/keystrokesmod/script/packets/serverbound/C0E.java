package keystrokesmod.script.packets.serverbound;

import keystrokesmod.script.classes.ItemStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;

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
        this.mode = packet.clickType().ordinal();
        this.action = 0;
        this.itemStack = new ItemStack(packet.changedItem(), (byte) 0);
    }

    @Override
    public ServerboundContainerClickPacket convert() {
        if (this.packet instanceof ServerboundContainerClickPacket clickPacket) {
            return clickPacket;
        }
        ClickType[] types = ClickType.values();
        ClickType actionType = this.mode >= 0 && this.mode < types.length ? types[this.mode] : ClickType.PICKUP;
        net.minecraft.world.item.ItemStack stack = this.itemStack != null ? this.itemStack.itemStack : net.minecraft.world.item.ItemStack.EMPTY;
        return new ServerboundContainerClickPacket(this.windowId, this.action, this.slot, this.button, actionType, stack, null);
    }
}
