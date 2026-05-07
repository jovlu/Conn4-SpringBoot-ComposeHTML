package org.example.connectfour

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConnectFourApplication

fun main(args: Array<String>) {
    runApplication<ConnectFourApplication>(*args)
}
