package cz.cvut.fit.travelmatesserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TravelMatesServerApplication

fun main(args: Array<String>) {
    runApplication<TravelMatesServerApplication>(*args)
}
