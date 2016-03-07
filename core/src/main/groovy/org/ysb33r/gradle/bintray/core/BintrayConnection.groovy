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



trait BintrayConnection implements ApiBase {

    private RESTClient apiClient
    String userName
    String apiKey
    Closure onSuccessDefault = { resp -> resp.data }
    Closure onFailDefault = { e -> new JsonBuilder([message: e.message, code: e.statusCode]) }
    def logger

    JsonBuilder RESTCall(
            String method = "get", String path = "", String body = "", Map query = [:], ContentType contentType = JSON,
            Closure onSuccess = onSuccessDefault, Closure onFailure = onFailDefault) {
        Map requestArgs = [path: path]
        if (contentType){requestArgs.contentType = contentType}
        if (body){requestArgs.body = body}
        if (query){requestArgs.query = query}
        println method
        println requestArgs
        try {
            HttpResponseDecorator response = client()."$method"(requestArgs)
            debugmsg "Response code is ${response.status}."
            return onSuccess(response)
        } catch (HttpResponseException e) {
            debugmsg e.message
            debugmsg "Response code is ${e.response.status}."
            return onFailure(e)
        }
    }

    RESTClient client() {
        if (apiClient == null) {
            apiClient = new RESTClient("${baseUrl}/")
            apiClient.auth.basic userName, apiKey
            apiClient.headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
        }
        return apiClient
    }


    private void debugmsg(String msg) {
        logger?.debug msg
    }
}




