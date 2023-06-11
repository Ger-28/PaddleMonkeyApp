package es.uca.paddlemonkey.ui.reservas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.uca.paddlemonkey.ApiService
import es.uca.paddlemonkey.FormularioActivity
import es.uca.paddlemonkey.R
import es.uca.paddlemonkey.databinding.FragmentReservasBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservasFragment : Fragment() {

    private var _binding: FragmentReservasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservaAdapter
    private lateinit var viewModel: ReservasViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        adapter = ReservaAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botonFormulario: Button = view.findViewById(R.id.botonFormulario)
        //val botonBorrarReserva: Button = view.findViewById(R.id.botonBorrarReserva)
        viewModel = ViewModelProvider(this).get(ReservasViewModel::class.java)

        botonFormulario.setOnClickListener{
            val intent = Intent(requireContext(), FormularioActivity::class.java)
            startActivity(intent)
        }

        var job = GlobalScope.launch(Dispatchers.IO) {

            // Realizar una tarea que puede tardar un tiempo, como una operación de red o base de datos
            val result = ApiService().recuperarReservas()

            // Actualizar la interfaz de usuario con el resultado en el hilo principal
            withContext(Dispatchers.Main) {
                viewModel.getReservas().observe(viewLifecycleOwner) { reservas ->
                    adapter.setReservas(reservas)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}