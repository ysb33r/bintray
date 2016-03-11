package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder

class DownloadKey implements DownloadKeysRequest {
    String id

    JsonBuilder createDownloadKey(String keyId = "") {
        if (keyId) {
            body.content.id = keyId // Supports optional argument overriding body
        }
        assertAttributes(subject, subjectType, body)

        def result = this.bintrayClient.RESTCall("post", getPath(), body.toString())
        this.id = result?.username?.split("@")[0] ?: this.id
        return result
    }

    JsonBuilder getDownloadKey() {
        assertAttributes(id, subject, subjectType)
        return this.bintrayClient.RESTCall("get", getPath(id))
    }

    JsonBuilder deleteDownloadKey() {
        assertAttributes(id, subject, subjectType)
        return this.bintrayClient.RESTCall("delete", getPath(id))
    }

    JsonBuilder updateDownloadKey() {
        assertAttributes(id, subject, subjectType, body.content)
        return this.bintrayClient.RESTCall("patch", getPath(id), body.toString())
    }
}
