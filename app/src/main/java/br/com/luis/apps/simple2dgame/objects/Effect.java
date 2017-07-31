package br.com.luis.apps.simple2dgame.objects;

import android.graphics.Bitmap;

import org.jphysics.api.BasicObjectImpl;
import org.jphysics.api.Force;
import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.util.SpriteSheet;

/**
 * Created by luis on 6/30/17.
 */

public abstract class Effect implements Force {

    protected Bitmap bitmap;
    protected Vector2f position = new Vector2f();
    protected float radius;
    protected SpriteSheet sheet;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    public Effect setPosition(Vector2f position) {
        this.position.set(position);
        return this;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public <E extends BasicObjectImpl> boolean in(E... objs) {
        return false;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public SpriteSheet getSheet() {
        return sheet;
    }

    public void setSheet(SpriteSheet sheet) {
        this.sheet = sheet;
    }
}


