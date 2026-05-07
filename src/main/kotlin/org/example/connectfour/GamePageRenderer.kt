package org.example.connectfour

import kotlinx.html.ButtonType
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.stream.createHTML
import kotlinx.html.title
import org.springframework.stereotype.Component

@Component
class GamePageRenderer {

    fun render(config: GameConfig): String {
        return "<!DOCTYPE html>\n" + createHTML().html {
            lang = "en"
            head {
                meta(charset = "UTF-8")
                meta {
                    name = "viewport"
                    content = "width=device-width, initial-scale=1.0"
                }
                title("Connect Four Game")
                link(rel = "stylesheet", href = "/app.css", type = "text/css")
            }
            body {
                div("container") {
                    h1 { +"Connect Four" }
                    p { +"Drop pieces into a column. First to connect ${config.connect} wins." }
                    p { +"Rows: ${config.rows}" }
                    p { +"Columns: ${config.columns}" }
                    p { +"Connect: ${config.connect}" }

                    div("game-root") {
                        id = "game-root"
                        attributes["data-columns"] = config.columns.toString()
                        attributes["data-rows"] = config.rows.toString()
                        attributes["data-connect"] = config.connect.toString()
                    }

                    p {
                        id = "status"
                        +"Player 1 turn"
                    }

                    div("board-wrap") {
                        div("controls") {
                            id = "controls"
                        }

                        div("board") {
                            id = "board"
                        }
                    }

                    div("actions") {
                        button {
                            id = "restart-button"
                            type = ButtonType.button
                            +"Restart"
                        }

                        a("/", classes = "button-link") { +"Back to setup" }
                    }
                }

                script(src = "/game.js") {}
            }
        }
    }
}
