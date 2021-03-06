package MarioSDK.component;

import MarioSDK.helper.EventType;

public class Event {
    private EventType eventType;
    private int eventParam;
    private float marioX;
    private float marioY;
    private int marioState;
    private int time;

    public Event(EventType eventType, int eventParam, float x, float y, int state, int time) {
        this.eventType = eventType;
        this.eventParam = eventParam;
        this.marioX = x;
        this.marioY = y;
        this.marioState = state;
        this.time = time;
    }

    public int getEventType() {
        return this.eventType.getValue();
    }

    public int getEventParam() {
        return this.eventParam;
    }

    public float getMarioX() {
        return this.marioX;
    }

    public int getMarioState() {
        return this.marioState;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public boolean equals(Object obj) {
        Event otherEvent = (Event) obj;
        return this.eventType == otherEvent.eventType &&
                (this.eventParam == 0 || this.eventParam == otherEvent.eventParam);
    }
}
