package org.ysb33r.gradle.bintray.content

import groovyx.net.http.HttpResponseException

import static groovyx.net.http.ContentType.BINARY

class BintrayFileOld {
//    List getFileListForVersion(String version, Boolean includeUnpublished = true) {
//        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
//        try {
//            def response = client().get(
//                    path: "${rest}/versions/${version}/files",
//                    query: ["include_unpublished": (includeUnpublished ? 1 : 0)]
//            )
//            debugmsg "Response code is ${response.status}."
//            return response.data
//        } catch (HttpResponseException e) {
//            debugmsg e.message
//            if (e.response.status != 404) {
//                throw e
//            }
//            debugmsg "Response code is ${e.response.status}."
//            return []
//        }
//    }
//
//    List getFileListLatestVersion(String version = getLatestVersion(), Boolean includeUnpublished = true) {
//        def response = getFileListForVersion(version, includeUnpublished)
//        return response
//    }
//
//    Boolean fileHasUpdate(String filename, String oldSHA1) {
//        List latestFileList = getFileListLatestVersion()
//        String currentSHA1 = latestFileList.find { it.name == filename }.sha1
//        return (currentSHA1 != oldSHA1)
//    }
//
//    def getContent(String filepath) {
//        final String rest = "content/${repoOwner}/${repoName}/${filepath}"
//        debugmsg "About to download ${rest}"
//        try {
//            def response = client().get(
//                    contentType: BINARY,
//                    path: "${rest}",
//                    query: [bt_package: "${packageName}"]
//            )
//            debugmsg "${rest}/${filepath}: ${response.status}"
//            return response.data
//        } catch (HttpResponseException e) {
//            if (e.response.status != 404) {
//                throw e
//            }
//            return ""
//        }
//    }
//
//    def uploadContent(final BintrayFileOld content) {
//        assertVersionAttributes()
//        assert content.exists()
//        try {
//            client().headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
//            def response = client().put(
//                    requestContentType: BINARY,
//                    path: "content/${repoOwner}/${repoName}/${packageName}/${version}/${content.name}",
//                    body: content.bytes
//            )
//            return response.isSuccess()
//        } catch (HttpResponseException e) {
//            debugmsg "${repoOwner}/${repoName}/${packageName}/${version}/${content.name}: ${response.status}"
//            throw e
//        }
//    }
//

}