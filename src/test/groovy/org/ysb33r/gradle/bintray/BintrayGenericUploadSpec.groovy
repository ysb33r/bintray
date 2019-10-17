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

import spock.lang.*
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class BintrayGenericUploadSpec extends spock.lang.Specification {
    def Project project = ProjectBuilder.builder().build()
    def upload = project.task('test', type: BintrayGenericUpload )

    def "Newly created Task will set default api path"() {
        expect:
        upload.baseUrl == BintrayAPI.API_BASE_URL
    }

    def "Let's configure sources"() {
        when:
            upload.sources('file1.txt')

        then:
            upload.artifacts[0] == project.file('file1.txt').absoluteFile
    }
}
