package br.com.luis.apps.simple2dgame.objects;

import android.graphics.Bitmap;

import org.jphysics.api.GameObjectImpl;
import org.jphysics.projectile.Shot;
import org.jphysics.steering.ProjectileSteering;
import org.jphysics.steering.Steering;

import br.com.luis.apps.simple2dgame.Config;
import br.com.luis.apps.simple2dgame.GameViewObject;
import br.com.luis.apps.simple2dgame.SimpleGameViewObject;
import br.com.luis.apps.simple2dgame.util.AssetUtil;
import br.com.luis.apps.simple2dgame.util.SpriteSheet;

/**
 * Created by luis on 6/29/17.
 */

public class Fire extends Shot implements GameViewObject {

    private final Bitmap bitmap;
    private boolean isScaled;
    private SpriteSheet spriteSheet = new SpriteSheet(1, 1);

    public Fire(SimpleGameViewObject from) {
        this(new ProjectileSteering(from), from, null, 0);
    }

    private Fire(Steering steering, GameObjectImpl from, GameObjectImpl target, int minProximity) {
        super(steering, from, target, minProximity);
        this.bitmap = AssetUtil.getInstance().getBitmap("fire.png");
        this.isScaled = true;
        this.explosionForce = 150f;
        this.explosionRadius = 15f;
        this.health = 1;
        this.lifeTime = Config.getOrDefault("fire.time", 15f );
        this.initialVelocity = 700f;
    }

    @Override
    public void setAnimation(SpriteSheet animation) {
        spriteSheet = animation;
    }

    @Override
    public SpriteSheet getAnimation() {
        return spriteSheet;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public boolean isScaled() {
        return isScaled;
    }

}