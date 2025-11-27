package fr.esgi.avis.repository;

import fr.esgi.avis.business.Editeur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditeurRepository extends JpaRepository<Editeur, Long> {
    Editeur findByNom(String nom);
    Editeur findByLogo(String logo);
    Editeur findByNomAndLogo(String nom, String logo);
    Editeur findByNomOrLogo(String nom, String logo);
}