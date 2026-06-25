package keystrokesmod.module.impl.movement;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;

public class VClip extends Module {
    private SliderSetting distance;
    private ButtonSetting sendMessage;

    public VClip() {
        super("VClip", category.movement, 0);
        this.registerSetting(distance = new SliderSetting("Distance", 3.0, -20.0, 20.0, 0.5));
        this.registerSetting(sendMessage = new ButtonSetting("Send message", true));
    }

    public void onEnable() {
        if (!Mc.nullCheck()) {
            return;
        }
        double dist = this.distance.getInput();
        if (dist != 0.0D) {
            mc.player.setPos(mc.player.getX(), mc.player.getY() + dist, mc.player.getZ());
            if (sendMessage.isToggled()) {
                Utils.sendMessage("&7Teleported you " + (dist > 0.0 ? "upwards" : "downwards") + " by &b" + dist + " &7blocks.");
            }
        }
        this.disable();
    }
}
