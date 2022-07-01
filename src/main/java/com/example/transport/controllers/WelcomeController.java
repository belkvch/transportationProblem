package com.example.transport.controllers;

import com.example.transport.entity.*;
import com.example.transport.repository.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/start")
public class WelcomeController {
    private final MatrixInfoRepo matrixInfoService;
    private final MatrixRepo matrixService;

    @GetMapping
    public String welcome(Model model) {
        matrixService.deleteAll();
        matrixInfoService.deleteAll();
        MatrixInfo newMatrixInfo = new MatrixInfo();
        newMatrixInfo.setId(1);
        matrixInfoService.save(newMatrixInfo);
        model.addAttribute("newMatrixInfo", newMatrixInfo);
        return "start";
    }

    @PostMapping()
    public String enterRowAndColumn(Model model, @RequestParam(value = "id") int id, @RequestParam(value = "row") int row, @RequestParam(value = "column") int column) {
        MatrixInfo newMatrix = matrixInfoService.getById(id);
        newMatrix.setRows(row+1);
        newMatrix.setColumns(column+1);
        matrixInfoService.save(newMatrix);

        return "redirect:/createMatrix";
    }
}

