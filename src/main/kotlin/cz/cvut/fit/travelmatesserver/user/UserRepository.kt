package cz.cvut.fit.travelmatesserver.user

import cz.cvut.fit.travelmatesserver.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String>