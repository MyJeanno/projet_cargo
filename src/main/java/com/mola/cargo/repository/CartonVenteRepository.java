package com.mola.cargo.repository;

import com.mola.cargo.model.CartonVente;
import com.mola.cargo.model.VueCommandeStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CartonVenteRepository extends JpaRepository<CartonVente, Long> {

    @Query("select c, SUM(c.carton.prixCarton) from CartonVente c group by c.commandeCarton.id")
    List<CartonVente> etatventeCarton();

    @Query(value = "SELECT cc.libelle_commande,\n" +
            "            cc.quantite_commande,\n" +
            "            (cc.quantite_commande-cc.qterv),\n" +
            "            cc.qterv,\n" +
            "            cc.montant_commande,\n" +
            "            SUM(c.prix_carton*ca.qte_vente),\n" +
            "            (SUM(c.prix_carton*ca.qte_vente)-cc.montant_commande)\n" +
            "            FROM commande_carton cc join carton_vente ca\n" +
            "            on ca.commande_cartonid = cc.id\n" +
            "            join carton c on c.id = ca.cartonid\n" +
            "            WHERE date_commande BETWEEN ?1 AND ?2\n" +
            "            GROUP by cc.libelle_commande, cc.quantite_commande, cc.montant_commande, cc.qterv", nativeQuery = true)
    List<String> etatventeCarton2(LocalDate d1, LocalDate d2);
}
