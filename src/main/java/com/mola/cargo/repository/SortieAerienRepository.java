package com.mola.cargo.repository;

import com.mola.cargo.model.SortieAerien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SortieAerienRepository extends JpaRepository<SortieAerien, Long> {

}
