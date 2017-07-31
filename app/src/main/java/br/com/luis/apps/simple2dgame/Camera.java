package br.com.luis.apps.simple2dgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import org.jphysics.Engine;
import org.jphysics.api.BasicObjectImpl;
import org.jphysics.api.Force;
import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;

import java.util.List;

import br.com.luis.apps.simple2dgame.abs.CameraFocusSteering;
import br.com.luis.apps.simple2dgame.objects.Asteroid1;
import br.com.luis.apps.simple2dgame.objects.AsteroidDirection;
import br.com.luis.apps.simple2dgame.objects.Effect;
import br.com.luis.apps.simple2dgame.util.TextureBitmap;
import br.com.luis.apps.simple2dgame.util.Vector2;

/**
 * Created by luis on 6/24/17.
 */
public class Camera implements PhysicObject {

    private Vector2f position = new Vector2f(-100, -200);
    private Vector2f viewSize = new Vector2f(1280f, 768f);

    private Vector2f direction = Vector2f.fromAngle(270f); // Looking to up
    private Vector2f velocity = new Vector2f(0.001f, 0.001f);

    private float zoom = 1f;
    private boolean zoomChanged = false;

    private final float MAX_ZOOM;
    private final float MIN_ZOOM;
    private final float ZOOM_SCALE_CHANGE;
    private Canvas canvas;

    private Vector2f viewSizeScaled;
    private TextureBitmap texture;

    private SimpleGameViewObject focusViewObject;
    private CameraFocusSteering focusSteering = new CameraFocusSteering();
    private AsteroidDirection directionHelper = new AsteroidDirection();

    private float viewScope;

    protected final Paint debugRedPaint = new Paint();
    protected final Paint debugYellowPaint = new Paint();

    private boolean showDebug;
    private final Engine engine;

    public Camera(Engine engine) {
        this.engine = engine;
        debugRedPaint.setColor(Color.RED);
        debugRedPaint.setTextSize(15f);
        debugYellowPaint.setColor(Color.YELLOW);
        debugYellowPaint.setStrokeWidth(3f);
        focusSteering.from(this);

        ZOOM_SCALE_CHANGE = Config.getOrDefault("camera.zoom_scale_change", 0.001f);
        MIN_ZOOM = Config.getOrDefault("camera.min_zoom", 0.1f);
        MAX_ZOOM = Config.getOrDefault("camera.max_zoom", 3f);

        showDebug = Config.getOrDefault("app.debug", false);

        setPosition(new Vector2f(Config.getOrDefault("camera.pos.x", 0f), Config.getOrDefault("camera.pos.y", 0f)));

    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public float getMass() {
        return 0;
    }

    @Override
    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    @Override
    public Vector2f getVelocity() {
        return new Vector2f(velocity);
    }

    @Override
    public Vector2f getDirection() {
        return new Vector2f(direction);
    }

    @Override
    public Vector2f getScale() {
        return null;
    }

    @Override
    public float getMaxVelocity() {
        return 1000;
    }

    @Override
    public float getRadius() {
        return getViewSizeWithZoom().x * 2f;
    }

    @Override
    public List<PhysicObject> getChildren() {
        return null;
    }

    @Override
    public PhysicObject getParent() {
        return null;
    }

    @Override
    public PhysicObject setParent(PhysicObject physicObject) {
        return null;
    }

    @Override
    public Camera setVelocity(Vector2f vector2f) {
        velocity.set(vector2f);
        return this;
    }

    public Vector2f getViewSizeWithZoom() {
        return new Vector2f(this.viewSize).mul(zoom);
    }

    public Vector2f getViewSize() {
        return zoomChanged ? new Vector2f(this.viewSize).mul(zoom) : new Vector2f(this.viewSize); // scaled size
    }

    @Override
    public Camera setPosition(Vector2f position) {
        this.position = position;
        return this;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    public Camera zoomOut() {
        return zoomOut(ZOOM_SCALE_CHANGE);
    }

    public Camera zoomOut(float scale) {

        zoom += scale;
        zoom = Math.min(MAX_ZOOM, zoom);
        zoomChanged = true;
        return this;
    }

    public Camera setZoom(float newZoom) {

        this.zoom = Math.max(Math.min(newZoom, MAX_ZOOM), MIN_ZOOM);

        if (Math.abs(newZoom - 1f) < 0.001f) {
            this.zoom = 1f;
            zoomChanged = false;
        } else {
            zoomChanged = true;
        }
        return this;
    }

    public Camera zoomIn() {
        return zoomIn(ZOOM_SCALE_CHANGE);
    }

    public Camera zoomIn(float scale) {
        zoom -= scale;
        zoom = Math.max(MIN_ZOOM, zoom);
        zoomChanged = true;
        return this;
    }

    public Camera restoreZoom() {

        if (!zoomChanged) {
            return this;
        }

        zoom += (zoom > 1) ? -ZOOM_SCALE_CHANGE : ZOOM_SCALE_CHANGE;

        if (Math.abs(zoom - 1f) < 0.001f) {
            zoom = 1f;
            zoomChanged = false;
        }

        return this;
    }

    public void setViewSize(Vector2f viewSize) {
        this.viewSize = viewSize;
    }

    public float getViewScope() {
        return viewScope;
    }


    @Override
    public void update(float secs) {
        // Must calculate the camera pos before viewSize.
        if (focusViewObject != null) {
            updateFollow(secs);
        }
    }

    public void draw(Engine engine) {

        final Vector2f viewSize = getViewSize();
        viewSizeScaled = new Vector2f(canvas.getWidth() / viewSize.x, canvas.getHeight() / viewSize.y);
        viewScope = Math.max(viewSize.x, viewSize.y) * 1.3f; // Max view + 30%

        drawBackground(canvas);

        // canvas 1280
        // view 240
        // center do canvas 1280 / 2
        // center da view 240 / 2
        Vector2f viewCorrection = new Vector2f(canvas.getWidth(), canvas.getHeight()).mul(0.5f);
        final float zoomCorrection = canvas.getWidth() / viewSize.x;

        // Como pegamos os atores da engine, usamos a posicao da camera em relacao à engine
        for (PhysicObject physicObject : engine.getVisibleActors(getPosition(), viewSize.x * 1.5f)) {
            draw(canvas, physicObject, viewCorrection, engine.getDeltaTime(), zoomCorrection);
        }

        for (Force force : engine.getVisibleForces(getPosition(), viewSize.x * 1.5f)) {
            draw(canvas, force, viewCorrection, engine.getDeltaTime(), zoomCorrection);
        }

        drawDirection(canvas, viewCorrection, engine.getDeltaTime(), zoomCorrection);


//        drawShadow(canvas, cameraPos, viewSize);
    }

    private void drawDirection(Canvas canvas, Vector2f viewCenter, float delta, float zoomCorrection) {


        // Vamos escrever agora, a direção de todos os asteroids
        for (PhysicObject as : engine.getActorsByType(Asteroid1.class)) {
            final Vector2f dir = focusViewObject.getPosition().sub(as.getPosition()).negate();

            final Vector2f obPos = new Vector2f(focusViewObject.getPosition()).sub(getPosition()).mul(zoomCorrection).add(viewCenter); // Pega a posicao em relacao à engine
            //we will ignore objects that is too far away
            final Bitmap bitmap = directionHelper.getBitmap(dir.length());
            dir.normalize();

            Vector2f scale = new Vector2f(viewSizeScaled);
            Matrix matrix = new Matrix();
            Vector2f drawPos = new Vector2f(obPos).sub(bitmap.getWidth() * scale.x * 0.5f, bitmap.getHeight() * scale.y * 0.5f);

            matrix.setTranslate(drawPos.x, drawPos.y);

            matrix.preRotate(dir.angle(), bitmap.getWidth() * scale.x, bitmap.getHeight() * scale.y);
            matrix.preTranslate(100f, 0f);

            canvas.drawBitmap(bitmap, matrix, null);
        }
    }

    private void updateFollow(float delta) {
        focusSteering.calculate(delta);
    }

    public SimpleGameViewObject getFocus() {
        return focusViewObject;
    }

    private void draw(Canvas canvas, PhysicObject object, Vector2f viewCenter, float delta, float zoomCorrection) {

        if (object instanceof GameViewObject) {
            final GameViewObject viewObject = (GameViewObject) object;

            final Vector2f dir = viewObject.getDirection();

            Vector2f obPos = new Vector2f(viewObject.getPosition()).sub(getPosition()).mul(zoomCorrection).add(viewCenter); // Pega a posicao em relacao à engine
            //we will ignore objects that is too far away
            Bitmap bitmap;
            if (viewObject.getAnimation().hasMappings()) {
                bitmap = viewObject.getAnimation().getAnimation(delta);
            } else {
                bitmap = viewObject.getBitmap();
            }

            final Vector2f scale = (viewObject.isScaled() ? new Vector2f(1, 1) : viewObject.getScale()).mul(viewSizeScaled);

            Matrix matrix = new Matrix();
            Vector2f drawPos = new Vector2f(obPos).sub(bitmap.getWidth() * scale.x * 0.5f, bitmap.getHeight() * scale.y * 0.5f);

            matrix.preTranslate(drawPos.x, drawPos.y);
            matrix.preScale(scale.x, scale.y);
            matrix.preRotate(dir.angle(), bitmap.getWidth() * 0.5f, bitmap.getHeight() * 0.5f);

            canvas.drawBitmap(bitmap, matrix, null);

            if (showDebug) {
                debugRedPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(obPos.x, obPos.y, object.getRadius() * viewSizeScaled.x, debugRedPaint);

                Vector2f tgt;
                if (!dir.isZero()) {
                    tgt = Vector2f.fromAngle(dir.angle()).setLength(object.getRadius() * viewSizeScaled.x);
                } else {
                    tgt = Vector2f.fromAngle(0f).setLength(object.getRadius() * viewSizeScaled.x);
                }

                tgt = new Vector2f(obPos).add(tgt);
                canvas.drawLine(obPos.x, obPos.y, tgt.x, tgt.y, debugYellowPaint);
            }
        }

    }

    private void draw(Canvas canvas, Force object, Vector2f viewCenter, float delta, float zoomCorrection) {

        if (object instanceof Effect) {
            final Effect viewObject = (Effect) object;

            Vector2f obPos = new Vector2f(viewObject.getPosition()).sub(getPosition()).mul(zoomCorrection).add(viewCenter); // Pega a posicao em relacao à engine
            //we will ignore objects that is too far away
            Bitmap bitmap;

            if (viewObject.getSheet().hasMappings()) {
                bitmap = viewObject.getSheet().getAnimation(delta);
            } else {
                bitmap = viewObject.getBitmap();
            }

            final Vector2f scale = new Vector2f(viewSizeScaled);

            Matrix matrix = new Matrix();
            Vector2f drawPos = new Vector2f(obPos).sub(bitmap.getWidth() * scale.x * 0.5f, bitmap.getHeight() * scale.y * 0.5f);

            matrix.preTranslate(drawPos.x, drawPos.y);
            matrix.preScale(scale.x, scale.y);
            canvas.drawBitmap(bitmap, matrix, null);

            if (showDebug) {
                debugRedPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(obPos.x, obPos.y, object.getRadius() * viewSizeScaled.x, debugRedPaint);
            }
        }

    }

    private void drawBackground(Canvas canvas) {
        if (texture != null) {
            canvas.drawBitmap(texture.getBitmap(new Vector2(viewSize.x, viewSize.y), new Vector2(getPosition().x, getPosition().y)), 0, 0, null);
        }
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {

//        final SpriteSheet sheet = new SpriteSheet(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
//        sheet.setBitmap(backgroundBitmap);
//        sheet.addDefault(new SpriteSheet.Mapping(SpriteSheet.generateIndex(0,3), true, false, 1f, true));

        texture = new TextureBitmap(backgroundBitmap);
    }

    public Camera focus(SimpleGameViewObject focus) {
        focusViewObject = focus;
        focusSteering.target(focus);
        return this;
    }

    @Override
    public <E extends BasicObjectImpl> boolean in(E... es) {
        return false;
    }

    @Override
    public Camera setDirection(Vector2f direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public PhysicObject decreaseLife() {
        return this;
    }

    private Vector2f transformPos(Vector2f position, Vector2f cameraPos) {
        return position;
//        final Matrix3f cameraMatrix = new Matrix3f();
//        cameraMatrix.rotate(getDirection().angle(), new Vector3f(cameraPos, 0));
//
//        final Vector3f tgtPos = new Vector3f(position, 0f);
//        cameraMatrix.getScale(tgtPos);
//
//        return new Vector2f(tgtPos.x, tgtPos.y);
    }

    public boolean isAttached() {
        return focusSteering.isAttached();
    }

    public float getZoom() {
        return zoom;
    }

}
