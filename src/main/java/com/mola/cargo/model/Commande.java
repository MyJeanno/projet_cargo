package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pin;
    @Column(name = "date_envoi")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEnvoi;
    @Column(name = "lieu_paiement")
    private String lieuPaiement;
    private String typeEnvoi;
    private String transport;
    private double reduction;

    @ManyToOne
    @JoinColumn(name = "pieceid", insertable = false, updatable = false)
    private Piece piece;
    private Long pieceid;

    @ManyToOne
    @JoinColumn(name = "emetteurid", insertable = false, updatable = false)
    private Emetteur emetteur;
    private Long emetteurid;

    @ManyToOne
    @JoinColumn(name = "recepteurid", insertable = false, updatable = false)
    private Recepteur recepteur;
    private Long recepteurid;

    @Transient
    private final int NBRE_INITIAL = 10000;
    @Transient
    private final int NBRE_FINAL = 99999;
    @Transient
    private final String  PREFIX_COMMANDE = "MF";
    @Transient
    private final String  PREFIX_COLIS = "MFA";
    @Transient
    private final String  PREFIX_COLIS_MARITIME = "MFM";

}
