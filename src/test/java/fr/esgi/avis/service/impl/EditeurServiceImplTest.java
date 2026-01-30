package fr.esgi.avis.service.impl;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.exception.EditeurInexistantException;
import fr.esgi.avis.repository.EditeurRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Tests unitaires du service Editeur avec un repository mocke.
 */
class EditeurServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditeurServiceImplTest.class);

    @Mock
    EditeurRepository editeurRepository;

    @InjectMocks
    EditeurServiceImpl editeurServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAjouterEditeur() {
        // Arrange
        Editeur editeur = new Editeur("nom", "logo");
        when(editeurRepository.save(any(Editeur.class))).thenReturn(editeur);
        LOGGER.info("Préparation du testAjouterEditeur avec editeur={}", editeur);

        // Act
        Editeur result = editeurServiceImpl.ajouterEditeur(new Editeur("nom", "logo"));
        LOGGER.info("Résultat du save: {}", result);

        // Assert
        Assertions.assertEquals(editeur, result);
        verify(editeurRepository, times(1)).save(any(Editeur.class));
        LOGGER.info("Vérifications OK pour testAjouterEditeur");
    }

    @Test
    void testRecupererEditeurs() {
        // Arrange
        List<Editeur> expected = List.of(new Editeur("nom", "logo"));
        when(editeurRepository.findAll()).thenReturn(expected);
        LOGGER.info("Préparation du testRecupererEditeurs avec expected={}", expected);

        // Act
        List<Editeur> result = editeurServiceImpl.recupererEditeur();
        LOGGER.info("Résultat findAll: {}", result);

        // Assert
        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("nom", result.get(0).getNom());
        Assertions.assertEquals("logo", result.get(0).getLogo());
        verify(editeurRepository, times(1)).findAll();
        LOGGER.info("Vérifications OK pour testRecupererEditeurs");
    }

    @Test
    void testRecupererEditeurByIdFound() {
        // Arrange
        Editeur editeur = new Editeur("nom", "logo");
        when(editeurRepository.findById(1L)).thenReturn(Optional.of(editeur));
        LOGGER.info("Préparation du testRecupererEditeurByIdFound avec id=1, editeur={}", editeur);

        // Act
        Editeur result = editeurServiceImpl.recupererEditeur(1L);
        LOGGER.info("Résultat findById: {}", result);

        // Assert
        Assertions.assertEquals(editeur, result);
        verify(editeurRepository, times(1)).findById(1L);
        LOGGER.info("Vérifications OK pour testRecupererEditeurByIdFound");
    }

    @Test
    void testRecupererEditeurByIdNotFound() {
        // Arrange
        when(editeurRepository.findById(1L)).thenReturn(Optional.empty());
        LOGGER.info("Préparation du testRecupererEditeurByIdNotFound avec id=1 (empty)");

        // Act + Assert
        Assertions.assertThrows(EditeurInexistantException.class,
                () -> editeurServiceImpl.recupererEditeur(1L));
        verify(editeurRepository, times(1)).findById(1L);
        LOGGER.info("Exception attendue levée pour testRecupererEditeurByIdNotFound");
    }

    @Test
    void testSupprimerEditeurExists() {
        // Arrange
        when(editeurRepository.existsById(1L)).thenReturn(true);
        LOGGER.info("Préparation du testSupprimerEditeurExists avec id=1 (existe)");

        // Act
        editeurServiceImpl.supprimerEditeur(1L);
        LOGGER.info("Suppression effectuée pour id=1");

        // Assert
        verify(editeurRepository, times(1)).existsById(1L);
        verify(editeurRepository, times(1)).deleteById(1L);
        LOGGER.info("Vérifications OK pour testSupprimerEditeurExists");
    }

    @Test
    void testSupprimerEditeurNotExists() {
        // Arrange
        when(editeurRepository.existsById(1L)).thenReturn(false);
        LOGGER.info("Préparation du testSupprimerEditeurNotExists avec id=1 (n'existe pas)");

        // Act + Assert
        Assertions.assertThrows(EditeurInexistantException.class,
                () -> editeurServiceImpl.supprimerEditeur(1L));
        verify(editeurRepository, times(1)).existsById(1L);
        verify(editeurRepository, never()).deleteById(anyLong());
        LOGGER.info("Exception attendue levée pour testSupprimerEditeurNotExists");
    }

    @Test
    void testPutEditeurExists() {
        // Arrange
        Editeur editeur = new Editeur("nom", "logo");
        when(editeurRepository.existsById(1L)).thenReturn(true);
        when(editeurRepository.save(any(Editeur.class))).thenReturn(editeur);
        LOGGER.info("Préparation du testPutEditeurExists avec id=1, editeur={}", editeur);

        // Act
        Editeur result = editeurServiceImpl.putEditeur(1L, editeur);
        LOGGER.info("Résultat save put: {}", result);

        // Assert
        Assertions.assertEquals(editeur, result);
        ArgumentCaptor<Editeur> captor = ArgumentCaptor.forClass(Editeur.class);
        verify(editeurRepository, times(1)).save(captor.capture());
        Assertions.assertEquals(1L, captor.getValue().getId());
        LOGGER.info("Vérifications OK pour testPutEditeurExists");
    }

    @Test
    void testPutEditeurNotExists() {
        // Arrange
        when(editeurRepository.existsById(1L)).thenReturn(false);
        LOGGER.info("Préparation du testPutEditeurNotExists avec id=1 (n'existe pas)");

        // Act + Assert
        Assertions.assertThrows(EditeurInexistantException.class,
                () -> editeurServiceImpl.putEditeur(1L, new Editeur("nom", "logo")));
        verify(editeurRepository, times(1)).existsById(1L);
        verify(editeurRepository, never()).save(any(Editeur.class));
        LOGGER.info("Exception attendue levée pour testPutEditeurNotExists");
    }
}
