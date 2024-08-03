package com.ihcl.qwikcilver.util
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.ihcl.qwikcilver.config.Configuration
import org.apache.commons.codec.digest.HmacAlgorithms
import org.apache.commons.codec.digest.HmacUtils
import java.net.URLEncoder
import java.util.Arrays

class Signature {
    suspend fun signatureGenerator(payload: Any?, method:String, apiUrl:String): String {
        val prop = Configuration.env
        val signature: String
        val clientSecret = prop.woohooClientSecret


        if (payload == null || payload == "") {
            var endpoint: String
            if (apiUrl.contains("?")) {
                val queryArray: Array<String> = apiUrl.substring(apiUrl.indexOf("?") + 1).split("&").toTypedArray()
                Arrays.sort(queryArray)
                val queryParams = java.lang.String.join("&", queryArray.toList())
                val splitArray: Array<String> = apiUrl.split("?").toTypedArray()
                val finalUrl: String = splitArray[0] + "?" + queryParams
                endpoint = URLEncoder.encode(finalUrl, "UTF-8");
            } else {
                endpoint = URLEncoder.encode(apiUrl, "UTF-8");
            }
            val baseString: String = method + "&" + endpoint
            signature = HmacUtils(HmacAlgorithms.HMAC_SHA_512, clientSecret).hmacHex(baseString)
            println(signature)
        } else {
            val mapper = ObjectMapper()
            val jsonString: String = mapper.writeValueAsString(payload)

            //sort the payload
            mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            val map = mapper.readValue(jsonString, HashMap::class.java)
            val json = mapper.writeValueAsString(map)

            val reuestData = URLEncoder.encode(json, "UTF-8").replace("+", "%20")
            var endpoint: String = URLEncoder.encode(apiUrl, "UTF-8")

            // httpMethod = POST, PUT, GET etc
            val baseString = method + "&" + endpoint + "&" + reuestData
            signature = HmacUtils(HmacAlgorithms.HMAC_SHA_512,clientSecret).hmacHex(baseString)
        }
        return signature
    }
}



