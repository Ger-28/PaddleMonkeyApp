package es.uca.paddlemonkey.ui.reservas

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar
import es.uca.paddlemonkey.ApiService
import es.uca.paddlemonkey.MainActivity
import es.uca.paddlemonkey.R
import es.uca.paddlemonkey.UpdatedFormularioActivity
import es.uca.paddlemonkey.ui.Reserva
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DetallesReservaActivity : AppCompatActivity() {


    private lateinit var reserva: Reserva

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_reserva)

        val reserva = intent.extras

        val pistaReservaTextView = findViewById<TextView>(R.id.pistaReservaDetalles)
        pistaReservaTextView.text = reserva?.getString("pista")

        val nombreReservaTextView = findViewById<TextView>(R.id.nombreReservaDetalles)
        nombreReservaTextView.text = reserva?.getString("nombre")

        val apellReservaTextView = findViewById<TextView>(R.id.apellReservaDetalles)
        apellReservaTextView.text = reserva?.getString("apellidos")

        val dniReservaTextView = findViewById<TextView>(R.id.DNIReservaDetalles)
        dniReservaTextView.text = reserva?.getString("DNI")

        val tlfReservaTextView = findViewById<TextView>(R.id.TlfReservaDetalles)
        tlfReservaTextView.text = reserva?.getString("telefono")

        val emailReservaTextView = findViewById<TextView>(R.id.EmailReservaDetalles)
        emailReservaTextView.text = reserva?.getString("email")

        val fechaInicioTextView = findViewById<TextView>(R.id.fecha_inicioDetalles)
        fechaInicioTextView.text = reserva?.getString("horaInicio")

        val fechaFinTextView = findViewById<TextView>(R.id.fecha_finDetalles)
        fechaFinTextView.text = reserva?.getString("horaFin")

        val fechaTextView = findViewById<TextView>(R.id.fechaDetalles)
        fechaTextView.text = reserva?.getString("fecha")

        val asistentesTextView = findViewById<TextView>(R.id.asistentesDetalles)
        asistentesTextView.text = reserva?.getString("asistentes")

        val comentarioTextView = findViewById<TextView>(R.id.comentarioDetalles)
        comentarioTextView.text = reserva?.getString("comentario")

        val reservaJson = JSONObject()
        val usuarioJson = JSONObject()

        reservaJson.put("pista", reserva?.getString("pista"))
        reservaJson.put("DNI", reserva?.getString("DNI"))
        usuarioJson.put("nombre", reserva?.getString("nombre"))
        usuarioJson.put("apellidos", reserva?.getString("apellidos"))
        reservaJson.put("usuario", usuarioJson)
        reservaJson.put("telefono", reserva?.getString("telefono"))
        reservaJson.put("email", reserva?.getString("email"))
        reservaJson.put("horaInicio", reserva?.getString("horaInicio"))
        reservaJson.put("horaFin", reserva?.getString("horaFin"))
        reservaJson.put("fecha", reserva?.getString("fecha"))
        reservaJson.put("asistentes", reserva?.getString("asistentes"))
        reservaJson.put("comentario", reserva?.getString("comentario"))

        val botonEliminarReserva = findViewById<Button>(R.id.botonEliminarReserva)

        botonEliminarReserva.setOnClickListener {
            val job = GlobalScope.launch {
                try {
                    val response = reserva?.getString("_id")
                        ?.let { it1 -> ApiService().eliminarReserva(it1) }

                    withContext(Dispatchers.Main) {
                        // Mostrar mensaje de éxito
                        val intent = Intent(this@DetallesReservaActivity, MainActivity::class.java)
                        intent.putExtra("accion", "eliminar")
                        startActivity(intent)
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        // Mostrar mensaje de error
                        NotificacionSnackBar("Error al eliminar la reserva: ${e.message}")
                    }
                }
            }
        }

        val botonActualizarReserva = findViewById<Button>(R.id.botonActualizarReserva)
        botonActualizarReserva.setOnClickListener {

            val intent = Intent(this, UpdatedFormularioActivity::class.java)
            intent.putExtra("_id", reserva?.getString("_id"))
            intent.putExtra("pista", reserva?.getString("pista"))
            intent.putExtra("DNI", reserva?.getString("DNI"))
            intent.putExtra("nombre", reserva?.getString("nombre"))
            intent.putExtra("apellidos", reserva?.getString("apellidos"))
            intent.putExtra("telefono", reserva?.getString("telefono"))
            intent.putExtra("email", reserva?.getString("email"))
            intent.putExtra("horaInicio", reserva?.getString("horaInicio"))
            intent.putExtra("horaFin", reserva?.getString("horaFin"))
            intent.putExtra("fecha", reserva?.getString("fecha"))
            intent.putExtra("asistentes", reserva?.getString("asistentes"))
            intent.putExtra("comentario", reserva?.getString("comentario"))
            startActivity(intent)

        }
    }
    private fun NotificacionSnackBar(mensaje: String) {
        val rootView: View = findViewById(android.R.id.content)
        Snackbar.make(rootView, mensaje, Snackbar.LENGTH_SHORT).show()

    }



}

