package keystrokesmod.event;


public class PostSetSliderEvent  {
    public double previousVal;
    public double newVal;

    public PostSetSliderEvent(double previousVal, double newVal) {
        this.previousVal = previousVal;
        this.newVal = newVal;
    }
}
