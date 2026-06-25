package keystrokesmod.module.impl.combat;

import keystrokesmod.event.MouseEvent;
import keystrokesmod.event.RenderWorldLastEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RenderUtils;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;

import java.awt.*;

public class HitBox extends Module {
    public static SliderSetting multiplier;
    public ButtonSetting showHitbox;
    public ButtonSetting playersOnly;
    public ButtonSetting weaponOnly;

    public HitBox() {
        super("HitBox", category.combat, 0);
        this.registerSetting(multiplier = new SliderSetting("Multiplier", "x", 1.2, 1.0, 5.0, 0.05));
        this.registerSetting(playersOnly = new ButtonSetting("Players only", true));
        this.registerSetting(showHitbox = new ButtonSetting("Show new hitbox", false));
        this.registerSetting(weaponOnly = new ButtonSetting("Weapon only", false));
        this.closetModule = true;
    }

    @Override
    public String getInfo() {
        return ((int) multiplier.getInput() == multiplier.getInput() ? (int) multiplier.getInput() + "" : multiplier.getInput()) + multiplier.getSuffix();
    }

    @SubscribeEvent
    public void onMouse(MouseEvent e) {
        if (e.button != 0 || !e.buttonState || !Mc.nullCheck() || multiplier.getInput() == 1 || mc.screen != null) {
            return;
        }
        if (weaponOnly.isToggled() && !Utils.holdingWeapon()) {
            return;
        }
        Entity c = getExpandedTarget();
        if (c == null) {
            return;
        }
        if (c instanceof Player p && Utils.isFriended(p)) {
            return;
        }
        if (!(c instanceof Player) && playersOnly.isToggled()) {
            return;
        }
        mc.hitResult = new EntityHitResult(c);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent e) {
        if (!showHitbox.isToggled() || !Mc.nullCheck()) {
            return;
        }
        for (Entity en : mc.level.entitiesForRendering()) {
            if (en != mc.player && en instanceof LivingEntity living && living.isAlive() && !en.isInvisible()) {
                AABB box = en.getBoundingBox().inflate((multiplier.getInput() - 1) * 0.5);
                RenderUtils.drawBox(box, Color.WHITE.getRGB());
            }
        }
    }

    private Entity getExpandedTarget() {
        double reach = 3.0 * multiplier.getInput();
        Entity best = null;
        double bestDist = reach;
        for (Entity en : mc.level.entitiesForRendering()) {
            if (!(en instanceof LivingEntity living) || !living.isAlive() || en == mc.player) {
                continue;
            }
            double d = mc.player.distanceTo(en);
            if (d <= bestDist) {
                bestDist = d;
                best = en;
            }
        }
        return best;
    }
}
