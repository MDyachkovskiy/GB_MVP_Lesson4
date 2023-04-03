package gb.com.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import gb.com.databinding.FragmentImageConverterBinding
import moxy.MvpAppCompatFragment

class ImageConverterFragment: MvpAppCompatFragment() {

    private var _binding: FragmentImageConverterBinding? = null
    private val binding get() = _binding!!

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}