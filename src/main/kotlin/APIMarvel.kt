import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

class APIMarvel {
    fun buscarPersonaje(xml: Document, nombre: String ): Personaje?{
        val personajes = xml.getElementsByTagName("results")
        var personaje: Personaje? = null
        var i = 0
        while(personaje == null&&i<personajes.length) {
            val cr = personajes.item(i).childNodes
            val caracteristicas = personajes.item(i).childNodes as Document
            val nom = caracteristicas.getElementsByTagName("name").item(0) as Element
            if(nom.textContent == nombre){
                var id = 0
                var name = nombre
                var desc = ""
                val comicList = mutableListOf<Comic>()
                lateinit var nodo: NodeList
                for(i in 0..cr.length-1){
                when(cr.item(i).nodeName){
                    "id" -> id = cr.item(i).textContent.toInt()
                    "description" -> desc = cr.item(i).textContent
                    "comics" -> nodo = cr.item(i).childNodes
                }}
                personaje = Personaje(id,name,desc,comicList)
            }

        }
        return personaje
    }
}