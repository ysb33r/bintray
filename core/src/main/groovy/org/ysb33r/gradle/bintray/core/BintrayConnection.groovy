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
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException

trait BintrayConnection implements ApiBase {

    private RESTClient apiClient
    String userName
    String apiKey
    JsonBuilder body
    Closure onSuccessDefault = { resp -> resp.data }
    Closure onFailDefault = { e -> new JsonBuilder([message: e.message, code: e.statusCode]) }
    def logger

    def RESTCall(Closure onSuccess, Closure onFailure, Closure operation){
        try {
            HttpResponseDecorator response = operation()
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

    def assertAttributes(Object... attributes){
        attributes.each{
            assert it?.toString().size()
        }
    }

//    private def assertRequiredAttributes(Object object) {
//        assertAttributes(
//            object.declaredFields.findAll {
//                !it.synthetic && it.name != 'props'
//            }
//        )
//    }
//
//    /** There seems to be an issue converting to JSON when items are of type GStringImpl.
//     *
//     * @param attrValues
//     */
//    @PackageScope
//    def convertAttributes(Map attrs) {
//        attrs.collect { k, v ->
//
//            def converted
//            if (v instanceof Collection || v.class.isArray()) {
//                converted = v.collect { a -> (a instanceof GString) ? a.toString() : a }
//            } else {
//                converted = [(v instanceof GString) ? v.toString() : v]
//            }
//            [name: "${k}", values: converted]
//        }
//    }

    private void debugmsg(String msg) {
        logger?.debug msg
    }


}




