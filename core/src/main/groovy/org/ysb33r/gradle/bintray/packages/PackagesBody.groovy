package org.ysb33r.gradle.bintray.packages

import org.ysb33r.gradle.bintray.core.JsonBodyFilter

class PackagesBody implements JsonBodyFilter {
    String name
    String desc
    List labels
    List licenses
    List custom_licenses
    String vcs_url
    String website_url
    String issue_tracker_url
    String github_repo
    String github_release_notes_file
    Boolean public_download_numbers
    Boolean public_stats
}
