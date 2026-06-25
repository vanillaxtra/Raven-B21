package keystrokesmod.utility.command.impl;

import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import keystrokesmod.utility.command.Command;

public class Name extends Command {
    public Name() {
        super("name", new String[] { "ign", "name" });
    }

    @Override
    public void onExecute(String[] args) {
        if (!Utils.nullCheck()) {
            return;
        }
        String name = Mc.player().getGameProfile().name();
        Utils.addToClipboard(name);
        chatWithPrefix("&7Copied &b" + name + " &7to clipboard");
    }
}
