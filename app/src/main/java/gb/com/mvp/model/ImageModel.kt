package gb.com.mvp.model

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.rxjava3.core.Observable
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ImageModel(
    private val context: Context?
) {

    fun loadImage(uri: Uri?): Observable<Bitmap> {
        return Observable.create { emitter ->

            var inputStream: InputStream? = null

             try {
                inputStream = uri?.let { context?.contentResolver?.openInputStream(it) }
                 val bitmap = BitmapFactory.decodeStream(inputStream)
                 if (bitmap != null) {
                     emitter.onNext(bitmap)
                     emitter.onComplete()
                 } else {
                     emitter.onError(Exception("Could not load image"))
                 }
             } catch (error: IOException) {
                 emitter.onError(error)
             } finally {
                 inputStream?.close()
             }
        }
    }

    fun convertToPng(bitmap: Bitmap): Observable<Bitmap> {
        return Observable.create { emitter ->
            try {
                Thread.sleep(5000)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                val byteArray = outputStream.toByteArray()
                val pngBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                emitter.onNext(pngBitmap)
                emitter.onComplete()
                outputStream.flush()
                outputStream.close()

            } catch (error: IOException) {
                emitter.onError(error)

            } catch (error: InterruptedException) {
                error.printStackTrace()
            }
        }
    }

    fun saveImage(context: Context?, bitmap: Bitmap): Observable<String> {
        return Observable.create { emitter ->

            val contentResolver = context?.contentResolver

            val displayName = "image_${System.currentTimeMillis()}.png"
            val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val imageDetails = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            }
            val imageUriWithDisplayName = contentResolver?.insert(imageUri, imageDetails)
            if(imageUriWithDisplayName != null) {

                val outputStream = contentResolver.openOutputStream(imageUriWithDisplayName)

                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    emitter.onNext(displayName)
                    emitter.onComplete()

                } else {
                    emitter.onError(Exception("Unable to open output stream"))
                }
            } else {
                emitter.onError(Exception("Could not save image"))
            }
        }
    }
}