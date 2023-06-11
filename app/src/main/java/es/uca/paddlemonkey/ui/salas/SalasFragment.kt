package es.uca.paddlemonkey.ui.salas

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import es.uca.paddlemonkey.FormularioActivity
import es.uca.paddlemonkey.R
import es.uca.paddlemonkey.databinding.FragmentSalasBinding
import es.uca.paddlemonkey.ui.Pista
import java.io.File
import java.io.FileOutputStream

class SalasFragment : Fragment() {

    private var _binding: FragmentSalasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerViewSalas: RecyclerView
    private lateinit var pistas: List<Pista>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSalasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        pistas= listOf(
            Pista("Babuino\n", "4\nNo\nCristal\n2'5-3'5€", "\nPista con capacidad de hasta cuatro jugadores al aire libre y con perfecta iluminación durante todo el día sin que el sol deslumbre, e incluso por la noche con focos para jugar a cualquier hora.", BitmapFactory.decodeResource(resources, R.drawable.ic_babuino)),
            Pista("Machango\n", "4\nNo\nCristal\n2'5-3'5€", "\nEn esta pista pueden jugar cuatro personas, las paredes son de cristal y se encuentra al aire libre con buena iluminación tanto de día como de noche.", BitmapFactory.decodeResource(resources, R.drawable.machango)),
            Pista("Macaco Japonés\n", "4\nNo\nHormigón\n2'5-3'5€", "\nLas paredes de esta pista son muros, para aquellos jugadores que prefieran este material y quieran jugar al aire libre.", BitmapFactory.decodeResource(resources, R.drawable.macaco_japones)),
            Pista("Tití Rojizo\n", "4\nSí\nCristal\n4-5€", "\nEsta pista para cuatro jugadores está cubierta para los días de peor temporal o aquellos jugadores que se sienten más cómodos que jugando en el exterior.", BitmapFactory.decodeResource(resources, R.drawable.titi_rojizo)),
            Pista("Chimpancé\n", "4\nSí\nHormigón\n4-5€", "\nPista con muros cubierta para que los jugadores que prefieran este tipo de paredes no se queden sin ellas en días lluviosos.", BitmapFactory.decodeResource(resources, R.drawable.chimpance)),
            Pista("Gorila Oriental\n", "2\nNo\nCristal\n3-4€", "\nPista exterior para dos jugadores con paredes de cristal con buena iluminación durante todo el día con focos para la noche.", BitmapFactory.decodeResource(resources, R.drawable.gorila_oriental)),
            Pista("Orangután\n", "2\nNo\nHormigón\n3-4€", "\nSituada al aire libre con muros al fondo de cada lado para dos personas. Ideal para realizar entrenamientos Individuales.", BitmapFactory.decodeResource(resources, R.drawable.orangutan)),
            Pista("Macaco Gibraltareño\n", "2\nNo\nCristal\n3-4€", "\nPista individual en el interior de nuestras instalaciones para dos personas en días de mal tiempo.", BitmapFactory.decodeResource(resources, R.drawable.macaco_gibraltareno))
            )

        recyclerViewSalas = binding.recyclerView
        val adapter = SalaAdapter(pistas)
        recyclerViewSalas.adapter = adapter
        recyclerViewSalas.layoutManager = LinearLayoutManager(requireContext())
        val botonPDF: Button = binding.botonPDF

        botonPDF.setOnClickListener{
            generarPDF()
        }

        return root
    }

    // Método para generar y descargar el archivo PDF
    private fun generarPDF() {
        val document = Document()

        // Ruta del archivo PDF
        val filePath = requireContext().getExternalFilesDir(null)?.absolutePath + "/pistas.pdf"
        val file = File(filePath)

        // Crear el archivo PDF y escribir el contenido
        try {
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Agregar el contenido al PDF
            for (pista in pistas) {
                val caracteristica = pista.caracteristicas.split("\n")
                document.add(Paragraph("Nombre de la pista:  " + pista.nombre))
                document.add(Paragraph("Capacidad:  " + caracteristica[0]))
                document.add(Paragraph("Cubierta:  " + caracteristica[1]))
                document.add(Paragraph("Material:  " + caracteristica[2]))
                document.add(Paragraph("Precio:  " + caracteristica[3]))
                document.add(Paragraph("Descripcion:  " + pista.descripcion))
                document.add(Paragraph("\n"))
                document.add(Paragraph("\n"))
            }

            document.close()

            // Mostrar mensaje de éxito
            mostrarNotificacion("Archivo PDF generado correctamente")

            // Iniciar la descarga del archivo PDF
            descargarPDF(file)
        } catch (e: Exception) {
            // Mostrar mensaje de error
            mostrarNotificacion("Error al generar el archivo PDF: ${e.message}")
        }
    }

    // Método para iniciar la descarga del archivo PDF
    private fun descargarPDF(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", file)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(intent)
    }

    // Método para mostrar la notificación
    private fun mostrarNotificacion(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}