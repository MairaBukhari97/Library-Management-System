public class Main {
    // Only staff know this code, so only staff can sign up as librarians.
    private static final String STAFF_CODE = "LIB-2026";

    public static void main(String[] args) {
        Library library = new Library();
        seedData(library);

        System.out.println("=== Library Management System ===");
        System.out.println("Demo logins: L1 (librarian), M1 (member)");

        boolean running = true;
        while (running) {
            System.out.println("\nWho are you?");
            System.out.println("1. Librarian");
            System.out.println("2. Member");
            System.out.println("0. Exit");

            switch (Console.readInt("Choose an option: ")) {
                case 1 -> roleScreen(library, true);
                case 2 -> roleScreen(library, false);
                case 0 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // Log in / sign up screen for the chosen role.
    private static void roleScreen(Library library, boolean isLibrarian) {
        String role = isLibrarian ? "Librarian" : "Member";
        boolean back = false;
        while (!back) {
            System.out.println("\n--- " + role + " ---");
            System.out.println("1. Log in");
            System.out.println("2. Sign up");
            System.out.println("0. Back");

            switch (Console.readInt("Choose an option: ")) {
                case 1 -> logIn(library, isLibrarian);
                case 2 -> signUp(library, isLibrarian);
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void logIn(Library library, boolean isLibrarian) {
        String role = isLibrarian ? "librarian" : "member";
        String id = Console.readLine("Enter your ID: ").trim().toUpperCase();

        User user = isLibrarian ? library.findLibrarian(id) : library.findMember(id);
        if (user == null) {
            System.out.println("No " + role + " account with that ID.");
            return;
        }
        System.out.println("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        user.showMenu(library); // polymorphism: Librarian or Member menu runs
    }

    private static void signUp(Library library, boolean isLibrarian) {
        if (isLibrarian && !Console.readLine("Staff access code: ").trim().equalsIgnoreCase(STAFF_CODE)) {
            System.out.println("Incorrect staff code.");
            return;
        }

        String name = Console.readLine("Your name: ").trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        User user = isLibrarian ? library.registerLibrarian(name) : library.registerMember(name);
        System.out.println("Account created! Your ID is " + user.getId()
                + ". Use it to log in.");
    }

    private static void seedData(Library library) {
        Librarian sara = library.registerLibrarian("Sara"); // L1
        library.registerMember("Ali");                      // M1

        library.addBook(sara, new Book("101", "Clean Code", "Robert C. Martin"));
        library.addBook(sara, new Book("102", "Effective Java", "Joshua Bloch"));
        library.addBook(sara, new Book("103", "Head First Java", "Kathy Sierra"));
        library.addBook(sara, new Book("104", "Design Patterns", "Erich Gamma"));
    }
}