package com.example.transport.controllers;

import java.util.Arrays;

public class Vogels {
    static long VogelTimer;
    //demand[] is to store the
    static int[] demand;
//            = {140, 100, 70, 40};

    //supply[] is to store the supplies
    static int[] supplly;
//            = {150, 100, 100};

    //noOfDemands for storing no of demands
    static int noOfDemands;

    //noOfSupplies for storing no of supplies
    static int noOfSupplies;

    //mathMatrix[][] for storing costs
    static int[][] mathMatrix;
//
//            {{2, 3, 1, 2},
//                    {3, 7, 5, 6},
//                    {3, 6, 3, 7}
//            };

    //totalSupplly is sum of all supplies
    static int totalSupplly = 0;

    //rowPenalty is for storing difference between two min values in a row
    static int[] rowPenalty;
    //colPenalty is for storing difference between two min values in a column
    static int[] colPenalty;

    //totalCost for storing total cost. totalcost = cost*(supply || demand)
    static int totalCost = 0;

    //colIndexOfMinValueInRow stores Indexes of minimum values in each row . Vice versa with rowIndexOfMinValueInCol
    static int[] colIndexOfMinValueInRow;
    static int[] rowIndexOfMinValueInCol;


    static int max, maxIndex;

    //penaltyRow is true if that row contains the maximum penalty value
    //penaltyCol is true if that column contains the maximum penalty value
    static boolean penaltyRow = false, penaltyCOl = false;

    /*
     * if rowCOunt=0 then whole matrix has one row to compute.
     * if colCOunt=0 then whole matrix has one column to compute.
     * In both cases calculating penalty should not be done.
     */
    static int rowCount, colCount;

    /*
     * rowOver stores true if its row has been completely allocated
     * colOver stores true if its col has been completely allocated
     */
    static boolean[] rowOver;
    static boolean[] colOver;

    static int countDown = 100;

    public static void vogelsApproximationMethod(int[] demandT, int[] suppllyT, int[][] mathMatrixT) {
        long VogelTimerStart = System.nanoTime();
        demand = demandT;
        supplly = suppllyT;
        mathMatrix = mathMatrixT;
        noOfDemands = demand.length;
        noOfSupplies = supplly.length;
        rowPenalty = new int[noOfSupplies];
        colPenalty = new int[noOfDemands];
        colIndexOfMinValueInRow = new int[noOfSupplies];
        rowIndexOfMinValueInCol = new int[noOfDemands];
        rowCount = noOfSupplies - 1;
        colCount = noOfDemands - 1;
        rowOver = new boolean[noOfSupplies];
        colOver = new boolean[noOfDemands];

        for (int sum : supplly) {
            totalSupplly = totalSupplly + sum;
        }

        Arrays.fill(rowOver, false);
        Arrays.fill(colOver, false);

        System.out.println("demands = " + noOfDemands);
        System.out.println("supplies = " + noOfSupplies);
        System.out.println("the costs of mathematical matrix");


        while (totalSupplly != 0) {
            int rowPosition = 0, colPosition = 0;

            if ((rowCount == 0) || (colCount == 0)) {
                printMatrix();
            }

            if (rowCount == 0) {


                for (int i = 0; i < noOfSupplies; i++) {

                    if (rowOver[i]) {
                        continue;
                    }
                    rowPosition = i;
                }//end of for

                boolean dec = true;
                while (dec) {
                    int tempMin = Integer.MAX_VALUE;
                    for (int i = 0; i < noOfDemands; i++) {
                        if (colOver[i]) {
                            continue;
                        }
                        if (mathMatrix[rowPosition][i] < tempMin) {
                            tempMin = mathMatrix[rowPosition][i];
                            colPosition = i;
                        }
                    }//end of for
                    cal(rowPosition, colPosition);
                    colOver[colPosition] = true;
                    tempMin = Integer.MAX_VALUE;

                    if (totalSupplly == 0) {
                        dec = false;
                    }
                }//end of while
                return;
            }// if(rowCount==0)

            if (colCount == 0) {


                for (int i = 0; i < noOfDemands; i++) {

                    if (colOver[i]) {
                        continue;
                    }
                    colPosition = i;
                }//end of for


                boolean dec = true;
                while (dec) {
                    int tempMin = Integer.MAX_VALUE;
                    for (int i = 0; i < noOfSupplies; i++) {
                        if (rowOver[i]) {
                            continue;
                        }
                        if (mathMatrix[i][colPosition] < tempMin) {
                            tempMin = mathMatrix[i][colPosition];
                            rowPosition = i;
                        }
                    }//end of for
                    cal(rowPosition, colPosition);
                    rowOver[rowPosition] = true;

                    tempMin = Integer.MAX_VALUE;
                    printMatrix();
                    if (totalSupplly == 0) {
                        dec = false;
                    }
                }//end of while
                System.out.println("..............");
                printMatrix();
                long end = System.nanoTime();
                System.out.println("VOGEL timer end " + end);
                System.out.println("VOGEL timer start " + VogelTimerStart);
                VogelTimer = end - VogelTimerStart;
                return;
            } //if(colCount==0)
            pen();


            if (penaltyRow) {
                //finding total cost and reducing penalty row/column value
                rowPosition = maxIndex;
                colPosition = colIndexOfMinValueInRow[maxIndex];
                cal(rowPosition, colPosition);
            } else {
                //finding the least cost in the col
                rowPosition = rowIndexOfMinValueInCol[maxIndex];
                colPosition = maxIndex;
                cal(rowPosition, colPosition);
            }


            if (demand[colPosition] == 0) {
                colCount--;
                colOver[colPosition] = true;
            }
            if (supplly[rowPosition] == 0) {
                rowCount--;
                rowOver[rowPosition] = true;
            } // end of if

            if (countDown < 0) {
                return;
            }
        }//end of while
    }    //end of main()

    public static int pen() {
        //calculating penalty in rows
        int minValueInRow[] = new int[noOfSupplies], minValueInCol[] = new int[noOfDemands];
        for (int i = 0; i < noOfSupplies; i++) {
            if (rowOver[i]) {
                continue;
            }
            int firstMin = Integer.MAX_VALUE, secondMin = Integer.MAX_VALUE;

            for (int j = 0; j < noOfDemands; j++) {
                if (colOver[j]) {
                    continue;
                }

                if (mathMatrix[i][j] <= secondMin) {
                    secondMin = mathMatrix[i][j];
                    if (secondMin <= firstMin) {
                        int temp = firstMin;
                        firstMin = secondMin;
                        secondMin = temp;
                        colIndexOfMinValueInRow[i] = j;
                    }//first if
                } //second if

            }//end of 1st for loop
            rowPenalty[i] = secondMin - firstMin;
            minValueInRow[i] = firstMin;
        } //end of 2nd for loop


        for (int value : rowPenalty) {
            System.out.println("Value in row penalty = " + value);
        }

        //  calculating penalty in cols
        for (int i = 0; i < noOfDemands; i++) {
            if (colOver[i]) {
                continue;
            }
            int firstMin = Integer.MAX_VALUE, secondMin = Integer.MAX_VALUE;

            for (int j = 0; j < noOfSupplies; j++) {
                if (rowOver[j]) {
                    continue;
                }
                if (mathMatrix[j][i] <= secondMin) {
                    secondMin = mathMatrix[j][i];
                    if (secondMin <= firstMin) {
                        int temp = firstMin;
                        firstMin = secondMin;
                        secondMin = temp;
                        rowIndexOfMinValueInCol[i] = j;
                    }//second if
                }//first if
            }    //end of 1st for loop
            colPenalty[i] = secondMin - firstMin;    //calculating penalty
            minValueInCol[i] = firstMin;
        }//end of 2nd for loop


        for (int value : colPenalty) {
            System.out.println("Value in column penalty= " + value);
        }


        int rowMaxValue = rowPenalty[0];
        int rowMaxValueIndex = 0;
        for (int i = 0; i < rowPenalty.length; i++) {
            if (rowOver[i]) {
                continue;
            }
            if (rowPenalty[i] >= rowMaxValue) {
                rowMaxValue = rowPenalty[i];
                rowMaxValueIndex = i;
            }//end of if
        }//end of for loop

        int colMaxValue = colPenalty[0];
        int colMaxValueIndex = 0;
        for (int i = 0; i < colPenalty.length; i++) {
            if (colOver[i]) {
                continue;
            }
            if (colPenalty[i] >= colMaxValue) {
                colMaxValue = colPenalty[i];
                colMaxValueIndex = i;
            }//end of if
        }//end of for loop

        penaltyRow = false;
        penaltyCOl = false;
        if (rowMaxValue > colMaxValue) {
            maxIndex = rowMaxValueIndex;
            penaltyRow = true;
            int maxValue = rowMaxValue;
            int firstMin = minValueInRow[0];
            for (int i = 0; i < noOfSupplies; i++) {
                if (rowOver[i]) {
                    continue;
                }
                if (maxValue == rowPenalty[i]) {
                    if (minValueInRow[i] <= firstMin) {
                        maxIndex = i;
                        firstMin = minValueInRow[i];
                    }

                }//end of if
            }//end of for

        } else {
            maxIndex = colMaxValueIndex;
            penaltyCOl = true;
            int maxValue = colMaxValue;
            int firstMin = minValueInCol[0];
            for (int i = 0; i < noOfDemands; i++) {
                if (colOver[i]) {
                    continue;
                }

                if (maxValue == colPenalty[i]) {
                    if (minValueInCol[i] <= firstMin) {
                        firstMin = minValueInCol[i];
                        maxIndex = i;
                    }
                }
            }//end of if
        }    //end of far

        return 0;
    }

    public static void cal(int rowPosition, int colPosition) {
        countDown = countDown - 1;

        if (supplly[rowPosition] < demand[colPosition]) {
            demand[colPosition] = demand[colPosition] - supplly[rowPosition];
            totalCost = mathMatrix[rowPosition][colPosition] * supplly[rowPosition] + totalCost;
            totalSupplly = totalSupplly - supplly[rowPosition];
            supplly[rowPosition] = 0;
        } else {
            supplly[rowPosition] = supplly[rowPosition] - demand[colPosition];
            totalCost = mathMatrix[rowPosition][colPosition] * demand[colPosition] + totalCost;
            totalSupplly = totalSupplly - demand[colPosition];
            demand[colPosition] = 0;
        }
        System.out.println("total cost = " + totalCost);
    }

    public static void printMatrix() {
        for (int i = 0; i < noOfSupplies; i++) {
            for (int j = 0; j < noOfDemands; j++) {
                System.out.print("[" + mathMatrix[i][j] + "]");
            }
            System.out.println();
        }
    }
//vogels class ending
}