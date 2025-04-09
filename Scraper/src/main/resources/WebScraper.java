package src.main.resources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WebScraper {

    private static final String CONTENT_SEPARATOR = "\n--------------------------\n";
    private Consumer<String> logCallback;

    public WebScraper() {
        this.logCallback = s -> {};
    }

    public WebScraper(Consumer<String> logCallback) {
        this.logCallback = logCallback;
    }

    public List<Article> Scraper(String url) {
        List<Article> articles = new ArrayList<>();
        try {
            logCallback.accept("Connessione al sito: " + url);
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000)
                    .get();
            
            logCallback.accept("Connessione stabilita. Analisi della struttura della pagina...");
            
            // Try multiple selectors to find article elements
            Elements articleElements = doc.select("article, .news-item, .post, .entry, .article, .story, div[class*=article], div[class*=post], div[class*=news]");
            
            if (articleElements.isEmpty()) {
                logCallback.accept("Nessun elemento articolo trovato con i selettori standard. Provando selettori alternativi...");
                // Try more generic selectors if no articles found
                articleElements = doc.select("div:has(h1), div:has(h2), div:has(h3), section");
            }
            
            logCallback.accept("Trovati " + articleElements.size() + " possibili elementi articolo. Inizio estrazione...");

            int count = 0;
            for (Element articleElement : articleElements) {
                count++;
                logCallback.accept("Elaborazione elemento " + count + "/" + articleElements.size() + "...");
                
                // Extract with null checks for optional fields
                String title = extractText(articleElement, "h1, h2, h3, .title, .headline");
                if (title.isEmpty()) {
                    logCallback.accept("Elemento " + count + " saltato: nessun titolo trovato");
                    continue; // Skip articles without a title
                }

                logCallback.accept("Titolo trovato: " + title);
                
                String subtitle = extractText(articleElement, ".subtitle, .summary, .excerpt, .description");
                String content = extractText(articleElement, ".content, .entry-content, .article-content, .post-content, .text, p");
                // Format content with separator if available
                if (!content.isEmpty()) {
                    content = CONTENT_SEPARATOR + content + CONTENT_SEPARATOR;
                }
                
                String author = extractText(articleElement, ".author, .byline, .writer, [rel=author]");
                String imageUrl = extractImageUrl(articleElement);
                
                if (!subtitle.isEmpty()) {
                    logCallback.accept("Sottotitolo trovato: " + subtitle);
                }
                
                if (!author.isEmpty()) {
                    logCallback.accept("Autore trovato: " + author);
                }
                
                // Create article with available information
                Article article = new Article(title, subtitle, content, author, imageUrl);
                
                // Download image if URL is available
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    logCallback.accept("Immagine trovata. Scaricamento in corso...");
                    byte[] imageData = downloadImage(imageUrl);
                    if (imageData != null) {
                        article.setImageData(imageData);
                        logCallback.accept("Immagine scaricata con successo");
                    } else {
                        logCallback.accept("Errore durante il download dell'immagine");
                    }
                }
                
                articles.add(article);
                logCallback.accept("Articolo aggiunto: " + title);
            }
            
            logCallback.accept("Scraping completato con successo. Trovati " + articles.size() + " articoli");
        } catch (IOException e) {
            logCallback.accept("Errore durante lo scraping (" + url + "): " + e.getMessage());
        }
        return articles;
    }

    private String extractText(Element element, String selector) {
        Elements selected = element.select(selector);
        return selected.isEmpty() ? "" : selected.text().trim();
    }
    
    private String extractImageUrl(Element element) {
        // Try different image selectors
        Elements images = element.select("img[src]");
        if (!images.isEmpty()) {
            String relativeUrl = images.first().attr("src");
            // Check if URL is relative and try to get absolute
            if (relativeUrl.startsWith("/")) {
                return images.first().absUrl("src");
            }
            return relativeUrl;
        }
        return "";
    }

    private byte[] downloadImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        try {
            logCallback.accept("Download immagine: " + imageUrl);
            return Jsoup.connect(imageUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .ignoreContentType(true)
                    .timeout(5000)
                    .execute()
                    .bodyAsBytes();
        } catch (IOException e) {
            logCallback.accept("Errore download immagine (" + imageUrl + "): " + e.getMessage());
            return null;
        }
    }
}
