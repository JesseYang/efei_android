package com.efei.lib.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.efei.lib.android.bean.net.ABaseReqBean;
import com.efei.lib.android.common.Constants;
import com.efei.lib.android.exception.EfeiException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class NetUtils {
	private NetUtils() {
	}

	public static <T> T postObjectAsJson(String urlApi, ABaseReqBean reqBean,
			Class<T> respClazz) {
		HttpClient client = newClient();
		HttpPost post = new HttpPost(Constants.Net.HOST_URL + urlApi);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json");
		try {
			byte[] bytes = reqBean.toJson().getBytes(HTTP.UTF_8);
			String jsonPost = new String(bytes, HTTP.UTF_8);
			HttpEntity entity = new StringEntity(jsonPost, HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			String respString = readResponseAsString(response);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			return mapper.readValue(respString, respClazz);
		} catch (IOException e) {
			throw new EfeiException(e);
		}
	}

	private static HttpClient newClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter("http.socket.timeout", 15000);
		return client;
	}

	private static String readResponseAsString(HttpResponse response)
			throws IOException {
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
}
