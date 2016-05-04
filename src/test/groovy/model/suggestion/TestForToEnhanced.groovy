package model.suggestion

import model.code.Instruction
import model.code.TestInstruction
import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */
class TestForToEnhanced extends GroovyTestCase {
    public Instruction basic = new Instruction(0, "for(int i = 1;i<list.size();i++) {\n", " {\n", "for(int i = 1;i<list.size();i++)");

    @Test
    public void testInitialize() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic);
        ForToEnhanced aFTE = new ForToEnhanced(aList);
        assert aFTE.instructions.size()==1;
    }

    @Test
    public void testBasic() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic, TestInstruction.getBasicInstruction());
        ForToEnhanced aFTE = new ForToEnhanced(aList);
        assert aFTE.isLineOfInterest(aList.get(0));
        assert aFTE.getOldLinesOfInterest().size()==1;
        assert aFTE.getOldLinesOfInterest().get(0).equals(basic);
        assert "for(Object alist : list) {\n".equals(aFTE.transform(basic).unlex());
    }
}
