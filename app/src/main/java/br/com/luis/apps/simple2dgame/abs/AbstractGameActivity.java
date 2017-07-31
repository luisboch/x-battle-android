package br.com.luis.apps.simple2dgame.abs;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.jphysics.math.Vector2f;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.luis.apps.simple2dgame.Config;
import br.com.luis.apps.simple2dgame.util.AppData;
import br.com.luis.apps.simple2dgame.util.AssetUtil;

/**
 * Created by luis on 17/06/17.
 */

public abstract class AbstractGameActivity extends AppCompatActivity {
    protected final Random rnd = new Random();
    protected final Paint paint = new Paint();

    protected float viewWidth = 800f, viewHeight = 480f;
    private boolean showDebug = false;
    protected final Paint debugPaint = new Paint();
    protected final List<String> msgs = new ArrayList<>();
    private final Vector2f realScreenWidth = new Vector2f();
    protected final Vector2f screenSize = new Vector2f();
    private float zoom = 1f; // 100% at start

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AssetUtil.create(getAssets(), getApplicationContext());
        AppData.load();
        Config.setup();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        showDebug = Config.getOrDefault("app.debug", false);
        setContentView(new RenderView(this.getApplicationContext()));
    }


    public abstract void update(float deltaTime, Canvas canvas);

    public void setDebug(boolean showDebug) {
        this.showDebug = showDebug;
    }


    protected int generateRandomColor() {
        return Color.argb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private class RenderView extends View {

        private float fps = 30f;

        private float maxFPS = 60f;
        private float minFPS = 30f;
        private long startTime;
        private float timeForMaxFPS, elapsedTime;
        private boolean created = false;
        private Canvas renderCanvas;
        private Bitmap screenBitmap;

        public RenderView(Context context) {

            super(context);
            startTime = 0;
            timeForMaxFPS = 1000f / maxFPS;
            debugPaint.setColor(Color.RED);
            debugPaint.setTextSize(30f);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            realScreenWidth.set(canvas.getWidth(), canvas.getHeight());
            screenSize.set(viewWidth, viewHeight);

            if (!created) {
                startTime = System.currentTimeMillis();
                AbstractGameActivity.this.create();
                created = true;
            }
            super.onDraw(canvas);

            msgs.clear();// Clear last msgs

            float deltaTime = (float) (System.currentTimeMillis() - startTime) / 1000f;
            startTime = System.currentTimeMillis(); // reset to next calculation

            elapsedTime += deltaTime;
            fps = 1000 / (1000 * deltaTime);

            canvas.drawRGB(0, 0, 0);

            //1280 x 768
            final DisplayMetrics d = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(d);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                screenBitmap = Bitmap.createBitmap(d, (int) viewWidth, (int) viewHeight, Bitmap.Config.RGB_565);
            } else {
                screenBitmap = Bitmap.createBitmap((int) viewWidth, (int) viewHeight, Bitmap.Config.RGB_565);
            }

            renderCanvas = new Canvas(screenBitmap);

            final float fakeAspect = (float) renderCanvas.getWidth() / (float) renderCanvas.getHeight();

            AbstractGameActivity.this.update(deltaTime, renderCanvas);

            Rect dst = new Rect();

            dst.set(0, 0, canvas.getWidth(), (int) (((float) canvas.getWidth()) / fakeAspect));

            canvas.drawBitmap(screenBitmap, null, dst, null);

            if (fps > maxFPS) {
                Log.i("FPS", "FPS reached");
            }

            if (showDebug) {

                float yIdxForText = 15f;
                final float increment = 30f;

                canvas.drawText("FPS: " + fps, 10f, yIdxForText += increment, debugPaint);
                canvas.drawText("Delta: " + deltaTime, 10f, yIdxForText += increment, debugPaint);
//                canvas.drawText("Time for MaxFPS: " + timeForMaxFPS, 10f, yIdxForText += increment, debugPaint);

                for (String msg : msgs) {
                    canvas.drawText(msg, 10f, yIdxForText += increment, debugPaint);
                }


            }
//
//
//            if (fps < minFPS) {
//                //  What we will do when performance is poor?
//            } else if (fps > maxFPS) {
//                try {
//                    long diff = (long) (timeForMaxFPS - deltaTime);
////                    Log.i("ROOT", "DIFFF: " + diff);
////                    Thread.sleep(diff);
//                    /// Cant sleep android prcesso (why?)
//                    SystemClock.sleep(diff);
//                } catch (Throwable e) {
////                    Log.i("ROOT", "Erro: " + e.getMessage());
//                }
//            }

            invalidate();
            //Log.i(System.nanoTime())
        }
    }

    // Optional
    protected void create() {
    }

}
