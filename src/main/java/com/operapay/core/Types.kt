package com.operapay.core

import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 *
 * @author Perfect <perfectm@opay.team>
 */

val AnyMapType = object : TypeToken<Map<String, Any>>() {}.type
val StringMapType = object : TypeToken<Map<String, String>>() {}.type
val OpayErrorsType = object : TypeToken<List<OpayError>>() {}.type

/**
 * Wrapper class for some Gateway Rest API calls
 *
 */
class WithInput(val input: Any)

@JsonAdapter(ServiceType.Serializer::class)
enum class ServiceType(val value: String) {
    AIRTIME("airtime"),
    DATA("data"),
    BANK("bank"),
    ELECTRICITY("electricity"),
    TV("tv"),
    WATER("water"),
    COINS("coins");

    class Serializer: JsonSerializer<ServiceType>, JsonDeserializer<ServiceType> {
        override fun serialize(src: ServiceType, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
            return context.serialize(src.value)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ServiceType {
            return ServiceType.values().first { it.value == json.asString }
        }
    }
}

@JsonAdapter(PaymentType.Serializer::class)
enum class PaymentType(val value: String) {
    COINS("coins"),
    TOKEN("token");

    class Serializer: JsonSerializer<PaymentType>, JsonDeserializer<PaymentType> {
        override fun serialize(src: PaymentType, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
            return context.serialize(src.value)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): PaymentType {
            return PaymentType.values().first { it.value == json.asString }
        }
    }
}
