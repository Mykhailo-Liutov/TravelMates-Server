package cz.cvut.fit.travelmatesserver.trip

import cz.cvut.fit.travelmatesserver.trip.converters.TripsConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TripsModule {

    @Bean
    fun provideTripsConverter(): TripsConverter = TripsConverter()

}