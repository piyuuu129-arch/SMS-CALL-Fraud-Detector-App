package com.example.frauddetectorapp

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ToneClassifier(private val context: Context) {

    private val interpreter: Interpreter by lazy {
        Interpreter(loadModel())
    }

    private fun loadModel(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predictTone(text: String): String {

        if (text.isEmpty()) return "NORMAL"

        // 🔥 MODEL EXPECTS 10000 INPUTS
        val input = Array(1) { FloatArray(10000) }

        val clean = text.lowercase()

        for (i in clean.indices) {
            if (i >= 10000) break
            input[0][i] = clean[i].code.toFloat()
        }

        // 🔥 OUTPUT IS 4 CLASSES
        val output = Array(1) { FloatArray(4) }

        interpreter.run(input, output)

        val scores = output[0]
        val maxIndex = scores.indices.maxByOrNull { scores[it] } ?: 0

        return when (maxIndex) {
            0 -> "SAFE"
            1 -> "SPAM"
            2 -> "THREAT"
            3 -> "URGENT"
            else -> "SAFE"
        }
    }
}
