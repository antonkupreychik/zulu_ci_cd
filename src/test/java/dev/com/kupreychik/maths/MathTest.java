package dev.com.kupreychik.maths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MathTest {
    private final Math math = new Math();

    @Test
    void throw_exception_when_first_agg_is_null() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> math.add(null, 1));
    }

    @Test
    void should_return_true(){
        assertTrue(true);
    }


    @Test
    void sho() {
        assertTrue(true);
    }
}