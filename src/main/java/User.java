public abstract class User {
    private final String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public abstract String getRole();

    // Polymorphism: each subclass provides its own menu and permissions.
    public abstract void showMenu(Library library);
}