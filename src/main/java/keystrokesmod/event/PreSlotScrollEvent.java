package keystrokesmod.event;

public class PreSlotScrollEvent extends CancelableEvent {
    public int slot;
    public int previousSlot;

    public PreSlotScrollEvent(int slot, int previousSlot) {
        this.slot = slot;
        this.previousSlot = previousSlot;
    }
}
