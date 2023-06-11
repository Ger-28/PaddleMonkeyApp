package es.uca.paddlemonkey.ui.reservas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.uca.paddlemonkey.ApiService
import es.uca.paddlemonkey.ui.Reserva
import org.json.JSONObject

class ReservasViewModel : ViewModel() {

    private var ReservasLiveData = MutableLiveData<List<Reserva>>()

    init {
        // Lista de mascotas
        val reservas = listOf(
            Reserva("Luna", "Golden Retriever", "", "", "", "","", "", "","","", "")
        )
        ReservasLiveData.value = reservas
    }

    suspend fun getReservas(): LiveData<List<Reserva>> {
        val ReservasLiveData = MutableLiveData<List<Reserva>>()
        ReservasLiveData.value = ApiService().recuperarReservas()
        return ReservasLiveData
    }

}