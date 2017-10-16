package org.musyozoku.appengine.session

import com.google.appengine.api.memcache.Expiration
import com.google.appengine.api.memcache.MemcacheService
import org.springframework.session.MapSession
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class MemcacheSessionRepository(private val memcacheService: MemcacheService) : SessionRepository<MapSession> {
    private val logger = Logger.getLogger(javaClass.simpleName)
    private val maxInactiveIntervalInSeconds: Int = 3600

    override fun createSession() = MapSession().also { session ->
        session.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds
        logger.info { "createSession() = ${session.id}" }
    }

    override fun save(session: MapSession) {
        logger.info { "save(${session.id}) with expiration ${session.maxInactiveIntervalInSeconds}" }
        memcacheService.put(session.id, session, Expiration.byDeltaSeconds(session.maxInactiveIntervalInSeconds))
    }

    override fun getSession(id: String): MapSession? =
            (memcacheService.get(id) as? MapSession)?.also { session ->
                session.lastAccessedTime = System.currentTimeMillis()
            }.also { session ->
                logger.info { "getSession($id) = ${session?.id}" }
            }

    override fun delete(id: String) {
        logger.info { "delete($id)" }
        memcacheService.delete(id)
    }
}