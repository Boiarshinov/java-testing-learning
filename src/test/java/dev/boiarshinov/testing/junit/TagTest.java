package dev.boiarshinov.testing.junit;

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

	//ignored while `gradle test` cause of tag
	@Test
	@Tag("integration")
	void integrationTest() {
		throw new RuntimeException();
	}
}
