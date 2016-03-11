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

package org.ysb33r.gradle.bintray.core

import groovy.json.JsonBuilder
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import groovyx.net.http.ContentType
import static groovyx.net.http.ContentType.*
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import static org.ysb33r.gradle.bintray.core.BintrayEndpoint.*

@TupleConstructor
class BintrayClientFactory {

    String userName
    String apiKey
    String proxyHost
    Integer proxyPort
    boolean ignoreSSLIssues = false

//    Closure onSuccessDefault = { resp -> resp.data }
//    Closure onFailDefault = { e -> new JsonBuilder([message: e.message, code: e.statusCode]) }

//    def logger

//    def RESTCall(
//            String method = "get", String path = "", String body = "", Map query = [:], Map headers = [:],
//            ContentType contentType = JSON, RESTClient endpoint = getApiClient(),
//            Closure onSuccess = onSuccessDefault, Closure onFailure = onFailDefault) {
//
//        Map requestArgs = [path: path]
//        if (contentType) {
//            requestArgs.contentType = contentType
//        }
//        if (body) {
//            requestArgs.body = body
//        }
//        if (query) {
//            requestArgs.query = query
//        }
//        if (headers) {
//            requestArgs.headers = headers
//        }
//
//        debugmsg "Method is: $method"
//        debugmsg "Endpoint is: $endpoint.text"
//        debugmsg "Request Args are: $requestArgs"
//
//        try {
//            HttpResponseDecorator response = getClient(endpoint)."$method"(requestArgs)
//            debugmsg "Response code is ${response.status}."
//            return onSuccess(response)
//        } catch (HttpResponseException e) {
//            debugmsg e.message
//            debugmsg "Response code is ${e.response.status}."
//            return onFailure(e)
//        }
//    }

    BintrayClient getApiClient() {
        getClient(BintrayEndpoint.API_BASE_URL.uri)
    }

    BintrayClient getDlClient() {
        getClient(BintrayEndpoint.API_DL_URL.uri)
    }

    BintrayClient getClient(final URI uri) {
        BintrayClient client = new BintrayClient(uri)
        configureRestClient(client)
    }

    private void debugmsg(String msg) {
        log.debug msg
    }

    private BintrayClient configureRestClient(BintrayClient client) {
        if(proxyHost && proxyPort) {
            client.setProxy(proxyHost, proxyPort, null)
        }
        if(ignoreSSLIssues) {
            client.ignoreSSLIssues()
        }
        client.auth.basic userName, apiKey
        client.headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
        client
    }
}




