package keystrokesmod.module.impl.player;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.world.level.block.state.BlockState;

public class AutoTool extends Module {
    private ButtonSetting swapBack;

    public AutoTool() {
        super("AutoTool", category.player, 0);
        this.registerSetting(swapBack = new ButtonSetting("Swap back", true));
    }

    private int previousSlot = -1;

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck() || mc.gameMode.isDestroying()) {
            return;
        }
        if (mc.hitResult == null || mc.hitResult.getType() != net.minecraft.world.phys.HitResult.Type.BLOCK) {
            if (swapBack.isToggled() && previousSlot != -1) {
                mc.player.getInventory().setSelectedSlot(previousSlot);
                previousSlot = -1;
            }
            return;
        }
        var blockPos = ((net.minecraft.world.phys.BlockHitResult) mc.hitResult).getBlockPos();
        BlockState state = mc.level.getBlockState(blockPos);
        int best = findBestTool(state);
        if (best != -1 && best != mc.player.getInventory().getSelectedSlot()) {
            if (previousSlot == -1) {
                previousSlot = mc.player.getInventory().getSelectedSlot();
            }
            mc.player.getInventory().setSelectedSlot(best);
        }
    }

    private int findBestTool(BlockState state) {
        int best = -1;
        float bestSpeed = 1.0f;
        for (int i = 0; i < 9; i++) {
            float speed = mc.player.getInventory().getItem(i).getDestroySpeed(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                best = i;
            }
        }
        return best;
    }
}
