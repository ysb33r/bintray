package org.ysb33r.gradle.bintray.versions

import org.ysb33r.gradle.bintray.core.JsonBodyFilter

class VersionsBody implements JsonBodyFilter {
    String name
    String desc
    String vcs_tag                              //optional
    String released                             //optional
    String github_release_notes_file            //optional
    Boolean github_use_tag_release_notes        //optional

}
