package org.musyozoku.appengine.session

import com.google.appengine.api.datastore.*
import org.springframework.session.MapSession
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import java.io.*
import java.util.logging.Logger

@Component
class DatastoreSessionRepository(private val datastoreService: DatastoreService) : SessionRepository<MapSession> {
    private val logger = Logger.getLogger(javaClass.simpleName)
    private val maxInactiveIntervalInSeconds: Int = 3600
    private val kind = "Session"
    private val propertyName = "serialized"

    override fun createSession(): MapSession = MapSession().also { session ->
        session.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds
        logger.info { "createSession() = ${session.id}" }
    }

    override fun save(session: MapSession) {
        logger.info { "save(${session.id}) with expiration ${session.maxInactiveIntervalInSeconds}" }

        ByteArrayOutputStream().use { byteArray ->
            ObjectOutputStream(byteArray).use { outStream ->
                outStream.writeObject(session)
            }
            datastoreService.put(Entity(kind, session.id).apply {
                setProperty(propertyName, Blob(byteArray.toByteArray()))
            })
        }
    }

    override fun getSession(id: String): MapSession? {
        val blob: Blob? = try {
            datastoreService.get(KeyFactory.createKey(kind, id))
                    .getProperty(propertyName) as? Blob
        } catch (_: EntityNotFoundException) {
            null
        }
        val session: MapSession? = blob?.let {
            ByteArrayInputStream(it.bytes).use { byteArray ->
                ObjectInputStream(byteArray).use { inStream ->
                    (inStream.readObject() as MapSession).also { session ->
                        session.lastAccessedTime = System.currentTimeMillis()
                    }
                }
            }
        }
        logger.info { "getSession($id) = ${session?.id}" }
        return session
    }

    override fun delete(id: String) {
        logger.info { "delete($id)" }
        datastoreService.delete(KeyFactory.createKey(kind, id))
    }
}