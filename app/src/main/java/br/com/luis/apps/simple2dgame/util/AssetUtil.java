package br.com.luis.apps.simple2dgame.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by luis on 6/25/17.
 */

public class AssetUtil {

    private final Map<CacheKey, Bitmap> cache = new HashMap<>();
    private final AssetManager manager;
    private final Context context;

    private static AssetUtil instance;

    private AssetUtil(AssetManager manager, Context context) {
        this.manager = manager;
        this.context = context;

    }

    public static void create(AssetManager manager, Context context) {
        instance = new AssetUtil(manager, context);
    }

    public static AssetUtil getInstance() {
        return instance;
    }

    public Bitmap getBitmap(String asset) {
        return getBitmap(asset, -1, -1);
    }

    public Bitmap getBitmap(String asset, int wd, int ht) {
        return getBitmap(asset, wd, ht, Integer.MAX_VALUE);
    }


    /**
     * Sum color to bitmap
     * note: all transparent pixels will change to this color.
     *
     * @param asset
     * @param wd
     * @param ht
     * @param toColor
     * @return
     */
    public Bitmap getBitmap(String asset, int wd, int ht, int toColor) {

        final CacheKey key = new CacheKey(asset, wd, ht, 0f, toColor);
        Bitmap bitmap;

        bitmap = cache.get(key);
        if (bitmap != null) {
            return bitmap;
        }

        try {
            final InputStream inputStream = manager.open(asset, Context.MODE_PRIVATE);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No resource found: " + asset);
        }

        if (wd > 0 && ht > 0) {
            bitmap = Bitmap.createScaledBitmap(bitmap, wd, ht, false);
        }

        if (toColor != Integer.MAX_VALUE) {
            Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            for (int x = 0; x < bitmap.getWidth(); x++) {
                for (int y = 0; y < bitmap.getHeight(); y++) {
                    int pixColor = bitmap.getPixel(x, y);
                    result.setPixel(x, y, (int) ((pixColor + toColor) * 0.5f));
                }
            }
            bitmap = result;
        }

        cache.put(key, bitmap);

        return bitmap;
    }

    public Bitmap getBitmapWithAlpha(String asset, int wd, int ht, float alpha) {

        final CacheKey key = new CacheKey(asset, wd, ht, 0f, Integer.MAX_VALUE, alpha);
        Bitmap bitmap;

        bitmap = cache.get(key);
        if (bitmap != null) {
            return bitmap;
        }

        try {
            final InputStream inputStream = manager.open(asset, Context.MODE_PRIVATE);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No resource found: " + asset);
        }

        if (wd > 0 && ht > 0) {
            bitmap = Bitmap.createScaledBitmap(bitmap, wd, ht, false);
        }

        if (alpha != Integer.MAX_VALUE) {
            Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            for (int x = 0; x < bitmap.getWidth(); x++) {
                for (int y = 0; y < bitmap.getHeight(); y++) {
                    int pixColor = bitmap.getPixel(x, y);
                    int r = Color.red(pixColor);
                    int g = Color.green(pixColor);
                    int b = Color.blue(pixColor);
                    int a = Color.alpha(pixColor);
                    result.setPixel(x, y, Color.argb((int) ((alpha + a) * 0.5f), r, g, b));
                }
            }
            bitmap = result;
        }

        cache.put(key, bitmap);

        return bitmap;
    }

    public Bitmap getBitmapWithAlpha(String asset, float scale, float alpha) {

        final CacheKey key = new CacheKey(asset, -1, -1, scale, Integer.MAX_VALUE, alpha);
        Bitmap bitmap;

        bitmap = cache.get(key);
        if (bitmap != null) {
            return bitmap;
        }

        try {
            final InputStream inputStream = manager.open(asset, Context.MODE_PRIVATE);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No resource found: " + asset);
        }

        if (scale != 1f) {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), false);
        }

        if (alpha != Integer.MAX_VALUE) {
            Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            for (int x = 0; x < bitmap.getWidth(); x++) {
                for (int y = 0; y < bitmap.getHeight(); y++) {
                    int pixColor = bitmap.getPixel(x, y);
                    int r = Color.red(pixColor);
                    int g = Color.green(pixColor);
                    int b = Color.blue(pixColor);
                    int a = Color.alpha(pixColor);
                    result.setPixel(x, y, Color.argb((int) ((alpha + a) * 0.5f), r, g, b));
                }
            }
            bitmap = result;
        }

        cache.put(key, bitmap);

        return bitmap;
    }

    public Bitmap getBitmap(String asset, float scale) {

        final CacheKey key = new CacheKey(asset, 0, 0, scale, -1);
        Bitmap bitmap;

        bitmap = cache.get(key);
        if (bitmap != null) {
            return bitmap;
        }


        try {
            final InputStream inputStream = manager.open(asset, Context.MODE_PRIVATE);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No resource found: " + asset);
        }

        if (scale != 1f) {
            return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), false);
        }

        cache.put(key, bitmap);

        return bitmap;
    }

    public List<String> getFileContent(String asset) {
        final List<String> result = new ArrayList<>();

        try {
            final InputStream inputStream = manager.open(asset, Context.MODE_PRIVATE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                result.add(line);
            }

            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No resource found: " + asset);
        }

        return result;
    }

    public Properties getProperties(String asset) {

        final Properties result = new Properties();

        try {
            final InputStream inputStream = manager.open(asset, Context.MODE_PRIVATE);
            result.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No resource found: " + asset);
        }

        return result;
    }


    public void writeText(String text, String file) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(text);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String getText(String file) {
        try {
            final StringBuilder builder = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(file));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStreamReader.close();
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return null;
        }
    }

    public AssetFileDescriptor getFD(String asset) {
        try {
            return manager.openFd(asset);
        } catch (Exception ex) {
            Log.e("ERROR", "File not found" + ex.getMessage(), ex);
            return null;
        }
    }

    private class CacheKey {

        String fileName;
        int width;
        int height;
        float alpha;
        float scale;
        int color;

        public CacheKey(String fileName, int width, int height, float scale, int color) {
            this.fileName = fileName;
            this.width = width;
            this.height = height;
            this.scale = scale;
            this.color = color;
            this.alpha = -1;
        }

        public CacheKey(String fileName, int width, int height, float scale, int color, float alpha) {
            this.fileName = fileName;
            this.width = width;
            this.height = height;
            this.scale = scale;
            this.color = color;
            this.alpha = alpha;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (width != cacheKey.width) return false;
            if (height != cacheKey.height) return false;
            if (Float.compare(cacheKey.alpha, alpha) != 0) return false;
            if (Float.compare(cacheKey.scale, scale) != 0) return false;
            if (color != cacheKey.color) return false;
            return fileName != null ? fileName.equals(cacheKey.fileName) : cacheKey.fileName == null;

        }

        @Override
        public int hashCode() {
            int result = fileName != null ? fileName.hashCode() : 0;
            result = 31 * result + width;
            result = 31 * result + height;
            result = 31 * result + (alpha != +0.0f ? Float.floatToIntBits(alpha) : 0);
            result = 31 * result + (scale != +0.0f ? Float.floatToIntBits(scale) : 0);
            result = 31 * result + color;
            return result;
        }
    }
}
