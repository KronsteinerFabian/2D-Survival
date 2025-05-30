package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static java.lang.System.load;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    static final float VELOCITY = 10 / 60f;
    private SpriteBatch batch;
    private Sprite map;
    private Player player;
    private OrthographicCamera cam;
    private World world;
    private Hindernis hindernis;
    private PlayerAnimator playerAnimator;
    private Audio audio;
    private Texture mapTexture;
    private ScreenViewport screenViewport;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tileMapRenderer;
    private TiledMapTileLayer collisionLayer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        mapTexture = new Texture("island.jpg");

        //map = new Sprite(new Texture("libgdx.png"));
        //map = new Sprite(mapTexture);
        //map.setPosition(0, 0);
        //map.setSize(100, 50);
        //map.setSize(mapTexture.getWidth(),mapTexture.getHeight());

        //cam = new OrthographicCamera();
        //cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        //cam.update();
        player = new Player();

        cam = new OrthographicCamera();
        screenViewport = new ScreenViewport(cam);


        //cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        //cam.update();

        //hindernis = new Hindernis("hindernis.jpg", batch, new Rectangle(0, 0, 50, 50));
        playerAnimator = new PlayerAnimator();
        playerAnimator.create();
        player.setPlayerAnimator(playerAnimator);

        tiledMap = new TmxMapLoader().load("tileMap2/FabianStuff.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,5f);
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");

        player.setTiledMapTileLayer(collisionLayer);

        audio = Gdx.audio;
        audio.newMusic(Gdx.files.internal("pauseMenuMusic.mp3")).play();
    }

    @Override
    public void resize(int width, int height) {
        screenViewport.update(width, height);
    }

    @Override
    public void render() {
        //world.step(1 / 60f, 6, 2);
        update();
        cam.position.set(player.x, player.y, 0);
        cam.update();

//        int currentTilex = (int) player.x / 16/5;
//        int currentTiley = (int) player.y / 16/5;
//
//        if(collisionLayer.getCell(currentTilex,currentTiley)==null){
//            System.out.println(currentTilex+" "+currentTiley);
//        }
//        else{
//            System.out.println("in bounds");
//        }


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        tileMapRenderer.setView(cam);
        tileMapRenderer.render();

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        //batch.draw(mapTexture, 0, 0);
        //hindernis.render(cam);
        //hindernis.render(cam);
        //playerAnimator.render(batch);
        player.renderAnimation(batch);
        batch.end();

        player.render(cam);
    }

    public void update() {
        handleInput();
    }

    private void handleInput() {
        int x = 0;
        int y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //cam.translate(-3, 0, 0);
            //player.move(-VELOCITY,0);
            x = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //cam.translate(3, 0, 0);
            //player.move(VELOCITY,0);
            x = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //cam.translate(0, -3, 0);
            //player.move(0,-VELOCITY);
            y = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //cam.translate(0, 3, 0);
            //player.move(0,VELOCITY);
            y = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        player.moveDirection(x, y);


        //cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 500 / cam.viewportWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
        //cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        //cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }

    @Override
    public void dispose() {
        batch.dispose();
        map = null;
    }
}
