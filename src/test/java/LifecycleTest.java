import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LifecycleTest {

	private Flag flag = Flag.ON;
	private static Preparation preparation = Preparation.NOT_PREPARED;

	@BeforeAll
	static void prepareTests() {
		preparation = Preparation.PREPARED;
	}

	@BeforeEach
	void setUp() {
		Assertions.assertEquals( Preparation.PREPARED, preparation );
		flag = Flag.ON;
	}

	@Test
	void lifecycle() {
		Assertions.assertEquals( Preparation.PREPARED, preparation );
		Assertions.assertEquals( Flag.ON, flag );
		flag = Flag.OFF;
	}

	enum Flag {
		ON, OFF
	}

	enum Preparation {
		PREPARED, NOT_PREPARED
	}
}
