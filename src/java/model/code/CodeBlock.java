package model.code;

import java.util.regex.Pattern;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public class CodeBlock {
    public String code;

    public CodeBlock(String code) {
        if(code==null)
            throw new NullPointerException("A CodeBlock was attempted to be created with a null code.  This is not allowed.");
        this.code = code;
    }

    public CodeBlock getCommented() {
        if("".equals(code)||null==code)
            return new CodeBlock(code);
        else if(!code.contains("\n"))
            return new CodeBlock("//"+code+"\n");
        else if(code.indexOf("\n")==code.length()-1)
            return new CodeBlock("//"+code);
        else {
            String aStart = "/*\n";
            String aEnd = "\n*/\n";
            return new CodeBlock(aStart+code+aEnd);
        }

    }

    public String withoutComments(int length) {
        String oneLineComment =  "//[\\s\\S]*\n";
        String multiLineComment = "/\\*[\\s\\S]*\\*/";
        String searchString = code.substring(0, length);
        String rest = code.substring(length);
        searchString = searchString.replaceAll(oneLineComment, "");
        searchString = searchString.replaceAll(multiLineComment, "");
        return searchString+rest;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof  CodeBlock) {
            if(this.code.equals(((CodeBlock) o).code))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Code Block with code: "+code;
    }
}
