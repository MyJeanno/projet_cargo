package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartonAppro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int qteAppro;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateAppro;

    @ManyToOne
    @JoinColumn(name = "cartonid", insertable = false, updatable = false)
    private Carton carton;
    private Long cartonid;

    @ManyToOne
    @JoinColumn(name = "commandeCartonid", insertable = false, updatable = false)
    private CommandeCarton commandeCarton;
    private Long commandeCartonid;
}
