package io.github.some_example_name;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    public float x;
    public float y;
    private PlayerAnimator playerAnimator;
    private ShapeRenderer shapeRenderer;

    public Player(){
        shapeRenderer= new ShapeRenderer();
    }

    public void render(OrthographicCamera cam){
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(x,y,2,20);
        shapeRenderer.end();

        System.out.println(x+" "+y);
    }

    public void move(float plusX,float plusY){
        x+=plusX;
        y+=plusY;
    }

}
