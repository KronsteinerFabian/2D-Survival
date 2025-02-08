package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import javafx.scene.shape.Rectangle;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    static final float VELOCITY=0.6f;
    private SpriteBatch batch;
    private Sprite map;
    private Player player;
    private OrthographicCamera cam;
    private World world;
    private Hindernis hindernis;

    @Override
    public void create() {
        batch = new SpriteBatch();
        map = new Sprite(new Texture("libgdx.png"));
        map.setPosition(0,0);
        map.setSize(100,50);

        cam = new OrthographicCamera();
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        player = new Player();

        world = new World(new Vector2(0,0),true);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        cam = new OrthographicCamera(30, 30 * (h / w));

        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        hindernis = new Hindernis("hindernis.jpg",batch,new Rectangle(0,0,20,40));
    }

    @Override
    public void render() {
        world.step(1/60f,6,2);
        update();
        cam.position.set(player.x,player.y,0);
        cam.update();


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        map.draw(batch);
        hindernis.render(cam);
        batch.end();

        player.render(cam);
    }

    public void update(){
        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //cam.translate(-3, 0, 0);
            player.move(-VELOCITY,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //cam.translate(3, 0, 0);
            player.move(VELOCITY,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //cam.translate(0, -3, 0);
            player.move(0,-VELOCITY);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //cam.translate(0, 3, 0);
            player.move(0,VELOCITY);

        }


        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100/cam.viewportWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
        //cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        //cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }

    @Override
    public void dispose() {
        batch.dispose();
        map=null;
    }
}
