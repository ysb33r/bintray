package org.ysb33r.gradle.bintray.content

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection

import static groovyx.net.http.ContentType.BINARY

class Content implements BintrayConnection, ContentRequest {
    String filePath
    Map queryMap

    def downloadContent(Boolean dynamicMode = false) {
        assertAttributes(filePath, subject, repo)
        return RESTCall("get", getPath(filePath, dynamicMode), null, queryMap, null, BINARY)
    }

    def uploadContent(Boolean publish = false, Boolean override = false, Boolean explode = false) {
        assertAttributes(filePath, subject, repo)
        Map headers = getHeaders(publish, override, explode)
        return RESTCall("post", getPath(filePath), null, null, headers, BINARY)
    }

    JsonBuilder publishContent(Integer publish_wait_for_secs) {
        body.publish_wait_for_secs = publish_wait_for_secs
        assertAttributes(filePath, subject, repo)
        return RESTCall("post", "${getPath(filePath)}/publish", body.toString())
    }

    JsonBuilder discardContent() {
        body.discard = true
        assertAttributes(body, filePath, subject, repo)
        return RESTCall("post", "${getPath(filePath)}/publish", body.toString())
    }

    JsonBuilder deleteContent() {
        assertAttributes(filePath, subject, repo)
        return RESTCall("delete", getPath(filePath))
    }
}