package pl.mjasion.nettool.controllers.tools

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.mjasion.nettool.domain.accesshistory.AccessHistory
import pl.mjasion.nettool.domain.accesshistory.AccessHistoryRepository

import javax.servlet.http.HttpServletRequest

import static pl.mjasion.nettool.domain.accesshistory.AccessType.HEADERS

@CompileStatic
@RestController
class HeadersController {

    @Autowired AccessHistoryRepository accessHistoryRepository

    @RequestMapping('/rest/headers')
    Map restHeaders(HttpServletRequest request) {
        return getHeaders(request)
    }

    @RequestMapping('/headers')
    String headers(HttpServletRequest request) {
        Map headers = getHeaders(request)
        return headers.collect { key, value -> "$key: $value" }.join('\n')
    }

    private Map getHeaders(HttpServletRequest request) {
        Map headers = [:]
        Enumeration<String> headerNames = request.getHeaderNames()
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headers.put(key, value);
        }
        return headers.sort()
    }

    @RequestMapping('/rest/user-agent')
    Map restUserAgent(HttpServletRequest request) {
        return ['user-agent': getUserAgent(request)]
    }

    @RequestMapping('/user-agent')
    String userAgent(HttpServletRequest request) {
        return getUserAgent(request)
    }

    private String getUserAgent(HttpServletRequest request) {
        accessHistoryRepository.save(new AccessHistory(ip: request.getRemoteAddr(), accessDate: new Date(), accessType: HEADERS))
        request.getHeader('user-agent')
    }
}
