package de.bitnoise.sonferenz.service.v2.services.idp.provider.crowd;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

public class CustomErrorHandler implements ResponseErrorHandler {

	private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return errorHandler.hasError(response);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			handleNotFound(response);
		} else {
			errorHandler.handleError(response);
		}
	}
	Pattern notFoundPattern = Pattern.compile("<error><reason>USER_NOT_FOUND</reason><message>(.*)</message>");

	protected void handleNotFound(ClientHttpResponse response) throws IOException {
		String body = getResponseBody(response);
		HttpStatus statusCode = response.getStatusCode();
		Matcher matcher = notFoundPattern.matcher(body);
		if(matcher.find()) {
			throw new UsernameNotFoundException("user not found : " + matcher.group(1));
		}
		throw new RestClientException("Unknown status code [" + statusCode + "]");
	}
	
	private String getResponseBody(ClientHttpResponse response) {
		try {
			InputStream responseBody = response.getBody();
			if (responseBody != null) {
				return IOUtils.toString(responseBody);
			}
		}
		catch (IOException ex) {
			// ignore
		}
		return "";
	}

}
