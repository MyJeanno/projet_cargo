package com.mola.cargo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProduitMaritime extends Produit{

    @ManyToOne
    @JoinColumn(name = "colisMaritimeid", insertable = false, updatable = false)
    private ColisMaritime colisMaritime;
    private Long colisMaritimeid;
}
