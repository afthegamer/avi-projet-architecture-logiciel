package fr.esgi.avis.repository;


import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.repository.EditeurRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

/**
 * Cette classe de test vérifie le bon fonctionnement de la repository editeurRepository
 */

@DataJpaTest
class EditeurRepositoryTest {
    @Autowired
    EditeurRepository editeurRepository;

    @Test
    void testFindByNom(){
        //Arrange

        String nomEditeur = "test";
        Editeur editeur = new Editeur();
        editeur.setNom(nomEditeur);
        editeurRepository.save(editeur);

        //Acte : invoque la méthode qui fait l'objet du test

        Optional<Editeur> resultat = editeurRepository.findByNom(nomEditeur);

        //Assert : Execute des méthodes de vérification

        //On vérifie que l'éditeur est bien présent dans l'objet optional
        Assertions.assertTrue(resultat.isPresent());

    }
    @Test
    @Sql("/ajouteEditeur.sql")
    void testFindByLogoContaining()
    {
        // Arrange
        String filter = ".png";

        // Act
        List<Editeur> result = editeurRepository.findByLogoContaining(filter);

        // Assert
        Assertions.assertEquals(2, result.size());
    }
}


//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme``
