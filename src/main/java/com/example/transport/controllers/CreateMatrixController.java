package com.example.transport.controllers;

import com.example.transport.entity.*;
import com.example.transport.repository.*;
import lombok.*;
import org.json.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/createMatrix")
public class CreateMatrixController {
    private final MatrixRepo matrixRepo;
    private final MatrixInfoRepo matrixInfoRepo;

    @GetMapping
    public String create(Model model) {
        MatrixInfo matrixInfo = matrixInfoRepo.findByIdMax();
        model.addAttribute("columnAmount", matrixInfo.getColumns());
        model.addAttribute("rowsAmount", matrixInfo.getRows());

        List<Matrix> matrices = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < matrixInfo.getColumns(); i++) {
            for (int j = 0; j < matrixInfo.getRows(); j++) {
                Matrix newMatrixWithValue = new Matrix();
                newMatrixWithValue.setValue(0);
                newMatrixWithValue.setRow(j);
                newMatrixWithValue.setColumnMatrix(i);
                newMatrixWithValue.setId(id);
                newMatrixWithValue.setMatrix(matrixInfo.getId());
                matrixRepo.save(newMatrixWithValue);
                matrices.add(newMatrixWithValue);
                id++;
                if (newMatrixWithValue.getRow() == 0) {
                    newMatrixWithValue.setColor(true);
                    matrixRepo.save(newMatrixWithValue);
                }
                if (newMatrixWithValue.getColumnMatrix() == 0) {
                    newMatrixWithValue.setColor(true);
                    matrixRepo.save(newMatrixWithValue);
                }
            }
        }
        model.addAttribute("matrices", matrices);
        return "createMatrix";
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createMatrix(@RequestBody String payload, Model model) throws JSONException {

        JSONObject object = new JSONObject(payload);
        JSONArray the_json_array = object.getJSONArray("value");

        List<String> exampleList = new ArrayList<String>();
        for (int i = 0; i < the_json_array.length(); i++) {
            exampleList.add(the_json_array.getString(i));
        }

        int size = exampleList.size();
        String[] valuesArrayString = exampleList.toArray(new String[size]);

        int [] valuesArray = new int[valuesArrayString.length];
        for (int i = 0; i < valuesArrayString.length; i++) {
            valuesArray[i] = (Integer.parseInt (valuesArrayString[i].trim ()));
        }

        System.out.print("Output int array will be : ");
        for (int s : valuesArray) {
            System.out.print(s + " ");
        }

        for (int i = 0; i < valuesArray.length; i++) {
            Matrix matrixForEnterValue = matrixRepo.getById(i);
            matrixForEnterValue.setValue(valuesArray[i]);
            matrixRepo.save(matrixForEnterValue);
        }

        return "redirect:/result";
    }

}
