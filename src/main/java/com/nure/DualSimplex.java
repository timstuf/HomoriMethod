package com.nure;

public class DualSimplex extends SimplexTable {
    private int minIndex;
    public DualSimplex(NormalSimplex simplex) {
        super();
        formNewBazis(simplex);
        table = simplex.table;
        cKoefs = simplex.cKoefs;
        formNewTable();
    }

    private void formNewBazis(NormalSimplex simplex) {
        int[] newBazis = new int[Constants.m + 1];
        System.arraycopy(simplex.bazis, 0, newBazis, 0, simplex.bazis.length);
        newBazis[newBazis.length - 1] = Constants.n + 1;
        this.bazis = newBazis;
    }

    private void formNewTable() {
        int i = 0;
        double[][] newTable = new double[Constants.m + 3][Constants.n + 4];
        for (; i < Constants.m; i++) {
            if (table[i][1] != Math.floor(table[i][1])) {
                for (int j = 0; j < newTable.length-2; j++) {
                    System.arraycopy(table[j], 0, newTable[j], 0, table[j].length-1);
                }
                break;
            }
        }
        formNewEquation(newTable, i);
        this.table = newTable;
        countdeltaJ();
        countEvaluations();
    }
    @Override
    protected void countdeltaJ() {
        for (int i = 1; i < Constants.n+1; i++) {
            int evaluation = 0;
            for (int j = 0; j < Constants.m+1; j++) {
                evaluation += table[j][0] * table[j][i];
            }
            if (i != 1) evaluation = evaluation - cKoefs[i - 2];
            table[Constants.m+1][i] = evaluation;
        }
    }
    private void formNewEquation(double[][] newTable, int fractIndex){
        newTable[Constants.m][Constants.n+2] = 1;
        newTable[Constants.m][1] = (int) newTable[fractIndex][1] - newTable[fractIndex][1];
        for (int i = 2; i < Constants.n+2; i++) {
            newTable[Constants.m][i] = (int) newTable[fractIndex][i] - newTable[fractIndex][i];
        }
    }
    @Override
    protected boolean isNotOptimized() {
        for (int i = 0; i < Constants.m; i++) {
            if (table[i][1] != Math.floor(table[i][1])) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void countEvaluations() {
        int minIndex = findMinBazis();
        for (int i = 1; i < Constants.n+3; i++) {
            if (table[minIndex][i] >= 0) table[Constants.m + 2][i] = Constants.DONT_COUNT;
            else table[Constants.m + 2][i] = table[Constants.m + 1][i] / table[minIndex][i];
        }
        this.minIndex = minIndex;
    }

    protected void recountTable() {
        int minEval = 0;
        double min = Double.MAX_VALUE;
        for (int i = 2; i < Constants.n+2; i++) {
            if (table[Constants.m+2][i] < min && table[Constants.m+2][i] < Constants.DONT_COUNT) {
                minEval = i;
                min = table[Constants.m+2][i];
            }
        }
        bazis[minIndex] = minEval - 1;
        table[minIndex][0] = cKoefs[minEval - 2];
        double superElement = table[minIndex][minEval];
        for (int i = 1; i < Constants.n + 3; i++) { //направляющая строка
            table[minIndex][i] /= superElement;
        }
        double[][] newTable = new double[Constants.m + 3][Constants.n + 4];
        for (int i = 0; i < newTable.length; i++) {
            System.arraycopy(table[i], 0, newTable[i], 0, newTable[0].length);
        }
        for (int i = 0; i < Constants.m + 2; i++) {
            for (int j = 1; j < Constants.n + 3; j++) {
                if (i != minIndex)
                    newTable[i][j] = table[i][j] - table[i][minEval] * table[minIndex][j];
            }
        }
        table = newTable;
    }

    protected void printTable() {
        System.out.println("_____________________________________________________________________________");
        int j;
        System.out.print("                   ");
        for (int cKoef : cKoefs) { //print koefs
            System.out.print(String.format("%8s", cKoef));
        }
        System.out.print('\n');
        System.out.println(" Base     C       X      x1      x2      x3      x4      x5");
        for (int i = 0; i < table.length; i++) {
            if (i < table.length - 2) {
                System.out.print("  x" + bazis[i]);
                j = 0;
            } else {
                System.out.print("            ");
                j = 1;
            }

            for (; j < table[0].length-1; j++) {
                if(i==table.length-1 && table[i][j]==Constants.DONT_COUNT) System.out.print(String.format("%8s", "-"));
                else System.out.print(String.format("%8.1f", table[i][j]));

            }
            System.out.print('\n');
        }
        System.out.println("_____________________________________________________________________________");
    }
    protected double[] getSolutions() {
        double[] solution = new double[Constants.n+1];
        solution[0] = table[Constants.m+1][1] + cKoefs[cKoefs.length - 1];
        for (int i = 0; i < bazis.length; i++) {
            solution[bazis[i]] = table[i][1];
        }
        return solution;
    }
    @Override
    protected void printSolution() {
        double[] solution = getSolutions();
        System.out.println("The optimal value of the function: " + solution[0]);
        for (int i = 1; i < Constants.n; i++) {
            System.out.println("x" + (i) + " has value " + String.format("%.1f", solution[i]));
        }
    }
    private int findMinBazis(){
        double min = 1;
        int minIndex = -1;
        for (int i = 0; i < Constants.m+1; i++) {
            if(table[i][1]>=0) continue;
            if(table[i][1]<min){
                min = table[i][1];
                minIndex = i;
            }
        }
        if(min>0) throw new IllegalArgumentException();
        return minIndex;
    }
}
