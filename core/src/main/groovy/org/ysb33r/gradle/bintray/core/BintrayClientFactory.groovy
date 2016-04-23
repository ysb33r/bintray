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

import groovy.transform.TupleConstructor


@TupleConstructor
class BintrayClientFactory {

    String userName
    String apiKey
    String proxyHost
    Integer proxyPort
    boolean ignoreSSLIssues = false

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
        log?.debug msg
    }

    private BintrayClient configureRestClient(BintrayClient client) {
        if(proxyHost && proxyPort) {
            client.setProxy(proxyHost, proxyPort, null)
        }
        if(ignoreSSLIssues) {
            client.ignoreSSLIssues()
        }
        if(userName && apiKey){
            client.auth.basic userName, apiKey
            client.headers.Authorization = """Basic ${"${userName}:${apiKey}".toString().bytes.encodeBase64()}"""
        }
        client
    }
}




