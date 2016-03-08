package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder

class DownloadKey implements DownloadKeysRequest {
    String id

    JsonBuilder createDownloadKey(String keyId = "") {
        if (keyId) {
            body.content."id" = keyId // Supports optional argument overriding body
        }
        assertAttributes(subject, subjectType, body)

        def result = btConn.RESTCall("post", getPath(), body.toString())
        this.id = result?.content?."id" ?: this.id
        return result
    }

    JsonBuilder getDownloadKey() {
        assertAttributes(id, subject, subjectType)
        return btConn.RESTCall("get", getPath(id))
    }

    JsonBuilder deleteDownloadKey() {
        assertAttributes(id, subject, subjectType)
        return btConn.RESTCall("delete", getPath(id))
    }

    JsonBuilder updateDownloadKey() {
        assertAttributes(id, subject, subjectType, body.content)
        return btConn.RESTCall("patch", getPath(id), body.toString())
    }
}
