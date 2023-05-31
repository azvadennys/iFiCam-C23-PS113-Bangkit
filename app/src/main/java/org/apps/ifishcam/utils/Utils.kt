package org.apps.ifishcam.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(currentDate: String): String? {
    val currentFormat = "yyyy-MM-dd'T'hh:mm:ss'Z'"
    val targetFormat = "dd MMM yyyy | HH:mm"
    val timezone = "GMT"
    val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
    currentDf.timeZone = TimeZone.getTimeZone(timezone)
    val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
    var targetDate: String? = null
    try {
        val date = currentDf.parse(currentDate)
        if (date != null) {
            targetDate = targetDf.format(date)
        }
    } catch (ex: ParseException) {
        ex.printStackTrace()
    }
    return targetDate
}

val timeStamp: String = SimpleDateFormat(
    "dd-MMM-yyyy",
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

private const val MAXIMAL_SIZE = 1000000
fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}


