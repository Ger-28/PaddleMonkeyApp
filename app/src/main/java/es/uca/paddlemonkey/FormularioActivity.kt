package es.uca.paddlemonkey

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class FormularioActivity : AppCompatActivity() {
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
                if (validarCampos()) {
                    enviarFormulario()
                } else {
                    mostrarNotificacion("Por favor, revise los campos ingresados.")
                }
            } else {
                mostrarNotificacion("Por favor, complete todos los campos.")
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

        }
        return true
    }

    private fun validarCampos(): Boolean {
        val nombre = inputNombre.text.toString().trim()
        val apellidos = inputApellidos.text.toString().trim()
        val dni = inputDNI.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val fecha = inputFecha.text.toString().trim()
        val horaInicio = inputHoraIni.text.toString().trim()
        val horaFin = inputHoraFin.text.toString().trim()

        if (nombre.length < 2) {
            mostrarNotificacion("El nombre debe tener al menos 2 caracteres.")
            return false
        }

        if (apellidos.length < 2) {
            mostrarNotificacion("Los apellidos deben tener al menos 2 caracteres.")
            return false
        }

        if (!isValidEmail(email)) {
            mostrarNotificacion("Por favor, ingrese un correo electrónico válido.")
            return false
        }

        val fechaSeleccionada = obtenerFechaDesdeTexto(fecha)
        if (fechaSeleccionada <= obtenerFechaActual()) {
            mostrarNotificacion("La fecha seleccionada debe ser superior a la fecha actual.")
            return false
        }

        val horaInicioSeleccionada = obtenerHoraDesdeTexto(horaInicio)
        val horaFinSeleccionada = obtenerHoraDesdeTexto(horaFin)
        if (horaFinSeleccionada < horaInicioSeleccionada.plusHours(1)) {
            mostrarNotificacion("La hora fin debe ser al menos 1 hora después de la hora inicio.")
            return false
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

        // Realizar la operación POST con el objeto JSON

        val job = GlobalScope.launch {
            try {
                val response = ApiService().anadirReserva(jsonObject)

                withContext(Dispatchers.Main) {
                    // Mostrar mensaje de éxito
                    mostrarNotificacion("Formulario enviado correctamente")
                    val intent = Intent(this@FormularioActivity, MainActivity::class.java)
                    intent.putExtra("accion", "actualizacionInsercion")
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Mostrar mensaje de error
                    mostrarNotificacion("Error al enviar el formulario: ${e.message}")
                }
            }
        }
    }

    private fun mostrarNotificacion(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}




