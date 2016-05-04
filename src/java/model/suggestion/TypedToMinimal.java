package model.suggestion;

import model.code.Instruction;
import model.code.InstructionTransform;
import model.code.MicroLexer;
import model.outdated.Outdated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public class TypedToMinimal extends Suggestion1To1 {
    public static String MASK = "("+MicroLexer.WS +")([A-Za-z0-9]*)\\<([A-Za-z0-9]*)\\> ([A-Za-z0-9]*)"+MicroLexer.WS +"\\="+MicroLexer.WS +"new \\2\\<\\3\\>\\(([\\s\\S]*)";
    public static String REPLACE_MASK = "$1$2\\<$3\\> $4=new $2\\<\\>\\($5";




    public TypedToMinimal(List<Instruction> instructions) {
        super(instructions);
    }

    @Override
    public String getSuggestionType() {
        return "TypedToMinimal";
    }




    public boolean isLineOfInterest(Instruction aInstruction) {
        if(aInstruction.minifiedInstruction.isPresent()) {
            return aInstruction.minifiedInstruction.get().matches(MASK);
        }
        return false;
    }

    public Instruction transform(Instruction aInstruction) {
        Optional<Instruction> newInstruction = aInstruction.minifiedInstruction.map(oldCode -> {
            String updated = oldCode.replaceAll(MASK, REPLACE_MASK);
            return aInstruction.changeInstruction(Optional.ofNullable(updated));
        });
        return  newInstruction.orElse(aInstruction);
    }
}
