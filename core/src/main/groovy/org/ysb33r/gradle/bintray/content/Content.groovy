package org.ysb33r.gradle.bintray.content

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection

import static groovyx.net.http.ContentType.BINARY
import static groovyx.net.http.ContentType.JSON

//class Content implements BintrayConnection, ContentRequest {
//
//    def getContent(
//            String filePath, Map queryMap = [:],
//            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
//        this.filePath = filePath
//        this.queryMap = queryMap
//        assertAttributes(filePath, subject, repo)
//        return RESTCall(onSuccess, onFail) {
//            return client().get(
//                    contentType: BINARY,
//                    path: getPath(),
//                    query: queryMap
//            )
//        }
//    }
//
//    JsonBuilder createContent(
//            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
//        assertAttributes(subject, repo, body.content."access")
//        return RESTCall(onSuccess, onFail) {
//            return client().post(
//                    contentType: JSON,
//                    path: getPath(),
//                    body: body.toString()
//            )
//        }
//    }
//
//    JsonBuilder updateContent(
//            String ContentId,
//            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
//        this.ContentId = ContentId
//        assertAttributes(this.ContentId, subject, repo)
//        return RESTCall(onSuccess, onFail) {
//            return client().patch(
//                    contentType: JSON,
//                    path: getPath(),
//                    body: body.toString()
//            )
//        }
//    }
//
//
//    JsonBuilder deleteContent(
//            String ContentId,
//            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
//        this.ContentId = ContentId
//        assertAttributes(this.ContentId, subject, repo)
//        return RESTCall(onSuccess, onFail) {
//            return client().delete(
//                    contentType: JSON,
//                    path: getPath()
//            )
//        }
//    }
//}