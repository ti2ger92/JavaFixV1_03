<%--
  Created by IntelliJ IDEA.
  User: TY-MSI
  Date: 3/19/2016
  Time: 4:43 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Java Source Cleaner</title>

    <asset:javascript src="application.js"/>
    <asset:stylesheet  src="application.css"/>
    <asset:javascript src="lib/codemirror.js"/>
    <asset:stylesheet src="codemirror.css"/>
    <asset:javascript src="mode/clike/clike.js"/>
    <script>
        function callAjax(){
            $.ajax({
                url: "main/transform",
                type:"post",
                dataType: 'json',
                data:{codeIn:codeIn.getValue()},
                success: function(data) {
                    console.log(data); //<-----this logs the data in browser's console
                    codeOut.setValue(data.resultCode);
                },
                error: function(xhr){
                    alert(xhr.responseText); //<----when no data alert the err msg
                }
            });
            codeOut.setSize(700,25*codeIn.lineCount()+10)
        }
    </script>
</head>

<body>

<div id="navbar">
    <a target="_blank" href="https://github.com/ti2ger92/JavaFixV1_03.git">This project on</a>
    <a target="_blank" href="http://www.github.com"><asset:image class="logoSize" src="GitHub_Logo.png"></asset:image></a>
</div>

<h1>Java Source Cleaner</h1>

<p>Welcome!  This tool will find opportunities to clean up and modernize Java code to take advantages of Java 8. &nbsp;Anything you put in here can be stored and viewed by me.
Paste a code segment in the first box aand click "Clean Code".  Updated code will appear below, with suggestions</p>

<p>&nbsp;</p>

<p>&nbsp;</p>

<g:textArea name="codeIn" id="codeIn" >Put your Code Here</g:textArea>

<p>&nbsp;</p>

<p><input type="submit" value="Clean Code" onclick="callAjax();return false;"></p>

<p>&nbsp;</p>

<p><g:textArea cols="100" name="codeOutArea" id="codeOutArea" wrap="soft">Updated code appears here.</g:textArea></p>



<script>
    var codeIn = CodeMirror.fromTextArea(document.getElementById("codeIn"), {
        lineNumbers: true,
        matchBrackets: true,
        mode: "text/x-java",
        viewportMargin: Infinity
    });

    var codeOut = CodeMirror.fromTextArea(document.getElementById("codeOutArea"), {
        lineNumbers: true,
        matchBrackets: true,
        mode: "text/x-java",
        viewPortMargin: Infinity,
        readOnly: true
    });
</script>
</body>
</html>