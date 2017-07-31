package br.com.luis.apps.simple2dgame.objects;

import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.Config;
import br.com.luis.apps.simple2dgame.SimpleGameViewObject;
import br.com.luis.apps.simple2dgame.util.SpriteSheet;
import br.com.luis.apps.simple2dgame.util.AssetUtil;

/**
 * Created by luis on 6/29/17.
 */

public class Asteroid1 extends SimpleGameViewObject implements Asteroid{


    public Asteroid1() {
        super(AssetUtil.getInstance().getBitmap("sprites/asteroid_01_no_moblur.png"),
                false,
                Config.getOrDefault("asteroid.radio", 10f),
                50f, 300f, new Vector2f(3f));// 15m with 15 ton 40f mps;

        setDirection(Vector2f.fromAngle(0f));
        setAnimation(new SpriteSheet(128, 128));
        getAnimation().setBitmap(getBitmap());
        getAnimation().addDefault(new SpriteSheet.Mapping(SpriteSheet.generateIndex(0, 32), true, false, 15f));

        health = Config.getOrDefault("asteroid.max_life", 3);
        initHealth = health;
    }

}

