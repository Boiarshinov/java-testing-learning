package dev.boiarshinov.gatling;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class GatlingExample extends Simulation {

    private static final int vu = Integer.getInteger("vu", 1);

    private static final HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8432")
        .acceptHeader("application/json");

    private static final ScenarioBuilder scenario = scenario("Gatling Example")
        .exec(http("Session").get("/ping"));

    private static final Assertion assertion = global().failedRequests().count().lt(1L);

    {
        setUp(scenario.injectOpen(constantUsersPerSec(10).during(Duration.ofSeconds(60))))
            .assertions(assertion)
            .protocols(httpProtocol);
    }
}
