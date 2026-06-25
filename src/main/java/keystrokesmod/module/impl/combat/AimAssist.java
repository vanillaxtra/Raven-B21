package keystrokesmod.module.impl.combat;

import keystrokesmod.module.Module;
import keystrokesmod.module.impl.world.AntiBot;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RotationUtils;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class AimAssist extends Module {
    private SliderSetting speed;
    private SliderSetting fov;
    private SliderSetting distance;
    private ButtonSetting clickAim;
    private ButtonSetting weaponOnly;
    private ButtonSetting aimInvis;
    private ButtonSetting blatantMode;
    private ButtonSetting ignoreTeammates;

    public AimAssist() {
        super("AimAssist", category.combat, 0);
        this.registerSetting(speed = new SliderSetting("Speed", 45.0D, 1.0D, 100.0D, 1.0D));
        this.registerSetting(fov = new SliderSetting("FOV", 90.0D, 15.0D, 180.0D, 1.0D));
        this.registerSetting(distance = new SliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
        this.registerSetting(clickAim = new ButtonSetting("Click aim", true));
        this.registerSetting(weaponOnly = new ButtonSetting("Weapon only", false));
        this.registerSetting(aimInvis = new ButtonSetting("Aim invis", false));
        this.registerSetting(blatantMode = new ButtonSetting("Blatant mode", false));
        this.registerSetting(ignoreTeammates = new ButtonSetting("Ignore teammates", false));
    }

    public void onUpdate() {
        if (mc.screen != null || !Mc.nullCheck()) {
            return;
        }
        if (weaponOnly.isToggled() && !Utils.holdingWeapon()) {
            return;
        }
        if (clickAim.isToggled() && !Utils.isClicking()) {
            return;
        }
        Entity en = getEnemy();
        if (en == null) {
            return;
        }
        if (blatantMode.isToggled()) {
            Utils.aim(en, 0.0F, false);
        } else {
            double n = Utils.n(en);
            if (n > 1.0D || n < -1.0D) {
                mc.player.setYRot(mc.player.getYRot() + (float) (-(n / (101.0D - speed.getInput()))));
            }
        }
    }

    private Entity getEnemy() {
        int fovVal = (int) fov.getInput();
        for (Player player : mc.level.players()) {
            if (player == mc.player || !player.isAlive()) {
                continue;
            }
            if (Utils.isFriended(player) || AntiBot.isBot(player)) {
                continue;
            }
            if (ignoreTeammates.isToggled() && Utils.isTeamMate(player)) {
                continue;
            }
            if (!aimInvis.isToggled() && player.isInvisible()) {
                continue;
            }
            if (mc.player.distanceTo(player) > distance.getInput()) {
                continue;
            }
            if (!blatantMode.isToggled() && fovVal != 360 && !Utils.inFov((float) fovVal, player)) {
                continue;
            }
            return player;
        }
        return null;
    }
}
