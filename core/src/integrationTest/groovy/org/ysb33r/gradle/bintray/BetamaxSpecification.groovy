package org.ysb33r.gradle.bintray

import org.junit.Rule
import org.ysb33r.gradle.bintray.core.BintrayClient
import org.ysb33r.gradle.bintray.core.BintrayClientFactory
import software.betamax.ProxyConfiguration
import software.betamax.TapeMode
import software.betamax.junit.RecorderRule
import spock.lang.Shared
import spock.lang.Specification

/**
 * @author Schalk W. Cronjé
 */
class BetamaxSpecification extends Specification {

    static final String BINTRAY_USERNAME = System.getProperty('BINTRAY_USERNAME') ?: 'fakeBintrayUser'
    static final String BINTRAY_APIKEY   = System.getProperty('BINTRAY_APIKEY')   ?: 'fakeBintrayApiKey'
    static final String BINTRAY_RO_ORG   = System.getProperty('BINTRAY_RO_ORG')   ?: 'ysb33r'
    static final String BINTRAY_RO_REPO  = System.getProperty('BINTRAY_RO_REPO')  ?: 'grysb33r'
    static final String BINTRAY_RO_PKG   = System.getProperty('BINTRAY_RO_PKG')   ?: 'bintray-gradle-plugin'
    static final String BINTRAY_TESTORG  = System.getProperty('BINTRAY_TESTORG')  ?: 'fakeBintrayOrg'
    static final String BINTRAY_TESTREPO = System.getProperty('BINTRAY_TESTREPO') ?: 'fakeBintrayRepo'
    static final String BINTRAY_TESTPKG  = System.getProperty('BINTRAY_TESTPKG')  ?: 'fakeBintrayPackage'
    static final File   TAPES = new File(System.getProperty('BETAMAX_TAPEDIR')    ?: 'src/integrationTest/resources/betamax/tapes')
    static final TapeMode TAPEMODE = System.getProperty('BETAMAX_MAKETAPES') ? TapeMode.READ_WRITE : TapeMode.READ_ONLY

    @Shared ProxyConfiguration configuration = ProxyConfiguration.builder().
        tapeRoot(TAPES).
        ignoreLocalhost(false).
        defaultMode( System.getProperty('BETAMAX_MAKETAPES') ? TapeMode.READ_WRITE : TapeMode.READ_ONLY  ).
        proxyPort( System.getProperty('BETAMAX_PROXYPORT') ? System.getProperty('BETAMAX_PROXYPORT').toInteger() : ProxyConfiguration.DEFAULT_PROXY_PORT ).
        sslEnabled(true).
        build()

    @Rule RecorderRule recorder = new RecorderRule(configuration)

    @Shared
    BintrayClientFactory clientFactory = new BintrayClientFactory(
        userName : BINTRAY_USERNAME,
        apiKey : BINTRAY_APIKEY,
        proxyHost : '127.0.0.1',
        proxyPort : configuration.proxyPort,
        ignoreSSLIssues : true
    )

    @Shared BintrayClient apiClient = clientFactory.apiClient
    @Shared BintrayClient dlClient = clientFactory.dlClient
}