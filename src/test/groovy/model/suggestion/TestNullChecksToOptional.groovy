package model.suggestion

import model.CodeTransformTest
import model.code.Instruction
import model.code.TestInstruction
import org.junit.Ignore
import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016
 */
class TestNullChecksToOptional  {
    public Instruction basic = new Instruction(0, "if(a!=null) {\n", " {\n", "if(a!=null)");

    @Test
    public void testInitialize() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic);
        NullChecksToOptional aNCTO = new NullChecksToOptional(aList);
        assert aNCTO.instructions.size()==1;
    }

    @Test
    public void testBasic() {
        ArrayList<Instruction> aList = TestInstruction.getAsList(basic, TestInstruction.getBasicInstruction());
        NullChecksToOptional aNCTO = new NullChecksToOptional(aList);
        assert aNCTO.getOldLinesOfInterest().size()==1;
        assert aNCTO.getOldLinesOfInterest().get(0).equals(basic);
    }

    @Test
    public void testFindNullChecks() {
        NullChecksToOptional subject = getSubjectfromFile("findNullChecks.txt");
        assert subject.getOldLinesOfInterest().size()==2;
        assert subject.getOutdateds().size()==2;
        assert subject.getOutdateds().get(0).instructionTransform.map.values().getAt(0).get(0).unlex().contains("//Consider defining this as an optional instead, then use ifPresent() or map() to do this.  See https://examples.javacodegeeks.com/core-java/util/optional/java-8-optional-example/");
        assert subject.getOutdateds().get(1).instructionTransform.map.values().getAt(0).get(0).unlex().contains("//Consider defining this as an optional instead, then use ifPresent() or map() to do this.  See https://examples.javacodegeeks.com/core-java/util/optional/java-8-optional-example/");
    }

    public NullChecksToOptional getSubjectfromFile(String fileName) {
        String address = "sources/nullChecksToOptional/"+fileName;
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource(address);
        return new NullChecksToOptional(source);
    }
}
