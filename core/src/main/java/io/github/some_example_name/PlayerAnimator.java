package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class PlayerAnimator {
    private Animation<TextureRegion> standingLeft;
    private TextureRegion[] standingLeftFrame;
    private Animation<TextureRegion> standingRightAnimation;
    private TextureRegion[] standingRightFrames;

    private Animation<TextureRegion> walkingLeftAnimation;
    private TextureRegion[] walkingLeftFrames;

    private TextureRegion[] walkingRightFrames;
    private Animation<TextureRegion> walkingRightAnimation;

    private TextureRegion currFrame = new TextureRegion();
}
