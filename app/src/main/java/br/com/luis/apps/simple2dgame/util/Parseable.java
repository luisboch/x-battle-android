package br.com.luis.apps.simple2dgame.util;

import br.com.luis.apps.simple2dgame.Config;

/**
 *
 * Created by luis on 6/28/17.
 * Utilizada pela {@link Config#getOrDefault(String, Parseable, Class)}
 * para deserializar um objeto.
 */

public interface Parseable<E> {
    E valueOf(String val);
}
