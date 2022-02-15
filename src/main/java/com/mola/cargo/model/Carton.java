package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carton {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "libelle_carton")
    private String libelleCarton;
    private String mesure;
    private double prixCarton;
    @Column(name = "poid_autorise")
    private double poidAutorise;
    @Column(name = "qte_stock")
    private int qteStock;
}
