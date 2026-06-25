package keystrokesmod.module.impl.combat;

import keystrokesmod.event.LivingUpdateEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.impl.world.AntiBot;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.player.Player;

public class TPAura extends Module {
    private SliderSetting range;
    private ButtonSetting weaponOnly;
    private double x;
    private double z;
    private double y;

    public TPAura() {
        super("TPAura", category.combat);
        this.registerSetting(range = new SliderSetting("Range", 10, 0, 50, 1));
        this.registerSetting(weaponOnly = new ButtonSetting("Weapon only", false));
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent e) {
        if (Mc.nullCheck() && e.entity == mc.player && mc.player.hurtDuration > 0 && mc.player.hurtTime == mc.player.hurtDuration) {
            updatePosition();
        }
    }

    private void updatePosition() {
        x = Utils.randomizeInt(-15, 15) / 10.0;
        y = Utils.randomizeInt(10, 15) / 10.0;
        z = Utils.randomizeInt(-15, 15) / 10.0;
    }

    @Override
    public void onEnable() {
        if (range.getInput() == 0.0) {
            Utils.sendMessage("&cTPAura range values are set to 0.");
            disable();
            return;
        }
        updatePosition();
    }

    public void onUpdate() {
        if (!Mc.nullCheck()) {
            return;
        }
        if (weaponOnly.isToggled() && !Utils.holdingWeapon()) {
            return;
        }
        double rangeSq = range.getInput() * range.getInput();
        for (Player player : mc.level.players()) {
            if (player == mc.player || !player.isAlive()) {
                continue;
            }
            if (mc.player.distanceToSqr(player) > rangeSq) {
                continue;
            }
            if (AntiBot.isBot(player) || Utils.isFriended(player)) {
                continue;
            }
            mc.player.setPos(player.getX() + x, player.getY() + y, player.getZ() + z);
            Utils.attack(player, true, false);
            break;
        }
    }
}
