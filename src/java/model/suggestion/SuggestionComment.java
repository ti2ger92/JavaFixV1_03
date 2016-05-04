package model.suggestion;

import model.code.Instruction;
import model.code.InstructionTransform;
import model.outdated.Outdated;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by TY-MSI on 4/30/2016.
 */
public abstract class SuggestionComment extends Suggestion {
    public SuggestionComment(List<Instruction> instructions) {
        super(instructions);
    }

    @Override
    public List<Outdated> getOutdateds() {
        return instructions.stream().filter(i->isLineOfInterest(i)).map(i->{
            InstructionTransform aTrans = new InstructionTransform();
            aTrans.add1toMany(i, Arrays.asList(new Instruction[]{getSuggestionComment(i), i.copyNewSeq(instructions)}));
            Outdated aOutdated = new Outdated(getSuggestionType());
            aOutdated.setInstructionTransform(aTrans);
            return aOutdated;
        }).collect(Collectors.toList());
    }

    public abstract Instruction getSuggestionComment(Instruction i);
}
