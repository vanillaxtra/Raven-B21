package keystrokesmod.module.impl.combat;

import keystrokesmod.event.MouseEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.BindUtil;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.glfw.GLFW;

public class Reach extends Module {
    public static SliderSetting min;
    public static SliderSetting max;
    public static ButtonSetting weaponOnly;
    public static ButtonSetting movingOnly;
    public static ButtonSetting sprintOnly;
    public static ButtonSetting hitThroughBlocks;

    public Reach() {
        super("Reach", category.combat, 0);
        this.registerSetting(min = new SliderSetting("Min", 3.1D, 3.0D, 6.0D, 0.05D));
        this.registerSetting(max = new SliderSetting("Max", 3.3D, 3.0D, 6.0D, 0.05D));
        this.registerSetting(weaponOnly = new ButtonSetting("Weapon only", false));
        this.registerSetting(movingOnly = new ButtonSetting("Moving only", false));
        this.registerSetting(sprintOnly = new ButtonSetting("Sprint only", false));
        this.registerSetting(hitThroughBlocks = new ButtonSetting("Hit through blocks", false));
        this.closetModule = true;
    }

    public void guiUpdate() {
        Utils.correctValue(min, max);
    }

    @SubscribeEvent
    public void onMouse(MouseEvent ev) {
        if (ev.button >= 0 && ev.buttonState && Mc.nullCheck()) {
            if (ModuleManager.autoClicker != null && ModuleManager.autoClicker.isEnabled()
                    && AutoClicker.leftClick.isToggled() && BindUtil.isBindDown(GLFW.GLFW_MOUSE_BUTTON_LEFT + 1000)) {
                return;
            }
            call();
        }
    }

    public static boolean call() {
        if (!Mc.nullCheck()) {
            return false;
        }
        if (weaponOnly.isToggled() && !Utils.holdingWeapon()) {
            return false;
        }
        if (movingOnly.isToggled() && Utils.getMovementForward() == 0 && Utils.getMovementSideways() == 0) {
            return false;
        }
        if (sprintOnly.isToggled() && !mc.player.isSprinting()) {
            return false;
        }
        double r = Utils.getRandomValue(min, max, Utils.getRandom());
        Entity target = findEntity(r);
        if (target == null) {
            return false;
        }
        mc.hitResult = new EntityHitResult(target);
        return true;
    }

    private static Entity findEntity(double range) {
        Entity best = null;
        double bestDist = range;
        for (Entity e : mc.level.entitiesForRendering()) {
            if (!(e instanceof LivingEntity living) || !living.isAlive() || e == mc.player) {
                continue;
            }
            double d = mc.player.distanceTo(e);
            if (d <= bestDist) {
                bestDist = d;
                best = e;
            }
        }
        return best;
    }
}
