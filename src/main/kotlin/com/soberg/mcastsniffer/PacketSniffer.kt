package com.soberg.mcastsniffer

import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class PacketSniffer {

    private val logTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

    fun start(address: InetAddress, port: Int) {
        val socket = MulticastSocket(port)
        socket.joinGroup(address)
        println("Connected to ${address.hostAddress}:$port at ${nowDateTime()}, listening for packets...")
        receive(socket)
    }

    private fun nowDateTime() = logTimeFormatter.format(Date())

    private fun receive(socket: MulticastSocket) {
        val fileWriter = FileWriter(createRecordFile())
        val buffer = ByteArray(65535)
        val packet = DatagramPacket(buffer, buffer.size)
        while (!socket.isClosed) {
            packet.length = buffer.size
            socket.receive(packet)
            printPacketStream(buffer, packet, fileWriter)
        }
    }

    private fun createRecordFile() = File("${SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(Date())}_MulticastSharkLog.txt")

    private fun printPacketStream(buffer: ByteArray, packet: DatagramPacket, fileWriter: FileWriter) {
        val message = String(buffer, packet.offset, packet.length)
        print("Message received from ${packet.address.hostAddress} at ${nowDateTime()}:\n$message", fileWriter)
    }

    private fun print(message: String, fileWriter: FileWriter) {
        val append = if (message.endsWith('\n')) "\n" else "\n\n"
        println("$message$append")
        fileWriter.write("$message$append")
        fileWriter.flush()
    }
}