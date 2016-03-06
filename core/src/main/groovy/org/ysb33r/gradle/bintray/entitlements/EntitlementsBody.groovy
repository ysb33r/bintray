package org.ysb33r.gradle.bintray.entitlements

import org.ysb33r.gradle.bintray.core.JsonBodyFilter

class EntitlementsBody implements JsonBodyFilter {
    String id
    String access
    List download_keys
    String path
}
