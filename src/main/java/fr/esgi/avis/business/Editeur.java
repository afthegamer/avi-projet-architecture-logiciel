package fr.esgi.avis.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Cette classe fait partie du modèle
 * dans notre modèle MVC
 */
@Entity
@Data
public class Editeur {

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank(message = "Le nom de l'éditeur ne doit pas être vide")
    private String nom;
    private String logo;

    public Editeur() {}
    public Editeur(String nom) {
        this.nom = nom;
    }

    public Editeur(String nom, String logo) {
        this.nom = nom;
        this.logo = logo;
    }
}