package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection

import static groovyx.net.http.ContentType.JSON

class DownloadKeys implements BintrayConnection, DownloadKeysRequest  {

    JsonBuilder getDownloadKeys(
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        assertAttributes(subject, subjectType)
        return RESTCall(onSuccess, onFail) {
            return client().get(
                    contentType: JSON,
                    path: getPath()
            )
        }
    }

    JsonBuilder createDownloadKey(
            String keyId = "",
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        if (keyId){
            body.getContent()."id" = keyId // Supports optional argument overriding body
        }
        this.keyId = "" // removes id from the key object, to keep out of URI
        assertAttributes(subject, subjectType, body.getContent()."id")
        return RESTCall(onSuccess, onFail) {
            return client().post(
                    contentType: JSON,
                    path: getPath(),
                    body: body.toString()
            )
        }
    }

    JsonBuilder getDownloadKey(
            String keyId,
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        this.keyId = keyId
        assertAttributes(this.keyId, subject, subjectType)
        return RESTCall(onSuccess, onFail) {

            return client().get(
                    contentType: JSON,
                    path: getPath()
            )
        }
    }

    JsonBuilder deleteDownloadKey(
            String keyId,
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        this.keyId = keyId
        assertAttributes(this.keyId, subject, subjectType)
        return RESTCall(onSuccess, onFail) {
            return client().delete(
                    contentType: JSON,
                    path: getPath()
            )
        }
    }

    JsonBuilder updateDownloadKey(
            String keyId,
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        this.keyId = keyId
        assertAttributes(this.keyId, subject, subjectType, body.getContent()."id")
        return RESTCall(onSuccess, onFail) {
            return client().patch(
                    contentType: JSON,
                    path: getPath(),
                    body: body.toString()
            )
        }
    }
}