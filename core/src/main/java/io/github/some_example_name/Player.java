package io.github.some_example_name;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Player {
    public float x = 2000;
    public float y = 2000;
    private PlayerAnimator playerAnimator;
    private ShapeRenderer shapeRenderer;
    private static final float MAX_VELOCITY = 20 * 6f / 60f;
    private static final float DRAG = 1 * 6f / 60f;
    private float current_velocityX = 0;
    private float current_velocityY = 0;
    private TiledMapTileLayer tiledMapTileLayer;
    private int tilexCenter = 0;
    private int tilexRight = 0;
    private int tileyCenter = 0;


    public Player() {
        shapeRenderer = new ShapeRenderer();
        //playerAnimator = new PlayerAnimator();
    }

    public void setPlayerAnimator(PlayerAnimator playerAnimator) {
        this.playerAnimator = playerAnimator;
    }

    public void setTiledMapTileLayer(TiledMapTileLayer tiledMapTileLayer) {
        this.tiledMapTileLayer = tiledMapTileLayer;
    }

    public void render(OrthographicCamera cam) {
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(x, y, 2, 20);
        shapeRenderer.circle(x + playerAnimator.getFrame_WIDTH() * (1 / PlayerAnimator.getSIZEING()) / 2, y, 5, 20);

        shapeRenderer.end();


        // System.out.println(x+" "+y);
    }

    public void renderAnimation(SpriteBatch batch) {
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

        if (x != 0 && y != 0) {
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

        if (checkHorizontalTileCollision((this.x+current_velocityX),this.y))
            this.x += current_velocityX;
        else
            current_velocityX=0;

        if(checkVerticalTileCollision(this.x,this.y+current_velocityY))
            this.y += current_velocityY;
        else
            current_velocityY=0;




        updateAnimation();
    }

    private boolean checkHorizontalTileCollision(float x,float y) {
        //Pixel / float koordinate an den Grenzen der player Textur
        float rightxPos = x + playerAnimator.getFrame_WIDTH() * (1 / PlayerAnimator.getSIZEING()) / 2;
        float leftxPos = x - playerAnimator.getFrame_WIDTH() * (1 / PlayerAnimator.getSIZEING()) / 2;
        //mapping auf tiles
        int rightTile = (int) (rightxPos / 16 / 5);
        int leftTile = (int) (leftxPos / 16 / 5);

        tilexRight = rightTile;
        tileyCenter = (int) (y / 16 / PlayerAnimator.getSIZEING());


       // System.out.println(" " + tilexRight);
//        System.out.println("    " + playerAnimator.getFrame_WIDTH() / 2 / PlayerAnimator.getSIZEING());
        if (tiledMapTileLayer.getCell(leftTile, tileyCenter) == null || tiledMapTileLayer.getCell(rightTile, tileyCenter) == null) {
            //System.out.println("Horizontally out of Bounds");
            return false;
        }
        return true;
    }

    private boolean checkVerticalTileCollision(float x, float y) {


        //Pixel / float koordinate an den Grenzen der player Textur
        tileyCenter = (int) (y / 16 / PlayerAnimator.getSIZEING());
        //float upyPos = y + playerAnimator.getFrame_HEIGHT() * (1 / PlayerAnimator.getSIZEING()) / 2;
        float upyPos = y + playerAnimator.getFrame_HEIGHT() * (1 / PlayerAnimator.getSIZEING() /3);
        //float downyPos = y - playerAnimator.getFrame_HEIGHT() * (1 / PlayerAnimator.getSIZEING()) / 2;
        float downyPos = y - playerAnimator.getFrame_HEIGHT() * (1/PlayerAnimator.getSIZEING());

        int tilexCenter = (int) (x / 16 / PlayerAnimator.getSIZEING());


        //mapping auf tiles
        int upTile = (int) (upyPos / 16 / 5);
        int downTile = (int) (downyPos / 16 / 5);

        tilexRight = upTile;


        if (tiledMapTileLayer.getCell(tilexCenter, (int) upTile) == null || tiledMapTileLayer.getCell(tilexCenter, downTile) == null) {
            //System.out.println("Vertically out of Bounds");
            return false;
        }
        return true;
    }

    private void updateAnimation() {
        if (current_velocityX == 0 && current_velocityY == 0) {
            // System.out.println("Standing");
            playerAnimator.updateFacing(Facing.STANDING);
        } else if (current_velocityX > 0) {
            // System.out.println("Right");
            playerAnimator.updateFacing(Facing.RIGHT);
        } else if (current_velocityX < 0) {
            //  System.out.println("Left");
            playerAnimator.updateFacing(Facing.LEFT);
        } else if (current_velocityY > 0) {
            // System.out.println("Up");
            playerAnimator.updateFacing(Facing.UP);
        } else if (current_velocityY < 0) {
            //  System.out.println("Down");
            playerAnimator.updateFacing(Facing.DOWN);
        }

        playerAnimator.updatePosition(x, y);
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
