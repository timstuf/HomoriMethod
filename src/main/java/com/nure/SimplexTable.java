package com.nure;

abstract class SimplexTable {
    double[][] table;
    int[] cKoefs;
    int[] bazis;
    private int[][] equations;

    SimplexTable() {
    }

    SimplexTable(int[] bazis, int[][] equations, int[] cKoefs) {
        table = new double[Constants.m + 1][Constants.n + 3];
        this.bazis = bazis;
        this.equations = equations;
        this.cKoefs = cKoefs;
        createTable();
    }

    protected abstract boolean isNotOptimized();

    protected abstract void countdeltaJ();

    protected abstract void countEvaluations();

    protected abstract void recountTable();

    protected void createTable() {
        table[0][0] = cKoefs[bazis[0] - 1];
        table[1][0] = cKoefs[bazis[1] - 1];

        for (int i = 0; i < Constants.m; i++) { //x
            table[i][1] = equations[i][Constants.n];
        }
        for (int i = 0; i < Constants.m; i++) { //rest
            for (int j = 2; j < Constants.n + 2; j++)
                table[i][j] = equations[i][j - 2];
        }
        countdeltaJ();
        countEvaluations();
    }

    protected abstract void printTable();

    protected abstract void printSolution();

    public SimplexTable go() {
        System.out.println("_____________________________________________________________________________");
        printTable();
        while (isNotOptimized()) {
            recountTable();
            if (isNotOptimized()) countEvaluations();
            printTable();
        }
        printSolution();
        return this;
    }
}

