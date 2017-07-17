package org.musyozoku.appengine.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class WebAppController {

    @RequestMapping("/")
    fun deliverIndexView() = "index"

    @RequestMapping("/home")
    fun deliverHomeView() = "home"
}