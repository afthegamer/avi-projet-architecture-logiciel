package fr.esgi.avis.vue;

import fr.esgi.avis.business.Editeur;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import java.util.List;
import java.util.Map;

public class EditerExcelView extends AbstractXlsxView {


    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Sheet sheet = workbook.createSheet("Editeurs");
        // TODO: ajouter une ligne pour chaque editeur

        // Créer une ligne d'en-tête
        Row header = sheet.createRow(0);
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ID");
        headerCell = header.createCell(1);
        headerCell.setCellValue("Nom de l'éditeur");
        headerCell = header.createCell(2);
        headerCell.setCellValue("Logo");
        int rowNum = 1;
        for (Editeur editeur : (List <Editeur>)  model.get("editeurs")) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(((fr.esgi.avis.business.Editeur) editeur).getId());
            row.createCell(2).setCellValue(((fr.esgi.avis.business.Editeur) editeur).getNom());
            row.createCell(3).setCellValue(((fr.esgi.avis.business.Editeur) editeur).getLogo());
        }



    }
}
