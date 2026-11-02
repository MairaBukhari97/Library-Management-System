import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserPolymorphismTest {

    @Test
    void eachSubclassReportsItsOwnRole() {
        User librarian = new Librarian("L1", "Sara");
        User member = new Member("M1", "Ali");

        assertEquals("Librarian", librarian.getRole());
        assertEquals("Member", member.getRole());
    }
}
