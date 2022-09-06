package com.mola.cargo.repository;

import com.mola.cargo.model.CommandeCarton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommandeCartonRepository extends JpaRepository<CommandeCarton, Long> {
    /*
    @Query("select cc from CommandeCarton cc where cc.quantiteCommande >= (select SUM(ca) from CartonAppro ca where ca.commandeCartonid = ?1)")
    List<CommandeCarton> commandeNonNul(Long id);*/

    @Query(value = "select * from commande_carton order by id DESC LIMIT 1 ", nativeQuery = true)
    CommandeCarton lastCommandeCarton();

    @Transactional
    @Modifying
    @Query("update CommandeCarton c SET c.qtera = c.qtera - :qte WHERE c.id = :id")
    void updateCommandeCartonAppro(@Param("id") Long id, @Param("qte") int qteStock);

    @Transactional
    @Modifying
    @Query("update CommandeCarton c SET c.qterv = c.qterv - :qte WHERE c.id = :id")
    void updateCommandeCartonVente(@Param("id") Long id, @Param("qte") int qteStock);

    @Query("select cc from CommandeCarton cc where cc.qterv !=0")
    List<CommandeCarton> commandeVente();
}
