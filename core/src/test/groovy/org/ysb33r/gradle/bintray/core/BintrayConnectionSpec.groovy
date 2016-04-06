package org.ysb33r.gradle.bintray.core

import spock.lang.Shared
import spock.lang.Specification


class BintrayConnectionSpec extends Specification {
    @Shared
    BintrayClientFactory btClient = new BintrayClientFactory().with{
        userName = System.getenv('BINTRAY_USERNAME')
        apiKey = System.getenv('BINTRAY_API_KEY')
        return it
    }


}