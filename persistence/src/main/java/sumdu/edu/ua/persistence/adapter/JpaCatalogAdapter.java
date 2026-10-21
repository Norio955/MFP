package sumdu.edu.ua.persistence.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.Page;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.persistence.repo.BookJpaRepository;

@Component
@Transactional // Важлива анотація для ORM
public class JpaCatalogAdapter implements CatalogRepositoryPort {

    private final BookJpaRepository bookRepo;

    public JpaCatalogAdapter(BookJpaRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public Page<Book> search(String query, sumdu.edu.ua.core.domain.PageRequest request) {
        // Використовуємо вбудовану пагінацію Spring Data
        var pageable = PageRequest.of(request.getPage(), request.getSize());
        var page = bookRepo.findAll(pageable);
        return new Page<>(page.getContent(), request, page.getTotalElements());
    }

    @Override
    public Book findById(long id) {
        return bookRepo.findById(id).orElse(null);
    }

    @Override
    public Book add(String title, String author, int pubYear) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPubYear(pubYear);
        return bookRepo.save(book);
    }
}