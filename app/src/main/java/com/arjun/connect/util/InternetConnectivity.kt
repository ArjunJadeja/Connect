package com.arjun.connect.util

import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object InternetConnectivity {
    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}