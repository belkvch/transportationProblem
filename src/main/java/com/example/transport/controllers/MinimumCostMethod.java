package com.example.transport.controllers;

public class MinimumCostMethod {
    public static int[][] matrixOriginal;

    static long minTimer;

    static int totalCost = 0;

    static float timer;

    public static int[][] additionalMatrix; // дополнительная матрица
    public static int min = 999;
    public static int minForIndexI = 0;
    public static int minForIndexJ = 0;

    public static void minimumCostMethod(int [][] matrixOriginalT) {
        long start = System.nanoTime();
        matrixOriginal = matrixOriginalT;

        matrixOutput(matrixOriginal);// вывод матрицы
        additionalMatrix = new int[matrixOriginal.length - 1][matrixOriginal.length];// инициация матрицы результатов
        System.out.println("A: " + sumOfStocks(matrixOriginal));
        System.out.println("B: " + sumOfRequest(matrixOriginal));

        if (isMatrixOpen(matrixOriginal)) {
            System.out.println("Задача была открытая и приведена к закрытой");
        } else {
            System.out.println("Задача закрытая");
        }

        while ((sumOfStocks(matrixOriginal) != 0) && (sumOfRequest(matrixOriginal) != 0)) { // считаем пока есть запасы и заявки
            findMinElement(matrixOriginal); // нахождение минимального элемента
            System.out.println("Минимальный элемент равен " + min + " = [" + minForIndexI + "]" + "[" + minForIndexJ + "]");
            if (matrixOriginal[minForIndexI][0] < matrixOriginal[0][minForIndexJ]) { // заявка меньше запаса
                totalCost = totalCost + (min * additionalMatrix[minForIndexI - 1][minForIndexJ - 1]);
                additionalMatrix[minForIndexI - 1][minForIndexJ - 1] = matrixOriginal[minForIndexI][0];
                matrixOriginal[minForIndexI][minForIndexJ] = 0;
                matrixOriginal[0][minForIndexJ] -= matrixOriginal[minForIndexI][0];
                matrixOriginal[minForIndexI][0] = 0;

            } else {
                additionalMatrix[minForIndexI - 1][minForIndexJ - 1] = matrixOriginal[0][minForIndexJ];
                totalCost = totalCost + (min * additionalMatrix[minForIndexI - 1][minForIndexJ - 1]);
                matrixOriginal[minForIndexI][minForIndexJ] = 0;
                matrixOriginal[minForIndexI][0] -= matrixOriginal[0][minForIndexJ];
                matrixOriginal[0][minForIndexJ] = 0;
            }

            sumOfStocks(matrixOriginal);  // сумма запасов
            sumOfRequest(matrixOriginal); // сумма заявок
        }
        matrixOutput(additionalMatrix);
        System.out.println(totalCost);
        // printMas(x);
        long end = System.nanoTime();
        minTimer = end - start;
        System.out.println("Min timer start " + start);
        System.out.println("Min timer end " + end);
    }

    // функция для вывода двумерной матрицы
    public static void matrixOutput(int[][] matrix) {
        System.out.println("Заданная матрица оценок:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(" " + matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    // нахождение минимального элемента
    public static int findMinElement(int[][] matrix) {
        min = 999;
        for (int i = matrix.length - 1; i > 0; i--) {
            for (int j = matrix[0].length - 1; j > 0; j--) {
                if ((matrix[i][0] != 0) && (matrix[0][j] != 0)) {
                    if ((matrix[i][j] < min)) {
                        min = matrix[i][j];
                        minForIndexI = i;
                        minForIndexJ = j;
                    }
                }
            }
        }
        return (min);
    }

    // сумма запасов
    public static int sumOfStocks(int[][] matrix) {
        int move = 0;
        for (int j = 0; j < matrixOriginal.length; j++) {
            move += matrix[j][0];
        }
        return (move);
    }

    // сумма заявок
    public static int sumOfRequest(int[][] matrix) {
        int move = 0;
        for (int j = 0; j < matrixOriginal[0].length; j++) {
            move += matrix[0][j];
        }
        return (move);
    }

    // проверка на открытость
    public static boolean isMatrixOpen(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 1; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    return (true);
                }
            }
        }
        return false;
    }

}
