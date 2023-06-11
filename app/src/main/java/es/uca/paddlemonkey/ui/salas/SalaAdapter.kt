package es.uca.paddlemonkey.ui.salas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.uca.paddlemonkey.R
import es.uca.paddlemonkey.ui.Pista

class SalaAdapter(private val pistas : List<Pista>) : RecyclerView.Adapter<SalaAdapter.SalaViewHolder>(){
    class SalaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nombreTextView: TextView = itemView.findViewById(R.id.nombrePista)
        val capacidadTextView: TextView = itemView.findViewById(R.id.capacidadPistaTabla)
        val cubiertaTextView: TextView = itemView.findViewById(R.id.cubiertaPistaTabla)
        val materialTextView: TextView = itemView.findViewById(R.id.materialPistaTabla)
        val precioTextView: TextView = itemView.findViewById(R.id.precioPistaTabla)

        val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionPista)
        val fotoImageView: ImageView = itemView.findViewById(R.id.fotoPista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_pista, parent, false)
        return SalaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SalaViewHolder, position: Int) {
        val currentPista = pistas[position]
        val caracteristicasSplit = currentPista.caracteristicas.split("\n")

        holder.nombreTextView.text = currentPista.nombre

        holder.capacidadTextView.text = caracteristicasSplit[0]
        holder.cubiertaTextView.text = caracteristicasSplit[1]
        holder.materialTextView.text = caracteristicasSplit[2]
        holder.precioTextView.text = caracteristicasSplit[3]

        holder.descripcionTextView.text = currentPista.descripcion

        holder.fotoImageView.setImageBitmap(currentPista.foto)
    }

    override fun getItemCount() = pistas.size
}