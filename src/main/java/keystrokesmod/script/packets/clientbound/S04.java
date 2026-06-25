package keystrokesmod.script.packets.clientbound;

import keystrokesmod.script.classes.ItemStack;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;

public class S04 extends SPacket {
    public int entityId;
    public int slot;
    public ItemStack item;

    public S04(ClientboundSetEquipmentPacket packet) {
        super(packet);
        this.entityId = 0;
        this.slot = 0;
        this.item = new ItemStack(net.minecraft.world.item.ItemStack.EMPTY, (byte) 0);
    }

    public S04(int entityId, int slot, ItemStack item) {
        super(new ClientboundSetEquipmentPacket(entityId, java.util.List.of()));
        this.entityId = entityId;
        this.slot = slot;
        this.item = item;
    }
}
