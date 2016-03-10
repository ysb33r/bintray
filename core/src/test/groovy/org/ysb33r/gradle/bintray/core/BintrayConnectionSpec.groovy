package org.ysb33r.gradle.bintray.core

import spock.lang.Shared
import spock.lang.Specification


class BintrayConnectionSpec extends Specification {
    @Shared
    BintrayConnection btConnection = new BintrayConnection().with{
        userName = System.getenv('BINTRAY_USERNAME')
        apiKey = System.getenv('BINTRAY_API_KEY')
        return it
    }


}