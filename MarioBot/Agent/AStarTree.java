package Agent;

import java.util.ArrayList;

import MarioSDK.component.AStarModel;
import MarioSDK.component.MTimer;
import MarioSDK.helper.GameStatus;

public class AStarTree {
    public FindNode bestPosition;
    public FindNode furthestPosition;
    float currentSearchStartingMarioXPos;
    ArrayList<FindNode> posPool;
    ArrayList<int[]> visitedStates = new ArrayList<>();
    private boolean requireReplanning = false;

    private ArrayList<boolean[]> currentActionPlan;
    int ticksBeforeReplanning = 0;

    private AStarModel find(MTimer timer) {
        FindNode current = bestPosition;
        boolean currentGood = false;
        int maxRight = 176;
        while (posPool.size() != 0
                && ((bestPosition.sceneSnapshot.getMarioFloatPos()[0] - currentSearchStartingMarioXPos < maxRight) || !currentGood)
                && timer.getRemainingTime() > 0) {
            current = playBestPos(posPool);
            if (current == null) {
                return null;
            }
            currentGood = false;
            float realRemainingTime = current.simPosition();

            if (realRemainingTime < 0) {
                continue;
            } else if (!current.isInVisitedList && isVisit((int) current.sceneSnapshot.getMarioFloatPos()[0],
                    (int) current.sceneSnapshot.getMarioFloatPos()[1], current.timeElapsed)) {
                realRemainingTime += Extras.visitedListPenalty;
                current.isInVisitedList = true;
                current.remainingTime = realRemainingTime;
                current.remainingTimeEstimated = realRemainingTime;
                posPool.add(current);
            } else if (realRemainingTime - current.remainingTimeEstimated > 0.1) {
                current.remainingTimeEstimated = realRemainingTime;
                posPool.add(current);
            } else {
                currentGood = true;
                markVisited((int) current.sceneSnapshot.getMarioFloatPos()[0], (int) current.sceneSnapshot.getMarioFloatPos()[1], current.timeElapsed);
                posPool.addAll(current.generateSubNode());
            }
            if (currentGood) {
                if (bestPosition.getRemainingTime() > current.getRemainingTime())
                    bestPosition = current;
                if (current.sceneSnapshot.getMarioFloatPos()[0] > furthestPosition.sceneSnapshot.getMarioFloatPos()[0])
                    furthestPosition = current;
            }
        }
        if (current.sceneSnapshot.getMarioFloatPos()[0] - currentSearchStartingMarioXPos < maxRight
                && furthestPosition.sceneSnapshot.getMarioFloatPos()[0] > bestPosition.sceneSnapshot.getMarioFloatPos()[0] + 20)
            bestPosition = furthestPosition;

        return current.sceneSnapshot;
    }

    private void start(AStarModel model, int repetitions) {
        FindNode startPos = new FindNode(null, repetitions, null);
        startPos.root(model);

        posPool = new ArrayList<>();
        visitedStates.clear();
        posPool.addAll(startPos.generateSubNode());
        currentSearchStartingMarioXPos = model.getMarioFloatPos()[0];

        bestPosition = startPos;
        furthestPosition = startPos;
    }

    private ArrayList<boolean[]> plan() {
        ArrayList<boolean[]> actions = new ArrayList<>();

        if (bestPosition == null) {
            for (int i = 0; i < 10; i++) {
                actions.add(Extras.generateAction(false, true, false, false, true));
            }
            return actions;
        }

        FindNode current = bestPosition;
        while (current.parentPos != null) {
            for (int i = 0; i < current.repetitions; i++)
                actions.add(0, current.action);
            if (current.hasBeenHurt) {
                requireReplanning = true;
            }
            current = current.parentPos;
        }
        return actions;
    }

    private FindNode playBestPos(ArrayList<FindNode> posPool) {
        FindNode bestPos = null;
        float bestPosCost = 10000000;
        for (FindNode current : posPool) {
            float currentCost = current.getRemainingTime() + current.timeElapsed * 0.90f;
            if (currentCost < bestPosCost) {
                bestPos = current;
                bestPosCost = currentCost;
            }
        }
        posPool.remove(bestPos);
        return bestPos;
    }

    public boolean[] optimiseMove(AStarModel model, MTimer timer) {
        int planAhead = 2;
        int stepsPerSearch = 2;

        AStarModel originalModel = model.clone();
        ticksBeforeReplanning--;
        requireReplanning = false;
        if (ticksBeforeReplanning <= 0 || currentActionPlan.size() == 0 || requireReplanning) {
            currentActionPlan = plan();
            if (currentActionPlan.size() < planAhead) {
                planAhead = currentActionPlan.size();
            }

            for (int i = 0; i < planAhead; i++) {
                model.advance(currentActionPlan.get(i));
            }
            start(model, stepsPerSearch);
            ticksBeforeReplanning = planAhead;
        }
        if (model.getGameStatus() == GameStatus.LOSE) {
            start(originalModel, stepsPerSearch);
        }
        find(timer);

        boolean[] action = new boolean[5];
        if (currentActionPlan.size() > 0)
            action = currentActionPlan.remove(0);
        return action;
    }

    private void markVisited(int x, int y, int t) {
        visitedStates.add(new int[]{x, y, t});
    }

    private boolean isVisit(int x, int y, int t) {
        int timeDiff = 5;
        int xDiff = 2;
        int yDiff = 2;
        for (int[] v : visitedStates) {
            if (Math.abs(v[0] - x) < xDiff && Math.abs(v[1] - y) < yDiff && Math.abs(v[2] - t) < timeDiff
                    && t >= v[2]) {
                return true;
            }
        }
        return false;
    }

}
