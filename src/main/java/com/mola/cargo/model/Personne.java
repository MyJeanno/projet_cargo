package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nom_personne")
    private String nomPersonne;
    @Column(name = "prenom_personne")
    private String prenomPersonne;
    @Column(name = "ville_personne")
    private String villePersonne;
    @Column(name = "contact_personne")
    private String contactPersonne;
    @Column(name = "email_personne")
    private String emailPersonne;
    @Column(name = "numero_personne")
    private String numeroPersonne;

    @Transient
    protected final String INITIAL_ENTREPRISE = "MO";


}
