package br.com.luis.apps.simple2dgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import org.joml.Matrix3f;
import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.util.SpriteSheet;

/**
 * Created by luis on 6/30/17.
 */

public interface GameViewObject extends PhysicObject{
    void setAnimation(SpriteSheet animation);

    SpriteSheet getAnimation();

    Bitmap getBitmap();

    boolean isScaled();

}
