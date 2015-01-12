package com.efei.lib.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.efei.lib.android.bean.net.ABaseReqBean;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.common.Constants;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.engine.ServiceFactory;
import com.efei.lib.android.exception.EfeiException;

public final class NetUtils
{
	private NetUtils()
	{
	}

	public static <T> T postObjectAsJson(String urlApi, ABaseReqBean reqBean, Class<T> respClazz)
	{
		HttpClient client = newClient();
		HttpPost post = new HttpPost(Constants.Net.HOST_URL + urlApi);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json");
		try
		{
			byte[] postBytes = reqBean.toJson().getBytes(HTTP.UTF_8);
			String jsonPost = new String(postBytes, HTTP.UTF_8);
			HttpEntity entity = new StringEntity(jsonPost, HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			String respString = readResponseAsString(response);
			return BaseRespBean.toObject(respString, respClazz);
		} catch (IOException e)
		{
			throw new EfeiException(e);
		}
	}

	public static String get(String strAPIUrl, Map<String, String> params) throws ClientProtocolException, IOException
	{
		params = injectAuthKey(params);
		final String url = encodeGet_DeleteUrl(strAPIUrl, params);
		HttpClient client = newClient();
		HttpGet get = new HttpGet(url);
		get.setHeader("Content-Type", "application/json");
		get.setHeader("Accept", "application/json");
		HttpResponse response = client.execute(get);
		return readResponseAsString(response);
	}

	private static Map<String, String> injectAuthKey(Map<String, String> params)
	{
		ILoginService service = ServiceFactory.INSTANCE.getService(ServiceFactory.LOGIN_SERVICE);
		Account defaultUser = service.getDefaultUser();
		final String authKey = null == defaultUser ? "" : defaultUser.getAuthKey();
		if (null == params)
			params = new HashMap<String, String>();
		params.put("auth_key", authKey);
		return params;
	}

	private static String encodeGet_DeleteUrl(String strAPIUrl, Map<String, String> params)
	{
		final String url;
		if (null != params)
		{
			Set<Entry<String, String>> entrySet = params.entrySet();
			StringBuilder builder = new StringBuilder(Constants.Net.HOST_URL + strAPIUrl + "?");
			final int iLen = entrySet.size();
			int i = 0;
			for (Entry<String, String> entry : entrySet)
			{
				builder.append(entry.getKey());
				builder.append("=");
				builder.append(entry.getValue());
				i++;
				if (i < iLen)
					builder.append("&");
			}
			url = builder.toString();
		} else
			url = Constants.Net.HOST_URL + strAPIUrl;
		return url;
	}

	private static HttpClient newClient()
	{
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter("http.socket.timeout", 15000);
		return client;
	}

	private static String readResponseAsString(HttpResponse response) throws IOException
	{
		final int code = response.getStatusLine().getStatusCode();
		if (200 != code)
			throw new EfeiException("获取数据失败：" + code);
		InputStream is = response.getEntity().getContent();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		int iLen = -1;
		while (-1 != (iLen = is.read(buffer)))
			os.write(buffer, 0, iLen);
		return new String(os.toByteArray(), "UTF-8");
	}

	public static String delete(String strAPIUrl, Map<String, String> params) throws ClientProtocolException, IOException
	{
		params = injectAuthKey(params);
		final String url = encodeGet_DeleteUrl(strAPIUrl, params);
		HttpClient client = newClient();
		HttpDelete delete = new HttpDelete(url);
		delete.setHeader("Content-Type", "application/json");
		delete.setHeader("Accept", "application/json");
		HttpResponse response = client.execute(delete);
		return readResponseAsString(response);
	}

	public static <T> T put(String urlApi, ABaseReqBean reqBean, Class<T> respClazz)
	{
		HttpClient client = newClient();
		HttpPut put = new HttpPut(Constants.Net.HOST_URL + urlApi);
		put.setHeader("Content-Type", "application/json");
		put.setHeader("Accept", "application/json");
		try
		{
			byte[] postBytes = reqBean.toJson().getBytes(HTTP.UTF_8);
			String jsonPost = new String(postBytes, HTTP.UTF_8);
			HttpEntity entity = new StringEntity(jsonPost, HTTP.UTF_8);
			put.setEntity(entity);
			HttpResponse response = client.execute(put);
			String respJson = readResponseAsString(response);
			return BaseRespBean.toObject(respJson, respClazz);
		} catch (IOException e)
		{
			throw new EfeiException(e);
		}
	}
}
