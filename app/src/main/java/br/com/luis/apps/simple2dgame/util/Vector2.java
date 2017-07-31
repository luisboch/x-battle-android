package br.com.luis.apps.simple2dgame.util;

/**
 * Controla um vetor 2d de inteiros
 * Obs: não está suportando operacoes, deve ser utilizada apenas para agregador de paremetros
 * Para operacoes com vetores, utilize a biblioteca "joml".
 * <p>
 * Created by Luis Boch on 02/07/17.
 */

public class Vector2 {

    public int x;
    public int y;

    /**
     * Constroi uma nova instancia com valores em zero.
     */
    public Vector2() {
        x = 0;
        y = 0;
    }

    /**
     * Constroi uma nova instancia com os valores passados
     * @param x
     * @param y
     */
    public Vector2(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    /**
     * Constroi uma nova instancia com os valores passados
     * @param x
     * @param y
     */
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Constroi uma nova instancia onde x e y recebem o valor passado
     * @param val
     */
    public Vector2(int val) {
        this.x = val;
        this.y = val;
    }
    /**
     * Constroi uma nova instancia onde x e y recebem o valor passado
     * @param x
     * @param y
     */
    public Vector2(float val) {
        this.x = (int) val;
        this.y = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int w() {
        return x;
    }

    public int h() {
        return y;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }
}
