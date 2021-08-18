package dev.boiarshinov.testing.junit;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName( "LocalDate" )
public class NestedTestsTest {

	@Nested
	@DisplayName( "create" )
	class LocalDateCreationTest {

		LocalDate expectedDate = LocalDate.of( 2021, 3, 1 );

		@Test
		@DisplayName( "by epoch day" )
		void createByEpochDay() {
			final LocalDate actualDate = LocalDate.ofEpochDay( 18687 );
			Assertions.assertEquals( expectedDate, actualDate );
		}

		@Test
		@DisplayName( "string to parse" )
		void createByParsing() {
			final LocalDate actualDate = LocalDate.parse( "2021-03-01" );
			Assertions.assertEquals( expectedDate, actualDate );
		}
	}

	@Nested
	@DisplayName( "plus days" )
	class LocalDateBeforeTest {
		@Test
		@DisplayName( "positive value" )
		void plusDaysPositive() {
			final LocalDate localDate = LocalDate.of( 2021, 3, 1 );
			final LocalDate expectedResult = LocalDate.of( 2021, 3, 4 );

			final LocalDate actualResult = localDate.plusDays( 3 );

			Assertions.assertEquals( expectedResult, actualResult );
		}

		@Test
		@DisplayName( "negative value" )
		void plusDaysNegative() {
			final LocalDate localDate = LocalDate.of( 2021, 3, 1 );
			final LocalDate expectedResult = LocalDate.of( 2021, 2, 26 );

			final LocalDate actualResult = localDate.plusDays( -3 );

			Assertions.assertEquals( expectedResult, actualResult );
		}
	}

}
