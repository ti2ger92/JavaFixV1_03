package model.code

import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */
class TestInstruction extends GroovyTestCase {
    @Test
    public void testInitialize() {
        Instruction basic = new Instruction(0, "int a = 1;\n", ";\n", "int a=1");
        assert true;
    }

    @Test
    public void testEquals() {
        Instruction basic1 = new Instruction(0, "int a = 1;\n", ";\n", "int a=1");
        Instruction basicSame = new Instruction(0, "int a = 1;\n", ";\n", "int a=1");
        Instruction basicDifferent1 = new Instruction(1, "int a = 1;\n", ";\n", "int a=1");
        Instruction basicDifferent2 = new Instruction(0, "int a = 1;\n", null, "int a=1");


        assert basic1.equals(basicSame);
        //assert !basic1.equals(basicDifferent1);
        assert !basic1.equals(basicDifferent2);
        assert !basicDifferent1.equals(basicDifferent2);
    }

    public static Instruction getBasicInstruction() {
        return new Instruction(0, "int a = 1;\n", ";\n", "int a=1");
    }

    public static Instruction getBasicInstruction2() {
        return new Instruction(0, "int b = 2;\n", ";\n", "int b=2");
    }

    public static Instruction getBasicInstruction3() {
        return new Instruction(0, "int c = 3;\n", ";\n", "int c=3");
    }

    public static ArrayList<Instruction> getAsList(Instruction aInstruction) {
        ArrayList<Instruction> retVal = new ArrayList<>();
        retVal.add(aInstruction);
        return retVal;
    }

    public static ArrayList<Instruction> getAsList(Instruction aInstruction, Instruction bInstruction) {
        ArrayList<Instruction> retVal = getAsList(aInstruction);
        retVal.add(bInstruction);
        return retVal;
    }
}
