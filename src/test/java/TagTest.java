import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TagTest {

	@Test
	void withoutTag() {

	}

	@Test
	@Tag("unit")
	void unitTest() {

	}

	@Test
	@Tag("component")
	void componentTest() {

	}

	@Test
	@Tag("integration")
	void integrationTest() {
		throw new RuntimeException();
	}
}
