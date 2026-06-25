package keystrokesmod.module.impl.player;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RotationUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class Scaffold extends Module {
    private SliderSetting rotation;
    public ButtonSetting safeWalk;
    public ButtonSetting autoSwap;
    public ButtonSetting showBlockCount;
    private String[] rotationModes = new String[]{"None", "Simple", "Snap"};
    public boolean isEnabled;

    public Scaffold() {
        super("Scaffold", category.player);
        this.registerSetting(rotation = new SliderSetting("Rotation", 1, rotationModes));
        this.registerSetting(autoSwap = new ButtonSetting("Auto swap", true));
        this.registerSetting(safeWalk = new ButtonSetting("Safe walk", true));
        this.registerSetting(showBlockCount = new ButtonSetting("Show block count", true));
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck()) {
            isEnabled = false;
            return;
        }
        isEnabled = true;
        int slot = findBlockSlot();
        if (slot == -1) {
            return;
        }
        if (autoSwap.isToggled() && mc.player.getInventory().getSelectedSlot() != slot) {
            mc.player.getInventory().setSelectedSlot(slot);
        }
        BlockPos below = mc.player.blockPosition().below();
        if (!mc.level.getBlockState(below).isAir()) {
            return;
        }
        BlockPos placeOn = below.below();
        BlockState support = mc.level.getBlockState(placeOn);
        if (support.isAir()) {
            return;
        }
        if ((int) rotation.getInput() > 0) {
            float[] rots = RotationUtils.getRotations(placeOn.above());
            RotationUtils.setSilentRotations(rots[0], rots[1]);
        }
        Vec3 hit = Vec3.atCenterOf(placeOn).add(0, 1, 0);
        BlockHitResult bhr = new BlockHitResult(hit, Direction.UP, placeOn, false);
        InteractionResult result = mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, bhr);
        if (result.consumesAction()) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).getItem() instanceof BlockItem) {
                return i;
            }
        }
        return -1;
    }

    public int totalBlocks() {
        if (!Mc.nullCheck()) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < 9; i++) {
            var stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() instanceof BlockItem) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public void onDisable() {
        isEnabled = false;
    }
}
