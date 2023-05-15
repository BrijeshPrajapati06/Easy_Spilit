package com.diyantech.easysplit.modelclass.response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ChangePasswordResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("meta")
    @Expose
    val meta: Meta
) {
    data class Data(
        @SerializedName("clusterTime")
        @Expose
        val clusterTime: ClusterTime,
        @SerializedName("connection")
        @Expose
        val connection: Connection,
        @SerializedName("electionId")
        @Expose
        val electionId: String,
        @SerializedName("matchedCount")
        @Expose
        val matchedCount: Int,
        @SerializedName("modifiedCount")
        @Expose
        val modifiedCount: Int,
        @SerializedName("n")
        @Expose
        val n: Int,
        @SerializedName("nModified")
        @Expose
        val nModified: Int,
        @SerializedName("ok")
        @Expose
        val ok: Int,
        @SerializedName("opTime")
        @Expose
        val opTime: OpTime,
        @SerializedName("operationTime")
        @Expose
        val operationTime: String,
        @SerializedName("result")
        @Expose
        val result: Result,
        @SerializedName("upsertedCount")
        @Expose
        val upsertedCount: Int,
        @SerializedName("upsertedId")
        @Expose
        val upsertedId: Any
    ) {
        data class ClusterTime(
            @SerializedName("clusterTime")
            @Expose
            val clusterTime: String,
            @SerializedName("signature")
            @Expose
            val signature: Signature
        ) {
            data class Signature(
                @SerializedName("hash")
                @Expose
                val hash: String,
                @SerializedName("keyId")
                @Expose
                val keyId: String
            )
        }

        data class Connection(
            @SerializedName("address")
            @Expose
            val address: String,
            @SerializedName("bson")
            @Expose
            val bson: Bson,
            @SerializedName("closed")
            @Expose
            val closed: Boolean,
            @SerializedName("destroyed")
            @Expose
            val destroyed: Boolean,
            @SerializedName("_events")
            @Expose
            val events: Events,
            @SerializedName("_eventsCount")
            @Expose
            val eventsCount: Int,
            @SerializedName("helloOk")
            @Expose
            val helloOk: Boolean,
            @SerializedName("host")
            @Expose
            val host: String,
            @SerializedName("id")
            @Expose
            val id: Int,
            @SerializedName("lastIsMasterMS")
            @Expose
            val lastIsMasterMS: Int,
            @SerializedName("monitorCommands")
            @Expose
            val monitorCommands: Boolean,
            @SerializedName("port")
            @Expose
            val port: Int,
            @SerializedName("socketTimeout")
            @Expose
            val socketTimeout: Int
        ) {
            class Bson

            class Events
        }

        data class OpTime(
            @SerializedName("t")
            @Expose
            val t: Int,
            @SerializedName("ts")
            @Expose
            val ts: String
        )

        data class Result(
            @SerializedName("clusterTime")
            @Expose
            val clusterTime: ClusterTime,
            @SerializedName("electionId")
            @Expose
            val electionId: String,
            @SerializedName("n")
            @Expose
            val n: Int,
            @SerializedName("nModified")
            @Expose
            val nModified: Int,
            @SerializedName("ok")
            @Expose
            val ok: Int,
            @SerializedName("opTime")
            @Expose
            val opTime: OpTime,
            @SerializedName("operationTime")
            @Expose
            val operationTime: String
        ) {
            data class ClusterTime(
                @SerializedName("clusterTime")
                @Expose
                val clusterTime: String,
                @SerializedName("signature")
                @Expose
                val signature: Signature
            ) {
                data class Signature(
                    @SerializedName("hash")
                    @Expose
                    val hash: String,
                    @SerializedName("keyId")
                    @Expose
                    val keyId: String
                )
            }

            data class OpTime(
                @SerializedName("t")
                @Expose
                val t: Int,
                @SerializedName("ts")
                @Expose
                val ts: String
            )
        }
    }

    data class Meta(
        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("message")
        @Expose
        val message: String
    )
}