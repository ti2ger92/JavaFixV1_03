package model.outdated

import model.code.InstructionTransform
import model.suggestion.RepeatedCode
import model.suggestion.Suggestion
import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */
class TestOutdated extends GroovyTestCase {
    @Test
    public void testInitialize() {
        Suggestion aSuggestion = new RepeatedCode();
        Outdated aOutdated = new Outdated(aSuggestion.getSuggestionType());
        assert aSuggestion.getSuggestionType().equals(aOutdated.suggestionType);
    }

    @Test
    public void testAddGetTransform() {
        Suggestion aSuggestion = new RepeatedCode();
        Outdated aOutdated = new Outdated(aSuggestion.getSuggestionType());
        aOutdated.setInstructionTransform(new InstructionTransform());
        assertNotNull(aOutdated.instructionTransform);
        InstructionTransform aTransform = aOutdated.getInstructionTransform();
        assertNotNull(aTransform);
    }
}
