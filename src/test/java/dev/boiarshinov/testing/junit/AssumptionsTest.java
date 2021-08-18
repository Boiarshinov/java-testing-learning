package dev.boiarshinov.testing.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

public class AssumptionsTest {

	@Test
	void assumption() {
		Assumptions.assumeFalse( true );
		Assertions.assertTrue( true );
	}
}
