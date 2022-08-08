package com.ajvm.graphqlserver

import com.ajvm.graphqlserver.db.DBRepositoryInsertionsImpl
import com.ajvm.graphqlserver.db.DBRepositoryRetrievalImpl
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class GraphQLServerApplicationTests {
	private val db = FakeDBInstance()
    private val retrieval = DBRepositoryRetrievalImpl(db)
	private val insertions = DBRepositoryInsertionsImpl(db)

//	@Test
//	fun insertProductTest() {
//        val id = insertions.insertProduct("SomeName", "SomeDescription", 5000)
//		val product = retrieval.getProduct(id)
//		assert(product == ProductInfo(id,"SomeName", "SomeDescription", 5000))
//	}
//	@Test
//	fun insertCustomerTest() {
//		val id = insertions.insertCustomer("SomeName","dfdf", "Something")
//		val customer = retrieval.getCustomerInfo(id)
//		assert(customer == CustomerInfo(id , "SomeName",  "ddsfsdfs"))
//	}
//
//}
//
//@SpringBootTest
//class ImplementationsTest {
//	@BeforeTestClass
//	fun getQueries() {
//	val allQueries = Queries::class.members.filter { !listOf("toString", "hashCode", "equals", "dbRetrieval")
//					.contains(it.name)}.map { it.name}
//	val dd = arrayListOf<String>().apply {
//		allQueries.forEach{
//			this.add(it)
//		}
//	}
//	}
//	@Test
////	@ParameterizedTest
////	@ValueSource(strings = dd.toArray<String>() as Array<String>)
//	fun `Function isPalindrome returns true for Palindromes`() {
////		val requestBody = """{"query":"query { getAllProducts { description id name } }"}"""
//		val requestBody = """{"query":"query { getProduct(productId:1) { price name } }"}"""
//		val client = HttpClient.newBuilder().build();
//		val request = HttpRequest.newBuilder()
//			.uri(URI.create("http://localhost:8080/graphql"))
//			.POST(HttpRequest.BodyPublishers.ofString(requestBody))
//			.headers("Content-Type", "application/json; charset=utf8")
//			.header("Authorization", "TOKEN")
//			.build()
//		val response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		println(response.body())
//	}
}
