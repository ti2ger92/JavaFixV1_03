package model.suggestion;

import model.CodeTransform;
import model.code.Instruction;
import model.code.InstructionTransform;
import model.outdated.Outdated;
import com.google.common.collect.ArrayListMultimap;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public class RepeatedStrings extends Suggestion {

    public static  int STRING_THRESHOLD=2;

    public static String STRING_PATTERN = "(\\\"[\\s\\S]*?\\\")";
    public static String STRING_CONTAINS_PATTERN = "([\\s\\S]*)"+STRING_PATTERN+"([\\s\\S]*)";
    public static String STRING_GET_PATTERN = "$2";
    public static String STRING_REPLACE_PATTERN = "CONSUMED";
    public static String DECLARE_COMMENT=" //Added this declaration to organize Strings";

    ArrayListMultimap<String, Instruction> stringMap = ArrayListMultimap.create();



    public RepeatedStrings(List<Instruction> instructions) {
        super(instructions);
    }


    @Override
    public String getSuggestionType() {
        return "RepeatedStrings";
    }

    @Override
    public List<Outdated> getOutdateds() {
        for(Instruction i : instructions)
            populateStringMap(i);
        ArrayList<Outdated> outdateds = new ArrayList<>();
        Object[] keys = stringMap.keySet().toArray();
        for(int c=0;c<keys.length;c++) {
            String string = (String)keys[c];
            List<Instruction> insts = stringMap.get(string);
            if(insts.size()>=STRING_THRESHOLD) {
                //For the moment we will not use the counter
                InstructionTransform aTransform = new InstructionTransform();
                Instruction firstInstruction = (Instruction)((List)insts).get(0);
                int firstLocation = instructions.indexOf(firstInstruction);
                String varName = "string"+(c+1);
                Instruction declareString = new Instruction();
                declareString.origSeq = firstLocation;
                declareString.minifiedInstruction = Optional.ofNullable("String "+varName+"="+string);
                declareString.endPunctuation= Optional.ofNullable(";"+DECLARE_COMMENT+"\n");
                declareString.touched=true;
                String newFirstCode = firstInstruction.minifiedInstruction.get().replace(string,varName);
                Instruction firstCode = firstInstruction.changeInstruction(Optional.ofNullable(newFirstCode));
                aTransform.add1toMany(firstInstruction, Arrays.asList(new Instruction[]{declareString, firstInstruction.comment(), firstCode}));
                insts.remove(firstInstruction);
                Outdated aOutdated = new Outdated(this.getSuggestionType());
                aOutdated.setInstructionTransform(aTransform);
                outdateds.add(aOutdated);
                for(Instruction i : insts) {
                    if(i.minifiedInstruction.isPresent()) {
                        String newCode = i.minifiedInstruction.get().replace(string, varName);
                        List<Instruction> currentTransform= aTransform.getTo(i);
                        if(currentTransform!=null)
                            for(Instruction aTransformedInst : currentTransform)
                                if(!aTransformedInst.minifiedInstruction.isPresent()&&aTransformedInst.minifiedInstruction.get().contains(DECLARE_COMMENT))
                                    newCode = aTransformedInst.minifiedInstruction.get().replace(string, varName);
                        aTransform = new InstructionTransform();
                        aTransform.add1toMany(i, Arrays.asList(new Instruction[]{i.comment(),i.changeInstruction(Optional.ofNullable(newCode))}));
                        aOutdated = new Outdated(this.getSuggestionType());
                        aOutdated.setInstructionTransform(aTransform);
                        outdateds.add(aOutdated);
                    }
                }
            }
        }
        return outdateds;
    }



    @Override
    public boolean isLineOfInterest(Instruction aInst) {return false;}

    public void populateStringMap(Instruction i) {
        i.minifiedInstruction.ifPresent(instructionCode -> {
            for(int bailout=0;bailout<10000&&instructionCode.matches(STRING_CONTAINS_PATTERN);bailout++) {
                String aString = instructionCode.replaceFirst(STRING_CONTAINS_PATTERN, STRING_GET_PATTERN);
                instructionCode = instructionCode.replaceFirst(STRING_PATTERN, STRING_REPLACE_PATTERN);
                stringMap.put(aString, i);
            }
        });
    }
}
