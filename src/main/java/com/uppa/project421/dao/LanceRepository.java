package com.uppa.project421.dao;

import com.uppa.project421.entities.Lance;
import com.uppa.project421.entities.Partie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;

public interface LanceRepository extends JpaRepository<Lance, Long> {
    public Collection<Lance> findByIdlance(Long id);

}
