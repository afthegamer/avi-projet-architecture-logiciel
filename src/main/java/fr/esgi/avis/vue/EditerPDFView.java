package fr.esgi.avis.vue;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import fr.esgi.avis.business.Editeur;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.List;
import java.util.Map;

public class EditerPDFView extends AbstractPdfView{

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO: ajouter une ligne pour chaque editeur
        for (Editeur editeur : (List <Editeur>)  model.get("editeurs")) {
            doc.add(new Paragraph(editeur.getId()));
            doc.add(new Paragraph(editeur.getNom()));
            doc.add(new Paragraph(editeur.getLogo()));
        }
    }
}
