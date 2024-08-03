package com.ihcl.qwikcilver.repository

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.config.MongoConfig
import com.ihcl.qwikcilver.dto.CPGRestrictionDto
import com.ihcl.qwikcilver.dto.RecordIdSequence
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.model.AuthToken
import com.ihcl.qwikcilver.model.TransactionIdCounter
import com.ihcl.qwikcilver.service.AuthorizationService
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.Constants.CPG_ERROR_MESSAGE
import com.mongodb.client.model.*
import org.koin.java.KoinJavaComponent
import org.litote.kmongo.eq
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DatabaseRepository {
    val collection = MongoConfig().getDatabase().getCollection<TransactionIdCounter>()
    private val conn = MongoConfig().getDatabase().getCollection<AuthToken>()
    private val recordIdCollection = MongoConfig().getDatabase().getCollection<RecordIdSequence>()
    val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val gifCardCollection=MongoConfig().getDatabase().getCollection<CPGRestrictionDto>("giftCardRestrictionValues")


    suspend fun incrementCounter(): Int? {
        val requestIdType = Constants.QC.TRANSACTION_ID
        val result = recordIdCollection.findOne(RecordIdSequence::type eq requestIdType)
        return if (!result?.value.isNullOrBlank()) {
            var incrementValue = result?.value?.toInt()
            incrementValue = incrementValue!! + 1
            recordIdCollection.updateOne(
                RecordIdSequence::value eq result?.value,
                RecordIdSequence(requestIdType, incrementValue.toString()),
                UpdateOptions().upsert(true)
            ).modifiedCount
            val dbResult = recordIdCollection.findOne(
                RecordIdSequence::type
                        eq
                        requestIdType
            )
            dbResult?.value?.toInt()
        } else {
            val firstRequestId = Configuration.env.qcTransactionId
            recordIdCollection.insertOne(
                RecordIdSequence(
                    requestIdType,
                    firstRequestId
                )
            )
            return firstRequestId.toInt()
        }
    }
    suspend fun getToken(): List<AuthToken> {
        return conn.find().toList()
    }

    suspend fun getGiftCardDetails(id: String): List<CPGRestrictionDto> {
        val result = gifCardCollection.find(CPGRestrictionDto::_id eq id).toList()
         if (result.isNotEmpty()){
             return result
         }
       throw QCInternalServerException(CPG_ERROR_MESSAGE)
    }
}
