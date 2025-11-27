package fr.esgi.avis.controller.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.exception.EditeurInexistantException;
import fr.esgi.avis.service.EditeurService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editeurs")
public class EditeurRestController {

    private final EditeurService editeurService;

    public EditeurRestController(EditeurService editeurService) {
        this.editeurService = editeurService;
    }

    @GetMapping("/")
    @Operation(summary = "Get all editeurs")
    public List<Editeur> getAllEditeurs() {
        return editeurService.recupererEditeur();
    }

    @PostMapping("/addEdditeur/{name}{logo}")
    @Operation(summary = "Add a new editeur")
    public ResponseEntity<Editeur> addEditeur(@RequestBody Editeur editeur) {

        if ( editeur == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(editeurService.ajouterEditeur(editeur));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an editeur by id")
    public Editeur getEditeurById(@PathVariable Long id) {
        return editeurService.recupererEditeur(id);
    }
    
    @ExceptionHandler(EditeurInexistantException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String EditeurInexistantException(EditeurInexistantException e) {
        return e.getMessage();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an editeur by id")
    public void deleteEditeur(@PathVariable Long id) {
        editeurService.supprimerEditeur(id);
    }

    /*@PutMapping("/update/{id}")
    @Operation(summary = "Update an editeur by id")
    public ResponseEntity<Editeur> updateEditeur(@PathVariable Long id, @RequestBody Editeur editeur) {

        if ( editeur == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(editeurService.updateEditeur(id,editeur));
    }*/
    @PutMapping("/put/{id}")
    @Operation(summary = "Put an editeur by id")
    public ResponseEntity<Editeur> putEditeur(@PathVariable Long id, @RequestBody Editeur editeur) {

        if ( editeur == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(editeurService.putEditeur(id,editeur));
    }


}

