package org.example.connectfour

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(
    private val gameConfigStore: GameConfigStore,
    private val gamePageRenderer: GamePageRenderer,
) {

    @PostMapping("/game", produces = [MediaType.TEXT_HTML_VALUE])
    fun saveAndShowGame(
        @RequestParam(defaultValue = "7") columns: Int,
        @RequestParam(defaultValue = "6") rows: Int,
        @RequestParam(name = "connect", defaultValue = "4") connect: Int,
    ): String {
        val config = gameConfigStore.update(columns, rows, connect)
        return gamePageRenderer.render(config)
    }

    @GetMapping("/game", produces = [MediaType.TEXT_HTML_VALUE])
    fun showGame(): String = gamePageRenderer.render(gameConfigStore.current())
}
