package com.example.codigosqr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.codigosqr.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var cameraPermissionGranted = false
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if (isGranted){
            //Tenemos el permiso
            actionPermissionGranted()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                //Le puedo dar razones adicionales
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.permiso_requerido))
                    .setMessage(getString(R.string.se_requiere_el_permiso_solamente_para_leer_codigos_QR))
                    .setPositiveButton(getString(R.string.entendido)){ _, _ ->
                        updateOrRequestPermission()
                    }
                    .setNegativeButton(getString(R.string.salir)){ dialog, _ ->
                        requireActivity().finish()
                    }
                    .create()
                    .show()

            } else {
                //De plano negó el permiso permanentemente
                Toast.makeText(
                    requireContext(),
                    getString(R.string.el_permiso_se_nego_permanentemente),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navegacion al Scanner
        binding.ivCamara.setOnClickListener {
            updateOrRequestPermission()
        }

        //Navegacion a CrearQR
        binding.btnCreateCode.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_crearQR)
        }
    }

    private fun updateOrRequestPermission(){

        //Revisar si se tiene el permiso
        cameraPermissionGranted =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        if (!cameraPermissionGranted){
            //Pedir el permiso
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            //Tenemos el permiso
            actionPermissionGranted()
        }
    }

    private fun actionPermissionGranted () {
        findNavController().navigate(R.id.action_mainFragment_to_scannerFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}