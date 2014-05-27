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

import groovy.transform.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.BINARY


// This code is WIP - I am planning to extend access
// to the Bintray API by manner of Gradle tasks
//@TupleConstructor
class BintrayAPI {

    static final String API_BASE_URL = 'https://api.bintray.com'

    String baseUrl = API_BASE_URL
    String repoOwner
    String repoName
    String packageName
    String version
    String userName
    String apiKey
    def logger

    def hasPackage() {
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        assertVersionAttributes()
        try {
            def response = client().get( path : "${rest}"  )
            debugmsg "Response code is ${response.status}. Assuming ${packageName} exists"
            return response.isSuccess()

        } catch(final HttpResponseException e)    {
            debugmsg e.message
            if(e.response.status != 404) {
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
    def createPackage( final String description, final def labels = [], final def licenses = [], final String vcsUrl = null  ) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}"

        def payload = createPackagePayload(description,labels,licenses,vcsUrl)
        payload['name']= packageName
        debugmsg "About to create package '${repoOwner}/${repoName}/${packageName}'"
        def response = client().post(
                contentType : JSON,
                path : "${rest}",
                body : payload
        )
        debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: ${response.status}"
        return response.isSuccess()
    }

    def updatePackage( final String description, final def labels = [], final def licenses = [], final String vcsUrl = null  ) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        def payload = createPackagePayload(description,labels,licenses,vcsUrl)
        debugmsg "About to update package '${repoOwner}/${repoName}/${packageName}'"

        try {

            def response = client().patch(
                    contentType : JSON,
                    path : "${rest}",
                    body : payload
            )
            debugmsg "${repoOwner}/${repoName}/${packageName}: ${response.status}"
            return response.isSuccess()
        } catch (HttpResponseException e) {

            debugmsg "${repoOwner}/${repoName}/${packageName}: ${e.response.status}"

            if(e.response.status != 404) {
                throw e
            }

            return false
        }

        return response.isSuccess()
    }

    boolean hasVersion() {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"

        try {
            def response = client().get( path : "${rest}/versions/${version}"  )
            debugmsg "Response code is ${response.status}. Assuming ${version} exists"
            return response.isSuccess()
        } catch (HttpResponseException e) {
            debugmsg e.message
            if(e.response.status != 404) {
                throw e
            }

            debugmsg "Response code is ${e.response.status}. Assuming ${version} does not exist"
            return false
        }
    }

    def createVersion ( String description ) {
        assertVersionAttributes()
        final String rest = "packages/${repoOwner}/${repoName}/${packageName}"

        debugmsg "About to create ${repoOwner}/${repoName}/${packageName}/${version}"
        def response = client().post(
                contentType : JSON,
                path : "${rest}/versions",
                body : [ name : version, desc : description ]
        )
        debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: ${response.status}"
        return response.isSuccess()
    }

    boolean deleteVersion() {
        assertVersionAttributes()
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"

        try {
            def response = client().delete( path : "${rest}/versions/${version}"  )
            return response.isSuccess()
        } catch (HttpResponseException e) {

            if(e.response.status != 404) {
                throw e
            }

            return false
        }
    }

    def updateVersion ( String description ) {
        assertVersionAttributes()
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        try {
            def response = client().patch(
                    contentType : JSON,
                    path : "${rest}/versions/${version}",
                    body : [ name : version, desc : description ]
            )
            return response.isSuccess()
        } catch (HttpResponseException e) {

            if(e.response.status != 404) {
                throw e
            }

            return false
        }
    }

    def signVersion( String passphrase = null ) {
        hasVersion()
        String rest = "gpg/${repoOwner}/${repoName}/${packageName}/versions/${version}"
        assertVersionAttributes()
        debugmsg "About to sign ${repoOwner}/${repoName}/${packageName}/${version}"
        def payload = [:]
        if(passphrase?.size()) {
            payload['passphrase'] = passphrase
        }
        def response = client().post(
                contentType : JSON,
                path : "${rest}",
                body : payload
        )
        debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: ${response.status}"
        return response.isSuccess()
    }

    def uploadContent(final File content) {
        assertVersionAttributes()
        assert content.exists()
        try {
            hasVersion()
            def response = client().put(
                    requestContentType : BINARY,
                    path : "content/${repoOwner}/${repoName}/${packageName}/${version}/${content.name}",
                    body : content.bytes
            )
            return response.isSuccess()
        } catch (HttpResponseException e) {
            debugmsg "${repoOwner}/${repoName}/${packageName}/${version}/${content.name}: ${response.status}"
            throw e
        }
    }

    private RESTClient client() {
        if(apiClient == null) {
            apiClient = new RESTClient("${baseUrl}/")
            apiClient.auth.basic userName, apiKey
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
    private def createPackagePayload( final String description, final def labels = [], final def licenses = [], final String vcsUrl = null  ) {
        def payload = [:]
        if(description?.size()) {
            payload['desc'] = description
        }
        if(labels?.size()) {
            payload['labels'] = labels
        }
        if(licenses?.size()) {
            payload['licenses'] = licenses
        }
        if(vcsUrl?.size()) {
            payload['vcs_url'] = vcsUrl
        }
        return payload
    }

    private void debugmsg( String msg ) {
        logger?.debug msg
    }

    private RESTClient apiClient
}




