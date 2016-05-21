import grails.plugins.metadata.GrailsPlugin
import org.grails.gsp.compiler.transform.LineNumber
import org.grails.gsp.GroovyPage
import org.grails.web.taglib.*
import org.grails.taglib.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_JavaFixV1_03index_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/index.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
printHtmlPart(1)
createTagBody(1, {->
printHtmlPart(2)
createTagBody(2, {->
createClosureForHtmlPart(3, 3)
invokeTag('captureTitle','sitemesh',10,[:],3)
})
invokeTag('wrapTitleTag','sitemesh',11,[:],2)
printHtmlPart(4)
invokeTag('javascript','asset',11,['src':("application.js")],-1)
printHtmlPart(2)
invokeTag('stylesheet','asset',13,['src':("application.css")],-1)
printHtmlPart(2)
invokeTag('javascript','asset',14,['src':("lib/codemirror.js")],-1)
printHtmlPart(2)
invokeTag('stylesheet','asset',15,['src':("codemirror.css")],-1)
printHtmlPart(2)
invokeTag('javascript','asset',16,['src':("mode/clike/clike.js")],-1)
printHtmlPart(5)
})
invokeTag('captureHead','sitemesh',37,[:],1)
printHtmlPart(0)
createTagBody(1, {->
printHtmlPart(6)
invokeTag('image','asset',60,['class':("logoSize"),'src':("GitHub_Logo.png")],-1)
printHtmlPart(7)
createClosureForHtmlPart(8, 2)
invokeTag('textArea','g',67,['name':("codeIn"),'id':("codeIn")],2)
printHtmlPart(9)
createClosureForHtmlPart(10, 2)
invokeTag('textArea','g',82,['cols':("100"),'name':("codeOutArea"),'id':("codeOutArea"),'wrap':("soft")],2)
printHtmlPart(11)
})
invokeTag('captureBody','sitemesh',97,[:],1)
printHtmlPart(12)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1462853711513L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'none'
public static final String TAGLIB_CODEC = 'none'
}
