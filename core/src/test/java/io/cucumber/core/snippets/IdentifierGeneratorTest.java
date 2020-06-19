package io.cucumber.core.snippets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdentifierGeneratorTest {

    private final IdentifierGenerator underscore = new IdentifierGenerator(SnippetType.UNDERSCORE.joiner());
    private final IdentifierGenerator camelCase = new IdentifierGenerator(SnippetType.CAMELCASE.joiner());

    @Test
    void testSanitizeEmptyFunctionName() {
        Executable testMethod = () -> underscore.generate("");
        IllegalArgumentException expectedThrown = assertThrows(IllegalArgumentException.class, testMethod);
        assertThat(expectedThrown.getMessage(), is(equalTo("Cannot create function name from empty sentence")));
    }

    @Test
    void testSanitizeFunctionName() {
        assertIdentifiers(
            "test_function_123",
            "testFunction123",
            ".test function 123 ");
    }

    private void assertIdentifiers(String expectedUnderscore, String expectedCamelCase, String sentence) {
        assertAll(
            () -> assertThat(underscore.generate(sentence), is(equalTo(expectedUnderscore))),
            () -> assertThat(camelCase.generate(sentence), is(equalTo(expectedCamelCase))));
    }

    @Test
    void sanitizes_simple_sentence() {
        assertIdentifiers(
            "i_am_a_function_name",
            "iAmAFunctionName",
            "I am a function name");
    }

    @Test
    void sanitizes_sentence_with_multiple_spaces() {
        assertIdentifiers(
            "i_am_a_function_name",
            "iAmAFunctionName",
            "I am a function name");
    }

    @Test
    void sanitizes_pascal_case_word() {
        assertIdentifiers(
            "function_name_with_pascalCase_word",
            "functionNameWithPascalCaseWord",
            "Function name with pascalCase word");
    }

    @Test
    void sanitizes_camel_case_word() {
        assertIdentifiers(
            "function_name_with_CamelCase_word",
            "functionNameWithCamelCaseWord",
            "Function name with CamelCase word");
    }

    @Test
    void sanitizes_acronyms() {
        assertIdentifiers(
            "function_name_with_multi_char_acronym_HTTP_Server",
            "functionNameWithMultiCharAcronymHTTPServer",
            "Function name with multi char acronym HTTP Server");
    }

    @Test
    void sanitizes_two_char_acronym() {
        assertIdentifiers(
            "function_name_with_two_char_acronym_US",
            "functionNameWithTwoCharAcronymUS",
            "Function name with two char acronym US");
    }

}
