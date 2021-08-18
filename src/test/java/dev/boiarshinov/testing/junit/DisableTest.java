package dev.boiarshinov.testing.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

public class DisableTest {

	@Test
	@Disabled
	void disabled() {
		throw new RuntimeException();
	}

	@Test
	@EnabledOnOs( OS.WINDOWS )
	void enableOnWindows() {
		Assertions.assertTrue( true );
	}

	@Test
	@DisabledOnJre( JRE.JAVA_11 )
	void disabledOnCurrentJDK() {
		throw new RuntimeException();
	}

	@Test
	@DisabledIf( "disableCondition" )
	void disabledByCondition() {
		throw new RuntimeException();
	}

	private boolean disableCondition() {
		return true;
	}
}
