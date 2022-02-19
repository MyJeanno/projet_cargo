package com.mola.cargo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProduitAerien extends Produit{

    private double poids;
    @Column(name = "prix_produit")
    private double prixProduit;

    @ManyToOne
    @JoinColumn(name = "tarifid", insertable = false, updatable = false)
    private Tarif tarif;
    private Long tarifid;

    @ManyToOne
    @JoinColumn(name = "colisAerienid", insertable = false, updatable = false)
    private ColisAerien colisAerien;
    private Long colisAerienid;

}
