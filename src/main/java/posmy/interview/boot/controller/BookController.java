package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import posmy.interview.boot.entity.BookEntity;
import posmy.interview.boot.exception.LmsException;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.CreateBookRequest;
import posmy.interview.boot.model.UpdateBookRequest;
import posmy.interview.boot.service.book.BookService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/{bookId}")
    @RolesAllowed({"MEMBER", "LIBRARIAN"})
    public ResponseEntity<Book> getBook(@PathVariable long bookId) {
        try {
            return ResponseEntity.ok(bookService.getBook(bookId));
        } catch (NoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found", e);
        }
    }

    @GetMapping(value = "/available")
    @RolesAllowed({"MEMBER", "LIBRARIAN"})
    public ResponseEntity<List<Book>> getAvailableBooks() {
        try {
            return ResponseEntity.ok(bookService.getAvailableBooks());
        } catch (LmsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Available Books Found", e);
        }
    }

    @PostMapping
    @RolesAllowed({"LIBRARIAN"})
    public ResponseEntity<BookEntity> addNewBook(@RequestBody CreateBookRequest createBookRequest) {
        return ResponseEntity.ok(bookService.createBook(createBookRequest));
    }

    @DeleteMapping(value = "/{bookId}")
    @RolesAllowed({"LIBRARIAN"})
    public ResponseEntity<Void> deleteBook(@PathVariable long bookId) {
        try {
            bookService.deleteBook(bookId);
        } catch (LmsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found", e);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    @RolesAllowed({"LIBRARIAN"})
    public ResponseEntity<BookEntity> updateBookDetails(@RequestBody UpdateBookRequest updateBookRequest) {
        try {
            return ResponseEntity.ok(bookService.updateBook(updateBookRequest));
        } catch (LmsException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
