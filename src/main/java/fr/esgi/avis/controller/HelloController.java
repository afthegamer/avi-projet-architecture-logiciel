package fr.esgi.avis.controller;

import fr.esgi.avis.business.Editeur;
import fr.esgi.avis.service.EditeurService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

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
    public void exportExcel(HttpServletResponse response) throws Exception{
        LOGGER.info("Début export Excel");
        var editeurs = editeurService.recupererEditeur();
        LOGGER.info("Nombre d'éditeurs à exporter: {}", editeurs.size());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"editeurs.xlsx\"");

        try (Workbook workbook = new XSSFWorkbook(); var baos = new java.io.ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Editeurs");
            Row header = sheet.createRow(0);
            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("ID");
            headerCell = header.createCell(1);
            headerCell.setCellValue("Nom de l'éditeur");
            headerCell = header.createCell(2);
            headerCell.setCellValue("Logo");
            int rowNum = 1;
            for (Editeur editeur : editeurs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(editeur.getId());
                row.createCell(1).setCellValue(editeur.getNom());
                row.createCell(2).setCellValue(editeur.getLogo());
            }
            // On passe par un buffer pour éviter les soucis de flux déjà utilisés,
            // puis on écrit une seule fois dans la réponse HTTP.
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.flushBuffer();
            LOGGER.info("Export Excel terminé ({} octets envoyés).", bytes.length);
        }
    }
    @GetMapping("/exportPdf")
    public void editerPdfVue(HttpServletResponse response) throws Exception{
        LOGGER.info("Début export PDF");
        var editeurs = editeurService.recupererEditeur();
        LOGGER.info("Nombre d'éditeurs à exporter: {}", editeurs.size());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=\"editeurs.pdf\"");

        try (var baos = new java.io.ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, baos);
            doc.open();
            for (Editeur editeur : editeurs) {
                doc.add(new Paragraph(String.valueOf(editeur.getId())));
                doc.add(new Paragraph(editeur.getNom()));
                doc.add(new Paragraph(editeur.getLogo()));
                doc.add(new Paragraph(" "));
            }
            doc.close(); // ferme le writer et pousse les octets dans le buffer

            byte[] bytes = baos.toByteArray();
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.flushBuffer();
            LOGGER.info("Export PDF terminé ({} octets envoyés).", bytes.length);
        }
    }

}
