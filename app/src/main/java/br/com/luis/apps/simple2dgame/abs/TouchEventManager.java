package br.com.luis.apps.simple2dgame.abs;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.jphysics.Engine;
import org.jphysics.ObjectController;
import org.jphysics.ProjectileResolver;
import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.Camera;
import br.com.luis.apps.simple2dgame.objects.Fire;

/**
 * Created by luis on 6/24/17.
 */

public class TouchEventManager {

    private final ObjectController controller;
    private final Engine engine;
    private final Camera camera;
    private Reference rightReference = new Reference();
    private Reference leftReference = new Reference();

    private int halfScreenWidth;

    public TouchEventManager(ObjectController controller, Engine engine, Camera camera) {
        this.controller = controller;
        this.engine = engine;
        this.camera = camera;
    }

    public void setScreenWidth(float width) {
        this.halfScreenWidth = (int) (width * 0.5f);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return handleKeys(keyCode, false);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return handleKeys(keyCode, true);
    }

    private boolean handleKeys(int keyCode, boolean down) {
        Log.i("WD", String.valueOf(down));
        switch (keyCode) {
            case KeyEvent.KEYCODE_W:
                controller.setAction1(down);
                return true;
            case KeyEvent.KEYCODE_U:
                if (down) {
                    camera.zoomIn();
                }
                return true;
            case KeyEvent.KEYCODE_J:
                if (down) {
                    camera.zoomOut();
                }
                return true;
            case KeyEvent.KEYCODE_K:
                if (down) {
                    camera.restoreZoom();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                controller.setLeft(down);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                controller.setRight(down);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                controller.setUp(down);
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                controller.setDown(down);
                return true;
        }
        return false;
    }

    /**
     * @param ev
     */
    public void onEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        int id;
        int index;

        switch (action) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                controller.setAction1(true);
                final Reference ref = (ev.getX(ev.getActionIndex()) > halfScreenWidth) ? rightReference : leftReference;//Right
                index = ev.getActionIndex();
                ref.id = ev.getPointerId(index);
                ref.index = index;
                ref.originalPos = new Vector2f(ev.getX(index), ev.getY(index));
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                controller.setAction1(true);
                actionMove(rightReference, ev);
                actionMove(leftReference, ev);
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {

                controller.setAction1(ev.getPointerCount() > 1);
                index = ev.getActionIndex();
                id = ev.getPointerId(index);

                if (id == rightReference.id) {
                    actionUP(rightReference, ev);
                } else if (id == leftReference.id) {
                    actionUP(leftReference, ev);
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                controller.setAction1(false);
                index = ev.getActionIndex();
                id = ev.getPointerId(index);

                if (id == rightReference.id) {
                    actionCancel(rightReference, ev);
                } else if (id == leftReference.id) {
                    actionCancel(leftReference, ev);
                }
                break;
            }

        }

        //Log.i("Controller", "" + controller);
    }

    private void actionMove(Reference ref, MotionEvent ev) {

        if (ref.id < 0) {
            return;
        }

        int index = ev.findPointerIndex(ref.id);
        if (index >= 0 && index < ev.getPointerCount()) {
            final boolean right = rightReference.id == ref.id;

            final float x = ev.getX(index);
            final float y = ev.getY(index);

            if (right) {

                // only check x
                controller.setRight(false);
                controller.setLeft(false);
                if (Math.abs(x - ref.originalPos.x) > 20) { // allow 20 px of move without change
                    if (x > ref.originalPos.x) {
                        controller.setRight(true);
                    } else if (x < ref.originalPos.x) {
                        controller.setLeft(true);
                    }
                }
            } else { // left?
                // check only y
                controller.setUp(false);
                controller.setDown(false);
                if (Math.abs(y - ref.originalPos.y) > 20) { // allow 20 px of move without change
                    if (y < ref.originalPos.y) {
                        controller.setUp(true);
                    } else if (y > ref.originalPos.y) {
                        controller.setDown(true);
                    }
                }
            }
        } else {
            ref.id = -1; // invalidate ref (maybe canceled)
        }
    }

    public void actionUP(Reference ref, MotionEvent ev) {
        if (ref.id < 0) {
            return;
        }

        int index = ev.findPointerIndex(ref.id);

        Log.i("UP", "idx: " + index + ", id: " + ref.id);
        final boolean right = rightReference.id == ref.id;

        if (right) {

            // only check x
            controller.setRight(false);
            controller.setLeft(false);
        } else { // left?
            // check only y
            controller.setUp(false);
            controller.setDown(false);
        }
        ref.id = -1; // Avoid wrong controls.
    }

    public void actionCancel(Reference ref, MotionEvent ev) {
        if (ref.id < 0) {
            return;
        }

        final boolean right = rightReference.id == ref.id;

        if (right) {

            // only check x
            controller.setRight(false);
            controller.setLeft(false);
        } else { // left?
            // check only y
            controller.setUp(false);
            controller.setDown(false);
        }

        ref.id = -1; // Avoid wrong controls.
    }

    private static class Reference {
        private int id = -1;
        private int index = -1;
        private Vector2f originalPos = new Vector2f();
    }
}
