package org.ysb33r.gradle.bintray.downloadkeys

import org.ysb33r.gradle.bintray.core.JsonBodyFilter

class DownloadKeysBody implements JsonBodyFilter {
    String id
    List white_cidrs
    List black_cidrs
    Integer expiry
    ExistenceCheck existence_check
}
