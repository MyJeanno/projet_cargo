package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Codepostal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "libelle_code")
    private int libelleCode;

    @ManyToOne
    @JoinColumn(name = "etatcode", insertable = false, updatable = false)
    private Etat etat;
    private Long etatcode;
}
