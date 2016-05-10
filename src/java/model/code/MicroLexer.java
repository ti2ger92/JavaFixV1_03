package model.code;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TY-MSI on 3/5/2016.
 */
public class MicroLexer {


    public static final String WS = "[\\s]*";

    public static final Pattern OLD_FOR_PATTERN = Pattern.compile("([^;]*)(for[ ]*\\(([^;\\)]*;[^;\\)]*;[^;\\)]*)\\))("+WS+"\\{"+WS+")");
    public static final Pattern LINE_END = Pattern.compile(WS+"[{};]"+ WS);
    public static final Pattern ONE_LINE_COMMENT =  Pattern.compile("//[\\s\\S]*?\\n|/\\*[\\s\\S]*?\\*/");




    public ArrayList<Instruction> instructions = new ArrayList<>();
    public ArrayList<Comment> comments = new ArrayList<>();

    public MicroLexer(String source) {
        lex(source);
    }

    public void lex(String source) {
        int bailout = 10000;
        if(source==null || source.isEmpty())
            return;
        CodeBlock in = new CodeBlock(source);
        int charIndex = 0;
        consumeComments(in);
        for(int i=0;i<bailout && in.code!=null;i++) {
            addComments(charIndex);
            int curLen = in.code.length();
            Instruction aInst = consumeInstruction(in, i);
            if(in.code!=null)
                charIndex+=curLen-in.code.length();
            if(!aInst.isEmpty())
                instructions.add(aInst);
        }
    }

    public String unlex() {
        String code = "";
        for(Instruction aInst :  instructions) {
            code = code + aInst.unlex();
        }
        return code;
    }

    public void consumeComments(CodeBlock block) {
        for(int i=0;i<1000;i++) {
            Matcher m = ONE_LINE_COMMENT.matcher(block.code);
            if (m.find()) {
                Comment aComment= new Comment();
                aComment.loc = m.start();
                aComment.commentString = m.group();
                comments.add(aComment);
                block.code = m.replaceFirst("");
            } else break;
        }
    }

    public void addComments(int charIndex) {
        Iterator<Comment> commentIterator= comments.iterator();
        while(commentIterator.hasNext()) {
            Comment aComment = commentIterator.next();
            if (charIndex >= aComment.loc) {
                CodeBlock source = new CodeBlock(aComment.commentString);
                Instruction aCommentInstruction = new Instruction(0l, Optional.ofNullable(source), Optional.empty(), Optional.empty());
                instructions.add(aCommentInstruction);
                commentIterator.remove();
            }
        }
    }

    public Instruction consumeInstruction(CodeBlock block, int order) {
        //Check for an old for loops
        int locSemicolon = block.code.indexOf(";");
        Instruction aInst = new Instruction();
        aInst.origSeq = order;
        Matcher m = OLD_FOR_PATTERN.matcher(block.code);
        if(m.find() && m.start()<locSemicolon) {
            aInst.source = Optional.ofNullable(new CodeBlock(m.group(0)));
            block.code = block.code.substring(m.end(0));
            aInst.minifiedInstruction = Optional.ofNullable(m.group(2));
            aInst.endPunctuation = Optional.ofNullable(m.group(4));
        }
        else if(locSemicolon==-1) {
            aInst.source = Optional.ofNullable(new CodeBlock(block.code));
            block.code = null;
        }
        else {
            m = LINE_END.matcher(block.code);
            m.find();
            aInst.source = Optional.ofNullable(new CodeBlock(block.code.substring(0,m.end())));
            aInst.minifiedInstruction = Optional.ofNullable(block.code.substring(0, m.start()));
            aInst.endPunctuation = Optional.ofNullable(m.group(0));
            block.code = block.code.substring(m.end());
        }
        return aInst;
    }

    public class Comment {
        long loc;
        String commentString;
    }

    public static OptionalLong getMaxLine(List<Instruction> instList) {
         return instList.stream().mapToLong(i->i.origSeq).max();
    }
}
