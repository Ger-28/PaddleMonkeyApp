package es.uca.paddlemonkey

import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import es.uca.paddlemonkey.ui.Reserva
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.content.TextContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ApiService {

    val url = "http://192.168.1.109:8080"


    public suspend fun recuperarReservas(): ArrayList<Reserva> {

        // Creamos una instancia del cliente HTTP
        var reservas: ArrayList<Reserva>
        var peticion = false
        try {
            val response = HttpClient(Android) {
            }.get<String>(url + "/reservas")

            withContext(Dispatchers.Main) {
                reservas = ConvertToReservas(response)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                reservas = ArrayList<Reserva>()
            }
        }

        return reservas
    }

    suspend fun ConvertToReservas(reservas: String): ArrayList<Reserva> {
        val arrayJSONReservas = JSONArray(reservas)

        val arrayStringReservas = ArrayList<Reserva>()

        for (i in 0 until arrayJSONReservas.length()) {
            val jsonObject = arrayJSONReservas.getJSONObject(i)

            val usuarioObject = jsonObject.getJSONObject("usuario")

            val reserva = Reserva(
                jsonObject.getString("_id"),
                jsonObject.getString("pista"),
                jsonObject.getString("DNI"),
                usuarioObject.getString("nombre"),
                usuarioObject.getString("apellidos"),
                jsonObject.getString("telefono"),
                jsonObject.getString("email"),
                jsonObject.getString("horaInicio"),
                jsonObject.getString("horaFin"),
                jsonObject.getString("fecha"),
                jsonObject.getString("asistentes"),
                jsonObject.getString("comentario")
            )
            arrayStringReservas.add(reserva)
        }

        return arrayStringReservas
    }


    public suspend fun anadirReserva(jsonData: JSONObject) {
        var reservas: String
        try {
            val response = HttpClient(Android) {
            }.post<String>(url + "/reservas") {

                body = FormDataContent(Parameters.build {
                    append("pista", jsonData.getString("pista"))
                    append("DNI", jsonData.getString("DNI"))
                    append("usuario[nombre]", jsonData.getJSONObject("usuario").getString("nombre"))
                    append("usuario[apellidos]", jsonData.getJSONObject("usuario").getString("apellidos"))
                    append("telefono", jsonData.getString("telefono"))
                    append("email", jsonData.getString("email"))
                    append("horaInicio", jsonData.getString("horaInicio"))
                    append("horaFin", jsonData.getString("horaFin"))
                    append("fecha", jsonData.getString("fecha"))
                    append("asistentes", jsonData.getString("asistentes"))
                    append("comentario", jsonData.getString("comentario"))
                })
            }

            withContext(Dispatchers.Main) {
                reservas = response
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                reservas = e.message.toString()
            }
        }
    }

    public suspend fun eliminarReserva(id: String) {
        var reservas: String

        try {
            val response = HttpClient(Android).delete<String>(url + "/reservas/" + id) {
                body = TextContent("", ContentType.Application.Json)
            }

            withContext(Dispatchers.Main) {
                reservas = response
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                reservas = e.message.toString()
            }
        }
    }

    suspend fun actualizarReserva(id: String, reserva: JSONObject) {
        try {
            val response = HttpClient(Android) {
            }.put<String>(url + "/reservas/" + id) {
                body = FormDataContent(Parameters.build {
                    append("pista", reserva.getString("pista"))
                    append("DNI", reserva.getString("DNI"))
                    append("usuario[nombre]", reserva.getJSONObject("usuario").getString("nombre"))
                    append("usuario[apellidos]", reserva.getJSONObject("usuario").getString("apellidos"))
                    append("telefono", reserva.getString("telefono"))
                    append("email", reserva.getString("email"))
                    append("horaInicio", reserva.getString("horaInicio"))
                    append("horaFin", reserva.getString("horaFin"))
                    append("fecha", reserva.getString("fecha"))
                    append("asistentes", reserva.getString("asistentes"))
                    append("comentario", reserva.getString("comentario"))
                })
            }

            withContext(Dispatchers.Main) {
                // Realiza alguna acción con la respuesta, si es necesario
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                // Maneja la excepción, si es necesario
            }
        }
    }

}