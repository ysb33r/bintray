package org.ysb33r.gradle.bintray.content

import org.ysb33r.gradle.bintray.core.JsonBodyFilter

class ContentBody implements JsonBodyFilter {
    Boolean discard = true
    Integer publish_wait_for_secs = -1
}
