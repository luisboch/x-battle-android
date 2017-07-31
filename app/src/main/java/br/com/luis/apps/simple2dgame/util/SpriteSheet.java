package br.com.luis.apps.simple2dgame.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Prove suporte à mapeamento de Sprites.
 * Pode ser usada para geração de animações ou controle de tilesets.
 * <p>
 * Created by luis on 6/25/17.
 */
public class SpriteSheet {

    private static final String DEFAULT_MAPPING_NAME = "_DEFAULT_";
    private static Random rnd = new Random();

    private Bitmap bitmap;

    private int width, height;

    private String currentMapping = DEFAULT_MAPPING_NAME;

    private Map<String, Mapping> mappings = new HashMap<>();

    private Map<Mapping, Bitmap[]> cachedBitmap = new HashMap<>();

    private float timeElapsed = 0f;
    private int tileXSize, tileYSize, numOfTiles;

    /**
     * Cria um novo SpriteSheet
     *
     * @param width  obrigatório
     * @param height obrigatório
     */
    public SpriteSheet(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private Mapping getCurrentConfig() {

        if (mappings.size() == 1 && currentMapping.equals(DEFAULT_MAPPING_NAME)) {
            return mappings.values().iterator().next();
        } else if (currentMapping == null || currentMapping.isEmpty()) {
            throw new IllegalStateException("SpriteSheet não possui animcacao padrao ou mapeada");
        }

        return mappings.get(currentMapping);
    }

    public boolean hasMappings() {
        return !mappings.isEmpty();
    }

    private int getNextIndexByFPS(Mapping mapping) {

        //frames por segundo;
        float timeForFrame = 1000f / mapping.fps;
        int curIndex = mapping.reversing ? mapping.mappingIndex.length - 1 - (int) (timeElapsed / timeForFrame) : (int) ((timeElapsed * 1000f) / timeForFrame);

        if (curIndex >= mapping.mappingIndex.length || curIndex < 0) {

            timeElapsed = 0f;
            if (mapping.sequence) {
                if (mapping.reverse && !mapping.reversing) {
                    mapping.reversing = true;
                    curIndex = mapping.mappingIndex.length - 1;
                } else if (mapping.reverse && mapping.reversing) {
                    mapping.reversing = false;
                    curIndex = 0;
                } else {
                    curIndex = 0;
                }
            } else {
                curIndex = mapping.mappingIndex.length - 1; // use last index
            }
        }

        curIndex = mapping.mappingIndex[curIndex];

        return curIndex;
    }

    private Rect getRectByIndex(int index) {

        int x = (index % tileXSize) * width;
        int y = ((int) ((float) index / (float) tileXSize)) * height;

        Rect rect = new Rect();
        rect.set(x, y, x + width, y + height);

        return rect;
    }

    public Bitmap getAnimation(float delta) {
        timeElapsed += delta;
        final Mapping mp = getCurrentConfig();
        int index = getNextIndexByFPS(mp);
        return getTileByIndex(mp, index);
    }

    /**
     * Limpa o cache de imagens geradas e cria novamente para todos os mappings.
     * Otimiza a geração das imagens. Idealmente deve ser criado antes de iniciar a animação ou jogo.
     */
    public void createCache() {

    }

    private void createCache(String mappingName) {
        final Mapping mp = mappings.get(mappingName);
        Bitmap[] cache = new Bitmap[mp.mappingIndex.length];
        cachedBitmap.put(mp, cache);

        for (int idx : mp.mappingIndex) {
            cache[idx] = createBitmapForCache(idx);
        }

    }

    /**
     * Usado apenas para criar cache
     * @param index
     * @return
     */
    private Bitmap createBitmapForCache(int index) {
        Bitmap cropBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Rect rect = getRectByIndex(index);
        Canvas canvas = new Canvas(cropBitmap);
        canvas.drawBitmap(bitmap, rect, new Rect(0, 0, width, height), null);
        return cropBitmap;
    }

    private Bitmap getTileByIndex(Mapping mapping, int index) {
        if (mapping.cache) {
            return cachedBitmap.get(mapping)[index];
        } else {
            Bitmap cropBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Rect rect = getRectByIndex(index);
            Canvas canvas = new Canvas(cropBitmap);
            canvas.drawBitmap(bitmap, rect, new Rect(0, 0, width, height), null);
            return cropBitmap;
        }
    }


    public Bitmap getNextTile() {
        Mapping mapping = getCurrentConfig();
        return getTileByIndex(mapping, mapping.nextIndex());
    }

    public void setWidth(int width) {
        this.width = width;
        if (bitmap != null) {
            this.tileXSize = bitmap.getWidth() / this.width;
        }
    }

    public void setHeight(int height) {
        this.height = height;
        if (bitmap != null) {
            this.tileYSize = bitmap.getWidth() / this.height;
        }
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.tileXSize = bitmap.getWidth() / this.width;
        this.tileYSize = bitmap.getHeight() / this.height;
        this.numOfTiles = tileXSize * tileYSize;
    }

    public int getTileXSize() {
        return tileXSize;
    }

    public int getTileYSize() {
        return tileYSize;
    }

    public void setCurrentMapping(String currentMapping) {
        this.currentMapping = currentMapping;
        if (!mappings.containsKey(currentMapping)) {
            Log.e("SpriteSheet", "No animation found with name: " + currentMapping);
        }
    }

    public void addAnimation(String name, Mapping mapping) {
        this.mappings.put(name, mapping);
        createCache(name);
    }

    /**
     * Add one Mapping and set it as default.
     *
     * @param mapping
     */
    public void addDefault(Mapping mapping) {
        addAnimation(DEFAULT_MAPPING_NAME, mapping);
        currentMapping = DEFAULT_MAPPING_NAME;
    }

    public int getNumOfTiles() {
        return numOfTiles;
    }

    public void setNumOfTiles(int numOfTiles) {
        this.numOfTiles = numOfTiles;
    }

    public static class Mapping {

        private int lastUsedIndex = -1;

        public final int[] mappingIndex;
        public final boolean sequence;
        public final boolean reverse;
        private boolean reversing = false;

        private float fps = 30f;
        public final boolean cache;

        public Mapping(int[] mappingIndex, boolean loop, boolean reverse, float fps) {
            this(mappingIndex, loop, reverse, fps, true);
        }
        public Mapping(int[] mappingIndex, boolean sequence, boolean reverse, float fps, boolean useCache) {
            this.mappingIndex = mappingIndex;
            this.sequence = sequence;
            this.reverse = reverse;
            this.fps = fps;
            this.cache = useCache;
        }


        public void setFps(float fps) {
            this.fps = fps;
        }

        public int nextIndex() {
            if (sequence) {
                if (lastUsedIndex >= mappingIndex.length) {
                    if (reverse) {
                        reversing = true;
                    } else {
                        lastUsedIndex = -1;
                    }
                } else if (lastUsedIndex <= 0 && reversing) {
                    reversing = false;
                }

                lastUsedIndex = reversing ? lastUsedIndex-- : lastUsedIndex++;
            } else {
                lastUsedIndex = SpriteSheet.rnd.nextInt(mappingIndex.length);
            }

            return mappingIndex[lastUsedIndex];
        }
    }

    public static int[] generateIndex(int start, int end) {
        return generateIndex(start, end, 1);
    }

    /**
     * Generate an int array based on start (inclusive) and end (exclusive) based on step
     *
     * @param start
     * @param end
     * @param step
     * @return
     */
    public static int[] generateIndex(int start, int end, int step) {

        int[] idx = new int[(end - start) / step];

        int curIdx = 0;
        for (int i = start; i < end; i += step) {
            idx[curIdx] = i;
            curIdx++;
        }

        return idx;
    }
}
