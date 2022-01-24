package com.mola.cargo.repository;

import com.mola.cargo.model.Codepostal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodepostalRepository extends JpaRepository<Codepostal, Long> {
}
