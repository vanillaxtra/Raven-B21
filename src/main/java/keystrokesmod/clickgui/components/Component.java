package keystrokesmod.clickgui.components;

import net.minecraft.client.gui.GuiGraphics;

public class Component {
    public boolean visible = true;

    public void render(GuiGraphics context) {
    }

    public void drawScreen(int x, int y) {
    }

    public boolean onClick(int x, int y, int b) {
        return false;
    }

    public void mouseReleased(int x, int y, int m) {
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public void updateHeight(int n) {
    }

    public int getHeight() {
        return 0;
    }

    public void onGuiClosed() {
    }

    public void onScroll(int scroll) {
    }
}
