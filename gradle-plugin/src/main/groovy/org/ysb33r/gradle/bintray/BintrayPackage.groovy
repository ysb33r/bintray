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

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional


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
    def apiBaseUrl = 'https://api.bintray.com'
    
    /** Name of an existing repository
     * 
     */
    @Input
    String repoName
    
    /** Name of the user that created the repository. If not specified, then
     * task will use @b username instead.
     */
    @Input
    @Optional
    String repoOwner
    
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
    String description = ''
    
    /** URL to another document containing the package description
     * 
     */
    @Input
    @Optional
    def descUrl = ''
    
    /** Tags to apply to package
     * 
     */
    @Input
    @Optional
    def tags = null

    /** Licenses to apply to package
     *
     */
    @Input
    @Optional
    def licenses = null

    /** Source Control URL
     *
     */
    @Input
    @Optional
    String vcsUrl = null


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
    Map versionAttributes = [:]

    def setTags (Object... t) {
        tags = t as List    
    }

    def setLicenses (Object... t) {
        licenses = t as List
    }

    def getSource() {
        repoOwner ?: username
    }
    
    def mavenUrl() {
        "${apiBaseUrl}/maven/${source}/${repoName}/${packageName}"
    }
    
    def mavenUrl(def moduleName) {
        "${apiBaseUrl}/maven/${source}/${repoName}/${moduleName}"
    }
    
    def ivyUrl(def moduleName,def moduleVersion) {
        "${apiBaseUrl}/content/${source}/${repoName}/${moduleName}/${moduleVersion}"
    }
 
    def ivyUrl(def moduleVersion) {
        "${apiBaseUrl}/content/${source}/${repoName}/${packageName}/${moduleVersion}"
    }
    
    @TaskAction
    def createPackage() {
        def bintray = new BintrayAPI(
            'repoOwner'   : source,
            'repoName'    : repoName,
            'packageName' : packageName,
            'version'     : project.version,
            'userName'    : username,
            'apiKey'      : apiKey,
            'logger'      : project.logger
        )

        boolean updated=false
        if(autoCreatePackage && !bintray.hasPackage()) {
            if(!bintray.createPackage(description,tags,licenses,vcsUrl)) {
                return false
            }
            updated=true
        }

        if(updatePackage && !updated) {
            if(!bintray.updatePackage(description,tags,licenses,vcsUrl)) {
                return false
            }
        }

        boolean updateResult
        if(!bintray.hasVersion()) {
            updateResult = bintray.createVersion(description)
        } else {
            updateResult = bintray.updateVersion(description)
        }

        if(updateResult && versionAttributes?.size()) {
            updateResult = bintray.setVersionAttributes(versionAttributes)
        }

        return updateResult
   }

}




