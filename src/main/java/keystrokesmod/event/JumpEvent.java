package keystrokesmod.event;

public class JumpEvent extends CancelableEvent {
    private float motionY, yaw;
    private boolean applySprint;

    public JumpEvent(float motionY, float yaw, boolean applySprint) {
        this.motionY = motionY;
        this.yaw = yaw;
        this.applySprint = applySprint;
    }

    public float getMotionY() {
        return motionY;
    }

    public void setMotionY(float motionY) {
        this.motionY = motionY;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public boolean applySprint() {
        return applySprint;
    }

    public void setSprint(boolean applySprint) {
        this.applySprint = applySprint;
    }
}
