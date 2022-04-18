package cz.cvut.fit.travelmatesserver.security

/**
 * Details of user which are included in the JWT token
 */
data class UserDetails(
    val email:String,
    val name:String
)