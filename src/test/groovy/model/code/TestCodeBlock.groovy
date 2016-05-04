package model.code

import org.junit.Test

/**
 * Created by TY-MSI on 3/7/2016.
 */
class TestCodeBlock extends GroovyTestCase {
    @Test
    public void testInitialize() {
        CodeBlock aBlock = new CodeBlock("int a=1;");
        assert "int a=1;".equals(aBlock.code);
    }

    @Test
    public void testComment() {
        CodeBlock aBlock = new CodeBlock("int a=1;");
        assert "//int a=1;\n".equals(aBlock.getCommented().code);
    }

}
