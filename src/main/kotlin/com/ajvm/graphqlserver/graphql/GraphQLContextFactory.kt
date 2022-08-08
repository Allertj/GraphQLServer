package com.ajvm.graphqlserver.graphql

import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class GraphQLContextFactory : DefaultSpringGraphQLContextFactory() {
    override suspend fun generateContextMap(request: ServerRequest): Map<*, Any>  {
        val aa = request.headers().header("Authorization").firstOrNull()
        return super.generateContextMap(request) + mapOf(
            "Authorization" to aa.toString()
        )
    }
}