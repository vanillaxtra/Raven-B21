package keystrokesmod.utility;

import keystrokesmod.event.MouseEvent;
import keystrokesmod.event.SubscribeEvent;

public class CPSCalculator {
    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (!event.buttonState) {
            return;
        }
    }
}
