package org.ysb33r.gradle.bintray.core

import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.JSON

/**
 * @author Schalk W. Cronjé
 */
class BintrayClient extends RESTClient {

    Closure onSuccessDefault = { resp -> resp.data }
    Closure onFailDefault = { e -> new JsonBuilder([message: e.message, code: e.statusCode]) }

    BintrayClient(final URI uri) {
        super(uri)
    }

    def RESTCall(
        String method = "get", String path = "", String body = "", Map query = [:], Map headers = [:],
        ContentType contentType = JSON,
        Closure onSuccess = onSuccessDefault, Closure onFailure = onFailDefault) {

        Map requestArgs = [path: path]
        
        if (contentType) {
            requestArgs.contentType = contentType
        }
        if (body) {
            requestArgs.body = body
        }
        if (query) {
            requestArgs.query = query
        }
        if (headers) {
            requestArgs.headers = headers
        }

        debugmsg "Method is: ${method}"
        debugmsg "Endpoint is: ${this.uri}"
        debugmsg "Request Args are: ${requestArgs}"

        try {
            HttpResponseDecorator response = "$method"(requestArgs)
            debugmsg "Response code is ${response.status}."
            return onSuccess(response)
        } catch (HttpResponseException e) {
            debugmsg e.message
            debugmsg "Response code is ${e.response.status}."
            return onFailure(e)
        }
    }

    private void debugmsg(final String msg) {
        log.debug msg
    }

}
