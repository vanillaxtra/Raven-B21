package keystrokesmod.module.impl.player;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Freecam extends Module {
    public static Entity freeEntity;
    private SliderSetting speed;
    private ButtonSetting disableOnDamage;
    private Vec3 startPos;
    private float startYaw;
    private float startPitch;

    public Freecam() {
        super("Freecam", category.player, 0);
        this.registerSetting(speed = new SliderSetting("Speed", 1.0, 0.1, 5.0, 0.1));
        this.registerSetting(disableOnDamage = new ButtonSetting("Disable on damage", true));
    }

    public void onEnable() {
        if (!Mc.nullCheck()) {
            return;
        }
        freeEntity = mc.player;
        startPos = mc.player.position();
        startYaw = mc.player.getYRot();
        startPitch = mc.player.getXRot();
        mc.player.noPhysics = true;
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck()) {
            return;
        }
        if (disableOnDamage.isToggled() && mc.player.hurtTime > 0) {
            disable();
            return;
        }
        mc.player.setDeltaMovement(Vec3.ZERO);
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlyingSpeed((float) (speed.getInput() * 0.05f));
    }

    public void onDisable() {
        if (Mc.nullCheck()) {
            mc.player.noPhysics = false;
            mc.player.getAbilities().flying = false;
            if (startPos != null) {
                mc.player.setPos(startPos);
                mc.player.setYRot(startYaw);
                mc.player.setXRot(startPitch);
            }
        }
        freeEntity = null;
    }
}
