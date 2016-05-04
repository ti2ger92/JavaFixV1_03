package model;

import model.code.Instruction;
import model.code.MicroLexer;
import model.outdated.Outdated;
import model.suggestion.Suggestion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Created by TY-MSI on 3/7/2016.
 * Primary class which takes incoming code block and returns cleaned up version.
 */
public class CodeTransform {
    public static String[] SUGGESTION_TYPES = {"TypedToMinimal", "RepeatedStrings", "ForToEnhanced", "RepeatedCode", "ForToFunctional", "NullChecksToOptional"};

    String oldCode;

    public CodeTransform(String aCodeSet) {
        oldCode = aCodeSet;
    }

    public String getTransformed() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        MicroLexer aLexer = new MicroLexer(oldCode);
        for(String suggestionType : SUGGESTION_TYPES) {
            Class<Suggestion> aSuggestionClass = (Class<Suggestion>) this.getClass().getClassLoader().loadClass("model.suggestion."+suggestionType);
            Constructor constructor = aSuggestionClass.getConstructor(List.class);
            Suggestion aSuggestion = (Suggestion) constructor.newInstance(aLexer.instructions);
            for(Outdated aOutdated : aSuggestion.getOutdateds()) {
                    aOutdated.applyTransform(aLexer.instructions);
            }

        }
        return aLexer.unlex();
    }

}
