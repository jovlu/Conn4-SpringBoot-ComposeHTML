package org.example.connectfour

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController(
    private val gameConfigStore: GameConfigStore,
    private val setupPageRenderer: SetupPageRenderer,
) {

    @GetMapping("/", produces = [MediaType.TEXT_HTML_VALUE])
    fun home(): String = setupPageRenderer.render(gameConfigStore.current())
}
