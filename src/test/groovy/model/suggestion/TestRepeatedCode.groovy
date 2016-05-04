package model.suggestion

import model.CodeTransformTest
import model.code.Instruction
import model.code.TestInstruction
import org.junit.Ignore
import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */
class TestRepeatedCode {
    public Instruction basic1 = new Instruction(0, "int loc = aString.indexOf(\"searched\");\n", ";\n", "int loc=aString.indexOf(\"searched\")");
    public Instruction basic2 = new Instruction(0, "aString = aString.subString(0, loc);\n", ";\n", "aString=aString.subString(0,loc)");
    public Instruction basic3 = new Instruction(0, "int loc = aString.indexOf(\"searched\");\n", ";\n", "int loc=aString.indexOf(\"searched\")");
    public Instruction basic4 = new Instruction(0, "aString = aString.subString(0, loc);\n", ";\n", "aString=aString.subString(0,loc)");

    @Test
    public void testInitialize() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic1);
        RepeatedCode aRC = new RepeatedCode(aList);
        assert aRC.instructions.size()==1;
    }

    @Test
    public void testCompareInstructionToRecurseSimpleMatch() {
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource("sources/repeatedCodeIdentify01/simpleMatch.txt");
        RepeatedCode repeatedCode = new RepeatedCode(source);
        repeatedCode.blockMatches = new HashSet<>();
        RepeatedCode.InstructionBlockLocation expected = repeatedCode.getInstructionBlockLocation(2,4);
        RepeatedCode.InstructionBlockLocation unexpected = repeatedCode.getInstructionBlockLocation(3,5);
        Suggestion.InstructionBlock fromBlock = source.subList(0,2);
        RepeatedCode.InstructionBlockLocation toLoc = repeatedCode.getInstructionBlockLocation(2,4);

        HashSet<RepeatedCode.InstructionBlockLocation> result = repeatedCode.compareInstructionToRecurse(fromBlock, toLoc);
        assert result.size()==1;
        assert result.contains(expected);
        assert !result.contains(unexpected);
    }

    @Test
    public void testCompareInstructionToRecurseSeimpleNoMatch() {
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource("sources/repeatedCodeIdentify01/simpleNoMatch.txt");
        RepeatedCode repeatedCode = new RepeatedCode(source);
        RepeatedCode.InstructionBlockLocation expected = repeatedCode.getInstructionBlockLocation(2,4);
        RepeatedCode.InstructionBlockLocation unexpected = repeatedCode.getInstructionBlockLocation(3,5);
        Suggestion.InstructionBlock fromBlock = source.subList(0,2);
        RepeatedCode.InstructionBlockLocation toLoc = repeatedCode.getInstructionBlockLocation(2,4);

        HashSet<RepeatedCode.InstructionBlockLocation> result = repeatedCode.compareInstructionToRecurse(fromBlock, toLoc);
        assert result==null;
    }

    @Test
    public void testCompareInstructionToRecurse2BetweenMatch() {
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource("sources/repeatedCodeIdentify01/match2inbetween.txt");
        RepeatedCode repeatedCode = new RepeatedCode(source);
        repeatedCode.blockMatches = new HashSet<>();
        RepeatedCode.InstructionBlockLocation expected = repeatedCode.getInstructionBlockLocation(4,6);
        RepeatedCode.InstructionBlockLocation unexpected = repeatedCode.getInstructionBlockLocation(3,5);
        Suggestion.InstructionBlock fromBlock = source.subList(0,2);
        RepeatedCode.InstructionBlockLocation toLoc = repeatedCode.getInstructionBlockLocation(2,4);
        HashSet<RepeatedCode.InstructionBlockLocation> result = repeatedCode.compareInstructionToRecurse(fromBlock, toLoc);
        assert result.size()==1;
        assert result.contains(expected);
        assert !result.contains(unexpected);
    }

    @Test
    public void testCompareInstructionFromRecurseSimpleMatch() {
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource("sources/repeatedCodeIdentify01/simpleMatch.txt");
        RepeatedCode repeatedCode = new RepeatedCode(source);
        repeatedCode.blockMatches = new HashSet<>();
        RepeatedCode.InstructionBlockLocation fromLoc = repeatedCode.getInstructionBlockLocation(0,2);
        RepeatedCode.InstructionBlockLocation expected = repeatedCode.getInstructionBlockLocation(2,4);
        RepeatedCode.InstructionBlockLocation unexpected = repeatedCode.getInstructionBlockLocation(3,5);
        HashSet<RepeatedCode.InstructionBlockLocation> result = repeatedCode.compareInstructionFromRecurse(fromLoc);
        assert result.size()==2;
        assert result.contains(fromLoc);
        assert result.contains(expected);
        assert !result.contains(unexpected);
    }

    @Test
    public void testCompareInstructionFromRecurse2BetweenMatch() {
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource("sources/repeatedCodeIdentify01/match2inbetween.txt");
        RepeatedCode repeatedCode = new RepeatedCode(source);
        repeatedCode.blockMatches = new HashSet<>();
        RepeatedCode.InstructionBlockLocation fromLoc = repeatedCode.getInstructionBlockLocation(0,2);
        RepeatedCode.InstructionBlockLocation expected = repeatedCode.getInstructionBlockLocation(4,6);
        RepeatedCode.InstructionBlockLocation unexpected = repeatedCode.getInstructionBlockLocation(3,5);
        HashSet<RepeatedCode.InstructionBlockLocation> result = repeatedCode.compareInstructionFromRecurse(fromLoc);
        assert result.size()==2;
        assert result.contains(fromLoc);
        assert result.contains(expected);
        assert !result.contains(unexpected);
    }

    @Test
    public void testCompareInstruction2BetweenMatch() {
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource("sources/repeatedCodeIdentify01/match2inbetween.txt");
        RepeatedCode repeatedCode = new RepeatedCode(source);
        repeatedCode.blockMatches = new HashSet<>();
        RepeatedCode.InstructionBlockLocation expected1 = repeatedCode.getInstructionBlockLocation(0,2);
        RepeatedCode.InstructionBlockLocation expected2 = repeatedCode.getInstructionBlockLocation(4,6);
        RepeatedCode.InstructionBlockLocation unexpected = repeatedCode.getInstructionBlockLocation(3,5);
        HashSet<RepeatedCode.InstructionBlockLocation> result = repeatedCode.compareInstruction(2);
        assert result.size()==2;
        assert result.contains(expected1);
        assert result.contains(expected2)
        assert !result.contains(unexpected);
    }

    @Test
    public void testCompareInstruction3BlocksSize3() {
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource("sources/repeatedCodeIdentify01/match3BlocksSize3.txt");
        RepeatedCode repeatedCode = new RepeatedCode(source);
        repeatedCode.blockMatches = new HashSet<>();
        HashSet<RepeatedCode.InstructionBlockLocation> expected = new HashSet<>();
        expected.add(repeatedCode.getInstructionBlockLocation(1,4));
        expected.add(repeatedCode.getInstructionBlockLocation(9,12));
        expected.add(repeatedCode.getInstructionBlockLocation(17,20));
        RepeatedCode.InstructionBlockLocation unexpected = repeatedCode.getInstructionBlockLocation(3,5);
        HashSet<RepeatedCode.InstructionBlockLocation> result = repeatedCode.compareInstruction(3);
        assert result.size()==3;
        for(RepeatedCode.InstructionBlockLocation aExpected: expected.asList())
            assert result.contains(aExpected);
    }

    @Test
    public void testPopulateMatches3BlocksSize3() {
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource("sources/repeatedCodeIdentify01/match3BlocksSize3.txt");
        RepeatedCode repeatedCode = new RepeatedCode(source);
        repeatedCode.blockMatches = new HashSet<>();
        HashSet<RepeatedCode.InstructionBlockLocation> expected = new HashSet<>();
        expected.add(repeatedCode.getInstructionBlockLocation(1,4));
        expected.add(repeatedCode.getInstructionBlockLocation(9,12));
        expected.add(repeatedCode.getInstructionBlockLocation(17,20));
        RepeatedCode.InstructionBlockLocation unexpected = repeatedCode.getInstructionBlockLocation(3,5);
        repeatedCode.populateMatches();
        assert repeatedCode.blockMatches.size()==1;
        HashSet<RepeatedCode.InstructionBlockLocation> result = repeatedCode.blockMatches.getAt(0);
        assert result.size()==3;
        for(RepeatedCode.InstructionBlockLocation aExpected: expected.asList())
            assert result.contains(aExpected);
    }

}
