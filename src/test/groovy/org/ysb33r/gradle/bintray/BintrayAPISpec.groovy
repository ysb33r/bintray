/*
 * ============================================================================
 * (C) Copyright Schalk W. Cronje 2013-2018
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
package org.ysb33r.gradle.bintray

import groovy.json.JsonBuilder
import spock.lang.*
import com.github.restdriver.clientdriver.*
import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.*

class BintrayAPISpec extends Specification {
    
    @Shared def port = System.getenv()['MockServerPort'] ?: '65531'
    @Shared def mockUrl = "http://localhost:${port}"
    
    BintrayAPI api = new BintrayAPI(
            baseUrl     : mockUrl,
            repoOwner   : 'jim',
            repoName    : 'uss',
            packageName : 'enterprise',
            userName    : 'foo',
            apiKey      : 'bar',
            version     : '1.99'
    )

    def ClientDriverRule bintrayMock = new ClientDriverRule(port.toInteger() )

    @Ignore
    def "Check if version exists" () {
        given:
            get '/packages/jim/enterprise/versions/1.99', '', '', 200

        expect:
            api.hasVersion()
    }

    @Ignore
    def "Create package version if it does not exist" () {
        api.hasVersion
        expect:
            false
    }
    
    @Ignore
    def "Update package it if does exist" () {
        expect:
            false

    }
    
    @Ignore
    def "Create package information if it does not exist" () {
        expect:
            false

    }
    
    @Ignore
    def "Update version info if asked" () {
        expect:
            false

    }

    def "Convert Attributes"() {
        given:
            def attrs = api.convertAttributes  'gradle-plugin' : "org.ysb33r.bintray:${'bintray'}:${'1.0.0'}"
            def builder = new JsonBuilder()
            builder attrs

        expect:
            attrs.size() == 1
            attrs instanceof List
            attrs[0] == [ name:'gradle-plugin', values:['org.ysb33r.bintray:bintray:1.0.0'] ]
            builder.toString() == '[{"name":"gradle-plugin","values":["org.ysb33r.bintray:bintray:1.0.0"]}]'

    }
    void get(
            String path,
            String request,
            String response,
            int status
    ) {
        bintrayMock.addExpectation(
                new ClientDriverRequest( path ).withMethod(GET),
                new ClientDriverResponse(response).withStatus(status)
        )

    }
}