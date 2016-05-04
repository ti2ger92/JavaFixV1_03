package model.code;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Created by TY-MSI on 3/5/2016.
 */
public class Instruction {
    public long origSeq = 0;
    public Optional<CodeBlock> source = Optional.empty();
    public Optional<String> endPunctuation  = Optional.empty();
    public Optional<String> minifiedInstruction  = Optional.empty();
    public boolean touched;


    public Instruction() {

    }

    public Instruction(long origSeq, String source, String endPunctuation, String minifiedInstruction) {
        this(origSeq, Optional.ofNullable(new CodeBlock(source)), Optional.ofNullable(endPunctuation), Optional.ofNullable(minifiedInstruction));
    }

    public Instruction(long aOrigSeq, Optional<CodeBlock> aSource, Optional<String> aEndPunctuation, Optional<String> aMinifiedInstruction) {
        this.origSeq = aOrigSeq;
        if(aSource!=null)
            this.source = aSource;
        if(aEndPunctuation!=null)
            this.endPunctuation = aEndPunctuation;
        if(aMinifiedInstruction!=null)
            this.minifiedInstruction = aMinifiedInstruction;
    }

    public Instruction copyNewSeq(List<Instruction> currentList) {
        Instruction newInst = new Instruction();
        newInst.origSeq = MicroLexer.getMaxLine(currentList).getAsLong();
        newInst.source = source;
        newInst.endPunctuation = endPunctuation;
        newInst.minifiedInstruction = minifiedInstruction;
        newInst.touched = touched;
        return newInst;
    }

    public static Instruction commentInstruction(String comment) {
        CodeBlock source = new CodeBlock(comment);
        Instruction aInst = new Instruction(0l, Optional.ofNullable(source), Optional.empty(), Optional.empty());
        return aInst.comment();
    }

    public boolean containsText(String text) {
        return unlex().contains(text);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Instruction) {
            if(/*origSeq ==((Instruction) o).origSeq &&*/
                    source.equals(((Instruction) o).source) &&
                    endPunctuation.equals(((Instruction) o).endPunctuation) &&
                    minifiedInstruction.equals(((Instruction) o).minifiedInstruction))
                return true;
        }
        return false;
    }

    public boolean codeEquals(Instruction o) {
        if(equals(o)) return true;
        return minifiedInstruction.equals(o.minifiedInstruction);
    }

    public void ensureReturn() {
        if(touched && endPunctuation.isPresent() && !endPunctuation.get().contains("\n"))
            endPunctuation = Optional.ofNullable(endPunctuation.get()+"\n");
        if(!touched && source.isPresent() && !source.get().code.contains("\n"))
            source.get().code = source.get().code+"\n";
    }

    public boolean isEmpty() {
        if(!source.isPresent() || "".equals(source.get().code))
            return  true;
        return false;
    }

    @Override
    public String toString() {
        return "An Instruction:\nsource: "+
                source + "\nendPunctionation: "+endPunctuation +
                "\nminifiedInstruction: "+minifiedInstruction;
    }

    public Instruction changeInstruction(Optional<String> newMinifiedInstruction) {
        Instruction newInst = new Instruction(this.origSeq, this.source, this.endPunctuation, newMinifiedInstruction);
        newInst.touched = true;
        return newInst;
    }

    public boolean isComment() {
        return unlex().startsWith("//")||unlex().startsWith("/*");
    }

    public Instruction comment() {
        if(this.source.isPresent()) {
            CodeBlock newSource = this.source.get().getCommented();
            return new Instruction(this.origSeq, Optional.ofNullable(newSource), this.endPunctuation, this.minifiedInstruction);
        }
        return new Instruction(this.origSeq, Optional.empty(), this.endPunctuation, this.minifiedInstruction);
    }

    public String unlex() {
        String code = "";
        if(touched) {
            if(minifiedInstruction.isPresent())
                code = code + minifiedInstruction.get();
            if(endPunctuation.isPresent())
                code = code + endPunctuation.get();
        } else {
            if(source.isPresent())
                code = code + source.get().code;
        }
        return code;
    }
}
