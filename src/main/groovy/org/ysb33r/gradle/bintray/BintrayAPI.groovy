/*
 * ============================================================================
 * (C) Copyright Schalk W. Cronje 2013-2019
 *
 * This software is licensed under the Apache License 2.0
 * See http://www.apache.org/licenses/LICENSE-2.0 for license details
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * ============================================================================
 */
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

import groovy.json.JsonOutput
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovyx.net.http.NativeHandlers
import groovyx.net.http.OkHttpBuilder
import org.gradle.api.logging.Logger

import static groovyx.net.http.ContentTypes.BINARY
import static groovyx.net.http.OkHttpBuilder.configure

@CompileStatic
class BintrayAPI {

    static final String API_BASE_URL = System.getProperty('org,ysb33r.gradle.bintray.url') ?: 'https://api.bintray.com'
    static final String JSON = 'application/json'

    String baseUrl = API_BASE_URL
    String repoOwner
    String repoName
    String packageName
    String version
    String userName
    String apiKey
    Logger logger

    boolean hasPackage() {
        final String rest = "/packages/${repoOwner}/${repoName}/${packageName}"
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
        final String rest = "/packages/${repoOwner}/${repoName}"

        Map payload = createPackagePayload(description, labels, licenses, vcsUrl)
        payload['name'] = packageName
        debugmsg "About to create package '${repoOwner}/${repoName}/${packageName}'"

        client().post {
            request.uri.path = rest
            request.body = payload

            response.success {
                debugmsg "${repoOwner}/${repoName}/${packageName}: success"
            }

            response.exception { Throwable e ->
                debugmsg "${repoOwner}/${repoName}/${packageName}: failed for payload ${payload}"
                throw e
            }
        }
    }

    boolean updatePackage(
        final String description, final List labels = [], final List licenses = [], final String vcsUrl = null) {
        assertVersionAttributes()
        final String rest = "/packages/${repoOwner}/${repoName}/${packageName}"
        Map payload = createPackagePayload(description, labels, licenses, vcsUrl)
        debugmsg "About to update package '${repoOwner}/${repoName}/${packageName}'"
        boolean updateStatus = false

        client().patch {
            request.uri.path = rest
            request.body = payload

            response.success {
                debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: success"
                updateStatus = true
            }

            response.when(404) {
                debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: not found"
            }

            response.exception { Throwable e ->
                debugmsg "${repoOwner}/${repoName}/${packageName}: failed for payload ${payload}"
                throw e
            }
        }

        updateStatus
    }

    boolean hasVersion() {
        assertVersionAttributes()
        final String rest = "/packages/${repoOwner}/${repoName}/${packageName}"
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
        final String rest = "/packages/${repoOwner}/${repoName}/${packageName}"
        Map payload = [name: version, desc: description]
        debugmsg "About to create ${repoOwner}/${repoName}/${packageName}/${version}"

        client().post {
            request.uri.path = "${rest}/versions"
            request.body = payload

            response.success {
                debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: success"
            }

            response.exception { Throwable e ->
                debugmsg "${rest}/versions: failed for payload ${payload}"
                throw e
            }
        }
    }

    boolean deleteVersion() {
        assertVersionAttributes()
        String rest = "/packages/${repoOwner}/${repoName}/${packageName}"
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
        String rest = "/packages/${repoOwner}/${repoName}/${packageName}"
        boolean updateStatus = false
        Map payload = [name: version, desc: description]
        client().patch {
            request.uri.path = "${rest}/versions/${version}"
            request.body = payload

            response.success {
                updateStatus = true
            }

            response.when(404) {
                debugmsg "${rest}/versions/${version} not found. Will not update."
            }

            response.exception { Throwable e ->
                debugmsg "${rest}/versions/${version}: failed for payload ${payload}"
                throw e
            }        }

        updateStatus
    }

    void setVersionAttributes(Map attrs = [:]) {
        assertVersionAttributes()

        if (!attrs.size()) {
            return
        }

        final String rest = "/packages/${repoOwner}/${repoName}/${packageName}/versions/${version}"
        debugmsg "About to set attributes for ${repoOwner}/${repoName}/${packageName}/${version}"

        client().post {
            request.uri.path = "${rest}/attributes"
            request.body = convertAttributes(attrs)

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
        }
    }

    void uploadContent(final File content) {
        assertVersionAttributes()
        assert content.exists()

        debugmsg "About to upload ${content.name}"
        client().put {
//            client().headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
            request.uri.path = "/content/${repoOwner}/${repoName}/${packageName}/${version}/${content.name}"
            request.body = content.bytes
            request.contentType = BINARY
        }
    }

    private OkHttpBuilder client() {
        if (apiClient == null) {
            apiClient = (OkHttpBuilder) configure {
                request.uri = baseUrl
                request.auth.basic userName, apiKey
                request.contentType = JSON
                request.encoder JSON, NativeHandlers.Encoders.&json
                response.parser JSON, NativeHandlers.Parsers.&json
            }
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

    private OkHttpBuilder apiClient
}




