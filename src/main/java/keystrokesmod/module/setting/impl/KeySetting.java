package keystrokesmod.module.setting.impl;

import com.google.gson.JsonObject;
import keystrokesmod.module.setting.Setting;
import keystrokesmod.utility.BindUtil;

public class KeySetting extends Setting {
    private int key;
    public GroupSetting group;

    public KeySetting(String name, int key) {
        super(name);
        this.key = key;
    }

    public KeySetting(GroupSetting group, String name, int key) {
        super(name);
        this.group = group;
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public String getName() {
        return super.getName();
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isPressed() {
        return BindUtil.isBindDown(this.key);
    }

    @Override
    public void loadProfile(JsonObject data) {
        if (data.has(getName()) && data.get(getName()).isJsonPrimitive()) {
            int keyValue = this.key;
            try {
                keyValue = data.getAsJsonPrimitive(getName()).getAsInt();
            } catch (Exception ignored) {
            }
            this.key = keyValue;
        }
    }
}
