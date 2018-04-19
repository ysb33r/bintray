// ============================================================================
// (C) Copyright Schalk W. Cronje 2013
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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class BintraySignVersionSpec extends spock.lang.Specification {
    def Project project = ProjectBuilder.builder().build()
    def sign = project.task('test', type: BintraySignVersion )

    def "Newly created Task will set default api path"() {
        expect:
            sign.baseUrl == BintrayAPI.API_BASE_URL
    }

    def "Passphrase should be null by default"() {
        expect:
            sign.gpgPassphrase == null
    }
}
