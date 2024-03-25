package com.myprojects.demo;

import com.myprojects.demo.utilities.StringUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RandomOneApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testStringUtilities() {
        Assertions.assertEquals("creation_date_time", StringUtilities.camelCaseToSnakeCase("creationDateTime"));
        Assertions.assertEquals("my_project", StringUtilities.camelCaseToSnakeCase("MyProject"));
        Assertions.assertEquals("this_is_one_multi_word_string", StringUtilities.camelCaseToSnakeCase("ThisIsOneMultiWordString"));
    }

}
