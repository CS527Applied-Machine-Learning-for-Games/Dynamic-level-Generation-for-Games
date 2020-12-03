package Agent;

import java.util.ArrayList;

import MarioSDK.component.AStarModel;
import MarioSDK.helper.GameStatus;

public class FindNode {
    public int timeElapsed = 0;
    public float remainingTimeEstimated = 0;
    public float remainingTime = 0;

    public FindNode parentPos = null;
    public AStarModel sceneSnapshot = null;
    public int distanceFromOrigin = 0;
    public boolean hasBeenHurt = false;
    public boolean isInVisitedList = false;

    boolean[] action;
    int repetitions = 1;

    public float calculateTime(float marioX, float marioXA) {
        return (100000 - (maxShift(marioXA, 1000) + marioX)) / Extras.maxMarioSpeed - 1000;
    }

    public float getRemainingTime() {
        if (remainingTime > 0)
            return remainingTime;
        else
            return remainingTimeEstimated;
    }

    public float estimateRemainingTime(boolean[] action, int repetitions) {
        float[] childbehaviorDistanceAndSpeed = Extras.estMaxShift(
                this.sceneSnapshot.getMarioFloatVelocity()[0], action, repetitions);
        return calculateTime(this.sceneSnapshot.getMarioFloatPos()[0] + childbehaviorDistanceAndSpeed[0],
                childbehaviorDistanceAndSpeed[1]);
    }

    public FindNode(boolean[] action, int repetitions, FindNode parent) {
        this.parentPos = parent;
        if (parent != null) {
            this.remainingTimeEstimated = parent.estimateRemainingTime(action, repetitions);
            this.distanceFromOrigin = parent.distanceFromOrigin + 1;
        }
        this.action = action;
        this.repetitions = repetitions;
        if (parent != null)
            timeElapsed = parent.timeElapsed + repetitions;
        else
            timeElapsed = 0;
    }

    public void root(AStarModel model) {
        if (this.parentPos == null) {
            this.sceneSnapshot = model.clone();
            this.remainingTimeEstimated = calculateTime(model.getMarioFloatPos()[0], 0);
        }
    }

    public float simPosition() {
        this.sceneSnapshot = parentPos.sceneSnapshot.clone();
        for (int i = 0; i < repetitions; i++) {
            this.sceneSnapshot.advance(action);
        }
        int marioDamage = Extras.getDam(this.sceneSnapshot, this.parentPos.sceneSnapshot);
        remainingTime =
                calculateTime(this.sceneSnapshot.getMarioFloatPos()[0], this.sceneSnapshot.getMarioFloatVelocity()[0]) +
                        marioDamage * (1000000 - 100 * distanceFromOrigin);
        if (isInVisitedList)
            remainingTime += Extras.visitedListPenalty;
        hasBeenHurt = marioDamage != 0;

        return remainingTime;
    }

    public ArrayList<FindNode> generateSubNode() {
        ArrayList<FindNode> list = new ArrayList<>();
        ArrayList<boolean[]> possibleActions = Extras.generateActions(this);
        if (this.isDeadEnd()) {
            possibleActions.clear();
        }
        for (boolean[] action : possibleActions) {
            list.add(new FindNode(action, repetitions, this));
        }
        return list;
    }

    public boolean isDeadEnd() {
        if (this.sceneSnapshot == null) {
            return false;
        }
        return this.sceneSnapshot.getGameStatus() != GameStatus.RUNNING;
    }

    private float maxShift(float initialSpeed, int ticks) {
        float y = ticks;
        float s0 = initialSpeed;
        return (float) (99.17355373 * Math.pow(0.89, y + 1) - 9.090909091 * s0 * Math.pow(0.89, y + 1) + 10.90909091 * y
                - 88.26446282 + 9.090909091 * s0);
    }

}
