package ustccq.console.master;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class JmxWrite {
	void writeJmx(String path) {
//		try {
//			File = new 
//			XMLWriter writer = new XMLWriter();
//			Document document = new Document
//			writer.write(document);
//			
//			OutputFormat format = OutputFormat.createPrettyPrint();
//			format.setEncoding("utf-8");
//			
//		}
	}
	
	private final static Map<String, Map<String, String>> eleAttrMap = new HashMap<String, Map<String, String>>(){
		{
			put("TestPlan", new HashMap<String, String>(){
				{
					put("guiclass", 	"TestPlanGui");
					put("testclass", 	"TestPlan");
					put("testname", 	"VMC测试计划");
					put("enabled", 		"true");
				}
			});
			put("TestPlan.elementProp", new HashMap<String, String>(){
				{
					put("name", 		"TestPlan.user_defined_variables");
					put("elementType",	"Arguments");
					put("guiclass", 	"ArgumentsPanel");
					put("testclass", 	"Arguments");
					put("testname", 	"用户定义的变量");
					put("enabled", 		"true");
				}
			});
			put("ThreadGroup", new HashMap<String, String>(){
				{
					put("guiclass", 	"ThreadGroupGui");
					put("testclass", 	"ThreadGroup");
					put("testname", 	"VMC线程组1");
					put("enabled", 		"true");
				}
			});
			put("ThreadGroup.elementProp", new HashMap<String, String>(){
				{
					put("name", 		"ThreadGroup.main_controller");
					put("elementType", 	"LoopController");
					put("guiclass", 	"LoopControlPanel");
					put("testclass", 	"LoopController");
					put("testname", 	"循环控制器");
					put("enabled", 		"true");
				}
			});
			put("HTTPSamplerProxy", new HashMap<String, String>(){
				{
					put("guiclass", 	"HttpTestSampleGui");
					put("testclass", 	"HTTPSamplerProxy");
					put("testname", 	"HTTP请求Demo");
					put("enabled", 		"true");
				}
			});
			put("HTTPSamplerProxy.elementProp", new HashMap<String, String>(){
				{
					put("name", 		"HTTPsampler.Arguments");
					put("elementType", 	"Arguments");
					put("guiclass", 	"HTTPArgumentsPanel");
					put("testclass", 	"Arguments");
					put("testname", 	"用户定义的变量");
					put("enabled", 		"true");
				}
			});
		}
	};
	
	static void testWrite() {
		Document document = DocumentHelper.createDocument();
		Element testCase = document.addElement("jmeterTestPlan");
		testCase.addAttribute("version", "1.2");
		testCase.addAttribute("properties", "5.0");
		testCase.addAttribute("jmeter", "5.0 r1840935");
		
		Element hashTreeRoot = testCase.addElement("hashTree");
		Element testPlan = addSimpleChild(hashTreeRoot, "TestPlan", true);
		
		addSimpleChild(testPlan, "stringProp", "name", "TestPlan.comments");
		
		addSimpleChild(testPlan, "boolProp", "name", "TestPlan.functional_mode", "false");
		addSimpleChild(testPlan, "boolProp", "name", "TestPlan.tearDown_on_shutdown", "true");
		addSimpleChild(testPlan, "boolProp", "name", "TestPlan.serialize_threadgroups", "false");
		
		Element testPlanElementProp = addSimpleChild(testPlan, "TestPlan.elementProp", true);
		//TODO 适合把属性放到三维数组中，根据name进行遍历
		addSimpleChild(testPlanElementProp, "collectionProp", "name", "Arguments.arguments");
		addSimpleChild(testPlan, "stringProp", "name", "TestPlan.user_define_classpath");

		//**************************TestPlan 基本属性结束****************************
		//**********************线程属性开始***********************
		
		Element hashTreeRootLevel1 = hashTreeRoot.addElement("hashTree");
		Element threadGroup = addSimpleChild(hashTreeRootLevel1, "ThreadGroup", true);
		addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.on_sample_error", "continue");
		
		//循环控制器,即线程组执行多少次结束的控制
		Element threadGroupElementProp = addSimpleChild(threadGroup, "ThreadGroup.elementProp", true);
		addSimpleChild(threadGroupElementProp, "boolProp", "name", "LoopController.continue_forever", "false");		//永久循环
		addSimpleChild(threadGroupElementProp, "stringProp", "name", "LoopController.loops", "5");					//循环次数,需要封装
//		addSimpleChild(threadGroupElementProp, "intProp", "name", "LoopController.loops", "-1");					//未选择循环时取值-1
		
		addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.num_threads", "5");
		addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.ramp_time", "5");
		
		if (1 == Integer.valueOf(1).toString().length()) {
			addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.scheduler", "true");
			addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.duration", "1200");
			addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.delay", "100");
		}else {
			addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.scheduler", "false");
			addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.duration", "");
			addSimpleChild(threadGroup, "stringProp", "name", "ThreadGroup.delay", "");
		}
		//是否延迟启动
//		addSimpleChild(threadGroup, "boolProp", "name", "ThreadGroup.delayedStart", "true");
		
		//一个hashTree结点作为分隔符
		addSimpleChild(hashTreeRootLevel1, "hashTree", false);
		
		
		//添加http请求
		Element HTTPSamplerProxy = addSimpleChild(hashTreeRootLevel1, "HTTPSamplerProxy", true);
		HTTPSamplerProxy.addAttribute("testName", "MO请求");
		
		//http请求参数
		Element argumentsElementProp = addSimpleChild(HTTPSamplerProxy, "HTTPSamplerProxy.elementProp", true);
		//TODO
		//无参数时添加此prop即可
		Element argumentsCollectionProp = addSimpleChild(argumentsElementProp, "collectionProp", "name", "Arguments.arguments", null);
		//有参数时collectionProp继续添加,如下
		addHttpArgument(argumentsCollectionProp, "ref", "", true);
		addHttpArgument(argumentsCollectionProp, "pageNumber", "1", true);
		addHttpArgument(argumentsCollectionProp, "pageSize", "700", true);
		addHttpArgument(argumentsCollectionProp, "orderBy", "name+desc", true);
		
		//为http请求设置配置
		addSimpleHttpChild(HTTPSamplerProxy, "10.0.30.11", "/api/runs/5306", "GET");
		
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		format.setExpandEmptyElements(false);
		try {
		    File f = new File("D:/JMeter/bundleTest.jmx");
		    XMLWriter writer = new XMLWriter(new FileOutputStream(f), format);
		    //设置是否转义。默认true，代表转义
		    writer.setEscapeText(false);
		    writer.write(document);
		    writer.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}

	private static void batchAddElementProp(Element elementProp) {
		elementProp.addAttribute("elementType", "Arguments");
		elementProp.addAttribute("guiclass", "ArgumentsPanel");
		elementProp.addAttribute("testclass", "Arguments");
		elementProp.addAttribute("testname", "用户定义的变量");
		elementProp.addAttribute("enabled", "true");
	}

	private static Element addSimpleChild(Element subRoot, String elementName, String elementAttrName, String elementAttrValue, String textValue) {
		Element newEle = subRoot.addElement(elementName);
		newEle.addAttribute(elementAttrName, elementAttrValue);
		if (null != textValue)
			newEle.addText(textValue);
		return newEle;
	}
	
	private static Element addSimpleChild(Element subRoot, String elementName, String elementAttrName, String elementAttrValue) {
		Element newEle = subRoot.addElement(elementName);
		newEle.addAttribute(elementAttrName, elementAttrValue);
		return newEle;
	}
	
	private static Element addSimpleChild(Element root, String elementName, boolean useDefaultAttributesMap) {
		String prevElementName = elementName;
		if (elementName.contains(".")) {
			String[] names = elementName.split("\\.");
			elementName = names[names.length - 1];
		}
		
		Element newEle = root.addElement(elementName);
		if (useDefaultAttributesMap && eleAttrMap.containsKey(prevElementName)) {
			Map<String, String> attributes = eleAttrMap.get(prevElementName);
			if (null != attributes && !attributes.isEmpty()) {
				for (Map.Entry<String, String> entry : attributes.entrySet()) {
					newEle.addAttribute(entry.getKey(), entry.getValue());
				}
			}
		}
		return newEle;
	}
	
	private static Element addHttpArgument(Element argumentElement, String key, String value, boolean encode) {
		Element elementProp = addSimpleChild(argumentElement, "elementProp", "name", key);
		elementProp.addAttribute("elementType", "HTTPArgument");
		
		//TODO 是否需要加密默认设置为永久加密
		addSimpleChild(elementProp, "boolProp", "name", "HTTPArgument.always_encode", encode ? "true" : "false");
		addSimpleChild(elementProp, "stringProp", "name", "Argument.value", value);
		addSimpleChild(elementProp, "stringProp", "name", "Argument.metadata", "=");
		addSimpleChild(elementProp, "boolProp", "name", "HTTPArgument.use_equals", "true");
		addSimpleChild(elementProp, "stringProp", "name", "Argument.name", key);
		
		return elementProp;
	}
	
	private static void addSimpleHttpChild(Element httpSampleProxyElement, String domain, String path, String method) {
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.domain", domain);
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.port", "");
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.protocol", "http");
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.contentEncoding", "UTF-8");
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.path", path);
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.method", method);
		
		
		addSimpleChild(httpSampleProxyElement, "boolProp", "name", "HTTPSampler.follow_redirects", "true");
		addSimpleChild(httpSampleProxyElement, "boolProp", "name", "HTTPSampler.auto_redirects", "false");
		addSimpleChild(httpSampleProxyElement, "boolProp", "name", "HTTPSampler.use_keepalive", "true");
		addSimpleChild(httpSampleProxyElement, "boolProp", "name", "HTTPSampler.DO_MULTIPART_POST", "false");
		
		
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.embedded_url_re", "");
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.connect_timeout", "");
		addSimpleChild(httpSampleProxyElement, "stringProp", "name", "HTTPSampler.response_timeout", "");
	}
	
	public static void main(String[] args) {
		testWrite();
	}
}
