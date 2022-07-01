package com.example.transport.controllers;

import java.util.*;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

public class NorthWestCornerRule {

    static long northTimer;
    static long northPotTimer;

    static double totalCostPotential = 0;
    static double totalCost = 0;
    private static int[] require;
    private static int[] deliver;
    public static int[][] spending;
    private static Traffic[][] matrix;

    static List<Integer> optMatrix = new ArrayList<>();

    public static void northWestCornerRuleStart(int[] requireT, int[] deliverT, int[][] spendingT) {
        long start = System.nanoTime();
        require = requireT;
        deliver = deliverT;
        spending = spendingT;
        matrix = new Traffic[deliver.length][require.length];
        northWestCornerRule();
        long end = System.nanoTime();
        northTimer = end - start;
        System.out.println("TIMER NORTH: " + start);
        System.out.println("TIMER NORTH END: " + end);

        long startP = System.nanoTime();
        step();

        resultOutput();
        System.out.println("Без метода потенциалов: " + totalCost);
        long endP = System.nanoTime();
        northPotTimer = endP - startP;
        System.out.println("TIMER NORTH P: " + startP);
        System.out.println("TIMER NORTH END P: " + endP);
    }

    private static class Traffic {
        final double unitCost;
        final int row, column;
        double amount;

        public Traffic(double amount, double unitCost, int row, int column) {
            this.amount = amount;
            this.unitCost = unitCost;
            this.row = row;
            this.column = column;
        }
    }

//    static void initialization(String filename) throws Exception {
//
//        try (Scanner sc = new Scanner(new File(filename))) {
//            int numberOfSources = sc.nextInt();
//            int numberOfDestinations = sc.nextInt();
//
//            List<Integer> source = new ArrayList<>();
//            List<Integer> destination = new ArrayList<>();
//
//            for (int i = 0; i < numberOfSources; i++)
//                source.add(sc.nextInt());
//
//            for (int i = 0; i < numberOfDestinations; i++)
//                destination.add(sc.nextInt());
//
//            int commonSource = source.stream().mapToInt(i -> i).sum();
//            int commonDestination = destination.stream().mapToInt(i -> i).sum();
//            if (commonSource > commonDestination)
//                destination.add(commonSource - commonDestination);
//            else if (commonDestination > commonSource)
//                source.add(commonDestination - commonSource);
//
//            deliver = source.stream().mapToInt(i -> i).toArray();
//            require = destination.stream().mapToInt(i -> i).toArray();
//
//            spending = new double[deliver.length][require.length];
//            matrix = new Traffic[deliver.length][require.length];
//
//            for (int i = 0; i < numberOfSources; i++)
//                for (int j = 0; j < numberOfDestinations; j++)
//                    spending[i][j] = sc.nextDouble();
//        }
//    }

    static void northWestCornerRule() {
        for (int row = 0, northwest = 0; row < deliver.length; row++)
            for (int column = northwest; column < require.length; column++) {

                int amount = Math.min(deliver[row], require[column]);
                if (amount > 0) {
                    matrix[row][column] = new Traffic(amount, spending[row][column], row, column);
                    totalCost = totalCost + (amount * spending[row][column]);
                    deliver[row] -= amount;
                    require[column] -= amount;

                    if (deliver[row] == 0) {
                        northwest = column;
                        break;
                    }
                }
            }
    }

    static void step() {
        double maximumContraction = 0;
        Traffic[] move = null;
        Traffic leave = null;

        fixDegenerateCase();

        for (int row = 0; row < deliver.length; row++) {
            for (int column = 0; column < require.length; column++) {

                if (matrix[row][column] != null)
                    continue;

                Traffic test = new Traffic(0, spending[row][column], row, column);
                Traffic[] way = getClosedWay(test);

                double reduction = 0;
                double leastAmount = Integer.MAX_VALUE;
                Traffic leavegCandidate = null;

                boolean isSignPlus = true;
                for (Traffic s : way) {
                    if (isSignPlus) {
                        reduction += s.unitCost;
                    } else {
                        reduction -= s.unitCost;
                        if (s.amount < leastAmount) {
                            leavegCandidate = s;
                            leastAmount = s.amount;
                        }
                    }
                    isSignPlus = !isSignPlus;
                }
                if (reduction < maximumContraction) {
                    move = way;
                    leave = leavegCandidate;
                    maximumContraction = reduction;
                }
            }
        }

        if (move != null && leave != null) {
            double amount = leave.amount;
            boolean plus = true;
            for (Traffic s : move) {
                s.amount += plus ? amount : -amount;
                matrix[s.row][s.column] = s.amount == 0 ? null : s;
                plus = !plus;
            }
            step();
        }
    }

    static LinkedList<Traffic> matrixToListConversion() {
        return stream(matrix)
                .flatMap(row -> stream(row))
                .filter(s -> s != null)
                .collect(toCollection(LinkedList::new));
    }

    @SuppressWarnings("empty-statement")
    static Traffic[] getClosedWay(Traffic s) {
        LinkedList<Traffic> way = matrixToListConversion();
        way.addFirst(s);

        // удаление элементов, у которых нет соседей
        while (way.removeIf(e -> {
            Traffic[] neighbours = getNeighbors(e, way);
            return neighbours[0] == null || neighbours[1] == null;
        })) ;

        // расположение остальных элементов в правильном порядке плюс-минус
        Traffic[] stones = way.toArray(new Traffic[way.size()]);
        Traffic previous = s;
        for (int i = 0; i < stones.length; i++) {
            stones[i] = previous;
            previous = getNeighbors(previous, way)[i % 2];
        }
        return stones;
    }

    static Traffic[] getNeighbors(Traffic s, List<Traffic> list) {
        Traffic[] neighbours = new Traffic[2];
        for (Traffic object : list) {
            if (object != s) {
                if (object.row == s.row && neighbours[0] == null)
                    neighbours[0] = object;
                else if (object.column == s.column && neighbours[1] == null)
                    neighbours[1] = object;
                if (neighbours[0] != null && neighbours[1] != null)
                    break;
            }
        }
        return neighbours;
    }

    static void fixDegenerateCase() {
        final double minValue = Double.MIN_VALUE;

        if (deliver.length + require.length - 1 != matrixToListConversion().size()) {

            for (int row = 0; row < deliver.length; row++)
                for (int column = 0; column < require.length; column++) {
                    if (matrix[row][column] == null) {
                        Traffic dummyVariables = new Traffic(minValue, spending[row][column], row, column);
                        if (getClosedWay(dummyVariables).length == 0) {
                            matrix[row][column] = dummyVariables;
                            return;
                        }
                    }
                }
        }
    }

    static void resultOutput() {
        System.out.println("Оптимальное решение матрицы");
        for (int row = 0; row < deliver.length; row++) {
            for (int column = 0; column < require.length; column++) {

                Traffic s = matrix[row][column];
                if (s != null && s.row == row && s.column == column) {
                    System.out.printf(" %3s ", (int) s.amount);
                    optMatrix.add((int) s.amount);
                    totalCostPotential += (s.amount * s.unitCost);
                } else {
                    System.out.printf("  0  ");
                    optMatrix.add(0);
                }
            }
            System.out.println();
        }
        System.out.println("Общая стоимость: " + totalCostPotential);
    }

}