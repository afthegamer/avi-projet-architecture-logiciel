package fr.esgi.avis.repository;

import fr.esgi.avis.business.Editeur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repository = couche d'accès aux données pour l'entité Editeur.
public interface EditeurRepository extends JpaRepository<Editeur, Long> {
    Optional <Editeur> findByNom(String nom);
    Editeur findByLogo(String logo);
    List<Editeur> findByLogoContaining(String logoPart);
    Editeur findByNomAndLogo(String nom, String logo);
    Editeur findByNomOrLogo(String nom, String logo);
}
