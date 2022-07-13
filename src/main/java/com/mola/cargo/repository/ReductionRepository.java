package com.mola.cargo.repository;

import com.mola.cargo.model.Reduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReductionRepository extends JpaRepository<Reduction, Long> {

    @Query(value = "select * from reduction limit 1", nativeQuery = true)
    Reduction laReduction();
}
