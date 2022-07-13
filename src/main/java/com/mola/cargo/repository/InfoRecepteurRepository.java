package com.mola.cargo.repository;

import com.mola.cargo.model.InfoRecepteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRecepteurRepository extends JpaRepository<InfoRecepteur, Long> {
}

