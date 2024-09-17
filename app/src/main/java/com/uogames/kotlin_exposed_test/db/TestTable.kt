package com.uogames.kotlin_exposed_test.db

import android.content.Context
import android.util.Log
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object TestTable : UUIDTable() {
	val text = text("hello")

	data class Test(
		val id: UUID = UUID.randomUUID(),
		val text: String
	)


	fun toDTO(result: ResultRow): Test{
		return Test(
			id = result[id].value,
			text = result[text]
		)
	}
}

fun initDB(dbPath: String) {
	Database.connect("jdbc:sqlite:${dbPath}")
	transaction {
		SchemaUtils.create(TestTable)
	}

}


fun TestTable.save(test: TestTable.Test) = transaction {
	TestTable.insert {
		it[id] = test.id
		it[text] = test.text
	}
}

fun TestTable.getAll() = transaction {
	TestTable.selectAll().map { toDTO(it) }
}

fun TestTable.delete(test: TestTable.Test) = transaction {
	TestTable.deleteWhere {
		TestTable.id eq test.id
	}
}
