package com.mola.cargo.repository;

import com.mola.cargo.model.Emballage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmballageRepository extends JpaRepository<Emballage, Long> {
}
