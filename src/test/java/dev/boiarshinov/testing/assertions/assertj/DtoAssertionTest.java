package dev.boiarshinov.testing.assertions.assertj;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class DtoAssertionTest {

    record Dto(
        String name,
        int age,
        String description
    ) {
    }

    private Dto createDto() {
        return new Dto("name", 0, "description");
    }

    @Test
    void separately() {
        Dto dto = createDto();
        assertThat(dto.name).isEqualTo("name");
        assertThat(dto.age).isEqualTo(0);
        assertThat(dto.description).isEqualTo("description");
    }

    @Test
    void softly() {
        Dto dto = createDto();
        assertSoftly(softly -> {
            softly.assertThat(dto.name).isEqualTo("name");
            softly.assertThat(dto.age).isEqualTo(0);
            softly.assertThat(dto.description).isEqualTo("description");
        });
    }

    @Test
    void byReflection() {
        Dto dto = createDto();
        assertThat(dto)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("age", 0)
            .hasFieldOrPropertyWithValue("description", "description");
    }

    //A lot of trash in fail message when use without description
    @Test
    void byMatches() {
        Dto dto = createDto();
        assertThat(dto)
            .matches(it -> it.name.equals("name"))
            .matches(it -> it.age == 0)
            .matches(it -> it.description.equals("description"));
    }

    @Test
    void byReturns() {
        Dto dto = createDto();
        assertThat(dto)
            .returns("name", from(Dto::name))
            .returns(0, from(Dto::age))
            .returns("description", from(Dto::description));
    }

    @Test
    void bySatisfies() {
        Dto dto = createDto();
        assertThat(dto)
            .satisfies(it -> assertThat(it.name).isEqualTo("name"))
            .satisfies(it -> assertThat(it.age).isEqualTo(0))
            .satisfies(it -> assertThat(it.description).isEqualTo("description"));
    }
}
