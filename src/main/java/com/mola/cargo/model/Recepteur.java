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
public class Recepteur extends Personne{
    private String rue;
    @Column(name = "code_postal")
    private int codePostal;
    @Transient
    private final String SITUATION_RECEPTEUR="En cours";

    @ManyToOne
    @JoinColumn(name = "paysid", insertable = false, updatable = false)
    private Pays pays;
    private Long paysid;
}
