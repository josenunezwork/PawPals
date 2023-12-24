package com.bignerdranch.android.animalgallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.animalgallery.databinding.FragmentAnimalPageBinding

class AnimalPageFragment : Fragment() {

    private val args: AnimalPageFragmentArgs by navArgs()

    private var _binding: FragmentAnimalPageBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalPageBinding.inflate(
            inflater,
            container,
            false
        )

        binding.apply {
            progressBar.max = 100
            webView.apply {

                settings.javaScriptEnabled = true

                webViewClient = WebViewClient()
                loadUrl(args.animalPageUri.toString())

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if (newProgress == 100) {
                            progressBar.visibility = View.GONE
                        } else {
                            progressBar.visibility = View.VISIBLE
                            progressBar.progress = newProgress
                        }
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        val parent = requireActivity() as AppCompatActivity
                        parent.supportActionBar?.subtitle = title
                    }
                }
            }


        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.apply {
            webView.apply{
                val parent = requireActivity() as AppCompatActivity
                parent.supportActionBar?.subtitle = null
            }
        }
        _binding = null
    }

}