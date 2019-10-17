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

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.ysb33r.grolifant.api.StringUtils

@CompileStatic
class BintrayPackage extends DefaultTask {

    /** Bintray username
     *
     */
    @Input
    String username

    /* Bintray API Key
     * 
     */
    @Input
    String apiKey

    /**
     * Bintray URL. Usually no need to set this unless testing. 
     */
    @Input
    @Optional
    String apiBaseUrl = 'https://api.bintray.com'

    /** Name of an existing repository
     *
     */
    @Input
    String getRepoName() {
        StringUtils.stringize(this.pkgRepoName)
    }

    void repoName(Object o) {
        this.pkgRepoName = o
    }

    /** Name of the user that created the repository. If not specified, then
     * task will use @b username instead.
     */
    @Input
    @Optional
    String getRepoOwner() {
        this.pkgRepoOwner ? StringUtils.stringize(this.pkgRepoOwner) : null
    }

    void repoOwner(Object o) {
        this.pkgRepoOwner = o
    }

    /** Package name where artifacts will be published to.
     *
     */
    @Input
    String packageName

    /** Package textual description
     *
     */
    @Input
    @Optional
    String getDescription() {
        StringUtils.stringize(this.pkgDescription)
    }

    void setDescription(Object o) {
        this.pkgDescription = o
    }

    void description(Object o) {
        this.pkgDescription = o
    }
    /** URL to another document containing the package description
     *
     */
    @Input
    @Optional
    String descUrl = ''

    /** Tags to apply to package
     *
     */
    @Input
    @Optional
    List<String> getTags() {
        StringUtils.stringize(this.tags)
    }

    void setTags(Object... t) {
        tags.clear()
        tags.addAll(t as List)
    }

    void tags(Object... t) {
        tags.addAll(t as List)
    }

    /** Licenses to apply to package
     *
     */
    @Input
    @Optional
    List<String> getLicenses() {
        StringUtils.stringize(this.licenses)
    }

    void setLicenses(Object... t) {
        licenses.clear()
        licenses.addAll(t as List)
    }

    void licenses(Object... t) {
        licenses.addAll(t as List)
    }

    /** Source Control URL
     *
     */
    @Input
    @Optional
    String getVcsUrl() {
        StringUtils.stringize(this.vcsUrl)
    }

    void vcsUrl( Object o) {
        this.vcsUrl = o
    }

    /** If set, a non-existing package will be created prior to creating the version
     *
     */
    @Input
    @Optional
    boolean autoCreatePackage = false

    /** If set, a existing package will be updated prior to creating/updating the version
     *
     */
    @Input
    @Optional
    boolean updatePackage = false

    /** Attributes that can be set on a version */
    @Input
    @Optional
    Map<String, Object> getVersionAttributes() {
        this.attributes
    }

    void versionAttributes(Map<String, ?> attrs) {
        this.versionAttributes.putAll(attrs)
    }

    String getSource() {
        repoOwner ?: username
    }

    String mavenUrl() {
        "${apiBaseUrl}/maven/${source}/${repoName}/${packageName}"
    }

    String mavenUrl(def moduleName) {
        "${apiBaseUrl}/maven/${source}/${repoName}/${moduleName}"
    }

    String ivyUrl(def moduleName, def moduleVersion) {
        "${apiBaseUrl}/content/${source}/${repoName}/${moduleName}/${moduleVersion}"
    }

    String ivyUrl(def moduleVersion) {
        "${apiBaseUrl}/content/${source}/${repoName}/${packageName}/${moduleVersion}"
    }

    @TaskAction
    def createPackage() {
        def bintray = new BintrayAPI(
            'repoOwner': source,
            'repoName': repoName,
            'packageName': packageName,
            'version': project.version.toString(),
            'userName': username,
            'apiKey': apiKey,
            'logger': project.logger
        )

        boolean updated = false
        if (autoCreatePackage && !bintray.hasPackage()) {
            if (!bintray.createPackage(getDescription(), getTags(), getLicenses(), getVcsUrl())) {
                return false
            }
            updated = true
        }

        if (updatePackage && !updated) {
            if (!bintray.updatePackage(getDescription(), getTags(), getLicenses(), getVcsUrl())) {
                return false
            }
        }

        boolean updateResult
        if (!bintray.hasVersion()) {
            updateResult = bintray.createVersion(description)
        } else {
            updateResult = bintray.updateVersion(description)
        }

        Map<String, Object> attrs = getVersionAttributes()
        if (updateResult && attrs?.size()) {
            updateResult = bintray.setVersionAttributes(attrs)
        }

        return updateResult
    }

    private final List<Object> tags = []
    private final List<Object> licenses = []
    private final Map<String, Object> attributes = [:]

    private Object pkgDescription = ''
    private Object pkgRepoName
    private Object pkgRepoOwner
    private Object vcsUrl
}




