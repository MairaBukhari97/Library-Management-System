import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private final List<Book> books = new ArrayList<>();
    private final Map<String, Member> members = new HashMap<>();
    private final Map<String, Librarian> librarians = new HashMap<>();
    private int nextMemberNumber = 1;
    private int nextLibrarianNumber = 1;

    // ---------- Sign-up (open to everyone, IDs are auto-generated) ----------

    public Member registerMember(String name) {
        Member member = new Member("M" + nextMemberNumber++, name);
        members.put(member.getId(), member);
        return member;
    }

    public Librarian registerLibrarian(String name) {
        Librarian librarian = new Librarian("L" + nextLibrarianNumber++, name);
        librarians.put(librarian.getId(), librarian);
        return librarian;
    }

    // ---------- Catalog management (librarian-only) ----------
    // Requiring a Librarian parameter means a Member can't call these:
    // the compiler rejects it.

    public void addBook(Librarian librarian, Book book) {
        books.add(book);
    }

    public boolean removeBook(Librarian librarian, Book book) {
        return books.remove(book);
    }

    // ---------- Circulation (any member, on their own account) ----------

    public void issueBook(Member member, Book book)
            throws BorrowLimitExceededException, BookNotAvailableException {
        if (!member.canBorrow()) {
            throw new BorrowLimitExceededException(
                    "Borrow limit reached. " + member.getName() + " cannot borrow more books.");
        }
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(
                    "'" + book.getTitle() + "' is not available.");
        }
        book.setAvailable(false);
        member.borrow(book);
    }

    public void returnBook(Member member, Book book) {
        member.giveBack(book);
        book.setAvailable(true);
    }

    // ---------- Lookup ----------

    public Member findMember(String id) {
        return members.get(id);
    }

    public Librarian findLibrarian(String id) {
        return librarians.get(id);
    }

    // Returns whichever kind of user has this ID (used for login).
    public User findUser(String id) {
        Librarian librarian = librarians.get(id);
        if (librarian != null) {
            return librarian;
        }
        return members.get(id);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> findBooksByTitle(String title) {
        List<Book> result = new ArrayList<>();
        String query = title.toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query)) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> findBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        String query = author.toLowerCase();
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(query)) {
                result.add(book);
            }
        }
        return result;
    }

    // ---------- Display (open to everyone) ----------

    public void displayAllBooks() {
        printBooks(books, true);
    }

    public void displayAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                available.add(book);
            }
        }
        printBooks(available, false);
    }

    public void displayBooksByTitle(String title) {
        printBooks(findBooksByTitle(title), true);
    }

    public void displayBooksByAuthor(String author) {
        printBooks(findBooksByAuthor(author), true);
    }

    private void printBooks(List<Book> list, boolean showAvailability) {
        if (list.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (Book book : list) {
            String line = "ISBN: " + book.getIsbn()
                    + ", Title: " + book.getTitle()
                    + ", Author: " + book.getAuthor();
            if (showAvailability) {
                line += ", Available: " + book.isAvailable();
            }
            System.out.println(line);
        }
    }
}
