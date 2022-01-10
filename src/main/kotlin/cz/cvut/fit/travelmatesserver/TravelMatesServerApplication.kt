package cz.cvut.fit.travelmatesserver

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@SpringBootApplication
class TravelMatesServerApplication

fun main(args: Array<String>) {
    runApplication<TravelMatesServerApplication>(*args)
}
