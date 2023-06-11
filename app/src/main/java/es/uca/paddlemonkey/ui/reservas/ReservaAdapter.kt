package es.uca.paddlemonkey.ui.reservas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import es.uca.paddlemonkey.FormularioActivity
import es.uca.paddlemonkey.R
import es.uca.paddlemonkey.ui.Reserva

class ReservaAdapter(private var reservas : List<Reserva>) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>(){
    private lateinit var context: Context
    class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val PistaTextView: TextView = itemView.findViewById(R.id.pistaReserva)
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreReserva)
        val apellTextView: TextView = itemView.findViewById(R.id.apellReserva)
        val fechainiTextView: TextView = itemView.findViewById(R.id.fecha_inicio)
        val fechafinTextView: TextView = itemView.findViewById(R.id.fecha_fin)
        val fechaTextView: TextView = itemView.findViewById(R.id.fecha)
        val asistentesTextView: TextView = itemView.findViewById(R.id.asistentes)
        val botonDetallesReserva: Button = itemView.findViewById(R.id.botonDetallesReserva)
    }

    constructor():this(emptyList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.list_item_reserva, parent, false)
        return ReservaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reservaActual = reservas[position]

        holder.PistaTextView.text = reservaActual.nombrePista
        holder.nombreTextView.text = reservaActual.nombre
        holder.apellTextView.text = reservaActual.apellidos
        holder.fechainiTextView.text = reservaActual.fecha_inicio
        holder.fechafinTextView.text = reservaActual.fecha_fin
        holder.fechaTextView.text = reservaActual.fecha
        holder.asistentesTextView.text = reservaActual.asistentes

        holder.botonDetallesReserva.setOnClickListener {
            val intent = Intent(context, DetallesReservaActivity::class.java)
            intent.putExtra("_id", reservaActual._id)
            intent.putExtra("pista", reservaActual.nombrePista)
            intent.putExtra("DNI", reservaActual.DNI)
            intent.putExtra("nombre", reservaActual.nombre)
            intent.putExtra("apellidos", reservaActual.apellidos)
            intent.putExtra("telefono", reservaActual.telefono)
            intent.putExtra("email", reservaActual.email)
            intent.putExtra("horaInicio", reservaActual.fecha_inicio)
            intent.putExtra("horaFin", reservaActual.fecha_fin)
            intent.putExtra("fecha", reservaActual.fecha)
            intent.putExtra("asistentes", reservaActual.asistentes)
            intent.putExtra("comentario", reservaActual.comentario)
            context.startActivity(intent)
        }


    }

    override fun getItemCount() = reservas.size

    fun setReservas(reservas: List<Reserva>) {
        this.reservas = reservas
    }
}