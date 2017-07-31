package br.com.luis.apps.simple2dgame.objects;

import org.jphysics.api.Force;
import org.jphysics.math.Vector2f;

import br.com.luis.apps.simple2dgame.Config;
import br.com.luis.apps.simple2dgame.SimpleGameViewObject;
import br.com.luis.apps.simple2dgame.util.SpriteSheet;
import br.com.luis.apps.simple2dgame.util.AssetUtil;

/**
 * Created by luis on 6/25/17.
 */

public class Planet1 extends SimpleGameViewObject implements Force, Planet {
    public Planet1(float scale) {
        super(AssetUtil.getInstance().getBitmap("asteroid5.png"), false, 140f * scale,
                Config.getOrDefault("planet.mass.start", 3000f) * scale,
                new Vector2f(scale)); // 72px tamanho da imagem 7500 raio, logo 15000 tamanho da imagem na tela.
        setAnimation(new SpriteSheet(72, 72));
        getAnimation().setBitmap(getBitmap());
        getAnimation().addDefault(new SpriteSheet.Mapping(SpriteSheet.generateIndex(0, 19), true, false, 6f));
    }

    @Override
    public void update(float v) {
    }
}
