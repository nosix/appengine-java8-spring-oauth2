package org.musyozoku.appengine.listener

import org.springframework.stereotype.Component
import java.util.logging.Logger
import javax.servlet.ServletRequestEvent
import javax.servlet.ServletRequestListener
import javax.servlet.http.HttpServletRequest

@Component
class RequestListener : ServletRequestListener {

    private val logger = Logger.getLogger(javaClass.simpleName)

    override fun requestInitialized(sre: ServletRequestEvent) {
        val request = sre.servletRequest as HttpServletRequest
        logger.dumpSession(request, preInfo = "begin ${request.servletPath}")
    }

    override fun requestDestroyed(sre: ServletRequestEvent) {
        val request = sre.servletRequest as HttpServletRequest
        logger.dumpSession(request, postInfo = "end ${request.servletPath}")
    }

    private inline fun Logger.dumpSession(
            request: HttpServletRequest, preInfo: String? = null, postInfo: String? = null) {

        preInfo?.let { info(it) }
        request.getSession(false)?.let {
            session ->
            info("--- session info")
            session.attributeNames.asSequence()
                    .map { "$it = ${session.getAttribute(it)}" }
                    .forEach { info(it) }
            info("--- in $session")
        }
        postInfo?.let { info(it) }
    }
}