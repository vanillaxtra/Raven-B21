package keystrokesmod.module.impl.render;

import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.Mc;

public class ExtendCamera extends Module {
    public SliderSetting distance;
    private float lastDistance;

    public ExtendCamera() {
        super("ExtendCamera", category.render);
        this.registerSetting(new DescriptionSetting("Extends camera in third person."));
        this.registerSetting(new DescriptionSetting("Default is 4 blocks."));
        this.registerSetting(distance = new SliderSetting("Distance", " block", 4, 1, 40, 0.5));
    }

    public float getCameraDistance() {
        return isEnabled() ? (float) distance.getInput() : 4.0f;
    }

    public void onEnable() {
        lastDistance = (float) distance.getInput();
    }

    public void onUpdate() {
        if (!Mc.nullCheck()) {
            return;
        }
        float input = (float) distance.getInput();
        if (lastDistance != input) {
            lastDistance = input;
        }
    }

    public void onDisable() {
        lastDistance = 4.0f;
    }
}
