package com.example.routes.user


import com.example.data.response.ApiResponse
import com.example.service.UserService
import com.example.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route


fun Route.unblockUser(userService: UserService) {
    authenticate {
        post<UserRoutes.UnblockUserRoute> { blockUserRoute ->
            val userId = call.userId()
            val userIdToUnblock = blockUserRoute.id

            userService.unblockUser(userId, userIdToUnblock)

            call.respond(
                status = HttpStatusCode.OK,
                ApiResponse<Unit>(
                    successful = true,
                    message = "Successfully unblocked the user.",
                )
            )
        }
    }
}