package keystrokesmod.event;

import net.minecraft.world.item.ItemStack;

public class UseItemEvent {
    public ItemStack usedItemStack;

    public UseItemEvent(ItemStack usedItemStack) {
        this.usedItemStack = usedItemStack;
    }
}
