package keystrokesmod.module.impl.other;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.client.multiplayer.PlayerInfo;

public class NameHider extends Module {
    public static String fakeName = "raven";
    public static ButtonSetting hideAllNames;

    public NameHider() {
        super("Name Hider", category.other);
        this.registerSetting(new DescriptionSetting("Command: '§ecname [name]§r'"));
        this.registerSetting(hideAllNames = new ButtonSetting("Hide all names", false));
    }

    public static String getFakeName(String s) {
        if (!Mc.nullCheck()) {
            return s;
        }
        if (hideAllNames.isToggled()) {
            s = s.replace(Utils.getServerName(), "You");
            PlayerInfo self = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            for (PlayerInfo entry : mc.getConnection().getOnlinePlayers()) {
                if (entry.equals(self)) {
                    continue;
                }
                s = s.replace(entry.getProfile().name(), fakeName);
            }
        } else {
            s = s.replace(Utils.getServerName(), fakeName);
        }
        return s;
    }
}
