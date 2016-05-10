package model

import model.code.Instruction
import model.code.MicroLexer
import org.junit.Ignore
import org.junit.Test

/**
 * Created by TY-MSI on 3/15/2016.
 */
class CodeTransformTest extends GroovyTestCase {

    @Test
    public void testInitialize() {
        CodeTransform aTransform = new CodeTransform();
        assert aTransform!=null;
    }

    @Test
    public void testNullTransform() {
        CodeTransform aTransform = new CodeTransform(null);
        String transformed = aTransform.getTransformed();
    }

    @Test
    public void testUnchanged() {
        String unchangedCode = "This is some code which won't change";
        CodeTransform aT = new CodeTransform(unchangedCode);
        String transformed = aT.getTransformed();
        assert unchangedCode.equals(transformed);
    }


    @Test
    //@Ignore("Currently teseting a specific transform")
    public void testTransforms() {
        File transformsFolder = new File(this.getClass().getClassLoader().getResource("transforms").toURI());
        File[] folders = transformsFolder.listFiles();
        for(File aFolder : Arrays.asList(folders)) {
            testTransform(aFolder);
        }
    }

    @Test
    @Ignore("Currently testing all transforms")
    public void testTransform() {
        File transformFolder = new File(this.getClass().getClassLoader().getResource("transforms/forToFunctional01").toURI());
        testTransform(transformFolder);
    }

    @Test
    public void testSpeed() {
        String largeSource01 = getTextFromResource("sources/largeFiles/largeFile01.txt");
        CodeTransform aT = new CodeTransform(largeSource01);
        String transformed = aT.getTransformed();
    }

    public void testTransform(File aFolder) {
        String codeIn = getTextFromFile(aFolder.getAbsolutePath(), "in.txt");
        String codeOut = getTextFromFile(aFolder.absolutePath, "out.txt");
        testInputToOutput(codeIn, codeOut, aFolder.getName());
    }


    public String getTextFromFile(String path, String fileName) {
        File aFile = new File(path+"/"+fileName);
        return aFile.getText();
    }

    public static ArrayList<Instruction>getInsttructionsFromResource(String resourcePath) {
        String oldCode = getTextFromResource(resourcePath);
        MicroLexer aLexer = new MicroLexer(oldCode);
        return aLexer.instructions;
    }

    public static String getTextFromResource(String resourcePath) {
        File aFile = new File(getClassLoader().getResource(resourcePath).toURI());
        return aFile.getText();
    }

    public static void testInputToOutput(String codeIn, String out, String testName) {
        CodeTransform aT = new CodeTransform(codeIn);
        String transformed = aT.getTransformed();
        String outCleaned = out.replace("\r\n","\n");
        String transformedCleaned = transformed.replace("\r\n","\n");
        boolean matches = outCleaned.equals(transformedCleaned)
        if(!matches) {
            for(int i = 0;i<outCleaned.size();i=i+10) {
                int outRange = Math.min(i+10, outCleaned.size());
                String outCleanedSection = outCleaned.substring(i, outRange);
                int transformedRange = Math.min(i+10, transformedCleaned.size());
                String transformedCleanedSection = transformedCleaned.substring(i, transformedRange);
                assert outCleanedSection.equals(transformedCleanedSection) : "\nfor test: "+testName+"\nExpected Code of :\n"+outCleanedSection+"\n-----------------\nNot equal to code out of:\n"+transformedCleanedSection+"\n";
            }
            assert false : "\nfor test: "+testName+"\nExpected Code of :\n"+out+"\n-----------------\nNot equal to code out of:\n"+transformed+"\n";
        }
    }
}
