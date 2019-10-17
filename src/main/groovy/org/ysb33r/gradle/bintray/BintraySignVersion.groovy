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
// (C) Copyright Schalk W. Cronje 2014
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

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * Created by schalkc on 27/04/2014.
 */
@CompileStatic
class BintraySignVersion extends DefaultTask {

    @Input
    @Optional
    String baseUrl = BintrayAPI.API_BASE_URL

    @Input
    String repoOwner

    @Input
    String repoName

    @Input
    String packageName

    @Input
    @Optional
    String version = project.version

    @Input
    String userName

    @Input
    String apiKey

    /** Set this if the signing key requires a passphrase
     *
     */
    @Input
    @Optional
    String gpgPassphrase

    @TaskAction
    void exec() {
        BintrayAPI api=new BintrayAPI(
            'baseUrl'     : baseUrl,
            'repoOwner'   : repoOwner,
            'repoName'    : repoName,
            'packageName' : packageName,
            'version'     : version,
            'userName'    : userName,
            'apiKey'      : apiKey
        )

        api.signVersion(gpgPassphrase)
    }

    void username(final String u) {
        userName=u
    }

}
