package br.com.luis.apps.simple2dgame.abs;

import org.jphysics.SimpleProjectileResolver;
import org.jphysics.api.PhysicObject;
import org.jphysics.projectile.Projectile;

import br.com.luis.apps.simple2dgame.SimpleGameViewObject;
import br.com.luis.apps.simple2dgame.objects.Fire;
import br.com.luis.apps.simple2dgame.util.SoundManager;

/**
 * Responsável por criar os projéteis do jogo.
 * Created by luis on 6/29/17.
 */

public class ProjectileResolver extends SimpleProjectileResolver {
    final SoundManager manager;

    public  ProjectileResolver(SoundManager manager){
        setReloadTime(500l, Fire.class);
        this.manager = manager;
    }
    @Override
    protected Projectile _create(PhysicObject physicObject, Class<? extends Projectile> aClass) {

        if (aClass.equals(Fire.class) && physicObject instanceof SimpleGameViewObject) {
            manager.playEffect(SoundManager.FIRE);
            return new Fire((SimpleGameViewObject) physicObject);
        }

        throw new RuntimeException("Cant create projectile: " + aClass.getSimpleName());
    }
}
