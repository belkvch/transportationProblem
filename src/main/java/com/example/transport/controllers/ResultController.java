package com.example.transport.controllers;

import com.example.transport.entity.*;
import com.example.transport.repository.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/result")
public class ResultController {
    private final MatrixRepo matrixRepo;
    private final MatrixInfoRepo matrixInfoRepo;


    @GetMapping
    public String create(Model model) throws Exception {
        MatrixInfo matrixInfo = matrixInfoRepo.findByIdMax();
        model.addAttribute("columnAmount", matrixInfo.getColumns());
        model.addAttribute("rowsAmount", matrixInfo.getRows());

        List<Matrix> matrices = new ArrayList<>();
        int[][] matricesMinCost = new int[matrixInfo.getColumns()][matrixInfo.getRows()];
        int idCounter = matrixInfo.getColumns() * matrixInfo.getRows();
        for (int i = 0; i < idCounter; i++) {
            matrices.add(matrixRepo.getById(i));
            matricesMinCost [matrices.get(i).getColumnMatrix()][matrices.get(i).getRow()] = matrices.get(i).getValue();
        }
        model.addAttribute("matrices", matrices);


        int columnsNumber = matrixInfo.getColumns();
        int rowsNumber = matrixInfo.getRows();

        int[] demand = new int[rowsNumber - 1];
        int[] supplly = new int[columnsNumber - 1];
        int[][] mathMatrix = new int[columnsNumber - 1][rowsNumber - 1];

        for (Matrix matrix : matrices) {
            if (matrix.getRow() == 0 && matrix.getId() != 0) {
                supplly[matrix.getColumnMatrix() - 1] = matrix.getValue();
            }
            if (matrix.getColumnMatrix() == 0 && matrix.getId() != 0) {
                demand[matrix.getRow() - 1] = matrix.getValue();

            }
            if (matrix.getColumnMatrix() != 0 && matrix.getId() != 0 && matrix.getRow() != 0) {
                mathMatrix[matrix.getColumnMatrix() - 1][matrix.getRow() - 1] = matrix.getValue();
            }
        }


        Vogels.vogelsApproximationMethod(demand, supplly, mathMatrix);
        model.addAttribute("vogelResult", Vogels.totalCost);
        Vogels.totalCost = 0;
        System.out.println(Vogels.VogelTimer);
        model.addAttribute("vogelTimer", Vogels.VogelTimer/1000);

        int[] require = new int[rowsNumber - 1];
        int[] deliver = new int[columnsNumber - 1];
        int[][] spending = new int[columnsNumber - 1][rowsNumber - 1];

        for (Matrix matrix : matrices) {
            if (matrix.getRow() == 0 && matrix.getId() != 0) {
                deliver[matrix.getColumnMatrix() - 1] = matrix.getValue();
            }
            if (matrix.getColumnMatrix() == 0 && matrix.getId() != 0) {
                require[matrix.getRow() - 1] = matrix.getValue();

            }
            if (matrix.getColumnMatrix() != 0 && matrix.getId() != 0 && matrix.getRow() != 0) {
                spending[matrix.getColumnMatrix() - 1][matrix.getRow() - 1] = matrix.getValue();
            }
        }
        NorthWestCornerRule.northWestCornerRuleStart(require, deliver, spending);
        model.addAttribute("northCostResult",(int) NorthWestCornerRule.totalCost);
        model.addAttribute("northPotCostResult",(int) NorthWestCornerRule.totalCostPotential);
        NorthWestCornerRule.totalCostPotential = 0;
        NorthWestCornerRule.totalCost = 0;
        model.addAttribute("northWestTimer", NorthWestCornerRule.northTimer/1000);
        model.addAttribute("northWestPotTimer", NorthWestCornerRule.northPotTimer/1000);


        MinimumCostMethod.minimumCostMethod(matricesMinCost);
        model.addAttribute("minimumCostResult", MinimumCostMethod.totalCost);
        MinimumCostMethod.totalCost = 0;
        model.addAttribute("minimumCostTimer", MinimumCostMethod.minTimer/1000);

        model.addAttribute("northWestPotMatrix", NorthWestCornerRule.optMatrix);

        return "result";
    }
}
