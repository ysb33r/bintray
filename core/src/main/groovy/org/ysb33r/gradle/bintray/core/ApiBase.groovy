package org.ysb33r.gradle.bintray.core

trait ApiBase {
    static final String API_BASE_URL = 'https://api.bintray.com'
    String baseUrl = API_BASE_URL
    static final String API_DL_URL = 'https://dl.bintray.com'
    String dlUrl = API_DL_URL
}