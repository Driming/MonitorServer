package com.hc.util.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.hc.util.properties.PropertiesBuilder;

public class PhoneSms {

	private static final String URI_SEND_SMS = PropertiesBuilder.buildProperties().getUriSendSms();
	private static final String apiKey = PropertiesBuilder.buildProperties().getApiKey();
	private static String ENCODING = "UTF-8";

	/**
	 * 智能匹配模板接口发短信
	 *
	 * @param text
	 *            短信内容
	 * @param mobile
	 *            接受的手机号
	 * @return json格式字符串
	 * @throws IOException
	 */
	public static String sendSms(String text, String mobile) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("apikey", apiKey);
		params.put("text", text);
		params.put("mobile", mobile);
		return post(URI_SEND_SMS, params);
	}

	public static String post(String url, Map<String, String> paramsMap) {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost method = new HttpPost(url);
			if (paramsMap != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : paramsMap.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
					paramList.add(pair);
				}
				method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
			}
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = EntityUtils.toString(entity, ENCODING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseText;
	}

	public static String getCode() {
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < 6; i++)
			strBuff.append((int) Math.floor(Math.random() * 10));
		return strBuff.toString();
	}
}
