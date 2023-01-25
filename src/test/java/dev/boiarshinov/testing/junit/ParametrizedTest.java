package dev.boiarshinov.testing.junit;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ParametrizedTest {

	@Nested
	class ValueSourceTest {

		@ParameterizedTest(name = "[{index}] - check char count for string \"{0}\"")
		@ValueSource(strings = { "book", "1001", "абвг", "    ", "\t\n\r\n" })
		void strings(String fourLetterString) {
			Assertions.assertEquals( 4, fourLetterString.length() );
		}

		@ParameterizedTest
		@ValueSource(ints = { 0, 1, 17, -123 })
		void ints(int invalidYearsOld) {
			final boolean result = isAdult( invalidYearsOld );
			Assertions.assertFalse( result );
		}

		private boolean isAdult(int yearsOld) {
			return yearsOld >= 18;
		}

		@ParameterizedTest
		@ValueSource(classes = { DayOfWeek.class, TestInstance.Lifecycle.class })
		void classes(Class<?> enumClass) {
			Assertions.assertTrue( enumClass.isEnum() );
		}
	}

	@Nested
	class NullAndEmptySourceTest {
		@ParameterizedTest
		@NullSource
		@EmptySource
		@ValueSource(strings = { "  ", "\t" })
		void testIsBlank(String blankString) {
			Assertions.assertTrue( isBlank( blankString ) );
		}

		private boolean isBlank(String blankString) {
			return blankString == null || blankString.isBlank();
		}
	}

	@Nested
	class CsvSourceTest {
		@ParameterizedTest(name = "[{index}] - trim \"{0}\" to \"{1}\"]")
		@CsvSource(value = {
				"lol,lol",
				"'kek ',kek",
				"' cheburek',cheburek",
				"'   ',''",
				"'',''"
		})
		void trim(String stringToTrim, String trimmedString) {
			Assertions.assertEquals( trimmedString, stringToTrim.trim() );
		}
	}

	@Nested
	class EnumSourceTest {
		@ParameterizedTest
		@EnumSource(value = DayOfWeek.class, names = {"SATURDAY", "SUNDAY"}, mode = EnumSource.Mode.EXCLUDE)
		void isWorkDay(DayOfWeek dayOfWeek) {
			final EnumSet<DayOfWeek> workDays = EnumSet.of(
					DayOfWeek.MONDAY,
					DayOfWeek.TUESDAY,
					DayOfWeek.WEDNESDAY,
					DayOfWeek.THURSDAY,
					DayOfWeek.FRIDAY );
			Assertions.assertTrue( workDays.contains( dayOfWeek ) );
		}
	}

	@Nested
	@TestInstance( TestInstance.Lifecycle.PER_CLASS )
	class MethodSourceTest {
		@ParameterizedTest(name = "[{index}] - convert datetime {0} to date {1}")
		@MethodSource("dateTimeProvider")
		void dateFromDateTime(LocalDateTime dateTime, LocalDate expectedDate) {
			final LocalDate actualDate = dateTime.toLocalDate();
			Assertions.assertEquals( expectedDate, actualDate );
		}

		Stream<Arguments> dateTimeProvider() {
			return Stream.of(
					Arguments.of( LocalDateTime.parse("2021-03-08T15:00"), LocalDate.parse( "2021-03-08" ) ),
					Arguments.of( LocalDateTime.parse("1969-12-31T23:59:59"), LocalDate.parse( "1969-12-31" ) )
			);
		}
	}
}
