package keystrokesmod.module.impl.movement;

import keystrokesmod.utility.RenderUtils;

import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.combat.KillAura;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.client.KeyMapping;

public class Sprint extends Module {
    private ButtonSetting displayText;
    private ButtonSetting rainbow;
    public ButtonSetting disableBackwards;
    public String text = "[Sprint (Toggled)]";
    public float posX = 5;
    public float posY = 5;

    public Sprint() {
        super("Sprint", category.movement, 0);
        this.registerSetting(new DescriptionSetting("Command: 'Â§esprint [msg]Â§r'"));
        this.registerSetting(displayText = new ButtonSetting("Display text", false));
        this.registerSetting(rainbow = new ButtonSetting("Rainbow", false));
        this.registerSetting(disableBackwards = new ButtonSetting("Disable backwards", false));
        this.closetModule = true;
    }

    public void onUpdate() {
        if (Mc.nullCheck()) {
            mc.options.keySprint.setDown(true);
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent e) {
        if (e.phase != RenderTickEvent.Phase.END || !displayText.isToggled() || !Mc.nullCheck()) {
            return;
        }
        if (mc.screen != null) {
            return;
        }
        RenderUtils.drawTextWithShadow(mc.font, text, posX, posY, rainbow.isToggled() ? Utils.getChroma(2, 0) : -1);
    }

    public boolean disableBackwards() {
        if (!disableBackwards.isToggled() || !Mc.nullCheck()) {
            return false;
        }
        if (exceptions()) {
            return false;
        }
        float limit = net.minecraft.util.Mth.wrapDegrees(mc.player.getYRot() - mc.player.yRotO);
        if (limit <= -125 || limit >= 125) {
            return true;
        }
        return ModuleManager.bHop.isEnabled() && KillAura.target != null && Utils.getMovementForward() <= 0.5f;
    }

    private boolean exceptions() {
        return ModuleManager.scaffold.isEnabled() || mc.player.hurtTime > 0 || !mc.player.onGround();
    }
}
