package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.enemy.Enemy;
import io.github.some_example_name.enemy.EnemyAnimator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;


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
    private Enemy enemy;
    private EnemyAnimator enemyAnimator;
    private TreeSet<Entity> entities = new TreeSet<>(new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            if (o1.y>o2.y)
                return -1;
            else if (o1.y<o2.y)
                return 1;
            else
                return 0;
        }
    });

    @Override
    public void create() {
        batch = new SpriteBatch();
        mapTexture = new Texture("island.jpg");

        player = new Player();

        cam = new OrthographicCamera();
        screenViewport = new ScreenViewport(cam);


        playerAnimator = new PlayerAnimator();
        playerAnimator.create();
        player.setPlayerAnimator(playerAnimator);


        FileHandleResolver resolver = new InternalFileHandleResolver();
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        TmxMapLoader loader = new TmxMapLoader(resolver);
        tiledMap = loader.load("tileMap2/FabianStuff.tmx", params);


        tileMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,5f);
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");

        player.setTiledMapTileLayer(collisionLayer);

        enemy = new Enemy();
        enemyAnimator = new EnemyAnimator();
        enemyAnimator.create();
        enemy.setEnemyAnimator(enemyAnimator);

        entities.add(player);
        entities.add(enemy);

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

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        tileMapRenderer.setView(cam);
        tileMapRenderer.render();

        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        entities.remove(player);
        entities.remove(enemy);
        entities.add(player);
        entities.add(enemy);
        for (Entity e : entities){
            e.renderAnimation(batch);
        }

        //playerAnimator.render(batch);
        //player.renderAnimation(batch);
        //enemy.renderAnimation(batch);
        batch.end();

        //Shaperenders
        player.render(cam);
        enemy.render(cam);
    }

    public void update() {
        handleInput();
        if (!player.hitbox.overlaps(enemy.hitbox))
            enemy.moveToPlayer(player.x,player.y);

        //System.out.println(player.hitbox.overlaps(enemy.hitbox));
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
