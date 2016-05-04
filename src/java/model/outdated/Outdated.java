package model.outdated;


import model.code.Instruction;
import model.code.InstructionTransform;

import java.util.ArrayList;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public class Outdated {
    public String suggestionType;
    public InstructionTransform instructionTransform;

    public Outdated(String suggestionType) {
        if(suggestionType==null)
            throw new NullPointerException("An outdated cannot be created without adding a suggestionType");
        this.suggestionType = suggestionType;
    }

    public void setInstructionTransform(InstructionTransform aInstructionTransform) {
        instructionTransform = aInstructionTransform;
    }

    public InstructionTransform getInstructionTransform() {
        return instructionTransform;
    }

    public void applyTransform(ArrayList<Instruction> aInstList) {
        if(instructionTransform==null)
                return;
        //TODO: build this
        instructionTransform.apply(aInstList, suggestionType);
    }
}
