package com.example.frauddetectorapp

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object ModelManager {

    private var interpreter: Interpreter? = null

    fun getInterpreter(context: Context): Interpreter? {

        if (interpreter != null) return interpreter

        try {
            val fileDescriptor = context.assets.openFd("model.tflite")
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength

            val modelBuffer: MappedByteBuffer =
                fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

            interpreter = Interpreter(modelBuffer)

            Log.e("MODEL_STATUS", "TFLite model loaded successfully")

        } catch (e: Exception) {
            Log.e("MODEL_STATUS", "TFLite load failed: ${e.message}")
            interpreter = null
        }

        return interpreter
    }
}