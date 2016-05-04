package model.code

import model.CodeTransformTest
import org.junit.Test

/**
 * Created by TY-MSI on 3/5/2016.
 */
class TestMicroLexer extends GroovyTestCase {

    @Test
    public void testNull() {
        ArrayList<Instruction> emptyList = new ArrayList<>();
        testLex("", emptyList);
    }

    @Test
    public void testAssign() {
        String src = "a=1;\n\t";
        Instruction result = new Instruction(0, src, ";\n\t","a=1");
        testLexInstruction(src, result);
    }

    @Test
    public void testDeclareAssign() {
        String src = "\tint a;\n\ta=1;\n\t";
        Instruction inst = new Instruction(0,"\tint a;\n\t", ";\n\t", "int a");
        Instruction inst2 = new Instruction(0, "\tint a;\n\ta=1;\n\t", ";\n\t","a=1");
        ArrayList<Instruction> result = new ArrayList<>();
        result.add(inst);
        result.add(inst2);
        testLex(src, result);
    }

    @Test
    public void testForLoop() {
        MicroLexer subject = testLoadCode("lexTests/forLoop.txt");
        assert subject!=null;
        assert subject.instructions.size()==7;
        assert subject.instructions.get(0).minifiedInstruction.get().equals("String aVar = \"aString\"");
        assert subject.instructions.get(1).minifiedInstruction.get().equals("for(int i=0;i<max;i++)");
        assert subject.instructions.get(2).minifiedInstruction.get().equals("aClass.aMethod(i)");
        assert subject.instructions.get(3).minifiedInstruction.get().equals("aClass.anotherMethod(i)");
        assert subject.instructions.get(4).endPunctuation.get().equals("}\r\n")
        assert subject.instructions.get(5).minifiedInstruction.get().equals("aVar = \"anotherString\"");
        assert subject.instructions.get(6).minifiedInstruction.get().equals("int anotherVar = 5");
    }

    public void testLex(String source, ArrayList<Instruction> expected) {
        MicroLexer aLex = new MicroLexer(source);
        assert aLex.instructions.size()==expected.size();
        for(int i=0;i<expected.size() && i<aLex.instructions.size();i++) {
            Instruction got = expected.get(i);
            Instruction expectedInst = expected.get(i);
            assert got.equals(expectedInst);
        }
    }

    public void testLexInstruction(String source, Instruction expected) {
        MicroLexer aLex = new MicroLexer(source);
        assert aLex.instructions.size()==1;
        if(aLex.instructions.size()>0)
            assert aLex.instructions.get(0).equals(expected);
    }


    public MicroLexer testLoadCode(String resourcefileLoc) {
        String inputCode = CodeTransformTest.getTextFromResource(resourcefileLoc);
        MicroLexer aLex = new MicroLexer(inputCode);
    }
}
