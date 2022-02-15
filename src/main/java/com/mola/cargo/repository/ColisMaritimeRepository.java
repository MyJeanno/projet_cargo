package com.mola.cargo.repository;

import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisMaritimeRepository extends JpaRepository<ColisMaritime, Long> {

    @Query("select cm from ColisMaritime cm where cm.commandeid = ?1")
    List<ColisMaritime> findColisMaritimeCommande(Long id);
}
