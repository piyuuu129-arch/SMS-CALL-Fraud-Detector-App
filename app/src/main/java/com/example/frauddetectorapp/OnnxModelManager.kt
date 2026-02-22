package com.example.frauddetectorapp

import android.content.Context
import android.util.Log
import ai.onnxruntime.*

object OnnxModelManager {

    private var session: OrtSession? = null
    private var env: OrtEnvironment? = null

    fun init(context: Context) {
        try {
            if (session != null) return

            env = OrtEnvironment.getEnvironment()

            val modelBytes = context.assets.open("spam_model.onnx").readBytes()

            session = env!!.createSession(modelBytes)

            Log.e("ONNX", "Model loaded successfully")

        } catch (e: Exception) {
            Log.e("ONNX", "LOAD ERROR: ${e.message}")
        }
    }

    fun predict(context: Context, text: String): String {

        Log.e("ONNX", "ONNX FUNCTION STARTED")

        try {
            if (session == null) init(context)

            val vector = TextVectorizer.vectorize(text)
            val inputArray = arrayOf(vector)

            val tensor = OnnxTensor.createTensor(
                env,
                inputArray
            )

            val result = session!!.run(
                mapOf(session!!.inputNames.iterator().next() to tensor)
            )

            val output = (result[0].value as Array<FloatArray>)[0]

            Log.e("ONNX_RAW", output.joinToString())

            // 🔴 find max
            var maxIndex = 0
            var maxVal = output[0]

            for (i in output.indices) {
                if (output[i] > maxVal) {
                    maxVal = output[i]
                    maxIndex = i
                }
            }

            Log.e("ONNX", "maxVal=$maxVal index=$maxIndex")

            // 🔵 confidence filter
            if (maxVal < 0.05f) return "UNKNOWN"

            return when (maxIndex) {
                0 -> "SAFE"
                1 -> "SPAM"
                2 -> "PHISHING"
                else -> "UNKNOWN"
            }

        } catch (e: Exception) {
            Log.e("ONNX_ERROR", "FAILED: " + e.message)
            e.printStackTrace()
            return "UNKNOWN"
        }
    }
}