package gb.com.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import gb.com.databinding.FragmentImageConverterBinding
import gb.com.mvp.model.ImageModel
import gb.com.mvp.presenter.converter.IImageConverterPresenter
import gb.com.mvp.presenter.converter.ImageConverterPresenter
import gb.com.mvp.view.IImageConverterView
import gb.com.utility.PICK_IMAGE_REQUEST
import kotlinx.android.synthetic.main.fragment_image_converter.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ImageConverterFragment: MvpAppCompatFragment(), IImageConverterView {

    private var _binding: FragmentImageConverterBinding? = null
    private val binding get() = _binding!!

    private val presenter: IImageConverterPresenter by moxyPresenter {
        ImageConverterPresenter(this, ImageModel(context))
    }

    companion object {
        fun newInstance() = ImageConverterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentImageConverterBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonDownload.setOnClickListener {
            presenter.openGallery()
        }

        binding.buttonConvert.setOnClickListener {
            convertImage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showError(message: String) {
        Toast.makeText(context, "Unexpected error $message", Toast.LENGTH_LONG).show()
    }

    override fun showImage(bitmap: Bitmap) {
        binding.imageView.setImageBitmap(bitmap)
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    override fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*.jpg")
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleActivityResult(requestCode, resultCode,data)
    }

    private fun convertImage() {
        val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        if (bitmap == null) {
            showMessage("No image to convert")
            return
        } else {
            presenter.convertToPng(bitmap)
        }
    }
}