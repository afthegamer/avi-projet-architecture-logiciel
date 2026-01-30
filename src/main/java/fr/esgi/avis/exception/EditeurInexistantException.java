package fr.esgi.avis.exception;

public class EditeurInexistantException extends RuntimeException {
    // Exception métier simple pour signaler qu'un éditeur n'existe pas.
    // On l'utilise dans le service et un handler REST renvoie un 404.
    public EditeurInexistantException() {
        super("Editeur introuvable");
    }
}
