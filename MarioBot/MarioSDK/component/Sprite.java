package MarioSDK.component;

import java.awt.Graphics;

import MarioSDK.helper.SpriteType;
import MarioSDK.sprites.Fireball;
import MarioSDK.sprites.Shell;

public abstract class Sprite {
    public SpriteType type = SpriteType.UNDEF;

    public String initialCode;
    public float x, y, xa, ya;
    public int width, height, facing;
    public boolean alive;
    public World world;

    public Sprite(float x, float y, SpriteType type) {
        this.initialCode = "";
        this.x = x;
        this.y = y;
        this.xa = 0;
        this.ya = 0;
        this.facing = 1;
        this.alive = true;
        this.world = null;
        this.width = 16;
        this.height = 16;
        this.type = type;
    }

    public Sprite clone() {
        return null;
    }

    public void added() {

    }

    public void removed() {

    }

    public int getMapX() {
        return (int) (this.x / 16);
    }

    public int getMapY() {
        return (int) (this.y / 16);
    }

    public void render(Graphics og) {

    }

    public void update() {

    }

    public void collideCheck() {
    }

    public void bumpCheck(int xTile, int yTile) {
    }

    public boolean shellCollideCheck(Shell shell) {
        return false;
    }


    public boolean fireballCollideCheck(Fireball fireball) {
        return false;
    }
}