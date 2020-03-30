package com.nure;

public class Homori {

    public void go() {
        int[][] equations = {{2, 2, 1, 0, 7}, {4, -5, 0, 1, 9}};
        new DualSimplex((NormalSimplex) new NormalSimplex(new int[]{3, 4}, equations, new int[]{1, 2, 0, 0, 0}).go()).go();
    }
}
