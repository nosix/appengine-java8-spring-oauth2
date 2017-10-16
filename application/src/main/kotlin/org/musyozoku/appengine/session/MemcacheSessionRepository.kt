package org.musyozoku.appengine.session

import com.google.appengine.api.memcache.Expiration
import com.google.appengine.api.memcache.MemcacheService
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class MemcacheSessionRepository(private val memcacheService: MemcacheService) : SessionRepository<MemcacheSession> {
    private val logger = Logger.getLogger(javaClass.simpleName)
    private val maxInactiveIntervalInSeconds: Int = 3600

    override fun createSession() = MemcacheSession().also { session ->
        session.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds
        logger.info("createSession() = ${session.id}")
    }

    override fun save(session: MemcacheSession) {
        logger.info("save(${session.id}) with expiration ${session.maxInactiveIntervalInSeconds}")
        memcacheService.put(session.id, session, Expiration.byDeltaSeconds(session.maxInactiveIntervalInSeconds))
    }

    override fun getSession(id: String): MemcacheSession? =
            (memcacheService.get(id) as? MemcacheSession)?.also { session ->
                session.setLastAccessedTimeToNow()
            }.also { session ->
                logger.info("getSession($id) = ${session?.id}")
            }

    override fun delete(id: String) {
        logger.info("delete($id)")
        memcacheService.delete(id)
    }
}