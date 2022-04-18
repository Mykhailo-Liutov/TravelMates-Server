package cz.cvut.fit.travelmatesserver.trip

import cz.cvut.fit.travelmatesserver.trip.converters.TripsConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Dependency injection configuration for Trips module
 */
@Configuration
class TripsModule {

    @Bean
    fun provideTripsConverter(): TripsConverter = TripsConverter()

}