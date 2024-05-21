package Nhom6.services;

import Nhom6.entities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final List<Book> books;

    public List<Book> getAllBooks() {
        return books;
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void updateBook(Book book) {
        var bookOptional = getBookById(book.getId());
        if (bookOptional.isPresent()) {
            Book bookUpdate = bookOptional.get();
            bookUpdate.setTitle(book.getTitle());
            bookUpdate.setAuthor(book.getAuthor());
            bookUpdate.setPrice(book.getPrice());
            bookUpdate.setCategory(book.getCategory());
        }
    }

    public void deleteBookById(Long id) {
        getBookById(id).ifPresent(books::remove);
    }

    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return books;
        }
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }
}
