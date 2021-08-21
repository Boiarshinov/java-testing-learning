package dev.boiarshinov.testing.assertions.assertj;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CollectionsAssertingTest {

	@Test
	void collectionsAssert() {
		List<Integer> list = List.of( 1, 2, 3 );
		Set<Integer> set = Set.of( 3, 2, 1 );

		assertThat( list ).containsExactlyInAnyOrderElementsOf( set );
	}

	@Test
	@Disabled
	void collectionsAssertFail() {
		List<Integer> list = List.of( 1, 2, 3, 4, 5 );
		Set<Integer> set = Set.of( 1, 3, 5 );

		assertThat( list ).containsExactlyInAnyOrderElementsOf( set );
	}
}
