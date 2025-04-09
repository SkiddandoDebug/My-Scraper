package src.main.resources;

public class Article {
    private String title;
    private String subtitle;
    private String content;
    private String author;
    private String imageUrl;
    private byte[] imageData;

    //Costruttore
    public Article(String title, String subtitle, String content, String author, String imageUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
