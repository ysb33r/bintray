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
import groovyx.net.http.ContentType
import static groovyx.net.http.ContentType.*
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import static org.ysb33r.gradle.bintray.core.BintrayEndpoint.*

class BintrayConnection {
    private RESTClient apiClient
    private RESTClient dlClient

    String userName
    String apiKey
    Closure onSuccessDefault = { resp -> resp.data }
    Closure onFailDefault = { e -> new JsonBuilder([message: e.message, code: e.statusCode]) }
    def logger

    def RESTCall(
            String method = "get", String path = "", String body = "", Map query = [:], Map headers = [:],
            ContentType contentType = JSON, BintrayEndpoint endpoint = API_BASE_URL,
            Closure onSuccess = onSuccessDefault, Closure onFailure = onFailDefault) {

        Map requestArgs = [path: path]
        if (contentType) {
            requestArgs.contentType = contentType
        }
        if (body) {
            requestArgs.body = body
        }
        if (query) {
            requestArgs.query = query
        }
        if (headers) {
            requestArgs.headers = headers
        }
//        if (true) {
//            apiClient().setProxy('localhost', 8888, null)
//            apiClient().ignoreSSLIssues()
//        }
        println "Endpoint is: $endpoint.text"
        println "Method is: $method"
        println "Request Args are: $requestArgs"
        try {
            HttpResponseDecorator response = getClient(endpoint)."$method"(requestArgs)
            debugmsg "Response code is ${response.status}."
            return onSuccess(response)
        } catch (HttpResponseException e) {
            debugmsg e.message
            debugmsg "Response code is ${e.response.status}."
            return onFailure(e)
        }
    }

    RESTClient getClient(BintrayEndpoint endpoint) {
        if (endpoint == API_BASE_URL){
            if (apiClient == null) {
                apiClient = new RESTClient("${API_BASE_URL.text}/")
                apiClient.auth.basic userName, apiKey
                apiClient.headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
            }
            return apiClient
        }else if (endpoint == API_DL_URL){
            if (dlClient == null) {
                dlClient = new RESTClient("${API_DL_URL.text}/")
                dlClient.auth.basic userName, apiKey
                dlClient.headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
            }
            return dlClient
        }
    }

    private void debugmsg(String msg) {
        logger?.debug msg
    }
}



