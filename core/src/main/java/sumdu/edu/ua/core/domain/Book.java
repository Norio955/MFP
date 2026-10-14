package sumdu.edu.ua.core.domain;

public class Book {
    private long id;
    private String title;
    private String author;
    private int pubYear;

    public Book(long id, String title, String author, int pubYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pubYear = pubYear;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPubYear() {
        return pubYear;
    }
}