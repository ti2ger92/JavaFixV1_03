package model.suggestion;

import model.code.Instruction;
import model.code.MicroLexer;
import model.outdated.Outdated;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public class NullChecksToOptional extends SuggestionComment {

    public static final Pattern NULL_CHECK = Pattern.compile("!="+ MicroLexer.WS+"null");

    public NullChecksToOptional(List<Instruction> instructions) {
        super(instructions);
    }

    @Override
    public String getSuggestionType() {
        return "Use Optional instead of null checks";
    }


    @Override
    public Instruction getSuggestionComment(Instruction i) {
        return Instruction.commentInstruction("Consider defining this as an optional instead, then use ifPresent() or map() to do this.  See https://examples.javacodegeeks.com/core-java/util/optional/java-8-optional-example/");
    }

    @Override
    public boolean isLineOfInterest(Instruction aInst) {
        return aInst.minifiedInstruction.filter(mi->{
            Matcher m = NULL_CHECK.matcher(mi);
            return m.find();
        }).isPresent();
    }
}
