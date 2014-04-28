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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.file.FileCollection

/**
 * Created by schalkc on 27/04/2014.
 */
class BintrayGenericUpload extends DefaultTask {

    @InputFiles
    List<File> artifacts

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

    @TaskAction
    void exec() {
        BintrayAPI api=new BintrayAPI(
            'baseUrl' : baseUrl,
            'repoOwner' : repoOwner,
            'repoName' : repoName,
            'packageName' : packageName,
            'version' : version,
            'userName' : userName,
            'apiKey' : apiKey
        )

        artifacts.each {
            logger.info "Uploading ${it}"
            api.uploadContent(it)
        }
    }

    void username(final String u) {
        userName=u
    }

    void sources(final List inputs) {
        inputs.each { sources(it) }
    }

    void sources(final FileCollection inputs) {
        inputs.files.each { sources(it) }
    }


    void sources(final String input) {
        sources(new File(input))
    }

    void sources(final File input) {
        if(artifacts==null) {
            artifacts = []
        }
        artifacts.add(input)
    }
}
