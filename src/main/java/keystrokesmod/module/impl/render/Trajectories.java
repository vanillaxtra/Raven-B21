package keystrokesmod.module.impl.render;

import keystrokesmod.event.RenderWorldLastEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RenderUtils;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.phys.Vec3;

public class Trajectories extends Module {
    private ButtonSetting highlightTarget;

    public Trajectories() {
        super("Trajectories", category.render, 0);
        this.registerSetting(highlightTarget = new ButtonSetting("Highlight target", true));
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (!Mc.nullCheck() || mc.player.getMainHandItem().isEmpty()) {
            return;
        }
        var item = mc.player.getMainHandItem().getItem();
        if (!(item instanceof BowItem)) {
            return;
        }
        Vec3 start = mc.player.getEyePosition();
        Vec3 velocity = mc.player.getViewVector(e.tickDelta).scale(1.5);
        RenderUtils.drawTrajectory(start, velocity, highlightTarget.isToggled());
    }
}
