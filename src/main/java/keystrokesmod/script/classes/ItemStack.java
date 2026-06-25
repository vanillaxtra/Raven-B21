package keystrokesmod.script.classes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemStack {
    public String type = "";
    public String name = "";
    public String displayName = "";
    public int stackSize;
    public int maxStackSize;
    public int durability;
    public int maxDurability;
    public boolean isBlock;
    public net.minecraft.world.item.ItemStack itemStack;
    public int meta;

    public ItemStack(net.minecraft.world.item.ItemStack itemStack, byte ignored) {
        if (itemStack == null || itemStack.isEmpty()) {
            return;
        }
        this.itemStack = itemStack;
        this.isBlock = itemStack.getItem() instanceof BlockItem;
        this.type = itemStack.getItem().getClass().getSimpleName();
        Identifier id = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        this.name = id == null ? "" : id.getPath();
        this.displayName = itemStack.getHoverName().getString();
        this.stackSize = itemStack.getCount();
        this.maxStackSize = itemStack.getMaxStackSize();
        this.maxDurability = itemStack.getMaxDamage();
        this.durability = this.maxDurability - itemStack.getDamageValue();
        this.meta = 0;
    }

    public ItemStack(String name) {
        this(withMeta(name), (byte) 0);
    }

    private static net.minecraft.world.item.ItemStack withMeta(String name) {
        String[] parts = name.split(":");
        String itemName = parts[0];
        Item item = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("minecraft", itemName));
        if (item == null) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        return new net.minecraft.world.item.ItemStack(item, 1);
    }

    public List<String> getTooltip() {
        return new ArrayList<>();
    }
}
