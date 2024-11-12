package com.example.codigosqr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.codigosqr.databinding.FragmentScannerBinding

class ScannerFragment : Fragment() {

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScannerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cbvScanner.decodeContinuous { result ->

            Toast.makeText(
                requireContext(),
                "Texto de QR leido: ${result.text}",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    override fun onPause() {
        super.onPause()
        binding.cbvScanner.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.cbvScanner.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}