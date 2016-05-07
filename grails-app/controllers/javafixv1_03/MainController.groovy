package javafixv1_03

import grails.converters.JSON
import model.CodeTransform
import util.SCUtility

class MainController {

    def index() {}

    def transform() {
        String currentCode = params.get("codeIn");
        CodeTransform aTransform = new CodeTransform(currentCode);
        String resultStr = aTransform.getTransformed();
        def aRes = [resultCode:resultStr];
        withFormat {
            json { render aRes as JSON }
        }
    }

    def getTest() {
        String testPath = params.get("path");
        String text = SCUtility.getTextFromResource(testPath);
        def aRes = [resultCode:text];
        withFormat {
            json {render aRes as JSON }
        }
    }
}
