package model.suggestion;

import model.code.Instruction;
import model.code.MicroLexer;
import model.outdated.Outdated;

import java.util.List;
import java.util.Optional;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public class ForToEnhanced extends Suggestion1To1 {
    String OLD_FOR_PATTERN = "([^;]*)(for[ ]*\\(([\\s\\S]*;[\\s\\S]*<[\\s]*([\\S]*)(.length|.size\\(\\))[\\s]*;[\\s\\S]*)\\))";
    String NEW_FOR_PATTERN = "for\\(Object a$4 : $4)";


    public ForToEnhanced(List<Instruction> instructions) {
        super(instructions);
    }

    @Override
    public String getSuggestionType() {
        return "ForToEnhanced";
    }

    public boolean isLineOfInterest(Instruction aInstruction) {
        return aInstruction.minifiedInstruction.orElse("").matches(OLD_FOR_PATTERN);
    }

    public Instruction transform(Instruction in) {
        Optional<Instruction> newInstruction = in.minifiedInstruction.map(mi -> {
            String newCode =  mi.replaceAll(OLD_FOR_PATTERN, NEW_FOR_PATTERN);
            return in.changeInstruction(Optional.ofNullable(newCode));
        });
        return newInstruction.orElse(in);
    }
}
