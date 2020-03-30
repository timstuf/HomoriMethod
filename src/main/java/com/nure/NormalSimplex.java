package com.nure;

public class NormalSimplex extends SimplexTable{
    private int maxIndex;
    public NormalSimplex(int[] bazis, int[][] equations, int[] cKoefs) {
        super(bazis, equations, cKoefs);
    }
    @Override
    protected boolean isNotOptimized() {
        for (int i = 0; i < Constants.n; i++) {
            if (table[2][i + 2] < 0) return true;
        }
        return false;
    }
    @Override
    protected void printTable() {
        System.out.println("_____________________________________________________________________________");
        int j;
        System.out.print("                   ");
        for (int i = 0; i < cKoefs.length - 1; i++) { //print koefs
            System.out.print(String.format("%8s", cKoefs[i]));
        }
        System.out.print('\n');
        System.out.println(" Base     C       X      x1      x2      x3      x4      0");
        for (int i = 0; i < table.length; i++) {
            if (i < table.length - 1) {
                System.out.print("  x" + bazis[i]);
                j = 0;
            } else {
                System.out.print("            ");
                j = 1;
            }

            for (; j < table[0].length; j++) {
                if(j==table[0].length-1 && table[i][j]==Constants.DONT_COUNT) System.out.print(String.format("%8s", "-"));
                else System.out.print(String.format("%8.1f", table[i][j]));

            }
            System.out.print('\n');
        }
        System.out.println("_____________________________________________________________________________");
    }

    protected void printSolution() {}

    @Override
    protected void countdeltaJ() {
        for (int i = 1; i < Constants.n; i++) {
            int evaluation = 0;
            for (int j = 0; j < Constants.m; j++) {
                evaluation += table[j][0] * table[j][i];
            }
            if (i != 1) evaluation = evaluation - cKoefs[i - 2];
            table[Constants.m][i] = evaluation;
        }
    }
    @Override
    protected void recountTable() {
        int minEval = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < Constants.m; i++) {
            if (table[i][Constants.n+2] < min && table[i][Constants.n+2] > Constants.DONT_COUNT) {
                minEval = i;
                min = table[i][Constants.n+2];
            }
        }
        bazis[minEval] = maxIndex - 1;
        table[minEval][0] = cKoefs[maxIndex - 2];
        double superElement = table[minEval][maxIndex];
        for (int i = 1; i < Constants.n + 2; i++) { //направляющая строка
            table[minEval][i] /= superElement;
        }
        double[][] newTable = new double[Constants.m + 1][Constants.n + 3];
        for (int i = 0; i < newTable.length; i++) {
            System.arraycopy(table[i], 0, newTable[i], 0, newTable[0].length);
        }
        for (int i = 0; i < Constants.m + 1; i++) {
            for (int j = 1; j < Constants.n + 2; j++) {
                if (i != minEval)
                    newTable[i][j] = table[i][j] - table[i][maxIndex] * table[minEval][j];
            }
        }
        table = newTable;
    }
    @Override
    protected void countEvaluations() {
        int maxIndex = 0; //number of направляющий столбенц
        for (int i = 2; i < Constants.n + 2; i++) {
            if (table[2][i] < table[2][maxIndex]) maxIndex = i;
        }
        for (int i = 0; i < Constants.m; i++) {
            if (table[i][maxIndex] <= 0) table[i][Constants.n + 2] = Constants.DONT_COUNT;
            else table[i][Constants.n + 2] = table[i][1] / table[i][maxIndex];
        }
        this.maxIndex = maxIndex;
    }

}
