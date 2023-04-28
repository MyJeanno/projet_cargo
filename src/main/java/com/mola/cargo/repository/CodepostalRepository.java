package com.mola.cargo.repository;

import com.mola.cargo.model.Codepostal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CodepostalRepository extends JpaRepository<Codepostal, Long> {

    @Query(value = "select * from codepostal where libelle_code = :code LIMIT 1", nativeQuery = true)
    Codepostal etatCodePostal(@Param("code") int code);
}
