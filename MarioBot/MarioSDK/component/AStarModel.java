package MarioSDK.component;

import MarioSDK.helper.EventType;
import MarioSDK.helper.GameStatus;
import MarioSDK.helper.SpriteType;

public class AStarModel {
    public static final int OBS_NONE = 0;
    public static final int OBS_UNDEF = -42;
    private static final int OBS_S_S = 16;

    public static final int OBS_SOLID = OBS_S_S + 1;
    public static final int OBS_BRICK = OBS_S_S + 6;
    public static final int OBS_QUESTION_BLOCK = OBS_S_S + 8;
    public static final int OBS_COIN = OBS_S_S + 15;


    public static final int OBS_PLATFORM = OBS_S_S + 43;
    public static final int OBS_CANNON = OBS_S_S + 3;
    public static final int OBS_PIPE = OBS_S_S + 18;

    public static final int OBS_SCENE_OBJECT = OBS_S_S + 84;


    public static final int OBS_FIREBALL = 16;

    public static final int OBS_STOMPABLE_ENEMY = 2;
    public static final int OBS_NONSTOMPABLE_ENEMY = 8;
    public static final int OBS_SPECIAL_ITEM = 12;

    public static final int OBS_ENEMY = 1;

    private World world;
    private int fallKill;
    private int stompKill;
    private int fireKill;
    private int shellKill;
    private int mushrooms;
    private int flowers;
    private int breakBlock;

    public static int getSpriteTypeGeneralization(SpriteType sprite, int detail) {
        switch (detail) {
            case (0):
                switch (sprite) {
                    case MARIO:
                        return OBS_NONE;
                    default:
                        sprite.getValue();
                }
            case (1):
                switch (sprite) {
                    case MARIO:
                        return OBS_NONE;
                    case FIREBALL:
                        return OBS_FIREBALL;
                    case MUSHROOM:
                    case LIFE_MUSHROOM:
                    case FIRE_FLOWER:
                        return OBS_SPECIAL_ITEM;
                    case BULLET_BILL:
                    case SHELL:
                    case GOOMBA:
                    case GOOMBA_WINGED:
                    case GREEN_KOOPA:
                    case GREEN_KOOPA_WINGED:
                    case RED_KOOPA:
                    case RED_KOOPA_WINGED:
                        return OBS_STOMPABLE_ENEMY;
                    case SPIKY:
                    case SPIKY_WINGED:
                    case ENEMY_FLOWER:
                        return OBS_NONSTOMPABLE_ENEMY;
                    default:
                        return OBS_NONE;
                }
            case (2):
                switch (sprite) {
                    case FIREBALL:
                    case MARIO:
                    case MUSHROOM:
                    case LIFE_MUSHROOM:
                    case FIRE_FLOWER:
                        return OBS_NONE;
                    default:
                        return OBS_ENEMY;
                }
        }
        return OBS_UNDEF;
    }

    public static int getBlockValueGeneralization(int tile, int detail) {
        if (tile == 0) {
            return OBS_NONE;
        }
        switch (detail) {
            case (0):
                switch (tile) {
                    case 48:
                    case 49:
                        return OBS_NONE;
                    case 6:
                    case 7:
                    case 50:
                    case 51:
                        return OBS_BRICK;
                    case 8:
                    case 11:
                        return OBS_QUESTION_BLOCK;
                }
                return tile + OBS_S_S;
            case (1):
                switch (tile) {
                    case 48:
                    case 49:
                    case 47:
                        return OBS_NONE;
                    case 1:
                    case 2:
                    case 14:
                        return OBS_SOLID;
                    case 3:
                    case 4:
                    case 5:
                        return OBS_CANNON;
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                        return OBS_PIPE;
                    case 6:
                    case 7:
                    case 50:
                    case 51:
                        return OBS_BRICK;
                    case 8:
                    case 11:
                        return OBS_QUESTION_BLOCK;
                    case 15:
                        return OBS_COIN;
                    case 44:
                    case 45:
                    case 46:
                        return OBS_PLATFORM;
                }
                return OBS_NONE;
            case (2):
                switch (tile) {
                    case 48:
                    case 49:
                    case 47:
                        return OBS_NONE;
                }

                return OBS_SCENE_OBJECT;
        }
        return OBS_UNDEF;
    }
    public AStarModel(World world) {
        this.world = world;
    }

    public AStarModel clone() {
        AStarModel model = new AStarModel(this.world.clone());
        model.fallKill = this.fallKill;
        model.stompKill = this.stompKill;
        model.fireKill = this.fireKill;
        model.shellKill = this.shellKill;
        model.mushrooms = this.mushrooms;
        model.flowers = this.flowers;
        model.breakBlock = this.breakBlock;
        return model;
    }

    public void advance(boolean[] actions) {
        this.world.update(actions);
        for (Event e : this.world.lastFrameEvents) {
            if (e.getEventType() == EventType.FIRE_KILL.getValue()) {
                this.fireKill += 1;
            }
            if (e.getEventType() == EventType.STOMP_KILL.getValue()) {
                this.stompKill += 1;
            }
            if (e.getEventType() == EventType.FALL_KILL.getValue()) {
                this.fallKill += 1;
            }
            if (e.getEventType() == EventType.SHELL_KILL.getValue()) {
                this.shellKill += 1;
            }
            if (e.getEventType() == EventType.COLLECT.getValue()) {
                if (e.getEventParam() == SpriteType.FIRE_FLOWER.getValue()) {
                    this.flowers += 1;
                }
                if (e.getEventParam() == SpriteType.MUSHROOM.getValue()) {
                    this.mushrooms += 1;
                }
            }
            if (e.getEventType() == EventType.BUMP.getValue() && e.getEventParam() == OBS_BRICK
                    && e.getMarioState() > 0) {
                this.breakBlock += 1;
            }
        }
    }

    public GameStatus getGameStatus() {
        return this.world.gameStatus;
    }

    public float[] getLevelFloatDimensions() {
        return new float[]{this.world.level.width, this.world.level.height};
    }

    public float[] getMarioFloatPos() {
        return new float[]{this.world.mario.x, this.world.mario.y};
    }

    public float[] getMarioFloatVelocity() {
        return new float[]{this.world.mario.xa, this.world.mario.ya};
    }

    public boolean getMarioCanJumpHigher() {
        return this.world.mario.jumpTime > 0;
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

    public boolean mayMarioJump() {
        return this.world.mario.mayJump;
    }

}
