package Agent;

import java.util.ArrayList;

import MarioSDK.component.AStarModel;
import MarioSDK.helper.GameStatus;
import MarioSDK.helper.MarioActions;

public class Extras {
    public static final int visitedListPenalty = 1500;
    public static final float maxMarioSpeed = 10.9090909f;

    public static int getDam(AStarModel model, AStarModel prevModel) {
        int damage = 0;
        if (prevModel.getMarioMode() > model.getMarioMode()) {
            damage += 1;
        }
        if (model.getGameStatus() == GameStatus.LOSE) {
            if (model.getMarioFloatPos()[1] > model.getLevelFloatDimensions()[1] - 20) {
                damage += 5;
            } else {
                damage += 2;
            }
        }
        return damage;
    }

    public static float[] estMaxShift(float currentAccel, boolean[] action, int ticks) {
        float dist = 0;
        float runningSpeed = action[MarioActions.SPEED.getValue()] ? 1.2f : 0.6f;
        int dir = 0;
        if (action[MarioActions.LEFT.getValue()])
            dir = -1;
        if (action[MarioActions.RIGHT.getValue()])
            dir = 1;
        for (int i = 0; i < ticks; i++) {
            currentAccel += runningSpeed * dir;
            dist += currentAccel;
            currentAccel *= 0.89f;
        }
        float[] ret = new float[2];
        ret[0] = dist;
        ret[1] = currentAccel;
        return ret;
    }

    public static boolean[] generateAction(boolean left, boolean right, boolean down, boolean jump, boolean speed) {
        boolean[] action = new boolean[5];
        action[MarioActions.DOWN.getValue()] = down;
        action[MarioActions.JUMP.getValue()] = jump;
        action[MarioActions.LEFT.getValue()] = left;
        action[MarioActions.RIGHT.getValue()] = right;
        action[MarioActions.SPEED.getValue()] = speed;
        return action;
    }

    public static boolean isJumpPossible(FindNode node, boolean checkParent) {
        if (node.parentPos != null && checkParent && isJumpPossible(node.parentPos, false))
            return true;
        return node.sceneSnapshot.mayMarioJump() || node.sceneSnapshot.getMarioCanJumpHigher();
    }

    public static ArrayList<boolean[]> generateActions(FindNode node) {
        ArrayList<boolean[]> possibleActions = new ArrayList<boolean[]>();
        if (isJumpPossible(node, true))
            possibleActions.add(Extras.generateAction(false, false, false, true, false));
        if (isJumpPossible(node, true))
            possibleActions.add(Extras.generateAction(false, false, false, true, true));

        possibleActions.add(Extras.generateAction(false, true, false, false, true));
        if (isJumpPossible(node, true))
            possibleActions.add(Extras.generateAction(false, true, false, true, true));
        possibleActions.add(Extras.generateAction(false, true, false, false, false));
        if (isJumpPossible(node, true))
            possibleActions.add(Extras.generateAction(false, true, false, true, false));

        possibleActions.add(Extras.generateAction(true, false, false, false, false));
        if (isJumpPossible(node, true))
            possibleActions.add(Extras.generateAction(true, false, false, true, false));
        possibleActions.add(Extras.generateAction(true, false, false, false, true));
        if (isJumpPossible(node, true))
            possibleActions.add(Extras.generateAction(true, false, false, true, true));

        return possibleActions;
    }
}
