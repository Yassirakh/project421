package com.uppa.project421.dao;

import com.uppa.project421.entities.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface JoueurRepository extends JpaRepository<Joueur, Long> {
    public Collection<Joueur> findByPseudo(String pseudo);
    public Collection<Joueur> findByPseudoAndMotdepasse(String pseudo, String motdepasse);
}
