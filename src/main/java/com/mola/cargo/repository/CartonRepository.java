package com.mola.cargo.repository;

import com.mola.cargo.model.Carton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartonRepository extends JpaRepository<Carton, Long> {

    @Transactional
    @Modifying
    //@Query(value = "update carton set qte_stock=qte_stock + (:qte) WHERE id = :idCa", nativeQuery = true)
    @Query("update Carton c SET c.qteStock = c.qteStock + :qte WHERE c.id = :id")
    void updateCartonAddQteStock(@Param("id") Long id, @Param("qte") int qteStock);

    @Transactional
    @Modifying
    @Query("update Carton c SET c.qteStock = c.qteStock - :qte WHERE c.id = :id")
    void updateCartonRemoveQteStock(@Param("id") Long id, @Param("qte") int qteStock);

    @Query("select c from Carton c WHERE c.id = ?1")
    Carton findQteStockById(Long id);

}