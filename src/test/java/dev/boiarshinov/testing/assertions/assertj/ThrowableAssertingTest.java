package dev.boiarshinov.testing.assertions.assertj;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ThrowableAssertingTest {

	@Test
	void inFluentStyle() {
		assertThatThrownBy(() -> { throw new MyException("exception message"); })
			.isInstanceOf(MyException.class)
			.hasMessage("exception message")
			.hasNoCause();
	}

	// AAA - arrange, act, assert
	@Test
	void inStyleOfAAA() {
		var exception = catchThrowable(() -> { throw new MyException("exception message"); });

		assertThat(exception)
			.isInstanceOf(MyException.class)
			.hasMessage("exception message")
			.hasNoCause();
	}

	@Test
	void nothingThrown() {
		var exception = catchThrowable(() -> doNothing());
		//var 'exception' is null
		assertThat(exception).doesNotThrowAnyException();
	}

    @Test
	void throwWithCause() {
		var rootException = new IllegalArgumentException("root exception message");

		var exception = catchThrowable(() -> { throw new MyException("exception message", rootException); });

		assertThat(exception)
			.isInstanceOf(MyException.class)
			.hasMessage("exception message")
			.getCause()
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("root exception message");
	}

    private static class MyException extends RuntimeException {

		public MyException(String message) {
			super(message);
		}
		public MyException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	private void doNothing() {}
}
