package es.uca.paddlemonkey

import android.Manifest
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class UpdatedFormularioActivity : AppCompatActivity() {
    private lateinit var selectedSala: String
    private lateinit var inputNombre: EditText
    private lateinit var inputApellidos: EditText
    private lateinit var inputDNI: EditText
    private lateinit var inputPhone: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputFecha: EditText
    private lateinit var inputHoraIni: EditText
    private lateinit var inputHoraFin: EditText
    private lateinit var inputAsistentes: EditText
    private lateinit var inputComentario: EditText
    private lateinit var botonEnviar: Button

    private val NOTIFICATION_CHANNEL_ID = "reserva_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        selectedSala = findViewById<Spinner>(R.id.spSalas).selectedItem.toString()
        inputNombre = findViewById(R.id.inputNombre)
        inputApellidos = findViewById(R.id.inputApellidos)
        inputDNI = findViewById(R.id.inputDNI)
        inputPhone = findViewById(R.id.inputPhone)
        inputEmail = findViewById(R.id.inputEmail)
        inputFecha = findViewById(R.id.inputFecha)
        inputHoraIni = findViewById(R.id.inputHoraIni)
        inputHoraFin = findViewById(R.id.inputHoraFin)
        inputAsistentes = findViewById(R.id.inputAsistentes)
        inputComentario = findViewById(R.id.inputComentario)
        botonEnviar = findViewById(R.id.botonFormulario)

        inputFecha.inputType = InputType.TYPE_NULL
        inputHoraIni.inputType = InputType.TYPE_NULL
        inputHoraFin.inputType = InputType.TYPE_NULL

        inputFecha.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                mostrarCalendario()
            }
        }

        inputFecha.setOnClickListener {
            mostrarCalendario()
        }

        inputHoraIni.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                mostrarSelectorHora(inputHoraIni)
            }
        }

        inputHoraIni.setOnClickListener {
            mostrarSelectorHora(inputHoraIni)
        }

        inputHoraFin.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                mostrarSelectorHora(inputHoraFin)
            }
        }

        inputHoraFin.setOnClickListener {
            mostrarSelectorHora(inputHoraFin)
        }

        botonEnviar.setOnClickListener {
            if (verificarCamposLlenos()) {
                enviarFormulario()
            } else {
                Toast.makeText(this, "Por favor, revise todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarCalendario() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year)
                inputFecha.setText(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePickerDialog.show()
    }

    private fun mostrarSelectorHora(editText: EditText) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                editText.setText(selectedTime)
            },
            currentHour,
            currentMinute,
            true
        )

        timePickerDialog.show()
    }

    private fun verificarCamposLlenos(): Boolean {
        val campos = arrayOf(
            inputNombre,
            inputApellidos,
            inputDNI,
            inputPhone,
            inputEmail,
            inputFecha,
            inputHoraIni,
            inputHoraFin,
            inputAsistentes,
            inputComentario
        )

        for (campo in campos) {
            val texto = campo.text.toString().trim()

            if (texto.isEmpty()) {
                return false
            }

            // Validar que los nombres y apellidos tengan al menos 2 caracteres
            if ((campo == inputNombre || campo == inputApellidos) && texto.length < 2) {
                return false
            }

            // Validar el formato del correo electrónico
            if (campo == inputEmail && !isValidEmail(texto)) {
                return false
            }

            // Validar que la fecha sea superior a la de hoy
            if (campo == inputFecha) {
                val fechaSeleccionada = obtenerFechaDesdeTexto(texto)
                if (fechaSeleccionada <= obtenerFechaActual()) {
                    return false
                }
            }

            // Validar que la hora fin sea superior en al menos 1 hora a la hora inicio
            if (campo == inputHoraFin) {
                val horaInicio = obtenerHoraDesdeTexto(inputHoraIni.text.toString())
                val horaFin = obtenerHoraDesdeTexto(texto)
                if (horaFin < horaInicio.plusHours(1)) {
                    return false
                }
            }
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    private fun obtenerFechaDesdeTexto(texto: String): LocalDate {
        val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(texto, formatoFecha)
    }

    private fun obtenerFechaActual(): LocalDate {
        return LocalDate.now()
    }

    private fun obtenerHoraDesdeTexto(texto: String): LocalTime {
        val formatoHora = DateTimeFormatter.ofPattern("HH:mm")
        return LocalTime.parse(texto, formatoHora)
    }


    private fun enviarFormulario() {
        val reserva = intent.extras
        val sala = selectedSala
        val nombre = "Nombre cliente: " + inputNombre.text.toString()
        val apellidos = "Apellidos cliente: " + inputApellidos.text.toString()
        val dni = "DNI: " + inputDNI.text.toString()
        val telefono = "Telefono: " + inputPhone.text.toString()
        val email = "Email: " + inputEmail.text.toString()
        val fecha = "Fecha de la reserva: " + inputFecha.text.toString()
        val horaInicio = "Hora inicio: " + inputHoraIni.text.toString()
        val horaFin = "Hora fin: " + inputHoraFin.text.toString()
        val asistentes = "Numero de asistentes: " + inputAsistentes.text.toString()
        val comentario = "Comentario: " + inputComentario.text.toString()

        // Crear el objeto JSON con los datos del formulario
        val jsonObject = JSONObject()
        jsonObject.put("pista", sala)
        jsonObject.put("DNI", dni)

        val usuarioJson = JSONObject()
        usuarioJson.put("nombre", nombre)
        usuarioJson.put("apellidos", apellidos)
        jsonObject.put("usuario", usuarioJson)

        jsonObject.put("telefono", telefono)
        jsonObject.put("email", email)
        jsonObject.put("horaInicio", horaInicio)
        jsonObject.put("horaFin", horaFin)
        jsonObject.put("fecha", fecha)
        jsonObject.put("asistentes", asistentes)
        jsonObject.put("comentario", comentario)

        // Realizar la operación PUT con el objeto JSON

        val job = GlobalScope.launch {
            try {
                val response =
                    reserva?.getString("_id")
                        ?.let { ApiService().actualizarReserva(it, jsonObject) }

                withContext(Dispatchers.Main) {
                    // Mostrar mensaje de éxito
                    NotificacionBarraEstado("Formulario actualizado correctamente")
                    val intent = Intent(this@UpdatedFormularioActivity, MainActivity::class.java)
                    intent.putExtra("accion", "actualizacionInsercion")
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Mostrar mensaje de error
                    NotificacionBarraEstado("Error al actualizar el formulario: ${e.message}")
                }
            }
        }
    }

    private fun NotificacionBarraEstado(mensaje: String) {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle("Notificación de Reserva")
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            createNotificationChannel()
            if (ActivityCompat.checkSelfPermission(
                    this@UpdatedFormularioActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notify(1, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Reserva Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Descripción del canal de notificación"
            channel.enableLights(true)
            channel.lightColor = Color.RED

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}