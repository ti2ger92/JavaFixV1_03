package model.suggestion;

import model.CodeTransform;
import model.code.Instruction;
import model.code.InstructionTransform;
import model.code.MicroLexer;
import model.outdated.Outdated;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public class ForToFunctional extends SuggestionComment {

    public static final int TYPE_DEFAULT=1;
    public static final int TYPE_1LIST_TO_ANOTHER=2;
    public static final int TYPE_COUNTER=3;
    public static final int TYPE_FILTER=4;
    public static final int TYPE_SUM=5;

    public List<InstructionBlock> forLoops = new ArrayList<>();

    public Predicate<InstructionBlock> instStartsBlock(Instruction aInst) {return ib->ib.block.get(0).equals(aInst);}

    public ForToFunctional(List<Instruction> instructions) {
        super(instructions);
        identifyForLoops();
    }

    @Override
    public String getSuggestionType() {
        return "Replace For Loops with Functional statements";
    }


    @Override
    public Instruction getSuggestionComment(Instruction i) {
        Optional<InstructionBlock> loop = forLoops.stream().filter(instStartsBlock(i)).findFirst();
        if(loop.isPresent())
            return getForSuggestionComment(loop.get());
        return null;
    }

    @Override
    public boolean isLineOfInterest(Instruction aInst) {
        return forLoops.stream().anyMatch(instStartsBlock(aInst));
    }

    public void identifyForLoops() {
        Predicate<Instruction> forStatement = i -> !i.isComment() && i.minifiedInstruction.isPresent() && i.minifiedInstruction.get().contains("for(") &&
                i.endPunctuation.isPresent() && i.endPunctuation.get().contains("{");
        List<Instruction> forStatements = instructions.stream()
                .filter(forStatement)
                .collect(Collectors.toList());
        Collections.reverse(forStatements);
        forLoops = forStatements.stream().map(inst -> {
            for(int i = instructions.lastIndexOf(inst)+1;i<instructions.size();i++) {
                if(  instructions.get(i).containsText("}") && !instructions.get(i).isComment() ) {
                    return InstructionBlock.create(instructions.subList(instructions.lastIndexOf(inst), i+1), 0);
                }
            }
            return null;
        }).collect(Collectors.toList());
    }

    public static Instruction getForSuggestionComment(InstructionBlock aBlock) {
        switch(identifyForType(aBlock)) {
            case TYPE_1LIST_TO_ANOTHER :
                return Instruction.commentInstruction("A stream based operation could be used to create a new list from an old list.  One example is: secondList = firstList.stream().map(o -> {o.toString()}).collect(Collectors.toList());.  This will return a list of stings representing your objects.");
            case TYPE_COUNTER :
                return Instruction.commentInstruction("A stream based operation could be used to count items matching criteria.  One example is: long count = aList.stream().filter(o->o.toString().contains(\"value\")).count()");
            case TYPE_SUM :
                return Instruction.commentInstruction("A stream based operation could be used to sum items matching criteria.  One example is: long total = aList.stream().filter(o->o.toString().size()).sum()");
            case TYPE_FILTER :
                return Instruction.commentInstruction("A stream based operation could be used to filter your list for operations.  One example is: aList.stream().filter(o->o.equals(anObject)).forEach(o->{o.operation()});.  This will execute an operation on each item in the list, as long as it equals anObject.");
            default :
                return Instruction.commentInstruction("A stream based operation might be more readable here.  Check out http://www.tutorialspoint.com/java8/java8_streams.htm");
        }
    }

    public static int identifyForType(InstructionBlock aBlock) {
        if(aBlock.containsText(".add("))
            return TYPE_1LIST_TO_ANOTHER;
        if(aBlock.containsText("+1")||aBlock.containsText("++"))
            return TYPE_COUNTER;
        if(aBlock.containsText("+")||aBlock.containsText("+="))
            return TYPE_SUM;
        if(aBlock.containsText("if("))
            return TYPE_FILTER;
        return TYPE_DEFAULT;
    }
}