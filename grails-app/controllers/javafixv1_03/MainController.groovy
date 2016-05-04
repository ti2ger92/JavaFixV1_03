package javafixv1_03

import grails.converters.JSON
import model.CodeTransform

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
}
