package io.github.some_example_name;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    public float x;
    public float y;
    private PlayerAnimator playerAnimator;
    private ShapeRenderer shapeRenderer;
    private static final float MAX_VELOCITY = 20 / 60f;
    private static final float DRAG = 1 / 60f;
    private float current_velocityX = 0;
    private float current_velocityY = 0;



    public Player() {
        shapeRenderer = new ShapeRenderer();
        //playerAnimator = new PlayerAnimator();
    }

    public void setPlayerAnimator(PlayerAnimator playerAnimator){
        this.playerAnimator=playerAnimator;
    }

    public void render(OrthographicCamera cam) {
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(x, y, 2, 20);
        shapeRenderer.end();


        // System.out.println(x+" "+y);
    }

    public void renderAnimation(SpriteBatch batch){
        playerAnimator.render(batch);
    }

    public void move(float plusX, float plusY) {
        x += plusX;
        y += plusY;
    }

    public void moveDirection(int x, int y) {
        //System.out.println("x: "+x+" y: "+y);
        if (x == 0) current_velocityX = applyDrag(current_velocityX);
        if (y == 0) current_velocityY = applyDrag(current_velocityY);


        if (x == 1) {
            current_velocityX = MAX_VELOCITY;
        } else if (x == -1) {
            current_velocityX = -MAX_VELOCITY;
        }

        if (y == 1) {
            current_velocityY = MAX_VELOCITY;
        } else if (y == -1) {
            current_velocityY = -MAX_VELOCITY;
        }

        if (x != 0 && y!=0) {
//            System.out.println("hahahahahahahH");
            if (x == y) {
                current_velocityY = (float) (current_velocityY * 0.7);
                current_velocityX = (float) (current_velocityX * 0.7);
            } else if (x == (-1 * y)) {
                current_velocityY = (float) (current_velocityY * 0.7);
                current_velocityX = (float) (current_velocityX * 0.7);
            }
        }

//        if (x!=0 && x!=0){
//            x+=Math.sqrt((current_velocityX*current_velocityX)/2);
//        }

        this.x += current_velocityX;
        this.y += current_velocityY;

       updateAnimation();
    }

    private void updateAnimation() {
        if(current_velocityX == 0 && current_velocityY ==0) {
           // System.out.println("Standing");
            playerAnimator.updateFacing(Facing.STANDING);
        }else if(current_velocityX >0) {
           // System.out.println("Right");
            playerAnimator.updateFacing(Facing.RIGHT);
        }else if(current_velocityX<0){
          //  System.out.println("Left");
            playerAnimator.updateFacing(Facing.LEFT);
        }else if(current_velocityY>0){
           // System.out.println("Up");
            playerAnimator.updateFacing(Facing.UP);
        }else if (current_velocityY<0){
          //  System.out.println("Down");
            playerAnimator.updateFacing(Facing.DOWN);
        }

        playerAnimator.updatePosition(x,y);
    }

    private float applyDrag(float velocity) {
        if (velocity > 0) {
            velocity -= DRAG;
            if (velocity < 0) velocity = 0;
        } else if (velocity < 0) {
            velocity += DRAG;
            if (velocity > 0) velocity = 0;
        }
        return velocity;
    }


}
