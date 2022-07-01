package com.example.transport.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

@JsonAutoDetect
@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Setter
@Table(name = "matrix")
public class Matrix {
    @Id
    private int id;

    @ColumnDefault("false")
    private boolean isColor;

    private int columnMatrix;
    private int row;
    private int value;

    @Column(name = "matrix_info_id")
    private int matrix;
}
