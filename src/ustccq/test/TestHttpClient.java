package ustccq.test;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

public class TestHttpClient {

	private static RequestConfig requestConfig = RequestConfig.custom()
			.setConnectionRequestTimeout(60000)
			.setConnectTimeout(60000)
			.setSocketTimeout(60000)
			.build();
	
	public enum MethodType{
		GET,POST,PUT,DELETE,OPTIONS,HEAD,TRACE,PATCH//2.0 PATCH,MOVE,COPY,LINK,UNLINK,WRAPPED,Extension-method
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CloseableHttpClient httpHeaderClient = HttpClients.createDefault();
		String url= "http://localhost:8100/api/status";
		String paramsHeader = "";
		String paramsBody = "";
		MethodType methodType = MethodType.GET;
		
		HttpRequestBase request = generateMethod(url, paramsBody, methodType);
		String headerArrayStr = fillRequestHeaders(paramsHeader, request);
		
		try {
			String tmpJsonArray = "[]";
			JSONArray tmpObj = new JSONArray(tmpJsonArray);
			System.err.println(tmpObj.toString());
			
			CloseableHttpResponse response = httpHeaderClient.execute(request);
			
			Header[] headers = response.getHeaders("Cookie");
			for(Header header : headers) {
				System.err.println(header.getName() + ":" + header.getValue());
			}
			System.err.println("");
			
			Header[] headers1 = response.getHeaders("Set-Cookie");
			System.err.println("Set-Cookie:");
			for(Header header : headers1) {
				System.err.println(header.getName() + ":" + header.getValue());
			}
			System.err.println("");
			
			CloseableHttpResponse response2 = httpHeaderClient.execute(request);
			Header[] headers2 = response.getHeaders("Cookie");
			for(Header header : headers2) {
				System.err.println(header.getName() + ":" + header.getValue());
			}
			System.err.println("");
			
			Header[] headers3 = response.getHeaders("Set-Cookie");
			System.err.println("Set-Cookie:");
			for(Header header : headers3) {
				System.err.println(header.getName() + ":" + header.getValue());
			}
			System.err.println("");
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String fillRequestHeaders(String paramsHeader, HttpRequestBase request) {
		if (null != request){
			if (null != paramsHeader && !paramsHeader.isEmpty()){
				JSONObject headerObj = new JSONObject(paramsHeader);
				
				Map<String, Object> headerMap = headerObj.toMap();
				for(Entry<String, Object> entry : headerMap.entrySet()){
					request.addHeader(entry.getKey(), (String)entry.getValue());
				}

				//TODO 删除 默认的 app/json header，若有需求，则放到页面上加上
//				if (!headerObj.has("Content-Type")){
//					request.addHeader("Content-Type","application/json;Charset=utf-8");				
//				}
//				
				return Arrays.toString(headerMap.keySet().toArray());
			}
		}
		return new String();
	}
	
	private static HttpRequestBase generateMethod(String url, String paramsBody, MethodType methodType){
		HttpRequestBase httpRequest = null;
		HttpEntityEnclosingRequestBase httpEntity = null;

		switch(methodType){
		case GET:
			httpRequest = new HttpGet(url);
			break;
		case POST:
			httpEntity = optionMethodBody(new HttpPost(url), paramsBody);
			break;
		case PUT:
			httpEntity = optionMethodBody(new HttpPut(url), paramsBody);
			break;
		case DELETE:
			httpRequest = new HttpDelete(url);
			break;
		case OPTIONS:
			httpRequest = new HttpOptions(url);
			break;
		case HEAD:
			httpRequest = new HttpHead(url);
			break;
		case TRACE:
			httpRequest = new HttpTrace(url);
			break;
		case PATCH:
			httpEntity = optionMethodBody(new HttpPatch(url), paramsBody);
			break;
		default:
			break;
		}
		
		if (null != httpEntity) return httpEntity;
		if (null != httpRequest){
			httpRequest.setConfig(requestConfig);
		}
		return httpRequest;
	}
	

	private static HttpEntityEnclosingRequestBase optionMethodBody(HttpEntityEnclosingRequestBase method, String paramsBody){
		method.setConfig(requestConfig);
		if(paramsBody != null){
			StringEntity requestEntity = null;
			try {
				requestEntity = new StringEntity(paramsBody, "UTF-8");
			} catch (UnsupportedCharsetException e) {
				requestEntity = null;
			}
			if (null != requestEntity){
				requestEntity.setContentType("application/json");
				method.setEntity(requestEntity);
			}
		}
		return method;
	}
	
	private HttpEntityEnclosingRequestBase optionMethod(HttpEntityEnclosingRequestBase method, String params){
		method.setConfig(requestConfig);
		method.addHeader("Content-Type","application/json");
		if(params != null){
			StringEntity requestEntity = null;
			try {
				requestEntity = new StringEntity(params, "UTF-8");
			} catch (UnsupportedCharsetException e) {
				requestEntity = null;
			}
			if (null != requestEntity){
				requestEntity.setContentType("application/json");
				method.setEntity(requestEntity);
			}
		}
		return method;
	}
}
