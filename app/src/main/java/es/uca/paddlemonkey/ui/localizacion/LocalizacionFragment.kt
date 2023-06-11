package es.uca.paddlemonkey.ui.localizacion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import es.uca.paddlemonkey.MapsActivity
import es.uca.paddlemonkey.R
import es.uca.paddlemonkey.databinding.FragmentLocalizacionBinding

class LocalizacionFragment : Fragment() {

    private var _binding: FragmentLocalizacionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var botonAutobus: Button
    private lateinit var botonTren: Button
    private lateinit var botonMapa: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLocalizacionBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botonAutobus = view.findViewById(R.id.buttonAutobuses)
        botonAutobus.setOnClickListener{
            val url = "https://www.tgcomes.es/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        botonTren = view.findViewById(R.id.buttonTrenes)
        botonTren.setOnClickListener{
            val url = "https://www.renfe.com/es/es/cercanias/cercanias-cadiz/horarios"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        botonMapa = view.findViewById(R.id.botonMapa)
        botonMapa.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}