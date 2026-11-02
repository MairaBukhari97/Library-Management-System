import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibraryTest {
    private Library library;
    private Librarian librarian;
    private Member member;
    private Book cleanCode;
    private Book effectiveJava;

    @BeforeEach
    public void setUp() {
        library = new Library();
        librarian = library.registerLibrarian("Sara");
        member = library.registerMember("Ali");

        cleanCode = new Book("101", "Clean Code", "Robert C. Martin");
        effectiveJava = new Book("102", "Effective Java", "Joshua Bloch");

        library.addBook(librarian, cleanCode);
        library.addBook(librarian, effectiveJava);
    }

    @Test
    public void addedBookCanBeFoundByIsbn() {
        assertSame(cleanCode, library.findBookByIsbn("101"));
    }

    @Test
    public void titleSearchFindsBook() {
        List<Book> result = library.findBooksByTitle("clean");

        assertEquals(1, result.size());
        assertSame(cleanCode, result.get(0));
    }

    @Test
    public void issuingBookMakesItUnavailable() throws Exception {
        library.issueBook(member, cleanCode);

        assertFalse(cleanCode.isAvailable());
        assertTrue(member.getBorrowedBooks().contains(cleanCode));
    }

    @Test
    public void returningBookMakesItAvailableAgain() throws Exception {
        library.issueBook(member, cleanCode);
        library.returnBook(member, cleanCode);

        assertTrue(cleanCode.isAvailable());
        assertTrue(member.getBorrowedBooks().isEmpty());
    }
}

