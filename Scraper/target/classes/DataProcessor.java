package src.main.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

public class DataProcessor {
    private Consumer<String> logCallback;

    public DataProcessor() {
        this.logCallback = s -> {};
    }

    public DataProcessor(Consumer<String> logCallback) {
        this.logCallback = logCallback;
    }

    public List<Article> processData(List<Article> articles) {
        logCallback.accept("Inizio elaborazione di " + articles.size() + " articoli...");
        
        int count = 0;
        for (Article article : articles) {
            count++;
            logCallback.accept("Elaborazione articolo " + count + "/" + articles.size() + ": " + article.getTitle());
            
            // Pulizia e normalizzazione del testo
            cleanText(article);
            logCallback.accept("Testo pulito e normalizzato");

            // Elaborazione dell'immagine
            if (article.getImageUrl() != null && !article.getImageUrl().isEmpty() && article.getImageData() == null) {
                logCallback.accept("Elaborazione immagine: " + article.getImageUrl());
                try {
                    byte[] imageData = downloadImage(article.getImageUrl());
                    if (imageData != null) {
                        article.setImageData(imageData);
                        logCallback.accept("Immagine elaborata con successo");
                    } else {
                        logCallback.accept("Impossibile elaborare l'immagine");
                    }
                } catch (IOException e) {
                    logCallback.accept("Errore durante l'elaborazione dell'immagine: " + e.getMessage());
                }
            }
        }
        
        logCallback.accept("Elaborazione dati completata con successo");
        return articles;
    }

    private void cleanText(Article article) {
        // Implement text cleaning and normalization logic here
        if (article.getTitle() != null) {
            String cleanTitle = article.getTitle().trim()
                    .replaceAll("\\s+", " ")  // Replace multiple spaces with a single space
                    .replaceAll("^\\W+|\\W+$", ""); // Remove non-word characters from start and end
            article.setTitle(cleanTitle);
        }

        if (article.getSubtitle() != null) {
            String cleanSubtitle = article.getSubtitle().trim()
                    .replaceAll("\\s+", " ")
                    .replaceAll("^\\W+|\\W+$", "");
            article.setSubtitle(cleanSubtitle);
        }

        if (article.getContent() != null) {
            String cleanContent = article.getContent().trim()
                    .replaceAll("\\s+", " ")
                    .replaceAll("\\n\\s*\\n", "\n\n"); // Replace multiple newlines with double newline
            article.setContent(cleanContent);
        }
    }

    private byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            return in.readAllBytes();
        }
    }
}