package org.ysb33r.gradle.bintray.core

import spock.lang.Shared
import spock.lang.Specification


class BaseIntegTest extends Specification {


    @Shared
    BintrayClient btClient

    def setupSpec(){
        String userName = System.getenv('BINTRAY_USERNAME')
        String apiKey = System.getenv('BINTRAY_API_KEY')
        assert userName
        assert apiKey
        btClient = new BintrayClientFactory(userName, apiKey).apiClient
        assert btClient
    }

}