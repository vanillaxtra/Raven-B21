package keystrokesmod.module.impl.combat;

import keystrokesmod.event.AttackEntityEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class WTap extends Module {
    private SliderSetting chance;
    private ButtonSetting playersOnly;
    private final HashMap<Integer, Long> targets = new HashMap<>();
    public static boolean stopSprint = false;

    public WTap() {
        super("WTap", category.combat);
        this.registerSetting(chance = new SliderSetting("Chance", "%", 100, 0, 100, 1));
        this.registerSetting(playersOnly = new ButtonSetting("Players only", true));
        this.closetModule = true;
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!Mc.nullCheck() || event.player != mc.player || !mc.player.isSprinting()) {
            return;
        }
        if (chance.getInput() == 0) {
            return;
        }
        if (playersOnly.isToggled()) {
            if (!(event.target instanceof Player player)) {
                return;
            }
            if (player.hurtDuration == 0 || player.hurtTime > 3) {
                return;
            }
        } else if (!(event.target instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity) event.target;
        if (!living.isAlive()) {
            return;
        }
        long now = System.currentTimeMillis();
        Long last = targets.get(event.target.getId());
        if (last != null && Utils.timeBetween(last, now) <= 200L) {
            return;
        }
        if (chance.getInput() != 100.0D && Math.random() >= chance.getInput() / 100.0D) {
            return;
        }
        targets.put(event.target.getId(), now);
        stopSprint = true;
    }

    public void onDisable() {
        stopSprint = false;
        targets.clear();
    }
}
