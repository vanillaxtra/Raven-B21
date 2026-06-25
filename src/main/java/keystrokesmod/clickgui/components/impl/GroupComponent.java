package keystrokesmod.clickgui.components.impl;

import keystrokesmod.Raven;
import keystrokesmod.clickgui.components.Component;
import keystrokesmod.module.setting.impl.GroupSetting;
import keystrokesmod.utility.Mc;
import net.minecraft.client.gui.GuiGraphics;

public class GroupComponent extends Component {
    public GroupSetting setting;
    private ModuleComponent component;
    public int o;
    private int x;
    private int y;
    public boolean opened;

    public GroupComponent(GroupSetting setting, ModuleComponent moduleComponent, int o) {
        this.setting = setting;
        this.component = moduleComponent;
        this.o = o;
        this.x = moduleComponent.categoryComponent.getX() + moduleComponent.categoryComponent.getWidth();
        this.y = moduleComponent.categoryComponent.getY() + moduleComponent.yPos;
    }

    @Override
    public void render(GuiGraphics context) {
        context.pose().pushMatrix();
        context.pose().scale(0.5f, 0.5f);
        float strX = (float) ((this.component.categoryComponent.getX() + 4) * 2) + 1;
        float strY = (float) ((this.component.categoryComponent.getY() + this.o + 4) * 2);
        String label = this.opened ? "[v]  " + this.setting.getName() : "[>]  " + this.setting.getName();
        drawString(context, label, strX, strY);
        context.pose().popMatrix();
    }

    public void updateHeight(int n) {
        this.o = n;
    }

    @Override
    public void drawScreen(int x, int y) {
        this.y = this.component.categoryComponent.getModuleY() + this.o;
        this.x = this.component.categoryComponent.getX();
    }

    @Override
    public boolean onClick(int x, int y, int b) {
        if (this.i(x, y) && b == 1 && this.component.isOpened) {
            this.opened = !this.opened;
            for (CategoryComponent categoryComponent : Raven.clickGui.categories) {
                if (categoryComponent.category == this.component.mod.moduleCategory()) {
                    for (ModuleComponent moduleComponent : categoryComponent.modules) {
                        if (moduleComponent.mod.getName().equals(this.component.mod.getName())) {
                            moduleComponent.updateSettingPositions(7);
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean i(int x, int y) {
        return x > this.x && x < this.x + this.component.categoryComponent.getWidth() && y > this.y && y < this.y + 11;
    }

    private void drawString(GuiGraphics context, String text, float x, float y) {
        context.drawString(Mc.mc().font, text, (int) x, (int) y, -1, false);
    }
}
