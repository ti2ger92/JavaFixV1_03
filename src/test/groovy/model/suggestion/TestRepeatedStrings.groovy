package model.suggestion

import model.code.Instruction
import model.code.TestInstruction
import org.junit.Ignore
import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */
class TestRepeatedStrings {
    public Instruction basic1 = new Instruction(0, "int result = someMethod(\"string1\");\n", ";\n", "int result=someMethod(\"string1\")");
    public Instruction basic2 = new Instruction(0, "int result = someOtherMethod(\"string1\");\n", ";\n", "int result=someOtherMethod(\"string1\")");

    @Test
    public void testInitialize() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic1);
        RepeatedStrings aRS = new RepeatedStrings(aList);
        assert aRS.instructions.size()==1;
    }

}
