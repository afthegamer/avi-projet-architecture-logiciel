package fr.esgi.avis.controller;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.service.EditeurService;
import fr.esgi.avis.vue.EditerExcelView;
import fr.esgi.avis.vue.EditerPDFView;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@Controller//anotation java pour faire comprendre a spring que cette classe va traiter les requetes http : c'est le rôle du controller


public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    private final EditeurService editeurService;
    @Value("${logo-dev-token:}")
    private String logoDevToken;

    public HelloController(EditeurService editeurService) {
        this.editeurService = editeurService;
    }

    @PostConstruct
    public void init(){
        if (logoDevToken == null || logoDevToken.isBlank()) {
            logoDevToken = "${LOGO_DEV_TOKEN}";
        }
        // On crée quelques éditeurs dès le démarrage pour alimenter la page d’accueil.
        editeurService.ajouterEditeur(new Editeur("Ubisoft","ubisoft.com"));
        editeurService.ajouterEditeur(new Editeur("Bandai Namco","bandainamco.com"));
        editeurService.ajouterEditeur(new Editeur("Konami","konami.com"));
        editeurService.ajouterEditeur(new Editeur("Tencent","tencent.com"));
        editeurService.ajouterEditeur(new Editeur("Capcom","capcom.com"));
        editeurService.ajouterEditeur(new Editeur("Riot Games","riotgames.com"));
        editeurService.ajouterEditeur(new Editeur("CD Projekt","cdprojekt.com"));
        //Editeur e = Editeur.builder().nom("").build();
    }

    @GetMapping("/")
    public String hello(Model model) {
        //la vue va recevoir un enssemeble d'attirbue
        var editeurs = editeurService.recupererEditeur();
        LOGGER.info("Nombre d'éditeurs envoyés à la vue: {}", editeurs.size());
        model.addAttribute("editeurs", editeurs);
        model.addAttribute("logoDevToken", logoDevToken);
        //la méthode du controller utilise une vue qui s'appelle index.html
        //index.html est dans le dossier resources/templates
        return "index.html";
    }
    @GetMapping("/exportExcel")
    public ModelAndView exportExcel(){
        ModelAndView mav = new ModelAndView( new EditerExcelView());
        //deux ligne équivalente
        //mav.model().put("editeurs",editeurs);
        mav.addObject("editeurs", editeurService.recupererEditeur());
        return mav;
    }
    @GetMapping("/exportPdf")
    public ModelAndView editerPdfVue(){
        ModelAndView mav = new ModelAndView( new EditerPDFView());
        mav.addObject("editeurs", editeurService.recupererEditeur());
        return mav;

    }

}
