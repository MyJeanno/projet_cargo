package com.mola.cargo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ColisMaritime extends Colis {

    @ManyToOne
    @JoinColumn(name = "cartonid", insertable = false, updatable = false)
    private Carton carton;
    private Long cartonid;

    @Transient
    @ManyToOne
    @JoinColumn(name = "paysid", insertable = false, updatable = false)
    private Pays pays;
    @Transient
    private Long paysid;
}
