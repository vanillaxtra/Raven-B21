package keystrokesmod.module.impl.player;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.ClickType;

public class InvManager extends Module {
    private SliderSetting delay;
    private ButtonSetting dropGarbage;
    private ButtonSetting sortHotbar;
    private int tickWait;

    public InvManager() {
        super("InvManager", category.player, 0);
        this.registerSetting(delay = new SliderSetting("Delay", 2, 0, 10, 1));
        this.registerSetting(dropGarbage = new ButtonSetting("Drop garbage", true));
        this.registerSetting(sortHotbar = new ButtonSetting("Sort hotbar", false));
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck() || mc.screen != null) {
            return;
        }
        if (tickWait++ < delay.getInput()) {
            return;
        }
        tickWait = 0;
        if (dropGarbage.isToggled()) {
            for (int i = 9; i < 36; i++) {
                ItemStack stack = mc.player.getInventory().getItem(i);
                if (isGarbage(stack)) {
                    mc.gameMode.handleInventoryMouseClick(mc.player.inventoryMenu.containerId, i, 1, ClickType.THROW, mc.player);
                    return;
                }
            }
        }
    }

    private boolean isGarbage(ItemStack stack) {
        return stack.isEmpty() || stack.getItem() == Items.ROTTEN_FLESH || stack.getItem() == Items.POISONOUS_POTATO;
    }
}
