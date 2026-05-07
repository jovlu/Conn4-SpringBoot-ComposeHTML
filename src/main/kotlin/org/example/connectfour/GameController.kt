package org.example.connectfour

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(
    private val gameConfigStore: GameConfigStore,
    private val gamePageRenderer: GamePageRenderer,
) {

    @PostMapping("/game")
    fun saveAndShowGame(
        @RequestParam(defaultValue = "7") columns: Int,
        @RequestParam(defaultValue = "6") rows: Int,
        @RequestParam(name = "connect", defaultValue = "4") connect: Int,
    ): ResponseEntity<Unit> {
        gameConfigStore.update(columns, rows, connect)
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
            .header(HttpHeaders.LOCATION, "/game")
            .build()
    }

    @GetMapping("/game", produces = [MediaType.TEXT_HTML_VALUE])
    fun showGame(): String = gamePageRenderer.render(gameConfigStore.current(), gameConfigStore.currentSnapshot())

    @PostMapping("/game/state", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveState(@RequestBody snapshot: GameSnapshot) {
        gameConfigStore.saveSnapshot(snapshot)
    }
}
