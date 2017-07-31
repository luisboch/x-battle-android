package br.com.luis.apps.simple2dgame.objects;

import android.graphics.Bitmap;

import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.Config;
import br.com.luis.apps.simple2dgame.SimpleGameViewObject;
import br.com.luis.apps.simple2dgame.util.AssetUtil;

/**
 * Created by luis on 6/24/17.
 */

public class AircraftObject extends SimpleGameViewObject {
    private Bitmap withAccel;

    public AircraftObject() {
        super(AssetUtil.getInstance().getBitmap("sprites/aircraft.png"), true, 80f, 40f, 300f, new Vector2f(1f, 1f));// 15m with 15 ton 40f mps;
        setDirection(Vector2f.fromAngle(270));
        setVelocity(new Vector2f(0.0001f, 0.0001f));
        setHealth(2);
        setPosition(new Vector2f(Config.getOrDefault("aircraft.pos.x", 10000f), Config.getOrDefault("aircraft.pos.y", 10000f)));
        withAccel = AssetUtil.getInstance().getBitmap("sprites/aircraft-2.png");
    }

    @Override
    public Bitmap getBitmap() {

        if (getController() != null && getController().isUp()) {
            return withAccel;
        }

        return super.getBitmap();
    }
}
