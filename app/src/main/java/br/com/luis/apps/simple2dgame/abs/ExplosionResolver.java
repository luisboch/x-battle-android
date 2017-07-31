package br.com.luis.apps.simple2dgame.abs;

import org.jphysics.api.Force;
import org.jphysics.api.PhysicObject;
import org.jphysics.math.Vector2f;
import org.jphysics.projectile.Projectile;

import br.com.luis.apps.simple2dgame.objects.FireExplosion;

/**
 * Created by luis on 04/07/17.
 */

public class ExplosionResolver implements org.jphysics.api.ExplosionResolver {
    @Override
    public Force create(PhysicObject... ref) {
        if (ref.length > 0) {
            if (ref[0] instanceof Projectile) {
                Projectile projectile = (Projectile) ref[0];
                if (!projectile.isTimedOut() && !projectile.isAlive()) {
                    final Force force = new FireExplosion();
                    force.setPosition(new Vector2f(ref[0].getPosition()));
                    return force;
                }
            }
        }
        return null;
    }
}
