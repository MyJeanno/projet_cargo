package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String designation;
    private int quantite;
    private double poids;
    private double valeurMarchande;

    @ManyToOne
    @JoinColumn(name = "tarifid", insertable = false, updatable = false)
    private Tarif tarif;
    private Long tarifid;
}
