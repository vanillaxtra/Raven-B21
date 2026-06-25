package keystrokesmod.event;

import net.minecraft.client.gui.screens.Screen;

public class GuiUpdateEvent {
    public Screen Screen;
    public boolean opened;

    public GuiUpdateEvent(Screen Screen, boolean opened) {
        this.Screen = Screen;
        this.opened = opened;
    }
}
