package model.suggestion;

import model.CodeTransform;
import model.code.Instruction;
import model.code.InstructionTransform;
import model.outdated.Outdated;

import java.util.*;
import java.util.function.Predicate;

/**
 * Basic strategy:
 * Create InstructionBlocks for the code, and compare them
 * --Start with N/2 floor block size, then decrease to BLOCK_MINIMUM
 * ---For each size, create first block A from the top, then next block B of same size just underneath, then move block B down until block B hits the end
 * ------then move block A 1 instruction down, and repeat.
 */
public class RepeatedCode extends Suggestion {

    public static final int BLOCK_MINIMUM=2;

    public HashSet<HashSet<InstructionBlockLocation>> blockMatches = new HashSet<>();

    public RepeatedCode(List<Instruction> instructions) {
        super(instructions);
        populateMatches();
    }

    @Override
    public String getSuggestionType() {
        return "RepeatedCode";
    }

    @Override
    public List<Outdated> getOutdateds() {
        Iterator<HashSet<InstructionBlockLocation>> matchSetsIterator= blockMatches.iterator();
        int methodCounter = 0;
        ArrayList<Outdated> outdates= new ArrayList<>();
        while(matchSetsIterator.hasNext()) {
            methodCounter++;
            String methodName = "method"+methodCounter;
            Instruction methodCall = new Instruction(0l, methodName+"();\n", "();\n", methodName+"()");
            HashSet<InstructionBlockLocation> aSet = matchSetsIterator.next();
            Outdated aOutdated = new Outdated(getSuggestionType());
            InstructionTransform aTransform = new InstructionTransform();
            Iterator<InstructionBlockLocation> aSetIterator = aSet.iterator();
            while(aSetIterator.hasNext()) {
                InstructionBlockLocation aBlockLoc = aSetIterator.next();
                aTransform.addManyTo1(new ArrayList<Instruction>(instructions.subList(aBlockLoc.start,aBlockLoc.end)), methodCall);
                if(!aSetIterator.hasNext()) {
                    aTransform.addAtEnd.add(new Instruction(0l, "public void "+methodName+"(){\n","(){\n","public void "+methodName+"()"));
                    aTransform.addAtEnd.addAll(instructions.subList(aBlockLoc.start,aBlockLoc.end));
                    aTransform.addAtEnd.add(new Instruction(0l,"}\n","}\n",null));
                }
            }
            aOutdated.setInstructionTransform(aTransform);
            outdates.add(aOutdated);
        }
        return outdates;
    }

    @Override
    public boolean isLineOfInterest(Instruction aInst) {return false;}

    public void populateMatches() {
        for(int blockSize=instructions.size()/2;blockSize>=BLOCK_MINIMUM;blockSize--) {
            HashSet<InstructionBlockLocation> results = compareInstruction(blockSize);
            if(results!=null)
                blockMatches.add(results);
        }
    }

    public HashSet<InstructionBlockLocation> compareInstruction(int blockSize) {
        InstructionBlockLocation startLocation = new InstructionBlockLocation(0, blockSize);
        return compareInstructionFromRecurse(startLocation);
    }

    public HashSet<InstructionBlockLocation> compareInstructionFromRecurse(InstructionBlockLocation fromLocation) {
        if(fromLocation==null)
            return null;
        HashSet<InstructionBlockLocation> currentMatches = compareInstructionFromRecurse(fromLocation.step());
        if(!intersectionExists(currentMatches, fromLocation)) {
            InstructionBlock fromBlock = InstructionBlock.create(instructions.subList(fromLocation.start, fromLocation.end), BLOCK_MINIMUM);
            HashSet<InstructionBlockLocation> theseMatches = compareInstructionToRecurse(fromBlock, fromLocation.nextBlock());
            if (theseMatches != null) {
                if (currentMatches == null) currentMatches = new HashSet<>();
                currentMatches.addAll(theseMatches);
                currentMatches.add(fromLocation);
            }
        }
        return currentMatches;
    }

    public HashSet<InstructionBlockLocation> compareInstructionToRecurse(InstructionBlock fromBlock, InstructionBlockLocation toLocation) {
        if(toLocation==null || fromBlock==null)
            return null;
        HashSet<InstructionBlockLocation> currentMatches = compareInstructionToRecurse(fromBlock, toLocation.step());
        if(!intersectionExists(currentMatches, toLocation)) {
            InstructionBlock toBlock = new InstructionBlock(instructions.subList(toLocation.start, toLocation.end));
            if (fromBlock.equals(toBlock)) {
                if (currentMatches == null) currentMatches = new HashSet<>();
                currentMatches.add(toLocation);
            }
        }
        return currentMatches;
    }


    public InstructionBlockLocation getInstructionBlockLocation(int start, int end) {
        return new InstructionBlockLocation(start, end);
    }

    public boolean intersectionExists(Set<InstructionBlockLocation> currentSet, InstructionBlockLocation aLocation) {
        if(setContainsIntersection(currentSet, aLocation))
            return true;
        Iterator<HashSet<InstructionBlockLocation>> lists = blockMatches.iterator();
        while(lists.hasNext()) {
            HashSet<InstructionBlockLocation> list = lists.next();
            if(setContainsIntersection(list, aLocation))
                return true;
        }
        return false;
    }

    public static boolean setContainsIntersection(Set<InstructionBlockLocation> aSet, InstructionBlockLocation aLocation) {
        if(aSet!=null) {
            Iterator<InstructionBlockLocation> candidatesItorator= aSet.iterator();
            while(candidatesItorator.hasNext()) {
                InstructionBlockLocation candidate = candidatesItorator.next();
                if(candidate.intersects(aLocation))
                    return true;
            }
        }
        return false;
    }

    public class InstructionBlockLocation {
        public int start;
        public int end;

        public InstructionBlockLocation(int aStart, int aEnd) {
            start = aStart;
            end = aEnd;
        }

        public InstructionBlockLocation step() {
            if(end<instructions.size())
                return new InstructionBlockLocation(start+1,end+1);
            return null;
        }

        public InstructionBlockLocation nextBlock() {
            if(end+getSize()<=instructions.size())
                return new InstructionBlockLocation(start+getSize(), end+getSize());
            return null;
        }

        public int getSize() {
            return end-start;
        }

        public boolean intersects(InstructionBlockLocation other) {
            if(other==null) return false;
            return this.start <= other.end && other.start <= this.end;
        }

        @Override
        public String toString() {
            return "("+start+","+end+")";
        }

        @Override
        public boolean equals(Object other) {
            if(other instanceof InstructionBlockLocation) {
                return ((InstructionBlockLocation) other).start==start && ((InstructionBlockLocation) other).end==end;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return start*31+end*17;
        }
    }



}
