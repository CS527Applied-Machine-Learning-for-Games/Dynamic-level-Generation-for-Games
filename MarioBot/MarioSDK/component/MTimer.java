package MarioSDK.component;

public class MTimer {
    private long startTimer;
    private long remainingTime;

    public MTimer(long remainingTime) {
        this.startTimer = System.currentTimeMillis();
        this.remainingTime = remainingTime;
    }

    public long getRemainingTime() {
        return Math.max(0, this.remainingTime - (System.currentTimeMillis() - this.startTimer));
    }
}
