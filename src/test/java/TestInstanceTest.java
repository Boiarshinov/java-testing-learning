import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestInstanceTest {

	private Object commonVar;

	@Test
	@Order( 1 )
	void initializeVariable() {
		commonVar = new Object();
	}

	@Test
	@Order( 2 )
	void usingVariableInitializedInPreviousTest() {
		Assertions.assertNotNull( commonVar );
	}
}
