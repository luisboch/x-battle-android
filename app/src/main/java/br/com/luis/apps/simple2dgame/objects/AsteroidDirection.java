package br.com.luis.apps.simple2dgame.objects;

import android.graphics.Bitmap;
import android.graphics.Color;

import br.com.luis.apps.simple2dgame.util.AssetUtil;

/**
 * Just wrapper to directions bitmaps
 * Created by luis on 08/07/17.
 */

public class AsteroidDirection {

    private Bitmap closest;
    private Bitmap farFarAway;
    private Bitmap farAway;


    public AsteroidDirection() {
        closest = AssetUtil.getInstance().getBitmap("sprites/focus.png", 0.3f);
        farAway = AssetUtil.getInstance().getBitmap("sprites/focus-far.png", 0.3f);
        farFarAway = AssetUtil.getInstance().getBitmap("sprites/focus-far-far.png", 0.3f);
    }

    public Bitmap getBitmap(float distance) {
        if (distance > 30000) {
            return farFarAway;
        } else if (distance > 15000) {
            return farAway;
        } else {
            return closest;
        }
    }


}
