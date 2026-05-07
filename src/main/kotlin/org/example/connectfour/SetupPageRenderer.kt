package org.example.connectfour

import kotlinx.html.ButtonType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.stream.createHTML
import kotlinx.html.title
import org.springframework.stereotype.Component

@Component
class SetupPageRenderer {

    fun render(config: GameConfig): String {
        return "<!DOCTYPE html>\n" + createHTML().html {
            lang = "en"
            head {
                meta(charset = "UTF-8")
                meta {
                    name = "viewport"
                    content = "width=device-width, initial-scale=1.0"
                }
                title("Connect Four Setup")
                link(rel = "stylesheet", href = "/app.css", type = "text/css")
            }
            body {
                div("container") {
                    h1 { +"Connect Four" }
                    p { +"Choose the field size and the win condition." }

                    form {
                        id = "setup-form"
                        action = "/game"
                        method = FormMethod.post

                        div("field") {
                            label {
                                htmlFor = "columns"
                                +"Columns"
                            }
                            input(type = InputType.number, name = "columns") {
                                id = "columns"
                                min = "4"
                                max = "15"
                                value = config.columns.toString()
                            }
                        }

                        div("field") {
                            label {
                                htmlFor = "rows"
                                +"Rows"
                            }
                            input(type = InputType.number, name = "rows") {
                                id = "rows"
                                min = "4"
                                max = "15"
                                value = config.rows.toString()
                            }
                        }

                        div("field") {
                            label {
                                htmlFor = "connect"
                                +"Win condition"
                            }
                            input(type = InputType.number, name = "connect") {
                                id = "connect"
                                min = "4"
                                max = "10"
                                value = config.connect.toString()
                            }
                        }

                        p("hint") { +"Rows and columns: 4 to 15. Connect value: 4 to 10." }

                        button {
                            type = ButtonType.submit
                            +"Start Game"
                        }
                    }
                }
            }
        }
    }
}
