package com.example.data.repository.user

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.data.database.table.Users
import com.example.data.mapper.toUser
import com.example.data.model.UpdateUser
import com.example.data.model.User
import org.jetbrains.exposed.sql.*

class UserRepositoryImpl : UserRepository {

    override suspend fun createUser(user: User) {
        dbQuery {
            Users.insert { table ->
                table[email] = user.email
                table[hashPassword] = user.hashPassword
                table[fullName] = user.fullName
                table[username] = user.username
                table[bio] = user.bio
                table[profilePictureUrl] = user.profilePictureUrl
                table[emailVerificationCode] = user.emailVerificationCode
                table[passwordResetCode] = user.passwordResetCode
                table[isPasswordResetAllowed] = user.isPasswordResetAllowed
                table[isActive] = user.isActive
                table[isAdmin] = user.isAdmin
                table[lastActivity] = user.lastActivity
            }
        }
    }

    override suspend fun updateUser(updateUser: UpdateUser, id: Int): User? {
        val rowsUpdated = dbQuery {
            Users.update({ Users.id eq id }) { table ->
                table[hashPassword] = updateUser.hashPassword
                table[fullName] = updateUser.fullName
                table[username] = updateUser.username
                table[bio] = updateUser.bio
                table[profilePictureUrl] = updateUser.profilePictureUrl
                table[emailVerificationCode] = updateUser.emailVerificationCode
                table[passwordResetCode] = updateUser.passwordResetCode
                table[isPasswordResetAllowed] = updateUser.isPasswordResetAllowed
                table[isActive] = updateUser.isActive
            }
        }
        return if (rowsUpdated > 0) getUserById(id) else null
    }

    override suspend fun getUserByEmail(email: String): User? {
        return dbQuery {
            Users.select {
                Users.email eq email
            }.firstOrNull().toUser()
        }
    }

    override suspend fun getUserById(id: Int): User? {
        return dbQuery {
            Users.select {
                Users.id eq id
            }.firstOrNull().toUser()
        }
    }

    override suspend fun searchUsersByUsername(username: String): List<User> {
        return dbQuery {
            Users.select {
                Users.username
                    .trim()
                    .lowerCase()
                    .like("%$username%")
            }.mapNotNull { row ->
                row.toUser()
            }
        }
    }

}