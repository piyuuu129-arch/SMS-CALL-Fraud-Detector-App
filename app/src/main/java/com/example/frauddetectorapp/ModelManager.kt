package com.example.frauddetectorapp

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ModelManager {

    private var interpreter: Interpreter? = null

    fun getInterpreter(context: Context): Interpreter? {
        if (interpreter != null) return interpreter

        try {
            val inputStream = context.assets.open("model.tflite")
            val bytes = inputStream.readBytes()

            val buffer = ByteBuffer.allocateDirect(bytes.size)
            buffer.order(ByteOrder.nativeOrder())
            buffer.put(bytes)
            buffer.rewind()

            interpreter = Interpreter(buffer)
        } catch (e: Exception) {
            Log.e("MODEL", "TFLite failed, skipping")
            interpreter = null
        }

        return interpreter
    }
}