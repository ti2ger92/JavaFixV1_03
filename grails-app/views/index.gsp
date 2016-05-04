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

    <script>
        function callAjax(){
            $.ajax({
                url: "main/transform",
                type:"post",
                dataType: 'json',
                data:{codeIn:document.getElementById('codeIn').value},
                success: function(data) {
                    console.log(data); //<-----this logs the data in browser's console
                    document.getElementById('codeOutArea').innerHTML=data.resultCode;
                },
                error: function(xhr){
                    alert(xhr.responseText); //<----when no data alert the err msg
                }
            });
        }
    </script>
</head>

<body>

<div id="navbar">
    <a target="_blank" href="https://github.com/ti2ger92/JavaFixV1_03.git">This GitHub</a>
</div>

<h1>Java Source Cleaner</h1>

<p>Welcome!  This tool will find opportunities to clean up and modernize Java code to take advantages of Java 8. &nbsp;Anything you put in here can be stored and viewed by me.
Paste a code segment in the first box aand click "Clean Code".  Updated code will appear below, with suggestions</p>

<p>&nbsp;</p>

<p>&nbsp;</p>

<g:textArea cols="100" name="codeIn" id="codeIn" rows="15">Put your Code Here</g:textArea>

<p>&nbsp;</p>

<p><input type="submit" value="Clean Code" onclick="callAjax();return false;"></p>

<p>&nbsp;</p>

<p><g:textArea cols="100" name="codeOutArea" id="codeOutArea" rows="15" wrap="soft">Updated code appears here.</g:textArea></p>

</body>
</html>