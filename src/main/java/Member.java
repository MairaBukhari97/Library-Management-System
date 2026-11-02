import java.util.ArrayList;
import java.util.List;

public class Member extends User {
    // Same limit for every member, defined in one place.
    public static final int BORROW_LIMIT = 3;

    private final List<Book> borrowedBooks = new ArrayList<>();

    public Member(String id, String name) {
        super(id, name);
    }

    public boolean canBorrow() { return borrowedBooks.size() < BORROW_LIMIT; }
    public void borrow(Book book) { borrowedBooks.add(book); }
    public void giveBack(Book book) { borrowedBooks.remove(book); }
    public List<Book> getBorrowedBooks() { return new ArrayList<>(borrowedBooks); }

    @Override
    public String getRole() { return "Member"; }

    @Override
    public void showMenu(Library library) {
        boolean running = true;
        while (running) {
            System.out.println("\n------- MEMBER MENU -------");
            System.out.println("1. Show all books");
            System.out.println("2. Show available books");
            System.out.println("3. Search by title");
            System.out.println("4. Search by author");
            System.out.println("5. Borrow a book");
            System.out.println("6. Return a book");
            System.out.println("7. My borrowed books");
            System.out.println("0. Log out");
            System.out.println("---------------------------");

            switch (Console.readInt("Choose an option: ")) {
                case 1 -> library.displayAllBooks();
                case 2 -> library.displayAvailableBooks();
                case 3 -> library.displayBooksByTitle(Console.readLine("Title (or part): "));
                case 4 -> library.displayBooksByAuthor(Console.readLine("Author (or part): "));
                case 5 -> borrowBook(library);
                case 6 -> returnBook(library);
                case 7 -> showMyBooks();
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // Members can only act on their own account: these methods always pass `this`.
    private void borrowBook(Library library) {
        Book book = library.findBookByIsbn(Console.readLine("Book ISBN: "));
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        try {
            library.issueBook(this, book);
            System.out.println("You borrowed '" + book.getTitle() + "'.");
        } catch (BorrowLimitExceededException | BookNotAvailableException e) {
            System.out.println("Could not borrow: " + e.getMessage());
        }
    }

    private void returnBook(Library library) {
        Book book = library.findBookByIsbn(Console.readLine("Book ISBN: "));
        if (book == null || !borrowedBooks.contains(book)) {
            System.out.println("You haven't borrowed that book.");
            return;
        }
        library.returnBook(this, book);
        System.out.println("You returned '" + book.getTitle() + "'.");
    }

    private void showMyBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("You have no borrowed books.");
            return;
        }
        for (Book book : borrowedBooks) {
            System.out.println(book.getIsbn() + " - " + book.getTitle() + " by " + book.getAuthor());
        }
    }
}