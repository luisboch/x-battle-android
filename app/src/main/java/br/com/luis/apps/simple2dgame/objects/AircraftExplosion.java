package br.com.luis.apps.simple2dgame.objects;

import android.graphics.Bitmap;

import org.jphysics.Engine;
import org.jphysics.api.Force;

import br.com.luis.apps.simple2dgame.Config;
import br.com.luis.apps.simple2dgame.util.AssetUtil;
import br.com.luis.apps.simple2dgame.util.SpriteSheet;

/**
 * Created by luis on 04/07/17.
 */

public class AircraftExplosion extends Effect implements Force {

    private float timeToDie = Config.getOrDefault("aircraft.explosion.time", 3f);

    public AircraftExplosion() {

        final Bitmap bitmap = AssetUtil.getInstance().getBitmap("sprites/fireExplosion.png", 3f);
        sheet = new SpriteSheet(192, 192);
        sheet.setBitmap(bitmap);
        sheet.addDefault(new SpriteSheet.Mapping(SpriteSheet.generateIndex(0, 25), false, false, 25 / timeToDie));
        setRadius(100);

    }

    @Override
    public void update(float deltaTime) {
        timeToDie -= deltaTime;
    }

    @Override
    public float getMass() {
        return 0;
    }

    @Override
    public boolean isAlive() {
        return timeToDie > 0f;
    }
}
