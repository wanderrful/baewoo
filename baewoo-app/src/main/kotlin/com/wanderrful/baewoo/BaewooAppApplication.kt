package com.wanderrful.baewoo

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration(exclude=[MongoAutoConfiguration::class])
class BaewooAppApplication

fun main(args: Array<String>) {
	runApplication<BaewooAppApplication>(*args)
}
