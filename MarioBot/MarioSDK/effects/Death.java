package MarioSDK.effects;

import MarioSDK.component.Effects;

public class Death extends Effects {
    public Death(float x, float y, boolean flipX, int startIndex, float yv) {
        super(x, y, 0, yv, 0, 1f, startIndex, 30);
        this.graphics.flipX = flipX;
    }
}
