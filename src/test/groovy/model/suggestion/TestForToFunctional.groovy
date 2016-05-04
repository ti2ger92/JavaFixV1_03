package model.suggestion

import model.CodeTransformTest
import model.code.Instruction
import org.junit.Ignore
import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */


class TestForToFunctional extends GroovyTestCase {

    @Test
    public void testIdentifyFor() {
        ForToFunctional subjectDefault = getSubjectfromFile("defaultForComment.txt");
        ArrayList<Instruction> expectedList = new ArrayList<>();
        expectedList.addAll(subjectDefault.instructions.subList(2,8))
        Suggestion.InstructionBlock expected = Suggestion.InstructionBlock.create(expectedList, 0);
        subjectDefault.identifyForLoops();
        assert subjectDefault.forLoops.size()==1;
        assert subjectDefault.forLoops.get(0).equals(expected);
        assert ForToFunctional.identifyForType(subjectDefault.forLoops.get(0)).equals(ForToFunctional.TYPE_DEFAULT);
        ForToFunctional subject1ListToAnother = getSubjectfromFile("for1ListToAnother.txt");
        assert ForToFunctional.identifyForType(subject1ListToAnother.forLoops.get(0))==ForToFunctional.TYPE_1LIST_TO_ANOTHER;
        assert subject1ListToAnother.forLoops.size()==1;
        ForToFunctional subjectForCounter = getSubjectfromFile("forCounter.txt");
        assert ForToFunctional.identifyForType(subjectForCounter.forLoops.get(0))==ForToFunctional.TYPE_COUNTER;
        assert subjectForCounter.forLoops.size()==1;
ForToFunctional subjectForFilter = getSubjectfromFile("forFilter.txt");
        assert ForToFunctional.identifyForType(subjectForFilter.forLoops.get(0))==ForToFunctional.TYPE_FILTER;
        assert subjectForFilter.forLoops.size()==1;
        ForToFunctional subjectForSum = getSubjectfromFile("forSum.txt");
        assert subjectForSum.identifyForType(subjectForSum.forLoops.get(0))==ForToFunctional.TYPE_SUM;
        assert subjectForSum.forLoops.size()==1;
    }


    @Test
    public void testDefaultForComment() {
        ForToFunctional subject = getSubjectfromFile("defaultForComment.txt");
        assert subject.getOutdateds().size()==1;
        assert subject.getOutdateds().get(0).instructionTransform.map.keySet().iterator().next().source.get().code.contains("for(Dog d : dogs) {");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(0).source.get().code.contains("//A stream based operation might be more readable here.  Check out http://www.tutorialspoint.com/java8/java8_streams.htm");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(1).source.get().code.contains("for(Dog d : dogs) {");
    }

    @Test
    public void testfor1ListToAnother() {
        ForToFunctional subject = getSubjectfromFile("for1ListToAnother.txt");
        assert subject.getOutdateds().size()==1;
        assert subject.getOutdateds().get(0).instructionTransform.map.keySet().iterator().next().source.get().code.contains("for(Dog d : dogs) {");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(0).source.get().code.contains("//A stream based operation could be used to create a new list from an old list.  One example is: secondList = firstList.stream().map(o -> {o.toString()}).collect(Collectors.toList());.  This will return a list of stings representing your objects.");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(1).source.get().code.contains("for(Dog d : dogs) {");
    }

    @Test
    public void testForCounter() {
        ForToFunctional subject = getSubjectfromFile("forCounter.txt");
        assert subject.getOutdateds().size()==1;
        assert subject.getOutdateds().get(0).instructionTransform.map.keySet().iterator().next().source.get().code.contains("for(Dog d : dogs) {");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(0).source.get().code.contains("//A stream based operation could be used to count items matching criteria.  One example is: long count = aList.stream().filter(o->o.toString().contains(\"value\")).count()");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(1).source.get().code.contains("for(Dog d : dogs) {");
    }

    @Test
    public void testForFilter() {
        ForToFunctional subject = getSubjectfromFile("forFilter.txt");
        assert subject.getOutdateds().size()==1;
        assert subject.getOutdateds().get(0).instructionTransform.map.keySet().iterator().next().source.get().code.contains("for(Dog d : dogs) {");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(0).source.get().code.contains("//A stream based operation could be used to filter your list for operations.  One example is: aList.stream().filter(o->o.equals(anObject)).forEach(o->{o.operation()});.  This will execute an operation on each item in the list, as long as it equals anObject.");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(1).source.get().code.contains("for(Dog d : dogs) {");
    }

    @Test
    public void testForSum() {
        ForToFunctional subject = getSubjectfromFile("forSum.txt");
        assert subject.getOutdateds().size()==1;
        assert subject.getOutdateds().get(0).instructionTransform.map.keySet().iterator().next().source.get().code.contains("for(Dog d : dogs) {");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(0).source.get().code.contains("//A stream based operation could be used to sum items matching criteria.  One example is: long total = aList.stream().filter(o->o.toString().size()).sum()");
        assert subject.getOutdateds().get(0).instructionTransform.map.entrySet().iterator().next().getValue().get(1).source.get().code.contains("for(Dog d : dogs) {");
    }


    public ForToFunctional getSubjectfromFile(String fileName) {
        String address = "sources/forToFunctional/"+fileName;
        ArrayList<Instruction> source = CodeTransformTest.getInsttructionsFromResource(address);
        return new ForToFunctional(source);
    }
}
