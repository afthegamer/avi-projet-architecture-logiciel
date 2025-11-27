package fr.esgi.avis.service.impl;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.exception.EditeurInexistantException;
import fr.esgi.avis.repository.EditeurRepository;
import fr.esgi.avis.service.EditeurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditeurServiceImpl implements EditeurService {

    //Le service a besoin de faire appel au repository
    //(principe de délégation)

    private final EditeurRepository editeurRepository;

    public EditeurServiceImpl(EditeurRepository editeurRepository) {
        this.editeurRepository = editeurRepository;
    }

    @Override
    public Editeur ajouterEditeur(Editeur editeur) {
        return editeurRepository.save(editeur);
    }

    @Override
    public List<Editeur> recupererEditeur() {
        return editeurRepository.findAll();
    }

    @Override
    public Editeur recupererEditeur(Long id) {
        Optional<Editeur> editeurOptional = editeurRepository.findById(id);
        return editeurOptional.orElseThrow(
                EditeurInexistantException::new);
    }

    @Override
    public void supprimerEditeur(Long id) {
        if (editeurRepository.existsById(id)) {
            editeurRepository.deleteById(id);
        }else {
            throw new EditeurInexistantException();
        }
    }
}

