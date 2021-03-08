import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

public class RepetitionTest {

	@RepeatedTest( value = 10, name = RepeatedTest.LONG_DISPLAY_NAME)
	void repeated(RepetitionInfo info) {
		final int totalRepetitions = info.getTotalRepetitions();
		Assertions.assertEquals( 10, totalRepetitions );
	}
}
