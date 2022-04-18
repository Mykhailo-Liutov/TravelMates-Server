package cz.cvut.fit.travelmatesserver.core

import cz.cvut.fit.travelmatesserver.trip.converters.StringToTripFilterConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        //Add converter between TripsFilter and String
        registry.addConverter(StringToTripFilterConverter())
    }
}