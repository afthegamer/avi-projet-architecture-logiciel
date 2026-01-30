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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Tests unitaires du service Editeur avec un repository mocke.
 */
class EditeurServiceImplTest {

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

        // Act
        Editeur result = editeurServiceImpl.ajouterEditeur(new Editeur("nom", "logo"));

        // Assert
        Assertions.assertEquals(editeur, result);
        verify(editeurRepository, times(1)).save(any(Editeur.class));
    }

    @Test
    void testRecupererEditeurs() {
        // Arrange
        List<Editeur> expected = List.of(new Editeur("nom", "logo"));
        when(editeurRepository.findAll()).thenReturn(expected);

        // Act
        List<Editeur> result = editeurServiceImpl.recupererEditeur();

        // Assert
        Assertions.assertEquals(expected, result);
        verify(editeurRepository, times(1)).findAll();
    }

    @Test
    void testRecupererEditeurByIdFound() {
        // Arrange
        Editeur editeur = new Editeur("nom", "logo");
        when(editeurRepository.findById(1L)).thenReturn(Optional.of(editeur));

        // Act
        Editeur result = editeurServiceImpl.recupererEditeur(1L);

        // Assert
        Assertions.assertEquals(editeur, result);
        verify(editeurRepository, times(1)).findById(1L);
    }

    @Test
    void testRecupererEditeurByIdNotFound() {
        // Arrange
        when(editeurRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        Assertions.assertThrows(EditeurInexistantException.class,
                () -> editeurServiceImpl.recupererEditeur(1L));
        verify(editeurRepository, times(1)).findById(1L);
    }

    @Test
    void testSupprimerEditeurExists() {
        // Arrange
        when(editeurRepository.existsById(1L)).thenReturn(true);

        // Act
        editeurServiceImpl.supprimerEditeur(1L);

        // Assert
        verify(editeurRepository, times(1)).existsById(1L);
        verify(editeurRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSupprimerEditeurNotExists() {
        // Arrange
        when(editeurRepository.existsById(1L)).thenReturn(false);

        // Act + Assert
        Assertions.assertThrows(EditeurInexistantException.class,
                () -> editeurServiceImpl.supprimerEditeur(1L));
        verify(editeurRepository, times(1)).existsById(1L);
        verify(editeurRepository, never()).deleteById(anyLong());
    }

    @Test
    void testPutEditeurExists() {
        // Arrange
        Editeur editeur = new Editeur("nom", "logo");
        when(editeurRepository.existsById(1L)).thenReturn(true);
        when(editeurRepository.save(any(Editeur.class))).thenReturn(editeur);

        // Act
        Editeur result = editeurServiceImpl.putEditeur(1L, editeur);

        // Assert
        Assertions.assertEquals(editeur, result);
        ArgumentCaptor<Editeur> captor = ArgumentCaptor.forClass(Editeur.class);
        verify(editeurRepository, times(1)).save(captor.capture());
        Assertions.assertEquals(1L, captor.getValue().getId());
    }

    @Test
    void testPutEditeurNotExists() {
        // Arrange
        when(editeurRepository.existsById(1L)).thenReturn(false);

        // Act + Assert
        Assertions.assertThrows(EditeurInexistantException.class,
                () -> editeurServiceImpl.putEditeur(1L, new Editeur("nom", "logo")));
        verify(editeurRepository, times(1)).existsById(1L);
        verify(editeurRepository, never()).save(any(Editeur.class));
    }
}
