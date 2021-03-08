import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MethodOrderTest {

	private static int testsInvoked = 0;

	@Test
	@Order( 1 )
	void firstTest() {
		Assertions.assertEquals( 0, testsInvoked );
		testsInvoked++;
	}

	@Test
	@Order( 2 )
	void secondTest() {
		Assertions.assertEquals( 1, testsInvoked );
	}
}
