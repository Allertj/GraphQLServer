package com.ajvm.graphqlserver.graphql

import com.ajvm.graphqlserver.db.DBEvent
import com.ajvm.graphqlserver.db.DBRepositoryInsertionsImpl
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration

@Component
class SubscriptionToDBEvents(private final val dbInsertions: DBRepositoryInsertionsImpl) : Subscription {
    fun flow(): Flux<String> {
        return Flux.interval(Duration.ofSeconds(1))
            .map {
                runBlocking {
                    val aa = withContext(Dispatchers.Default) {
                        dbInsertions.db.receive()
                    }
                    aa.toString()
                }
            }
    }
}

