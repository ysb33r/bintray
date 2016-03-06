package org.ysb33r.gradle.bintray.versions

import groovyx.net.http.HttpResponseException

import static groovyx.net.http.ContentType.JSON

class Version {

    boolean hasVersion() {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"

        try {
            def response = client().get(path: "${rest}/versions/${version}")
            debugmsg "Response code is ${response.status}. Assuming ${version} exists"
            return response.isSuccess()
        } catch (HttpResponseException e) {
            debugmsg e.message
            if (e.response.status != 404) {
                throw e
            }

            debugmsg "Response code is ${e.response.status}. Assuming ${version} does not exist"
            return false
        }
    }

    String getLatestVersion() {
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        try {
            def response = client().get(path: "${rest}/versions/_latest")
            debugmsg "Response code is ${response.status}."
            return response.data."vcs_tag"
        } catch (HttpResponseException e) {
            debugmsg e.message
            if (e.response.status != 404) {
                throw e
            }
            debugmsg "Response code is ${e.response.status}."
            return ""
        }
    }

    def signVersion(String passphrase = null) {
        hasVersion()
        String rest = "gpg/${repoOwner}/${repoName}/${packageName}/versions/${version}"
        assertVersionAttributes()
        debugmsg "About to sign ${repoOwner}/${repoName}/${packageName}/${version}"
        def body = [:]
        if (passphrase?.size()) {
            body['passphrase'] = passphrase
        }
        def response = client().post(
                contentType: JSON,
                path: "${rest}",
                body: body
        )
        debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: ${response.status}"
        return response.isSuccess()
    }

    def updateVersion(String description) {
        assertVersionAttributes()
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        try {
            def response = client().patch(
                    contentType: JSON,
                    path: "${rest}/versions/${version}",
                    body: [name: version, desc: description]
            )
            return response.isSuccess()
        } catch (HttpResponseException e) {

            if (e.response.status != 404) {
                throw e
            }

            return false
        }
    }

    def createVersion(String description) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"

        debugmsg "About to create ${repoOwner}/${repoName}/${packageName}/${version}"
        def response = client().post(
                contentType: JSON,
                path: "${rest}/versions",
                body: [name: version, desc: description]
        )
        debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: ${response.status}"
        return response.isSuccess()
    }

    boolean deleteVersion() {
        assertVersionAttributes()
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"

        try {
            def response = client().delete(path: "${rest}/versions/${version}")
            return response.isSuccess()
        } catch (HttpResponseException e) {

            if (e.response.status != 404) {
                throw e
            }

            return false
        }
    }

    def setVersionAttributes(attrs = [:]) {
        assertVersionAttributes()

        if (!attrs.size()) {
            return true
        }

        String rest = "packages/${repoOwner}/${repoName}/${packageName}/versions/${version}"
        debugmsg "About to set attributes for ${repoOwner}/${repoName}/${packageName}/${version}"

        def response = client().post(
                contentType: JSON,
                path: "${rest}/attributes",
                body: convertAttributes(attrs)
        )
        debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: ${response.status}"
        return response.isSuccess()

    }
}