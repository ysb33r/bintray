package org.ysb33r.gradle.bintray.content

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayEndpoint

import static groovyx.net.http.ContentType.BINARY
import static org.ysb33r.gradle.bintray.core.BintrayEndpoint.API_BASE_URL
import static org.ysb33r.gradle.bintray.core.BintrayEndpoint.API_DL_URL

class Content implements ContentRequest {
    String filePath

    def downloadContent(Boolean dynamicMode = false,  Map queryMap = [:]) {
        BintrayEndpoint endpoint = (dynamicMode ? API_BASE_URL: API_DL_URL)
        assertAttributes(filePath, subject, repo)
        return btConn.RESTCall("get", getPath(filePath, dynamicMode), null, queryMap, null, BINARY, endpoint)
    }

    //TODO : Complete and Test
    def uploadContent(Boolean publish = false, Boolean override = false, Boolean explode = false) {
        assertAttributes(filePath, subject, repo)
        Map headers = getHeaders(publish, override, explode)
        return btConn.RESTCall("post", getPath(filePath), null, null, headers, BINARY)
    }

    //TODO : Complete and Test
    JsonBuilder publishContent(Integer publish_wait_for_secs) {
        body.publish_wait_for_secs = publish_wait_for_secs
        assertAttributes(filePath, subject, repo)
        return btConn.RESTCall("post", "${getPath(filePath)}/publish", body.toString())
    }

    //TODO : Complete and Test
    JsonBuilder discardContent() {
        body.discard = true
        assertAttributes(body, filePath, subject, repo)
        return btConn.RESTCall("post", "${getPath(filePath)}/publish", body.toString())
    }

    JsonBuilder deleteContent() {
        assertAttributes(filePath, subject, repo)
        return btConn.RESTCall("delete", getPath(filePath))
    }
}