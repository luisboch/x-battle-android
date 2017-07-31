package br.com.luis.apps.simple2dgame.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Responsável por gerenciar uma textura de acordo com o tamanho submetido por parãmetro.
 * Created by luis on 7/1/17.
 */

public class TextureBitmap {

    private final Bitmap original;
    private final Vector2 textureSize;
    private final SpriteSheet sheet;

    public TextureBitmap(Bitmap original) {
        this.original = original;
        this.textureSize = new Vector2(original.getWidth(), original.getHeight());
        this.sheet = null; // Não há várias texturas.
    }

    public TextureBitmap(SpriteSheet sheet) {
        this.sheet = sheet;
        original = null;
        textureSize = new Vector2(sheet.getWidth(), sheet.getHeight());
    }

    /**
     * @param returnSize
     * @param viewPos
     * @return
     */
    public Bitmap getBitmap(Vector2 returnSize, Vector2 viewPos) {

        Bitmap bitmap = Bitmap.createBitmap(returnSize.x, returnSize.y, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        int cropStartX = ((int) (viewPos.x % (float) textureSize.w()));
        int cropEndX = textureSize.w();
        int drawWidth = cropEndX - cropStartX;
        int drawPosX = 0;


        do {

            int drawPosY = 0;
            int cropStartY = ((int) (viewPos.y % (float) textureSize.h()));
            int cropEndY = textureSize.h();
            int drawHeight = cropEndY - cropStartY;

            do {

                Rect rect = new Rect(cropStartX, cropStartY, cropEndX, cropEndY);
                Rect to = new Rect(drawPosX, drawPosY, drawWidth + drawPosX, drawPosY + drawHeight);

                canvas.drawBitmap(getSource(), rect, to, null);

                cropEndY = (drawPosY + textureSize.h()) <= returnSize.y ? textureSize.h() : (textureSize.h() - ((int) (viewPos.y % (float) textureSize.h())));
                cropStartY = 0;
                drawPosY += drawHeight;
                drawHeight = cropEndY - cropStartY;
            } while (drawPosY < returnSize.y);

            cropEndX = (drawPosX + textureSize.w()) <= returnSize.x ? textureSize.w() : (textureSize.w() - ((int) (viewPos.x % (float) textureSize.w())));
            cropStartX = 0;
            drawPosX += drawWidth;
            drawWidth = cropEndX - cropStartX;
        } while (drawPosX < returnSize.x);

        return bitmap;
    }

    private Bitmap getSource() {

        if (sheet == null) {
            return original;
        }

        return sheet.getNextTile();
    }

}
