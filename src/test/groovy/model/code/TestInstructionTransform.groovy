package model.code

import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */
class TestInstructionTransform extends GroovyTestCase {
    @Test
    public void testInitialize() {
        InstructionTransform aTransform = new InstructionTransform();
        assert true;
    }

    @Test
    public void testAdd1To1() {
        InstructionTransform aTransform = new InstructionTransform();
        Instruction aInst = TestInstruction.basicInstruction;
        aTransform.add1To1(aInst, TestInstruction.basicInstruction2);
        assert aTransform.map.size()==1;
        assert aTransform.getTo(aInst).get(0).equals(TestInstruction.basicInstruction2);
    }

    @Test
    public void testManyToEnd() {
        InstructionTransform aTransform = new InstructionTransform();
        ArrayList<Instruction> from = new ArrayList<>();
        from.add(TestInstruction.basicInstruction);
        from.add(TestInstruction.basicInstruction2);
        ArrayList<Instruction> to = new ArrayList<>();
        to.add(TestInstruction.basicInstruction3);
        aTransform.addManyToEnd(from, to);
        assert aTransform.map.size()==2;
        assert aTransform.addAtEnd.size()==1;
    }

    @Test
    public void testAddManytoFirst() {
        InstructionTransform aTransform = new InstructionTransform();
        Instruction aInst = TestInstruction.basicInstruction
        ArrayList<Instruction> from = new ArrayList<>();
        from.add(aInst);
        from.add(TestInstruction.basicInstruction2);
        ArrayList<Instruction> to = new ArrayList<>();
        to.add(TestInstruction.basicInstruction3);
        aTransform.addManyToFirst(from, to);
        assert aTransform.map.size()==2;
        assert aTransform.map.get(aInst).get(0).equals(TestInstruction.basicInstruction3);
    }


}
