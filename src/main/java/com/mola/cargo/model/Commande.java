package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int pin;
    @Column(name = "date_envoi")
    private Date dateEnvoi;

    @ManyToOne
    @JoinColumn(name = "pieceid", insertable = false, updatable = false)
    private Piece piece;
    private Long pieceid;

    @ManyToOne
    @JoinColumn(name = "paiementid", insertable = false, updatable = false)
    private Paiement paiement;
    private Long paiementid;

    @ManyToOne
    @JoinColumn(name = "colisid", insertable = false, updatable = false)
    private Colis colis;
    private Long colisid;

    @ManyToOne
    @JoinColumn(name = "emetteurid", insertable = false, updatable = false)
    private Emetteur emetteur;
    private Long emetteurid;

    @ManyToOne
    @JoinColumn(name = "recepteurid", insertable = false, updatable = false)
    private Recepteur recepteur;
    private Long recepteurid;


}
