package model.suggestion;

import model.code.Instruction;
import model.code.InstructionTransform;
import model.outdated.Outdated;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by TY-MSI on 3/22/2016.
 */
public abstract class Suggestion1To1 extends Suggestion {
    public Suggestion1To1(List<Instruction> instructions) {
        super(instructions);
    }

    @Override
    public List<Outdated> getOutdateds() {
        return getOldLinesOfInterest().stream()
            .filter(i -> i.source.isPresent())
            .map(i -> {
                ArrayList <Instruction> newInstructions = new ArrayList<>();
                newInstructions.add(i.comment());
                newInstructions.add(transform(i));
                InstructionTransform aTransform = new InstructionTransform();
                aTransform.add1toMany(i, newInstructions);
                Outdated aOutdated = new Outdated(getSuggestionType());
                aOutdated.setInstructionTransform(aTransform);
                return aOutdated;
            })
            .collect(Collectors.toList());

    }


    public abstract Instruction transform(Instruction in);
}
