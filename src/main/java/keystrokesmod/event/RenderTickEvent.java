package keystrokesmod.event;

public class RenderTickEvent {
    public enum Phase {
        START,
        END
    }

    public final Phase phase;

    public RenderTickEvent(Phase phase) {
        this.phase = phase;
    }
}
