package com.mola.cargo.repository;

import com.mola.cargo.model.Augmentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AugmentationRepository extends JpaRepository<Augmentation, Long> {
}
