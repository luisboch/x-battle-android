package br.com.luis.apps.simple2dgame;

import android.graphics.Bitmap;

import org.jphysics.ObjectController;
import org.jphysics.api.ControllableObject;
import org.jphysics.api.GameObjectImpl;
import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.util.SpriteSheet;

/**
 * Created by luis on 6/24/17.
 */

public class SimpleGameViewObject extends GameObjectImpl implements ControllableObject, GameViewObject {

    private final Bitmap bitmap;
    private boolean isScaled;
    private ObjectController controller = new ObjectController();
    private SpriteSheet animation = new SpriteSheet(1, 1);

    public SimpleGameViewObject(Bitmap bitmap, boolean isScaled, float radio, float mass) {
        super(radio, mass);
        this.bitmap = bitmap;
        this.isScaled = isScaled;
    }

    public SimpleGameViewObject(Bitmap bitmap, boolean isScaled, float radio, float mass, Vector2f scale) {
        super(radio, mass, scale);
        this.bitmap = bitmap;
        this.isScaled = isScaled;
    }

    public SimpleGameViewObject(Bitmap bitmap, boolean isScaled, float radio, float mass, float maxVel, Vector2f scale) {
        super(radio, mass, maxVel, scale);
        this.bitmap = bitmap;
        this.isScaled = isScaled;
    }

    @Override
    public void setAnimation(SpriteSheet animation) {
        this.animation = animation;
    }

    @Override
    public SpriteSheet getAnimation() {
        return animation;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public boolean isScaled() {
        return isScaled;
    }

    @Override
    public ObjectController getController() {
        return controller;
    }

    @Override
    public void setController(ObjectController controller) {
        this.controller = controller;
    }
}
