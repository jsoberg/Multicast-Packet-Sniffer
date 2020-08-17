package com.soberg.mcastsniffer

import java.lang.Exception
import java.net.InetAddress

fun main(args: Array<String>) {
    require(args.size == 2) { "Must provide 2 arguments: a multicast address, and a port" }

    val address: InetAddress? = tryParseMulticastAddress(args[0])
    require(address != null) { println( "1st arg is not a valid multicast address") }

    val port: Int? = tryParseInt(args[1])
    require(port != null) { println("2nd arg is not a valid port") }

    PacketSniffer().start(address, port)
}

private fun tryParseMulticastAddress(addressVal: String): InetAddress? {
    return try {
        val address = InetAddress.getByName(addressVal)
        if (!address.isMulticastAddress) {
            return null
        }
        address
    } catch (e: Exception) {
        null
    }
}

private fun tryParseInt(portVal: String): Int? {
    return try {
        portVal.toInt()
    } catch (e: NumberFormatException) {
        null
    }
}