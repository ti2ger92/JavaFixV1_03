package model.suggestion

import model.code.CodeBlock
import model.code.Instruction
import model.code.TestInstruction
import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */
class TestTypedToMinimal {
    public Instruction basic = new Instruction(0, "TypedObject<Type> aTyped = new TypedObject<Type>();\n", ";\n", "TypedObject<Type> aTyped=new TypedObject<Type>()");

    @Test
    public void testInitialize() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic);
        TypedToMinimal aTTM = new TypedToMinimal(aList);
        assert aTTM.instructions.size()==1;
    }

    @Test
    public void testBasic() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic, TestInstruction.getBasicInstruction());
        TypedToMinimal aTTM = new TypedToMinimal(aList);
        assert aTTM.getOldLinesOfInterest().size()==1;
        assert aTTM.getOldLinesOfInterest().get(0).equals(basic);
    }

    @Test
    public void testIsLineOfInterest() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic);
        TypedToMinimal aTTM = new TypedToMinimal(aList);
        assert aTTM.isLineOfInterest(basic);
    }

    @Test
    public void testTransform() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic);
        TypedToMinimal aTTM = new TypedToMinimal(aList);
        Instruction result = aTTM.transform(basic);
        assert result!=null;
        assert "TypedObject<Type> aTyped=new TypedObject<>()".equals(result.minifiedInstruction.get());
    }
}
