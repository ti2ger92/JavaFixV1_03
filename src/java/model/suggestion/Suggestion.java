package model.suggestion;

import model.code.Instruction;
import model.outdated.Outdated;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public abstract class Suggestion {
    public List<Instruction> instructions;

    public ArrayList<Instruction> oldLinesOfInterest = null;

    public Suggestion(List<Instruction> instructions) {
        if(instructions!=null)
            this.instructions = instructions;
        else this.instructions = new ArrayList<>();

    }

    public abstract String getSuggestionType();
    public abstract List<Outdated> getOutdateds();
    public List<Instruction> getOldLinesOfInterest() {
        if(oldLinesOfInterest==null) {
            oldLinesOfInterest = new ArrayList<>();
            for(Instruction aInst : instructions)
                if(isLineOfInterest(aInst))
                    oldLinesOfInterest.add(aInst);
        }
        return oldLinesOfInterest;
    };

    public abstract boolean isLineOfInterest(Instruction aInstruction);

    public static class InstructionBlock {
        public List<Instruction> block = new ArrayList<>();

        public static InstructionBlock create(List<Instruction> incInstructions, int minimum) {
            Predicate<Instruction> hasValue = i -> i.minifiedInstruction.isPresent() && i.minifiedInstruction.get().length()>0;
            long trueInstructions = incInstructions.stream().filter(hasValue).count();
            if(trueInstructions>=minimum)
                return new InstructionBlock(incInstructions);
            return null;


        }

        public InstructionBlock(List<Instruction> incInstructions) {
            if(incInstructions==null)
                throw new NullPointerException("Can't pass in a null instructions set.");
            block = incInstructions;
        }

        public boolean containsText(String text) {
            return block.stream().anyMatch(i->i.containsText(text));
        }


        @Override
        public boolean equals(Object other) {
            if(other instanceof  InstructionBlock && ((InstructionBlock) other).block!=null && ((InstructionBlock) other).block.size()==block.size()) {
                for(int i=0;i<block.size();i++)
                    if(!block.get(i).codeEquals(((InstructionBlock) other).block.get(i)))
                        return false;
                return true;
            }
            return false;
        }
    }

}
