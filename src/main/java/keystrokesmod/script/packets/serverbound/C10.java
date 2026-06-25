package keystrokesmod.script.packets.serverbound;

import keystrokesmod.script.classes.ItemStack;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;

public class C10 extends CPacket {
    public int slot;
    public ItemStack itemStack;

    public C10(int slot, ItemStack itemStack) {
        super(null);
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public C10(ServerboundSetCreativeModeSlotPacket packet) {
        super(packet);
        this.slot = packet.slotNum();
        this.itemStack = new ItemStack(packet.itemStack(), (byte) 0);
    }

    @Override
    public ServerboundSetCreativeModeSlotPacket convert() {
        net.minecraft.world.item.ItemStack stack = this.itemStack != null ? this.itemStack.itemStack : net.minecraft.world.item.ItemStack.EMPTY;
        return new ServerboundSetCreativeModeSlotPacket(this.slot, stack);
    }
}
