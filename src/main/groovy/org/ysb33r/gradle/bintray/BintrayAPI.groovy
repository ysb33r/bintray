// ============================================================================
// (C) Copyright Schalk W. Cronje 2013-2014
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================

package org.ysb33r.gradle.bintray

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovyx.net.http.HttpBuilder
import org.gradle.api.logging.Logger

import static groovyx.net.http.ContentTypes.BINARY
import static groovyx.net.http.ContentTypes.JSON
import static groovyx.net.http.HttpBuilder.configure

@CompileStatic
class BintrayAPI {

    static final String API_BASE_URL = System.getProperty('org,ysb33r.gradle.bintray.url') ?: 'https://api.bintray.com'

    String baseUrl = API_BASE_URL
    String repoOwner
    String repoName
    String packageName
    String version
    String userName
    String apiKey
    Logger logger

    boolean hasPackage() {
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        assertVersionAttributes()
        boolean packageStatus = false
        client().get {
            request.uri.path = rest

            response.success {
                debugmsg "Response code is 200. Assuming ${packageName} exists"
                packageStatus = true
            }

            response.when(404) {
                debugmsg "Response code is 400. Assuming ${packageName} does not exist"
            }
        }
        packageStatus
    }

    /** Attempts to create a package
     *
     * @param description
     * @param labels
     * @param licenses
     * @param vcsUrl
     * @return
     */
    void createPackage(
        final String description, final List labels = [], final List licenses = [], final String vcsUrl = null) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}"

        Map payload = createPackagePayload(description, labels, licenses, vcsUrl)
        payload['name'] = packageName
        debugmsg "About to create package '${repoOwner}/${repoName}/${packageName}'"

        client().post {
            request.uri.path = rest
            request.body = payload
            request.contentType = JSON

            response.success {
                debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: success"
            }
        }
    }

    boolean updatePackage(
        final String description, final List labels = [], final List licenses = [], final String vcsUrl = null) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        Map payload = createPackagePayload(description, labels, licenses, vcsUrl)
        debugmsg "About to update package '${repoOwner}/${repoName}/${packageName}'"
        boolean updateStatus = false

        client().patch {
            request.uri.path = rest
            request.body = payload
            request.contentType = JSON

            response.success {
                debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: success"
                updateStatus = true
            }

            response.when(404) {
                debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: not found"
            }
        }

        updateStatus
    }

    boolean hasVersion() {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        boolean versionStatus = false

        client().get {
            request.uri.path = "${rest}/versions/${version}"

            response.success {
                debugmsg "Assuming ${version} exists"
                versionStatus = true
            }
            response.when(404) {
                debugmsg "Response code is 404. Assuming ${version} does not exist"
            }
        }

        versionStatus
    }

    void createVersion(String description) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        debugmsg "About to create ${repoOwner}/${repoName}/${packageName}/${version}"

        client().post {
            request.uri.path = "${rest}/versions"
            request.body = [name: version, desc: description]
            request.contentType = JSON

            response.success {
                debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: success"
            }
        }
    }

    boolean deleteVersion() {
        assertVersionAttributes()
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        boolean deleteStatus = false

        client().delete {
            request.uri.path = "${rest}/versions/${version}"

            response.success {
                debugmsg "Assuming ${version} exists"
                deleteStatus = true
            }
            response.when(404) {
                debugmsg "Response code is 404. Assuming ${version} does not exist"
            }

        }

        deleteStatus
    }

    boolean updateVersion(String description) {
        assertVersionAttributes()
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        boolean updateStatus = false

        client().patch {
            request.uri.path = "${rest}/versions/${version}"
            request.body = [name: version, desc: description]
            request.contentType = JSON

            response.success {
                updateStatus = true
            }

            response.when(404) {
                debugmsg "${rest}/versions/${version} not found. Will not update."
            }
        }

        updateStatus
    }

    void setVersionAttributes(Map attrs = [:]) {
        assertVersionAttributes()

        if (!attrs.size()) {
            return
        }

        final String rest = "packages/${repoOwner}/${repoName}/${packageName}/versions/${version}"
        debugmsg "About to set attributes for ${repoOwner}/${repoName}/${packageName}/${version}"

        client().post {
            request.uri.path = "${rest}/attributes"
            request.body = convertAttributes(attrs)
            request.contentType = JSON

            response.success {
                debugmsg "Attributes successfully set for ${repoOwner}/${repoName}/${packageName}/${version}"
            }
        }
    }

    void signVersion(String passphrase = null) {
        hasVersion()
        String rest = "gpg/${repoOwner}/${repoName}/${packageName}/versions/${version}"
        assertVersionAttributes()
        debugmsg "About to sign ${repoOwner}/${repoName}/${packageName}/${version}"
        Map payload = [:]
        if (passphrase?.size()) {
            payload['passphrase'] = passphrase
        }

        client().post {
            request.uri.path = rest
            request.body = payload
            request.contentType = JSON
        }
    }

    void uploadContent(final File content) {
        assertVersionAttributes()
        assert content.exists()

        client().put {
//            client().headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
            request.uri.path = "content/${repoOwner}/${repoName}/${packageName}/${version}/${content.name}"
            request.body = content.bytes
            request.contentType = BINARY
        }
    }

    private HttpBuilder client() {
        if (apiClient == null) {
            apiClient = configure {
                request.uri = API_BASE_URL
                request.auth.basic userName, apiKey
            }
//            apiClient = new RESTClient("${baseUrl}/")
//            apiClient.auth.basic userName, apiKey
//            apiClient.headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
        }
        return apiClient
    }

    private def assertVersionAttributes() {
        assert baseUrl?.size()
        assert repoOwner?.size()
        assert repoName?.size()
        assert packageName?.size()
        assert version?.size()
        assert userName?.size()
        assert apiKey?.size()
    }

    /** Internal function to build the payload required by createPackage() and updatePackage()
     *
     * @param description
     * @param labels
     * @param licenses
     * @param vcsUrl
     * @return A map containing the appropriate items (could be empty)
     */
    private Map createPackagePayload(
        final String description, final List labels = [], final List licenses = [], final String vcsUrl = null) {
        Map payload = [:]
        if (description?.size()) {
            payload['desc'] = description
        }
        if (labels?.size()) {
            payload['labels'] = labels
        }
        if (licenses?.size()) {
            payload['licenses'] = licenses
        }
        if (vcsUrl?.size()) {
            payload['vcs_url'] = vcsUrl
        }
        return payload
    }
/*
{
  "name": "my-package",
  "desc": "This package...",
  "labels": ["persistence", "database"],
  "licenses": ["Apache-2.0", "GPL-3.0"],
  "custom_licenses": ["my-license-1", "my-license-2"],
  "vcs_url": "https://github.com/bintray/bintray-client-java.git",
  "website_url": "http://jfrog.com",
  "issue_tracker_url": "https://github.com/bintray/bintray-client-java/issues",
  "github_repo": "bintray/bintray-client-java",
  "github_release_notes_file": "RELEASE.txt",
  "public_download_numbers": false,
  "public_stats": true
}
 */
    /** There seems to be an issue converting to JSON when items are of type GStringImpl.
     *
     * @param attrValues
     */
    @PackageScope
    def convertAttributes(Map attrs) {
        attrs.collect { k, v ->

            def converted
            if (v instanceof Collection || v.class.isArray()) {
                converted = v.collect { a -> (a instanceof GString) ? a.toString() : a }
            } else {
                converted = [(v instanceof GString) ? v.toString() : v]
            }
            [name: "${k}", values: converted]
        }
    }

    private void debugmsg(String msg) {
        logger?.debug msg
    }

    private HttpBuilder apiClient
}




