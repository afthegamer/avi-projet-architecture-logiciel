package fr.esgi.avis.business;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Editeur}
 */
@Value
public class EditeurDto implements Serializable {
    Long id;
    @NotBlank(message = "Le nom de l'éditeur ne doit pas être vide")
    String nom;
    String logo;
}