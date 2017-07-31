package br.com.luis.apps.simple2dgame.util;

import android.text.TextUtils;

import java.util.Collection;

/**
 * Created by luis on 6/28/17.
 * Metodos facilitadores (uteis)
 */

public class Util {

    public static boolean empty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return TextUtils.isEmpty((String) obj);
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue() == 0;
        }
        return false;
    }
}
