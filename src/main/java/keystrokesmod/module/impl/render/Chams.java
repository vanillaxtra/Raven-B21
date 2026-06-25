package keystrokesmod.module.impl.render;

import keystrokesmod.event.RenderWorldLastEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.impl.world.AntiBot;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class Chams extends Module {
    private ButtonSetting ignoreBots;
    private ButtonSetting renderSelf;
    private ButtonSetting hidePlayers;

    public Chams() {
        super("Chams", category.render, 0);
        this.registerSetting(ignoreBots = new ButtonSetting("Ignore bots", false));
        this.registerSetting(hidePlayers = new ButtonSetting("Hide players", false));
        this.registerSetting(renderSelf = new ButtonSetting("Render self", false));
    }

    public boolean shouldHide(Entity entity) {
        if (!isEnabled() || !hidePlayers.isToggled()) {
            return false;
        }
        if (entity == mc.player && renderSelf.isToggled()) {
            return false;
        }
        if (ignoreBots.isToggled() && AntiBot.isBot(entity)) {
            return false;
        }
        return entity instanceof Player;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        // chams depth handled via mixin using shouldHide/chams enabled state
    }
}
