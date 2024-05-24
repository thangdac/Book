package Nhom6.Controllers;

import Nhom6.entities.Book;
import Nhom6.services.BookService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    @GetMapping
    public String showAllBooks(@NonNull Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book/list";
    }

    @GetMapping("/add")
    public String addBookForm(@NonNull Model model) {
        model.addAttribute("book", new Book());
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book) {
        if(bookService.getBookById(book.getId()).isEmpty())
            bookService.addBook(book);
        return "redirect:/books";
    }
    @GetMapping("/edit/{id}")
    public String editBookForm(@NonNull Model model, @PathVariable long id)
    {
        var book = bookService.getBookById(id).orElse(null);
        model.addAttribute("book", book != null ? book : new Book());
        return "book/edit";
    }
    @PostMapping("/edit")
    public String editBook(@ModelAttribute("book") Book book) {
        bookService.updateBook(book);
        return "redirect:/books";
    }
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        if (bookService.getBookById(id).isPresent())
            bookService.deleteBookById(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(value = "search", required = false) String searchTerm,
                              Model model) {
        List<Book> searchResults = bookService.searchBooks(searchTerm);
        model.addAttribute("books", searchResults);
        return "book/list";
    }
    @GetMapping("/autocomplete")
    @ResponseBody
    public List<Book> autocomplete(@RequestParam String term) {
        String lowercaseTerm = term.toLowerCase();
        return bookService.searchBooks(term).stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowercaseTerm)
                        || book.getAuthor().toLowerCase().contains(lowercaseTerm))
                .collect(Collectors.toList());
    }
}