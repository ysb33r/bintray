package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection

class DownloadKey implements BintrayConnection, DownloadKeysRequest {
    String id

    JsonBuilder createDownloadKey(String id = "") {
        if (id) {
            body.content."id" = id // Supports optional argument overriding body
        }
        assertAttributes(subject, subjectType)
        def result = RESTCall("post", getPath(), body.toString())
        this.id = result?.content?."id" ?: this.id
        return result
    }

    JsonBuilder getDownloadKey() {
        assertAttributes(id, subject, subjectType)
        return RESTCall("get", getPath(id))
    }

    JsonBuilder deleteDownloadKey() {
        assertAttributes(id, subject, subjectType)
        return RESTCall("delete", getPath(id))
    }

    JsonBuilder updateDownloadKey() {
        assertAttributes(id, subject, subjectType, body.content)
        return RESTCall("patch", getPath(id), body.toString())
    }
}
