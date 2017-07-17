package org.musyozoku.appengine.listener

import org.springframework.stereotype.Component
import java.util.logging.Logger
import javax.servlet.http.HttpSessionAttributeListener
import javax.servlet.http.HttpSessionBindingEvent

@Component
class SessionAttributeListener : HttpSessionAttributeListener {

    private val logger = Logger.getLogger(javaClass.simpleName)

    override fun attributeReplaced(event: HttpSessionBindingEvent) {
        logger.info("${event.name} = ${event.value}")
    }

    override fun attributeRemoved(event: HttpSessionBindingEvent) {
        logger.info("${event.name} = ${event.value}")
    }

    override fun attributeAdded(event: HttpSessionBindingEvent) {
        logger.info("${event.name} = ${event.value}")
    }
}