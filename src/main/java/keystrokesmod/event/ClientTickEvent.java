package keystrokesmod.event;

public class ClientTickEvent {
    public enum Phase {
        START,
        END
    }

    public final Phase phase;

    public ClientTickEvent(Phase phase) {
        this.phase = phase;
    }
}
