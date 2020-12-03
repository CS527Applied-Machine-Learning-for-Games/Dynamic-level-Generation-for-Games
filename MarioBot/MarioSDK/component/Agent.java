package MarioSDK.component;

public interface Agent {

    void init(AStarModel model, MTimer timer);

    boolean[] actions(AStarModel model, MTimer timer);
}
