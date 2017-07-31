package br.com.luis.apps.simple2dgame.abs;

import org.jphysics.Engine;
import org.jphysics.ObjectController;
import org.jphysics.api.ControllableObject;
import org.jphysics.api.ControllerResolver;
import org.jphysics.math.MathUtils;
import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.Config;

/**
 * Created by luis on 6/30/17.
 */

public class Controller implements ControllerResolver {
    private final float accelStep;
    private final float rotateStep;
    private final float deAccelStep;

    public Controller() {
        accelStep = Config.getOrDefault("aircraft.acceleration", 10f);
        deAccelStep = Config.getOrDefault("aircraft.deceleration", 10f);
        rotateStep = Config.getOrDefault("aircraft.rotation", 10f);
    }


    @Override
    public Vector2f calculate(Engine engine, ControllableObject aircraft, ObjectController controller) {
        float delta = engine.getDeltaTime();
        final Vector2f dir = new Vector2f();

        if (controller.isUp()) {
            dir.set(new Vector2f(aircraft.getDirection()));
            dir.setLength(accelStep);
        } else if (aircraft.getController().isDown()) {
            dir.set(new Vector2f(aircraft.getVelocity()));
            dir.normalize().mul(-1).mul(deAccelStep);
        }

        if (controller.isLeft()) {
            aircraft.setDirection(aircraft.getDirection().rotateRad(delta * -rotateStep * MathUtils.degreesToRadians));
        } else if (controller.isRight()) {
            aircraft.setDirection(aircraft.getDirection().rotateRad(delta * rotateStep * MathUtils.degreesToRadians));
        }

        return dir;
    }

}
