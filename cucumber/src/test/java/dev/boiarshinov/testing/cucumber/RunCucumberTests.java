package dev.boiarshinov.testing.cucumber;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

// stolen from https://github.com/cronn/cucumber-junit5-example/blob/main/src/test/java/com/example/RunAllCucumberTests.java
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class RunCucumberTests {}
