package keystrokesmod.module.impl.world;

import keystrokesmod.event.EntityJoinWorldEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.player.Freecam;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AntiBot extends Module {
    private static final HashMap<Player, Long> entities = new HashMap<>();
    private static SliderSetting delay;
    private static SliderSetting pitSpawn;
    private static ButtonSetting tablist;
    private ButtonSetting printWorldJoin;

    public AntiBot() {
        super("AntiBot", category.world, 0);
        this.registerSetting(delay = new SliderSetting("Delay", " second", true, -1, 0.5, 15.0, 0.5));
        this.registerSetting(pitSpawn = new SliderSetting("Pit spawn", true, -1, 70, 120, 1));
        this.registerSetting(tablist = new ButtonSetting("Tab list", false));
        this.registerSetting(printWorldJoin = new ButtonSetting("Print world join", false));
        this.closetModule = true;
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e) {
        if ((e.entity instanceof Player player) && player != mc.player) {
            if (delay.getInput() != -1) {
                entities.put(player, System.currentTimeMillis());
            }
            if (printWorldJoin.isToggled()) {
                Utils.sendMessage("&7Entity &b" + player.getId() + " &7joined: &r" + player.getDisplayName().getString());
            }
        }
    }

    public void onUpdate() {
        if (delay.getInput() != -1 && !entities.isEmpty()) {
            long cutoff = System.currentTimeMillis() - (long) (delay.getInput() * 1000);
            entities.values().removeIf(n -> n < cutoff);
        }
    }

    public void onDisable() {
        entities.clear();
    }

    public static boolean isBot(net.minecraft.world.entity.Entity entity) {
        if (ModuleManager.antiBot == null || !ModuleManager.antiBot.isEnabled()) {
            return false;
        }
        if (Freecam.freeEntity != null && Freecam.freeEntity == entity) {
            return true;
        }
        if (!(entity instanceof Player player)) {
            return true;
        }
        if (delay.getInput() != -1 && entities.containsKey(player)) {
            return true;
        }
        if (!player.isAlive()) {
            return true;
        }
        if (player.getName().getString().isEmpty()) {
            return true;
        }
        if (tablist.isToggled() && !getTablist().contains(player.getName().getString())) {
            return true;
        }
        if (pitSpawn.getInput() != -1 && player.getY() >= pitSpawn.getInput() && player.getY() <= 130 && player.position().distanceTo(new net.minecraft.world.phys.Vec3(0, 114, 0)) <= 25) {
            if (Utils.isHypixel()) {
                List<String> sidebar = Utils.getSidebarLines();
                if (!sidebar.isEmpty() && Utils.stripColor(sidebar.getFirst()).contains("THE HYPIXEL PIT")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<String> getTablist() {
        List<String> tab = new ArrayList<>();
        if (!Mc.nullCheck()) {
            return tab;
        }
        for (PlayerInfo entry : Utils.getTablist(true)) {
            if (entry != null) {
                tab.add(entry.getProfile().name());
            }
        }
        return tab;
    }
}
