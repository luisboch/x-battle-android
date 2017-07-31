package br.com.luis.apps.simple2dgame.abs;

import org.jphysics.Engine;
import org.jphysics.api.PhysicObject;
import org.jphysics.math.MathUtils;
import org.jphysics.math.Vector2f;

import java.util.List;
import java.util.Random;

import br.com.luis.apps.simple2dgame.GameActivity;
import br.com.luis.apps.simple2dgame.objects.AircraftExplosion;
import br.com.luis.apps.simple2dgame.objects.AircraftObject;
import br.com.luis.apps.simple2dgame.objects.Planet;
import br.com.luis.apps.simple2dgame.util.AppData;
import br.com.luis.apps.simple2dgame.objects.Asteroid;
import br.com.luis.apps.simple2dgame.objects.Asteroid1;
import br.com.luis.apps.simple2dgame.objects.Fire;
import br.com.luis.apps.simple2dgame.util.SoundManager;

/**
 * Created by luis on 6/29/17.
 */

public class ContactListener implements org.jphysics.api.ContactListener {

    final Engine engine;
    final Random random = new Random();
    final GameActivity activity;
    final SoundManager soundManager;

    public ContactListener(Engine engine, GameActivity activity, SoundManager soundManager) {
        this.engine = engine;
        this.activity = activity;
        this.soundManager = soundManager;
    }

    @Override
    public void contact(PhysicObject physicObject, PhysicObject physicObject1, List<PhysicObject> list) {
        final Fire fire;
        PhysicObject asteroid;
        final Planet planet;
        final AircraftObject aircraft;

        if ((physicObject instanceof Fire) || (physicObject1 instanceof Fire)) {
            fire = (Fire) ((physicObject instanceof Fire) ? physicObject : physicObject1);
        } else {
            fire = null;
        }

        if ((physicObject instanceof Asteroid) || (physicObject1 instanceof Asteroid)) {
            asteroid = (physicObject instanceof Asteroid) ? physicObject : physicObject1;
        } else {
            asteroid = null;
        }

        if ((physicObject instanceof Planet) || (physicObject1 instanceof Planet)) {
            planet = (Planet) ((physicObject instanceof Planet) ? physicObject : physicObject1);
        } else {
            planet = null;
        }

        if ((physicObject instanceof AircraftObject) || (physicObject1 instanceof AircraftObject)) {
            aircraft = (AircraftObject) ((physicObject instanceof AircraftObject) ? physicObject : physicObject1);
        } else {
            aircraft = null;
        }

        if (fire != null && asteroid != null) {

            fire.decreaseLife();
            asteroid.decreaseLife();
            soundManager.playEffect(SoundManager.LOW_EXPLOSION);

            if (!asteroid.isAlive()) {
                AppData.getInstance().getCurrentScore().increase();
                // Add 2 new asteroids
                PhysicObject created = new Asteroid1();
                created.setPosition(new Vector2f(fire.getPosition()).add(new Vector2f(random.nextInt(15000)+5000, random.nextInt(15000)+5000).rotateRad(random.nextInt(360) * MathUtils.degreesToRadians)));
                created.setVelocity(Vector2f.fromAngle(random.nextInt(360)).setLength(random.nextInt(50)));
                created.setDirection(new Vector2f(created.getVelocity()).normalize());

                engine.add(created);

                created = new Asteroid1();
                created.setPosition(new Vector2f(fire.getPosition()).add(new Vector2f(random.nextInt(15000)+5000, random.nextInt(15000)+5000)).rotateRad(random.nextInt(360) * MathUtils.degreesToRadians));
                created.setVelocity(Vector2f.fromAngle(random.nextInt(360)).setLength(random.nextInt(50)));
                created.setDirection(new Vector2f(created.getVelocity()).normalize());

                engine.add(created);

                // Add explosions
                createExplosions(asteroid, fire);
            }


        } else if (fire != null && planet != null) {
            ;
            fire.decreaseLife();
            soundManager.playEffect(SoundManager.LOW_EXPLOSION);
        } else if (aircraft != null && (planet != null || asteroid != null)) {// ignore fire
            aircraft.decreaseLife();

            PhysicObject other = asteroid == null ? planet : asteroid;
            if (!aircraft.isAlive()) {
                createExplosions(aircraft, other);

                engine.addAction(new Runnable() {
                    @Override
                    public void run() {
                        AppData.getInstance().registerCurrent();
                        activity.goBack();
                    }
                }, 5000);
            }
        }
    }

    private void createExplosions(final PhysicObject dead, final PhysicObject other) {
        final Vector2f direction = other.getPosition().sub(dead.getPosition()).normalize();
        final Vector2f pos = new Vector2f(dead.getPosition());

        AircraftExplosion ex = new AircraftExplosion();
        ex.setPosition(new Vector2f(pos).add(new Vector2f(direction).mul(dead.getRadius())));

        soundManager.playEffect(SoundManager.HIGTH_EXPLOSION);
        engine.addForce(ex);

        engine.addAction(new Runnable() {
            @Override
            public void run() {
                AircraftExplosion ex1 = new AircraftExplosion();
                ex1.setPosition(pos);
                soundManager.playEffect(SoundManager.HIGTH_EXPLOSION);
                engine.addForce(ex1);
                AircraftExplosion ex2 = new AircraftExplosion();
                ex2.setPosition(new Vector2f(pos).sub(dead.getDirection().mul(dead.getRadius())));
                engine.addForce(ex2);
            }
        }, 700);

        engine.addAction(new Runnable() {
            @Override
            public void run() {
                AircraftExplosion ex1 = new AircraftExplosion();
                ex1.setPosition(new Vector2f(pos).add(dead.getDirection().rotateRad(45f * MathUtils.degreesToRadians).mul(dead.getRadius())));
                engine.addForce(ex1);
                soundManager.playEffect(SoundManager.HIGTH_EXPLOSION);
            }
        }, 300);

        engine.addAction(new Runnable() {
            @Override
            public void run() {
                AircraftExplosion ex2 = new AircraftExplosion();
                ex2.setPosition(new Vector2f(dead.getPosition()).sub(dead.getDirection().mul(dead.getRadius() * 0.5f)));
                engine.addForce(ex2);
                soundManager.playEffect(SoundManager.HIGTH_EXPLOSION);
            }
        }, 1000);

    }
}
