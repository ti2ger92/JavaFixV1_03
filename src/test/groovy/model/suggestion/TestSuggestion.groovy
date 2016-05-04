package model.suggestion

import model.code.Instruction
import model.code.TestInstruction
import model.outdated.Outdated
import org.junit.Test

/**
 * Created by TY-MSI on 3/5/2016.
 * Tests interface Suggestion.
 */

class TestSuggestion extends GroovyTestCase {
    @Test
    public void testInitialize() {
        Suggestion aSuggestion = new Suggestion(null) {
            @Override
            String getSuggestionType() {
                return null
            }

            @Override
            List<Outdated> getOutdateds() {
                return null
            }

            @Override
            boolean isLineOfInterest(Instruction aInstruction) {
                return false
            }
        }

        assert aSuggestion.instructions.size()==0;

        aSuggestion = new Suggestion(Arrays.asList([TestInstruction.getBasicInstruction()])) {
            @Override
            String getSuggestionType() {
                return null
            }

            @Override
            List<Outdated> getOutdateds() {
                return null
            }

            @Override
            boolean isLineOfInterest(Instruction aInstruction) {
                return false
            }
        }

        aSuggestion.instructions.size()==1;
    }



}
