package br.com.luis.apps.simple2dgame.objects;

import android.graphics.Bitmap;

import org.jphysics.math.Vector2f;

/**
 * Created by luis on 6/25/17.
 */

public class Star {

    private Bitmap bitmap;
    private Vector2f position = new Vector2f();
    private float scale = 1;

    public Star(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Vector2f getPosition() {
        return position;
    }
}
