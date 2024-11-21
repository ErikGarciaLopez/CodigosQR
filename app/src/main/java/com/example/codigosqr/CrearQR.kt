package com.example.codigosqr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codigosqr.databinding.FragmentCrearQRBinding

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