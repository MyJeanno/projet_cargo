package com.mola.cargo.util;

import com.mola.cargo.model.User;
import com.mola.cargo.service.CustomUserDetails;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
public class Constante {

    public static final String INITIAL = "EN DEPOT";
    public static final String SORTIE = "SORTIE";
    //public static final String INVENTAIRE = "COMPTABILISE";
    public static final String PREFIX_MARITIME = "MFM_DEPART";
    public static final String PREFIX_AERIEN = "MFA_DEPART";
    public static final String STATUT_CONVOI_NOUVEAU = "NOUVEAU";
    public static final String STATUT_CONVOI_ANCIEN = "ANCIEN";
    public static final String STATUT_COMMANDE_ACHEVE = "ACHEVE";
    public static final String STATUT_COMMANDE_INACHEVE = "INACHEVE";
    public static final String STATUT_COMMANDE_CREE = "COMMANDE";
    public static final String STATUT_COLIS_CREE = "COLIS";
    public static final String STATUT_PRODUIT_CREE = "PRODUIT";

    public static final String INVENTAIRE_NON_ENCAISSE = "NON ENCAISSE";
    public static final String INVENTAIRE_ENCAISSE = "ENCAISSE";
    public static final String INVENTAIRE_ATTENTE = "EN ATTENTE";
    public static final String ENVOI_MARITIME = "MARITIME";
    public static final String ENVOI_AERIEN = "AERIEN";
    public static final String LIEU_TOGO = "Togo";
    public static final String LIEU_ALL = "Allemagne";
    public static final String TAXE_OUI = "Oui";
    public static final String TAXE_NON = "Non";

    public static final String CLIENT_BLOQUE = "BLOQUE";
    public static final String CLIENT_AUTORISE = "AUTORISE";
    public static final String MF_COMMANDE_CARTON = "MF_COMMANDE_CARTON";

    public static final int BORNE_INF_CLIENT = 1000;
    public static final int BORNE_SUP_CLIENT = 9999;

    public static final int MOIS_JANVIER = 1;
    public static final int MOIS_FEVRIER = 2;
    public static final int MOIS_MARS = 3;
    public static final int MOIS_AVRIL = 4;
    public static final int MOIS_MAI = 5;
    public static final int MOIS_JUIN = 6;
    public static final int MOIS_JUILLET = 7;
    public static final int MOIS_AOUT = 8;
    public static final int MOIS_SEPTEMBRE = 9;
    public static final int MOIS_OCTOBRE = 10;
    public static final int MOIS_NOVEMBRE = 11;
    public static final int MOIS_DECEMBRE = 12;

    public static User showUserConnecte() {
        User user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
        } else {
            user = (User)principal;
        }
        return user;
    }

}
