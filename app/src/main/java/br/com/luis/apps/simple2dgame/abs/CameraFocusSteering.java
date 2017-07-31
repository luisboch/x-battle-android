package br.com.luis.apps.simple2dgame.abs;

import org.jphysics.ObjectController;
import org.jphysics.math.MathUtils;
import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.Camera;
import br.com.luis.apps.simple2dgame.Config;
import br.com.luis.apps.simple2dgame.SimpleGameViewObject;

/**
 * Created by luis on 6/25/17.
 */

public class CameraFocusSteering {

    private SimpleGameViewObject target;
    private Camera from;
    private ObjectController controller;

    private final float frontView;
    private final float moveVelocity;
    private boolean attached = false;

    public CameraFocusSteering() {
        frontView = Config.getOrDefault("camera.target.focus_front_view", 100f);
        moveVelocity = Config.getOrDefault("camera.move_velocity", 100f);
    }

    public CameraFocusSteering target(SimpleGameViewObject target) {

        attached = this.target == target && attached;

        this.target = target;

        if (target != null) {
            this.controller = target.getController();
        } else {
            this.controller = null;
        }


        return this;
    }

    public SimpleGameViewObject getTarget() {
        return target;
    }

    public CameraFocusSteering from(Camera from) {
        this.from = from;
        return this;
    }

    public void calculate(float deltaTime) {

        Vector2f result = new Vector2f(0f, 0f);

        if (this.target == null || from == null) {
            return;
        }

        final Vector2f targetPos = new Vector2f(target.getPosition());
        final float currVelocity = ((Float)(target.getVelocity().length())).isNaN() ? 0f : target.getVelocity().length();

        if (controller != null && currVelocity > 50f) {
            from.zoomOut();
            targetPos.add(new Vector2f(target.getDirection()).normalize().mul(frontView));
        } else {
            from.restoreZoom();
        }

        final Vector2f toTarget = new Vector2f(targetPos).sub(from.getPosition()).add(target.getVelocity());
        final float currDistance = toTarget.length();

        if (currDistance > moveVelocity) {
            result.set(toTarget).setLength(moveVelocity);
        } else {
            result.set(toTarget).setLength(currDistance);
        }

        result.mul(deltaTime);

        from.setPosition(from.getPosition().add(result));

        // Direction angle (degrees)
        float angle = target.getDirection().angle();
        final float camAngle = from.getDirection().angle();
        final float angleDiff = Math.abs(angle - camAngle);

//        Log.i("CAM DIR", "" + camAngle + ", TGT ANGLE: " + angle);
        if (angleDiff > 0f) {
//            Log.i("ANGLE DIFF ", angleDiff + "");
            if (angleDiff > 1f) {
                angle = (angle - camAngle) * 0.5f * deltaTime;
                Vector2f direction = from.getDirection().rotateRad(angle * MathUtils.degreesToRadians);
                from.setDirection(direction);
            } else {
                from.setDirection(Vector2f.fromAngle(angle));
            }

        }
    }

    public boolean isAttached() {
        return attached;
    }
}
