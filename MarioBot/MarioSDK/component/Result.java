package MarioSDK.component;

import java.util.ArrayList;

import MarioSDK.helper.EventType;
import MarioSDK.helper.GameStatus;
import MarioSDK.helper.SpriteType;

public class Result {
    private World world;
    private ArrayList<Event> gameEvents;

    public Result(World world, ArrayList<Event> gameEvents) {
        this.world = world;
        this.gameEvents = gameEvents;
    }

    public GameStatus getGameStatus() {
        return this.world.gameStatus;
    }

    public float getCompletionPercentage() {
        return this.world.mario.x / (this.world.level.exitTileX * 16);
    }

    public int getRemainingTime() {
        return this.world.currentTimer;
    }

    public int getMarioMode() {
        int value = 0;
        if (this.world.mario.isLarge) {
            value = 1;
        }
        if (this.world.mario.isFire) {
            value = 2;
        }
        return value;
    }

    public int getKillsTotal() {
        int kills = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.STOMP_KILL.getValue() || e.getEventType() == EventType.FIRE_KILL.getValue() ||
                    e.getEventType() == EventType.FALL_KILL.getValue() || e.getEventType() == EventType.SHELL_KILL.getValue()) {
                kills += 1;
            }
        }
        return kills;
    }

    public int getKillsByFire() {
        int kills = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.FIRE_KILL.getValue()) {
                kills += 1;
            }
        }
        return kills;
    }

    public int getKillsByStomp() {
        int kills = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.STOMP_KILL.getValue()) {
                kills += 1;
            }
        }
        return kills;
    }

    public int getKillsByShell() {
        int kills = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.SHELL_KILL.getValue()) {
                kills += 1;
            }
        }
        return kills;
    }

    public int getKillsByFall() {
        int kills = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.FALL_KILL.getValue()) {
                kills += 1;
            }
        }
        return kills;
    }

    public int getNumJumps() {
        int jumps = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.JUMP.getValue()) {
                jumps += 1;
            }
        }
        return jumps;
    }

    public float getMaxXJump() {
        float maxXJump = 0;
        float startX = -100;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.JUMP.getValue()) {
                startX = e.getMarioX();
            }
            if (e.getEventType() == EventType.LAND.getValue()) {
                if (Math.abs(e.getMarioX() - startX) > maxXJump) {
                    maxXJump = Math.abs(e.getMarioX() - startX);
                }
            }
        }
        return maxXJump;
    }

    public int getMaxJumpAirTime() {
        int maxAirJump = 0;
        int startTime = -100;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.JUMP.getValue()) {
                startTime = e.getTime();
            }
            if (e.getEventType() == EventType.LAND.getValue()) {
                if (e.getTime() - startTime > maxAirJump) {
                    maxAirJump = e.getTime() - startTime;
                }
            }
        }
        return maxAirJump;
    }

    public int getCurrentLives() {
        return this.world.lives;
    }

    public int getCurrentCoins() {
        return this.world.coins;
    }

    public int getNumCollectedMushrooms() {
        int collect = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.COLLECT.getValue() && e.getEventParam() == SpriteType.MUSHROOM.getValue()) {
                collect += 1;
            }
        }
        return collect;
    }

    public int getNumCollectedFireflower() {
        int collect = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.COLLECT.getValue() && e.getEventParam() == SpriteType.FIRE_FLOWER.getValue()) {
                collect += 1;
            }
        }
        return collect;
    }

    public int getNumDestroyedBricks() {
        int bricks = 0;
        for (Event e : this.gameEvents) {
            if (e.getEventType() == EventType.BUMP.getValue() &&
                    e.getEventParam() == AStarModel.OBS_BRICK && e.getMarioState() > 0) {
                bricks += 1;
            }
        }
        return bricks;
    }
}
