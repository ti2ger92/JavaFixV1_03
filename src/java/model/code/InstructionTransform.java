package model.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by TY-MSI on 3/7/2016.
 */
public class InstructionTransform {
    public HashMap<Instruction, List<Instruction>> map = new HashMap<>();
    public ArrayList<Instruction> addAtEnd = new ArrayList<>();

    public void add1To1(Instruction  from, Instruction to) {
        ArrayList<Instruction> toList = new ArrayList<>();
        toList.add(to);
        add1toMany(from, toList);
    }

    public void addManyToEnd(ArrayList<Instruction> from, ArrayList<Instruction> to) {
        delete(from);
        addAtEnd.addAll(to);
    }

    public void addManyTo1(List<Instruction> from, Instruction to) {
        ArrayList<Instruction> toList = new ArrayList<>();
        toList.add(to);
        addManyToFirst( from, toList);
    }

    public void addManyToFirst(List<Instruction> from, List<Instruction> to) {
        if(from.size()>0)
            add1toMany(from.get(0), to);
        from.remove(0);
        delete(from);
    }

    public void add1toMany(Instruction from, List<Instruction> to) {
        if(to==null)
            to = new ArrayList<Instruction>();
        map.put(from, to);
    }

    public List<Instruction> getTo(Instruction from) {
        return map.get(from);
    }

    public void delete(List<Instruction> delList) {
        for(Instruction aInst : delList) {
            add1toMany(aInst, null);
        }
    }

    public void apply(ArrayList<Instruction> instructions, String suggestion) {
        for(int i = 0;i<instructions.size();i++) {
            Instruction currentInstruction = instructions.get(i);
            if(map.containsKey(currentInstruction)){
                List<Instruction> replaceVal = map.get(currentInstruction);
                Instruction aComment = Instruction.commentInstruction("Suggested change from: "+suggestion);
                instructions.add(i, aComment);
                if(replaceVal==null)
                    instructions.remove(i+1);
                else {
                    instructions.addAll(i+1, replaceVal);
                    instructions.remove(i+1+replaceVal.size());
                }

            }
        }
        instructions.get(instructions.size()-1).ensureReturn();
        instructions.addAll(addAtEnd);
    }
}
