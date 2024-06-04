package dev.boiarshinov.testing.cucumber.step;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BaseStepdefs {

    @When("I run test")
    public void runTest() {
        System.out.println("I have started tests");
    }

    @Then("Everything works fine")
    public void checkResult() {
        System.out.println("Result is fine");
    }
}
