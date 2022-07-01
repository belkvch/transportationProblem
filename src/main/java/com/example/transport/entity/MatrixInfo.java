package com.example.transport.entity;

import lombok.*;
import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Setter
@Table(name = "matrixInfo")
public class MatrixInfo {
    @Id
    private int id;

    private int columns;
    private int rows;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "matrix_info_id")
    private List<Matrix> values;
}
