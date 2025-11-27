package fr.esgi.avis.service;

import fr.esgi.avis.business.Editeur;

import java.util.List;

public interface EditeurService {

    //c'est une histoire d'amour

    Editeur ajouterEditeur(Editeur editeur);

    List<Editeur> recupererEditeur();

    Editeur recupererEditeur(Long id);

    void supprimerEditeur(Long id);

    Editeur putEditeur(Long id, Editeur editeur);
}
