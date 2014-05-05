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

    def createPackage () {
       // POST /packages/:subject/:repo
       // { name: '', desc: '', labels : [] }
       // 201 
    }

    boolean hasVersion() {
        assertVersionAttributes()
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"

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
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"

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

    def updateVersion () {
        assertVersionAttributes()
        String rest = "packages/${repoOwner}/${repoName}/${packageName}"
        try {
            def response = rest.patch(
                    contentType : JSON,
                    path : "${rest}/versions/${packageVersion}",
                    body : [ name : packageVersion, desc : description ]
            )
            return response.isSuccess()
        } catch (HttpResponseException e) {

            if(e.response.status != 404) {
                throw e
            }

            return false
        }
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

    private void debugmsg( String msg ) {
        logger?.debug msg
    }

    private RESTClient apiClient
}




