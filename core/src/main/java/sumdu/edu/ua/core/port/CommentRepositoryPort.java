package sumdu.edu.ua.core.port;


import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.Page;
import sumdu.edu.ua.core.domain.PageRequest;

public interface CommentRepositoryPort  {
    Page<Book> search(String query, PageRequest request);
    Book findById(long id);
    public Book add(String title, String author, int pubYear);

    void delete(long bookId, long commentId);
}