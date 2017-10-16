package org.musyozoku.appengine

import com.google.appengine.api.memcache.MemcacheService
import com.google.appengine.api.memcache.MemcacheServiceFactory
import org.musyozoku.appengine.mapper.json.JsonObjectMapper
import org.musyozoku.appengine.session.MemcacheSessionRepository
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.session.web.http.SessionRepositoryFilter
import org.thymeleaf.spring4.SpringTemplateEngine
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring4.view.ThymeleafViewResolver


@SpringBootApplication
class Application : SpringBootServletInitializer() {

    // JSON Object Mapper

    @Bean
    fun jsonObjectMapper() = JsonObjectMapper()

    // Thymeleaf

    @Bean
    fun viewResolver() = ThymeleafViewResolver().apply {
        templateEngine = templateEngine()
    }

    @Bean
    fun templateEngine() = SpringTemplateEngine().apply {
        templateResolvers = setOf(templateResolver())
    }

    @Bean
    fun templateResolver() = SpringResourceTemplateResolver().apply {
        templateMode = "HTML5"
        prefix = "classpath:/templates/"
        suffix = ".html"
        isCacheable = false // TODO: make it true before release
    }

    // AppEngine Session

    @Bean
    fun memcacheService(): MemcacheService =
            MemcacheServiceFactory.getMemcacheService()

    @Bean
    fun springSessionRepositoryFilter(memcacheSessionRepository: MemcacheSessionRepository) =
            SessionRepositoryFilter(memcacheSessionRepository)
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}