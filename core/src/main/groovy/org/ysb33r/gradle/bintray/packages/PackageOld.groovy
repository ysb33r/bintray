package org.ysb33r.gradle.bintray.packages

import groovyx.net.http.HttpResponseException

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.JSON

class PackageOld {

    def hasPackage() {
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        assertVersionAttributes()
        try {
            def response = client().get(path: "${rest}")
            debugmsg "Response code is ${response.status}. Assuming ${packageName} exists"
            return response.isSuccess()

        } catch (final HttpResponseException e) {
            debugmsg e.message
            if (e.response.status != 404) {
                throw e
            }

            debugmsg "Response code is ${e.response.status}. Assuming ${packageName} does not exist"
            return false
        }
    }

    /** Attempts to create a package
     *
     * @param description
     * @param labels
     * @param licenses
     * @param vcsUrl
     * @return
     */


    def createPackage(
            final String description, final def labels = [], final def licenses = [], final String vcsUrl = null) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}"

        def body = createPackageBody(description, labels, licenses, vcsUrl)
        body['name'] = packageName
        debugmsg "About to create package '${repoOwner}/${repoName}/${packageName}'"
        def response = client().post(
                contentType: JSON,
                path: "${rest}",
                body: body
        )
        debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: ${response.status}"
        return response.isSuccess()
    }

    /** Attempts to update a package
     *
     * @param description
     * @param labels
     * @param licenses
     * @param vcsUrl
     * @return
     */

    def updatePackage(
            final String description, final def labels = [], final def licenses = [], final String vcsUrl = null) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        def body = createPackageBody(description, labels, licenses, vcsUrl)
        debugmsg "About to update package '${repoOwner}/${repoName}/${packageName}'"

        try {

            def response = client().patch(
                    contentType: JSON,
                    path: "${rest}",
                    body: body
            )
            debugmsg "${repoOwner}/${repoName}/${packageName}: ${response.status}"
            return response.isSuccess()
        } catch (HttpResponseException e) {

            debugmsg "${repoOwner}/${repoName}/${packageName}: ${e.response.status}"

            if (e.response.status != 404) {
                throw e
            }

            return false
        }

        return response.isSuccess()
    }

    /** Internal function to build the body required by createPackage() and updatePackage()
     *
     * @param description
     * @param labels
     * @param licenses
     * @param vcsUrl
     * @return A map containing the appropriate items (could be empty)
     */
    private def createPackageBody(
            final String description, final def labels = [], final def licenses = [], final String vcsUrl = null) {
        def body = [:]
        if (description?.size()) {
            body['desc'] = description
        }
        if (labels?.size()) {
            body['labels'] = labels
        }
        if (licenses?.size()) {
            body['licenses'] = licenses
        }
        if (vcsUrl?.size()) {
            body['vcs_url'] = vcsUrl
        }
        return body
    }
}