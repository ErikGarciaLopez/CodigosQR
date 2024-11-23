package com.example.codigosqr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.codigosqr.databinding.FragmentScannerBinding
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Pattern

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

            findNavController().navigate(R.id.action_scannerFragment_to_mainFragment)
            binding.cbvScanner.pause()

            when {
                isValidUrl(result.text) -> handleUrl(result.text)
                isValidVCard(result.text) -> handleVCard(result.text)
                else -> showInvalidQrMessage()
            }
        }
    }

    private fun isValidUrl(text: String): Boolean {
        return try {
            URL(text)
            true
        } catch (e: MalformedURLException) {
            false
        }
    }

    private fun isValidVCard(text: String): Boolean {
        // Verifica si el texto contiene los campos requeridos de una vCard
        return text.contains("BEGIN:VCARD") &&
                text.contains("VERSION:3.0") &&
                text.contains("FN:") &&
                text.contains("TEL:") &&
                text.contains("END:VCARD")
    }

    private fun handleUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }

    private fun handleVCard(vcard: String) {
        try {
            // Extraer la información de la vCard
            val name = extractVCardField(vcard, "FN:")
            val phone = extractVCardField(vcard, "TEL:")
            val email = extractVCardField(vcard, "EMAIL:")

            if (name != null && phone != null) {
                // Crear intent para agregar contacto
                val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
                    type = ContactsContract.RawContacts.CONTENT_TYPE
                    putExtra(ContactsContract.Intents.Insert.NAME, name)
                    putExtra(ContactsContract.Intents.Insert.PHONE, phone)
                    if (!email.isNullOrEmpty()) {
                        putExtra(ContactsContract.Intents.Insert.EMAIL, email)
                    }
                }
                startActivity(intent)
            } else {
                showInvalidQrMessage()
            }
        } catch (e: Exception) {
            showInvalidQrMessage()
        }
    }

    private fun extractVCardField(vcard: String, field: String): String? {
        val pattern = "$field([^\\s]+[^\\n]*)"
        val matcher = Pattern.compile(pattern).matcher(vcard)
        return if (matcher.find()) matcher.group(1)?.trim() else null
    }

    private fun showInvalidQrMessage() {
        Toast.makeText(
            requireContext(),
            "QR no válido. Debe ser una URL o una tarjeta de contacto",
            Toast.LENGTH_SHORT
        ).show()
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