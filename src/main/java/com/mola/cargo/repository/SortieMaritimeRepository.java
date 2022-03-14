package com.mola.cargo.repository;

import com.mola.cargo.model.SortieMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SortieMaritimeRepository extends JpaRepository<SortieMaritime, Long> {

}
