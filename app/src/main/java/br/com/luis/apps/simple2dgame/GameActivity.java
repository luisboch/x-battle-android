package br.com.luis.apps.simple2dgame;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.jphysics.Engine;
import org.jphysics.ObjectController;
import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;

import java.util.ArrayList;

import br.com.luis.apps.simple2dgame.abs.AbstractGameActivity;
import br.com.luis.apps.simple2dgame.abs.ContactListener;
import br.com.luis.apps.simple2dgame.abs.Controller;
import br.com.luis.apps.simple2dgame.abs.ExplosionResolver;
import br.com.luis.apps.simple2dgame.abs.ProjectileResolver;
import br.com.luis.apps.simple2dgame.abs.TouchEventManager;
import br.com.luis.apps.simple2dgame.objects.Asteroid;
import br.com.luis.apps.simple2dgame.objects.Asteroid1;
import br.com.luis.apps.simple2dgame.objects.Fire;
import br.com.luis.apps.simple2dgame.objects.Planet1;
import br.com.luis.apps.simple2dgame.objects.AircraftObject;
import br.com.luis.apps.simple2dgame.util.AppData;
import br.com.luis.apps.simple2dgame.util.ImageUtil;

import br.com.luis.apps.simple2dgame.util.SoundManager;

/**
 * Created by luis on 6/24/17.
 */
public class GameActivity extends AbstractGameActivity {
    private static final String APP_CONTEXT = "CTX_".concat(GameActivity.class.getCanonicalName());
    private Engine engine;
    protected Camera camera;

    // controlled objects;
    protected AircraftObject aircraft;
    protected TouchEventManager touchEventManager;
    private SoundManager soundManager = new SoundManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        engine = new Engine(100000f, 100000f);
        engine.setContactListener(new ContactListener(engine, this, soundManager));
        engine.setProjectileResolver(new ProjectileResolver(soundManager));
        engine.setControllerResolver(new Controller());
        engine.setExplosionResolver(new ExplosionResolver());

        camera = new Camera(engine);
        aircraft = new AircraftObject();
        engine.add(aircraft);

        for (int i = 1; i < 13; i++) {
            Planet1 planet1 = new Planet1(rnd.nextInt(20)+5);
            planet1.setPosition(new Vector2f(Config.getOrDefault("planet" + i + ".pos.x", 700f), Config.getOrDefault("planet" + i + ".pos.y", 700f)));

            engine.add(planet1);
            engine.addForce(planet1);
        }
        Asteroid asteroid = new Asteroid1();
        asteroid.setPosition(new Vector2f(50500f, 50500f));

        engine.add(asteroid);

        camera.focus(aircraft);
        touchEventManager = new TouchEventManager(aircraft.getController(), engine, camera);


        // cache sounds
        soundManager.addEffect(SoundManager.FIRE, "aircraft-laser.mp3");
        soundManager.addEffect(SoundManager.HIGTH_EXPLOSION, "explosion-long.mp3");
        soundManager.addEffect(SoundManager.LOW_EXPLOSION, "explosion-short.mp3");
        soundManager.play("music-loop.mp3", true);
    }

    @Override
    protected void create() {
        super.create();
        camera.setBackgroundBitmap(ImageUtil.createSpace(screenSize, 3000));
    }

    @Override
    public void update(float deltaTime, Canvas canvas) {
        touchEventManager.setScreenWidth(screenSize.x);
        engine.calculate(deltaTime);

        if (deltaTime < 1f) {
            ObjectController controller = aircraft.getController();
            new ArrayList<Class<? extends PhysicObject>>();
            PhysicObject asteroid = engine.getClosestActor(aircraft.getPosition(), engine.getWidth(), Asteroid.class);
            final Vector2f position = aircraft.getPosition();
            msgs.add("UP: " + controller.isUp() + ", DW: " + controller.isDown() + ", LF: " + controller.isLeft() + ", RG: " + controller.isRight());
            msgs.add("Aircraft pos: " + aircraft.getPosition() + ", Camera: " + camera.getPosition());
            msgs.add("Aircraft vel: " + aircraft.getVelocity());
            msgs.add("Asteroid pos: " + (asteroid != null ? asteroid.getPosition().toString() : ""));
            msgs.add("CurrentPoints: " + AppData.getInstance().getCurrentScore().getPoints());
            if (controller.isAction1()) {
                final org.jphysics.ProjectileResolver resolver = engine.getProjectileResolver();
                if (resolver.canCreateProjectile(Fire.class, controller.getActor())) {
                    engine.add(resolver.create(controller.getActor(), Fire.class));
                }
            }

        }

        camera.setCanvas(canvas);
        camera.update(deltaTime);
        camera.draw(engine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        touchEventManager.onEvent(ev);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveGameState();
            goBack();
        }
        touchEventManager.onKeyUp(keyCode, event);
        return true;
    }

    private void saveGameState() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        touchEventManager.onKeyDown(keyCode, event);
        return true;
    }

    public void goBack() {
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            soundManager.stop();
        } else {
            soundManager.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.resume();
    }


}
