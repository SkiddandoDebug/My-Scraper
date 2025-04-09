package src.main.resources;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFGenerator {

    private static final Logger LOGGER = Logger.getLogger(PDFGenerator.class.getName());
    private Consumer<String> logCallback;

    public PDFGenerator() {
        this.logCallback = s -> {};
    }

    public PDFGenerator(Consumer<String> logCallback) {
        this.logCallback = logCallback;
    }

    public void generatePdf(List<Article> articles, String outputPath) {
        FileOutputStream fos = null;
        Document document = new Document();

        try {
            logCallback.accept("Inizializzazione generazione PDF: " + outputPath);
            fos = new FileOutputStream(outputPath);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();
            logCallback.accept("Documento PDF creato con successo");

            // Add header
            logCallback.accept("Aggiunta intestazione al documento...");
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph titleParagraph = new Paragraph("Article Collection", headerFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);

            Paragraph dateParagraph = new Paragraph("Generated on: " + LocalDate.now(), dateFont);
            dateParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(dateParagraph);
            document.add(new Paragraph(" ")); // Empty line as spacer
            logCallback.accept("Intestazione aggiunta con successo");

            // Add articles
            logCallback.accept("Aggiunta di " + articles.size() + " articoli al documento...");
            int count = 0;
            for (Article article : articles) {
                count++;
                logCallback.accept("Elaborazione articolo " + count + "/" + articles.size() + ": " + article.getTitle());
                
                // Add title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
                Paragraph titlePara = new Paragraph(article.getTitle(), titleFont);
                titlePara.setSpacingBefore(10);
                document.add(titlePara);

                // Add subtitle if available
                if (article.getSubtitle() != null && !article.getSubtitle().isEmpty()) {
                    Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 14);
                    document.add(new Paragraph(article.getSubtitle(), subtitleFont));
                    logCallback.accept("Aggiunto sottotitolo");
                }

                // Add author if available
                if (article.getAuthor() != null && !article.getAuthor().isEmpty()) {
                    Font authorFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
                    Paragraph authorPara = new Paragraph("By: " + article.getAuthor(), authorFont);
                    authorPara.setSpacingAfter(5);
                    document.add(authorPara);
                    logCallback.accept("Aggiunto autore: " + article.getAuthor());
                }

                // Add image if available
                if (article.getImageData() != null && article.getImageData().length > 0) {
                    try {
                        logCallback.accept("Aggiunta immagine al documento...");
                        Image img = Image.getInstance(article.getImageData());
                        float width = document.getPageSize().getWidth() * 0.7f;
                        float height = img.getHeight() * width / img.getWidth();
                        img.scaleAbsolute(width, height);
                        img.setAlignment(Element.ALIGN_CENTER);
                        document.add(img);
                        logCallback.accept("Immagine aggiunta con successo");
                    } catch (Exception e) {
                        logCallback.accept("Errore durante l'aggiunta dell'immagine: " + e.getMessage());
                        LOGGER.log(Level.WARNING, "Failed to add image to PDF", e);
                    }
                }

                // Add content
                if (article.getContent() != null && !article.getContent().isEmpty()) {
                    logCallback.accept("Aggiunta contenuto dell'articolo...");
                    Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                    Paragraph contentPara = new Paragraph(article.getContent(), contentFont);
                    contentPara.setSpacingBefore(10);
                    document.add(contentPara);
                    logCallback.accept("Contenuto aggiunto con successo");
                }

                // Add separator between articles
                Paragraph separator = new Paragraph("* * *");
                separator.setAlignment(Element.ALIGN_CENTER);
                separator.setSpacingBefore(15);
                separator.setSpacingAfter(15);
                document.add(separator);
                
                logCallback.accept("Articolo " + count + " aggiunto con successo");
            }

            logCallback.accept("PDF generato con successo: " + outputPath);
            LOGGER.info("PDF successfully generated: " + outputPath);

        } catch (IOException e) {
            logCallback.accept("Errore I/O durante la generazione del PDF: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "I/O error generating PDF", e);
        } catch (DocumentException e) {
            logCallback.accept("Errore del documento durante la generazione del PDF: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Document error generating PDF", e);
        } catch (Exception e) {
            logCallback.accept("Errore imprevisto durante la generazione del PDF: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Unexpected error generating PDF", e);
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
                logCallback.accept("Documento PDF chiuso");
            }
            if (fos != null) {
                try {
                    fos.close();
                    logCallback.accept("File di output chiuso");
                } catch (IOException e) {
                    logCallback.accept("Errore durante la chiusura del file di output: " + e.getMessage());
                    LOGGER.log(Level.WARNING, "Error closing FileOutputStream", e);
                }
            }
        }
    }
}