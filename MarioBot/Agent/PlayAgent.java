package Agent;

import MarioSDK.component.AStarModel;
import MarioSDK.component.MTimer;
import MarioSDK.helper.MarioActions;

public class PlayAgent implements MarioSDK.component.Agent {
    private boolean[] action;
    private AStarTree tree;

    @Override
    public void init(AStarModel model, MTimer timer) {
        this.action = new boolean[MarioActions.numberOfActions()];
        this.tree = new AStarTree();
    }

    @Override
    public boolean[] actions(AStarModel model, MTimer timer) {
        action = this.tree.optimiseMove(model, timer);
        return action;
    }

}
