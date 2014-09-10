package com.exoftware.exactor.jenkins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Implementation of the URLChecker using java.net classes.
 *
 * @author Andoni del Olmo
 */
public class JavaNetURLChecker implements URLChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaNetURLChecker.class.getName());

    private String encodedAuthBytes;

    public void setServerAuth(String encodedAuthBytes) {
        this.encodedAuthBytes = encodedAuthBytes;
    }

    public boolean isUrlReachable(String reportUrl) {
        boolean isReportReachable = false;
        try {
            URL url = new URL(reportUrl);
            URLConnection connection = url.openConnection();
            setRequestAuthIfNeeded(connection);
            connection.connect();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("ResponseCode[" + httpConnection.getResponseCode() + "]");
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    isReportReachable = true;
            }
        } catch (Exception e) {
            isReportReachable = false;
        }
        return isReportReachable;
    }

    private void setRequestAuthIfNeeded(URLConnection connection) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("encoded auth string [" + encodedAuthBytes + "]");
        if (encodedAuthBytes != null) {
            connection.setRequestProperty("Authorization", "Basic " + encodedAuthBytes);
        }
    }
}