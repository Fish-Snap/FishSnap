package com.example.fishsnap.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FishDetectionModel(context: Context) {

    private val interpreter: Interpreter
    private val labels: List<String>
    private val inputImageWidth = 320
    private val inputImageHeight = 320
    private val inputImageChannels = 3
    private val modelInputSize = inputImageWidth * inputImageHeight * inputImageChannels * 4

    init {
        val model = FileUtil.loadMappedFile(context, "model_fish_detection.tflite")
        interpreter = Interpreter(model)
        labels = listOf(
            "Ikan_Bawal_Hitam", "Ikan_Cipa_Cipa", "Ikan_Kembung", "Ikan_Kenyar", "Ikan_Kuwe",
            "Ikan_Salem", "Ikan_Sebelah", "Ikan_Selar_Bulat", "Ikan_Tenggiri_Papan", "Ikan_Tuna"
        )
    }

    private fun resizeImage(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        byteBuffer.rewind()
        for (pixel in intValues) {
            val r = (pixel shr 16 and 0xFF).toFloat()
            val g = (pixel shr 8 and 0xFF).toFloat()
            val b = (pixel and 0xFF).toFloat()
            byteBuffer.putFloat(r / 255.0f)
            byteBuffer.putFloat(g / 255.0f)
            byteBuffer.putFloat(b / 255.0f)
        }
        return byteBuffer
    }

    fun detectFish(bitmap: Bitmap): Pair<String, Float> {
        val resizedBitmap = resizeImage(bitmap)
        val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, labels.size), DataType.FLOAT32)
        interpreter.run(byteBuffer, outputBuffer.buffer.rewind())

        val outputArray = outputBuffer.floatArray
        val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1

        return Pair(labels[maxIndex], outputArray[maxIndex])
    }

    fun drawBoundingBox(bitmap: Bitmap, confidence: Float): Bitmap {
        val tempBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(tempBitmap)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f

        val textPaint = Paint()
        textPaint.color = Color.RED
        textPaint.textSize = 40f

        val rect = RectF(50f, 50f, tempBitmap.width - 50f, tempBitmap.height - 50f)
        canvas.drawRect(rect, paint)
        canvas.drawText("Confidence: ${String.format("%.2f", confidence * 100)}%", 50f, 50f, textPaint)

        return tempBitmap
    }
}