package fr.esgi.avis.service.impl;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.exception.EditeurInexistantException;
import fr.esgi.avis.repository.EditeurRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        assertThat(result).isEqualTo(editeur);
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
        assertThat(result)
                .hasSize(1)
                .containsExactlyElementsOf(expected)
                .allSatisfy(e -> {
                    assertThat(e.getNom()).isEqualTo("nom");
                    assertThat(e.getLogo()).isEqualTo("logo");
                });
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
        assertThat(result).isEqualTo(editeur);
        verify(editeurRepository, times(1)).findById(1L);
        LOGGER.info("Vérifications OK pour testRecupererEditeurByIdFound");
    }

    @Test
    void testRecupererEditeurByIdNotFound() {
        // Arrange
        when(editeurRepository.findById(1L)).thenReturn(Optional.empty());
        LOGGER.info("Préparation du testRecupererEditeurByIdNotFound avec id=1 (empty)");

        // Act + Assert
        assertThatThrownBy(() -> editeurServiceImpl.recupererEditeur(1L))
                .isInstanceOf(EditeurInexistantException.class);
        verify(editeurRepository, times(1)).findById(1L);
        LOGGER.info("Exception attendue levée pour testRecupererEditeurByIdNotFound");
    }

    @Test
    void testRecupererEditeurAvecLogsDetaillees() {
        // Arrange
        long id = 42L;
        Editeur editeur = new Editeur("nom", "logo");
        when(editeurRepository.findById(id)).thenReturn(Optional.of(editeur));
        LOGGER.info("Préparation du testRecupererEditeurAvecLogsDetaillees id={}, editeur={}", id, editeur);

        // Act
        Editeur result = editeurServiceImpl.recupererEditeur(id);
        LOGGER.info("Résultat de recupererEditeur pour id {}: {}", id, result);

        // Assert
        assertThat(result).isEqualTo(editeur);
        verify(editeurRepository, times(1)).findById(id);
        LOGGER.info("Vérifications OK pour testRecupererEditeurAvecLogsDetaillees");
    }

    @Test
    void testRecupererEditeurAvecLogsDetailleesNotFound() {
        // Arrange
        long id = 99L;
        when(editeurRepository.findById(id)).thenReturn(Optional.empty());
        LOGGER.info("Préparation du testRecupererEditeurAvecLogsDetailleesNotFound id={} (empty)", id);

        // Act + Assert
        assertThatThrownBy(() -> editeurServiceImpl.recupererEditeur(id))
                .isInstanceOf(EditeurInexistantException.class);
        verify(editeurRepository, times(1)).findById(id);
        LOGGER.info("Exception attendue levée pour testRecupererEditeurAvecLogsDetailleesNotFound");
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
        assertThatThrownBy(() -> editeurServiceImpl.supprimerEditeur(1L))
                .isInstanceOf(EditeurInexistantException.class);
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
        assertThat(result).isEqualTo(editeur);
        ArgumentCaptor<Editeur> captor = ArgumentCaptor.forClass(Editeur.class);
        verify(editeurRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(1L);
        LOGGER.info("Vérifications OK pour testPutEditeurExists");
    }

    @Test
    void testPutEditeurNotExists() {
        // Arrange
        when(editeurRepository.existsById(1L)).thenReturn(false);
        LOGGER.info("Préparation du testPutEditeurNotExists avec id=1 (n'existe pas)");

        // Act + Assert
        assertThatThrownBy(() -> editeurServiceImpl.putEditeur(1L, new Editeur("nom", "logo")))
                .isInstanceOf(EditeurInexistantException.class);
        verify(editeurRepository, times(1)).existsById(1L);
        verify(editeurRepository, never()).save(any(Editeur.class));
        LOGGER.info("Exception attendue levée pour testPutEditeurNotExists");
    }
}
