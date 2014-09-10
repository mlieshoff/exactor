package com.exoftware.exactor.jenkins;

/**
 * Interface to check the existence/availability of URLs over HTTP.
 *
 * @author Andoni del Olmo
 */
public interface URLChecker {

    /**
     * Sets the server authorization credentials.
     *
     * @param serverAuth server authorization.
     */
    void setServerAuth(String serverAuth);

    /**
     * Checks whether the given url is available.
     *
     * @param url the URL.
     * @return true if the url is reachable, false otherwise.
     */
    boolean isUrlReachable(String url);
}
