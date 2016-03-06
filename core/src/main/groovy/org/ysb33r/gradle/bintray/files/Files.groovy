package org.ysb33r.gradle.bintray.files

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection
import static groovyx.net.http.ContentType.JSON

class Files implements BintrayConnection, FilesRequest {

    JsonBuilder getFiles(
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        assertAttributes(subject, repo)
        return RESTCall(onSuccess, onFail) {
            return client().get(
                    contentType: JSON,
                    path: getPath(),
                    query: getPublishOption()
            )
        }
    }

//    JsonBuilder createFile(
//            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
//        assertAttributes(subject, repo, body.getContent()."access")
//        return RESTCall(onSuccess, onFail) {
//            return client().post(
//                    contentType: JSON,
//                    path: getPath(),
//                    body: body.toString()
//            )
//        }
//    }
//
//    JsonBuilder updateFile(
//            String filePath,
//            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
//        this.filePath = filePath
//        assertAttributes(this.filePath, subject, repo)
//        return RESTCall(onSuccess, onFail) {
//            return client().patch(
//                    contentType: JSON,
//                    path: getPath(),
//                    body: body.toString()
//            )
//        }
//    }

    JsonBuilder searchFile(
            Map queryMap,
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        assertAttributes(queryMap)
        return RESTCall(onSuccess, onFail) {
            return client().get(
                    contentType: JSON,
                    path: getPathSearch(),
                    query: queryMap
            )
        }
    }

//    JsonBuilder deleteFile(
//            String fileId,
//            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
//        this.filePath = filePath
//        assertAttributes(this.filePath, subject, repo)
//        return RESTCall(onSuccess, onFail) {
//            return client().delete(
//                    contentType: JSON,
//                    path: getPath()
//            )
//        }
//    }
}