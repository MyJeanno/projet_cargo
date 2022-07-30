package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "libelle_tarif")
    private String libelleTarif;
    private double prixKilo;
    private double taxeAerien;
    private double taxeMaritime;

    @ManyToOne
    @JoinColumn(name = "categorieId", insertable = false, updatable = false)
    private CategorieProduit categorieProduit;
    private Long categorieId;
}
