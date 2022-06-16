package com.mola.cargo.repository;

import com.mola.cargo.model.Encaissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncaissementRepository extends JpaRepository<Encaissement, Long> {
}
