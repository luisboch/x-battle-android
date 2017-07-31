package br.com.luis.apps.simple2dgame;

import java.util.Properties;

import br.com.luis.apps.simple2dgame.util.AssetUtil;
import br.com.luis.apps.simple2dgame.util.Parseable;
import br.com.luis.apps.simple2dgame.util.Util;

/**
 * Created by luis on 6/28/17.
 */

public class Config {
    private static Properties configurations = new Properties();

    public static void setup() {
        configurations = AssetUtil.getInstance().getProperties("conf/config.properties");
    }

    public static void clear() {
        configurations.clear();
        configurations = null;
    }

    public static Properties getConfigurations() {
        return configurations;
    }

    /**
     * @param key
     * @param df  default value.
     * @param <E> object type.
     * @return
     */
    public static <E> E getOrDefault(String key, E df) {

        final String val = configurations.getProperty(key);

        if (Util.empty(val)) {
            return df;
        }

        if (df == null) {
            return (E) configurations.get(key);
        } else {
            Class<E> clz = (Class<E>) df.getClass();
            if (String.class.equals(clz)) {
                return (E) String.valueOf(val);
            } else if (Integer.class.isAssignableFrom(clz)) {
                return (E) Integer.valueOf(val);
            } else if (Float.class.isAssignableFrom(clz)) {
                return (E) Float.valueOf(val);
            } else if (Short.class.isAssignableFrom(clz)) {
                return (E) Short.valueOf(val);
            } else if (Long.class.isAssignableFrom(clz)) {
                return (E) Long.valueOf(val);
            } else if (Double.class.isAssignableFrom(clz)) {
                return (E) Double.valueOf(val);
            } else if (Boolean.class.isAssignableFrom(clz)) {
                return (E) Boolean.valueOf(val);
            }
            throw new IllegalArgumentException("Cant get the value of key: " + key + ", cause: can't discover type.");
        }
    }

    public static <E extends Parseable<E>> E getOrDefault(String key, E df, Class<E> clz) {

        final String val = configurations.getProperty(key);

        if (Util.empty(val)) {
            return df;
        }
        try {
            return clz.newInstance().valueOf(val);
        } catch (Exception e) {
            throw new RuntimeException("Type " + clz.getSimpleName() + " must have default constructor");
        }

    }
}
