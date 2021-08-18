package dev.boiarshinov.testing.assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HamcrestTest {

	@Test
	void objectAssert() {
		assertThat( new Object(), not( new Object() ) );
	}

	@Test
	@Disabled
	void collectionsAssert() {
		List<Integer> list = List.of( 1, 2, 3 );
		Set<Integer> set = Set.of( 3, 2, 1 );

		assertThat( list, containsInAnyOrder(set) );
	}

	@Test
	@Disabled
	void collectionsAssertNotFail() {
		List<Integer> list = List.of( 1, 2, 3, 4, 5 );
		Set<Integer> set = Set.of( 3, 2, 1 );

		assertThat( list, containsInAnyOrder(set) );
	}
}
