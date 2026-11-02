public class Librarian extends User {

    public Librarian(String id, String name) {
        super(id, name);
    }

    @Override
    public String getRole() { return "Librarian"; }

    @Override
    public void showMenu(Library library) {
        boolean running = true;
        while (running) {
            System.out.println("\n------ LIBRARIAN MENU ------");
            System.out.println(" 1. Add book");
            System.out.println(" 2. Remove book");
            System.out.println(" 3. Issue book to member");
            System.out.println(" 4. Return book from member");
            System.out.println(" 5. Show all books");
            System.out.println(" 6. Show available books");
            System.out.println(" 7. Search by title");
            System.out.println(" 8. Search by author");
            System.out.println(" 0. Log out");
            System.out.println("----------------------------");

            switch (Console.readInt("Choose an option: ")) {
                case 1 -> addBook(library);
                case 2 -> removeBook(library);
                case 3 -> issueBook(library);
                case 4 -> returnBook(library);
                case 5 -> library.displayAllBooks();
                case 6 -> library.displayAvailableBooks();
                case 7 -> library.displayBooksByTitle(Console.readLine("Title (or part): "));
                case 8 -> library.displayBooksByAuthor(Console.readLine("Author (or part): "));
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addBook(Library library) {
        String isbn = Console.readLine("ISBN: ");
        if (library.findBookByIsbn(isbn) != null) {
            System.out.println("A book with this ISBN already exists.");
            return;
        }
        String title = Console.readLine("Title: ");
        String author = Console.readLine("Author: ");
        library.addBook(this, new Book(isbn, title, author));
        System.out.println("Book added.");
    }

    private void removeBook(Library library) {
        Book book = library.findBookByIsbn(Console.readLine("ISBN of book to remove: "));
        if (book == null) {
            System.out.println("Book not found.");
        } else if (!book.isAvailable()) {
            System.out.println("Cannot remove a book that is currently issued.");
        } else {
            library.removeBook(this, book);
            System.out.println("Book removed.");
        }
    }

    private void issueBook(Library library) {
        Member member = library.findMember(Console.readLine("Member ID: ").toUpperCase());
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        Book book = library.findBookByIsbn(Console.readLine("Book ISBN: "));
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        try {
            library.issueBook(member, book);
            System.out.println("'" + book.getTitle() + "' issued to " + member.getName() + ".");
        } catch (BorrowLimitExceededException | BookNotAvailableException e) {
            System.out.println("Could not issue book: " + e.getMessage());
        }
    }

    private void returnBook(Library library) {
        Member member = library.findMember(Console.readLine("Member ID: ").toUpperCase());
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        Book book = library.findBookByIsbn(Console.readLine("Book ISBN: "));
        if (book == null || !member.getBorrowedBooks().contains(book)) {
            System.out.println("This member has not borrowed that book.");
            return;
        }
        library.returnBook(member, book);
        System.out.println("'" + book.getTitle() + "' returned by " + member.getName() + ".");
    }
}
