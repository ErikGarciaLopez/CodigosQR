package com.example.codigosqr

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.codigosqr.databinding.FragmentCrearQRBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class CrearQR : Fragment() {

    private var _binding: FragmentCrearQRBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearQRBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGenerarVCard.setOnClickListener {
            generarVCardQR()
        }

    }

    private fun generarVCardQR() {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val correo = binding.etCorreo.text.toString().trim()

        // Validaciones
        if (!validarDatos(nombre, apellido, telefono, correo)) return

        val nombreCompleto = "$nombre $apellido"
        val vCardContent = crearVCard(nombreCompleto, telefono, correo)

        try {
            val qrBitmap = generarQRBitmap(vCardContent)
            binding.ivQRCode.setImageBitmap(qrBitmap)
            binding.ivQRCode.visibility = View.VISIBLE  // Hacer visible el ImageView
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al generar QR", Toast.LENGTH_SHORT).show()
        }

    }

    private fun validarDatos(
        nombre: String,
        apellido: String,
        telefono: String,
        correo: String
    ): Boolean {
        var esValido = true

        // Validar nombre
        if (nombre.isEmpty()) {
            binding.etNombre.error = "Nombre es obligatorio"
            esValido = false
        }

        // Validar apellido
        if (apellido.isEmpty()) {
            binding.etApellido.error = "Apellido es obligatorio"
            esValido = false
        }

        // Validar teléfono (10 dígitos)
        if (telefono.isEmpty() || !telefono.matches("\\d{10}".toRegex())) {
            binding.etTelefono.error = "Teléfono debe ser de 10 dígitos"
            esValido = false
        }

        // Validar correo
        if (correo.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.etCorreo.error = "Correo inválido"
            esValido = false
        }

        return esValido
    }

    private fun crearVCard(
        nombreCompleto: String,
        telefono: String,
        correo: String
    ): String {
        return """
            BEGIN:VCARD
            VERSION:3.0
            FN:$nombreCompleto
            TEL:$telefono
            ${if (correo.isNotEmpty()) "EMAIL:$correo" else ""}
            END:VCARD
        """.trimIndent()
    }

    private fun generarQRBitmap(contenido: String): Bitmap {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix: BitMatrix = multiFormatWriter.encode(
            contenido,
            BarcodeFormat.QR_CODE,
            400,
            400
        )
        return BarcodeEncoder().createBitmap(bitMatrix)
    }

    override fun onPause() {
        super.onPause()
        //TODO
    }

    override fun onResume() {
        super.onResume()
        //TODO
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}