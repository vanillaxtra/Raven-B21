package keystrokesmod.event;

public class MouseEvent extends CancelableEvent {
    public final int button;
    public final boolean buttonState;

    public MouseEvent(int button, boolean buttonState) {
        this.button = button;
        this.buttonState = buttonState;
    }
}
