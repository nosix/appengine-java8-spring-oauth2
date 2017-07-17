package org.musyozoku.appengine

import org.musyozoku.appengine.mapper.json.JsonObjectMapper
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
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
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}