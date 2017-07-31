package br.com.luis.apps.simple2dgame.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import org.jphysics.math.Vector2f;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * Created by luis on 02/07/17.
 */

public class ImageUtil {


    public static Bitmap createSpace(Vector2f size, int starNumber) {
        return createSpace(new Vector2(size.x, size.y), starNumber);
    }

    public static Bitmap createSpace(Vector2 size, int starNumber) {

        Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.RGB_565);

        Random rnd = new Random();
        for (int i = 0; i < starNumber; i++) {
            bitmap.setPixel(rnd.nextInt(size.x), rnd.nextInt(size.y), Color.WHITE);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 15, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
//        return bitmap;

    }
}
