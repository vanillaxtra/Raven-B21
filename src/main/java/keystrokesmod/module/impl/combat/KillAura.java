package keystrokesmod.module.impl.combat;

import keystrokesmod.event.ClientTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.impl.world.AntiBot;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RotationUtils;
import keystrokesmod.utility.Utils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

import java.util.Comparator;

public class KillAura extends Module {
    private SliderSetting aps;
    private SliderSetting attackRange;
    private SliderSetting fov;
    private SliderSetting sortMode;
    private ButtonSetting weaponOnly;
    private ButtonSetting ignoreTeammates;
    private ButtonSetting attackMobs;
    private ButtonSetting requireMouseDown;
    private String[] sortModes = new String[]{"Distance", "Health", "Hurttime"};

    public static LivingEntity target;
    private long lastAttack;

    public KillAura() {
        super("KillAura", category.combat, 0);
        this.registerSetting(aps = new SliderSetting("APS", 12, 1, 20, 0.5));
        this.registerSetting(attackRange = new SliderSetting("Range", 3.2, 3, 6, 0.1));
        this.registerSetting(fov = new SliderSetting("FOV", 180, 30, 180, 1));
        this.registerSetting(sortMode = new SliderSetting("Sort", 0, sortModes));
        this.registerSetting(weaponOnly = new ButtonSetting("Weapon only", false));
        this.registerSetting(ignoreTeammates = new ButtonSetting("Ignore teammates", true));
        this.registerSetting(attackMobs = new ButtonSetting("Attack mobs", false));
        this.registerSetting(requireMouseDown = new ButtonSetting("Require mouse down", false));
    }

    @Override
    public String getInfo() {
        return target != null ? target.getName().getString() : "";
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent e) {
        if (e.phase != ClientTickEvent.Phase.END || !Mc.nullCheck()) {
            return;
        }
        if (requireMouseDown.isToggled() && !Utils.isClicking()) {
            target = null;
            return;
        }
        if (weaponOnly.isToggled() && !Utils.holdingWeapon()) {
            target = null;
            return;
        }
        target = findTarget();
        if (target == null) {
            return;
        }
        long delay = (long) (1000.0 / aps.getInput());
        if (System.currentTimeMillis() - lastAttack < delay) {
            return;
        }
        float[] rots = RotationUtils.getRotations(target);
        RotationUtils.setSilentRotations(rots[0], rots[1]);
        mc.gameMode.attack(mc.player, target);
        mc.player.swing(InteractionHand.MAIN_HAND);
        lastAttack = System.currentTimeMillis();
    }

    private LivingEntity findTarget() {
        double range = attackRange.getInput();
        return mc.level.getEntities(
                net.minecraft.world.level.entity.EntityTypeTest.forClass(LivingEntity.class),
                mc.player.getBoundingBox().inflate(range),
                e -> {
            if (e == mc.player || !e.isAlive()) {
                return false;
            }
            if (e instanceof Player player) {
                if (Utils.isFriended(player) || AntiBot.isBot(player)) {
                    return false;
                }
                if (ignoreTeammates.isToggled() && Utils.isTeamMate(player)) {
                    return false;
                }
            } else if (!attackMobs.isToggled()) {
                return false;
            }
            if (mc.player.distanceTo(e) > range) {
                return false;
            }
            return Utils.inFov((float) fov.getInput(), e);
        }).stream().min(getComparator()).orElse(null);
    }

    private Comparator<LivingEntity> getComparator() {
        return switch ((int) sortMode.getInput()) {
            case 1 -> Comparator.comparingDouble(LivingEntity::getHealth);
            case 2 -> Comparator.comparingInt(e -> e.hurtTime);
            default -> Comparator.comparingDouble(mc.player::distanceTo);
        };
    }

    public void onDisable() {
        target = null;
    }
}
