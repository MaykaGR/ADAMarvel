import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class APIMarvel {
    val xmlPersonajes = File("resources${System.getProperty("file.separator")}personajesMarvel.xml")
    val xmlComics = File("resources${System.getProperty("file.separator")}comicsMarvel.xml")
    val rootPersonajes = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlPersonajes)
    val rootComics = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlComics)
    fun buscarPersonaje(nombre: String): Personaje? {
        val personajes = rootPersonajes.getElementsByTagName("results")
        var personaje: Personaje? = null
        var i = 0
        var listaIDS = mutableListOf<String>()
        while (personaje == null && i < personajes.length) {
            val cr = personajes.item(i).childNodes
            var nom = cr.item(0)
            for (i in 0..cr.length - 1) {
                if (cr.item(i).nodeName == "name") {
                    nom = cr.item(i)
                }
            }

            if (nom.textContent == nombre) {
                var id = 0
                var name = nombre
                var desc = ""
                val comicList = mutableListOf<Comic?>()
                var nodo: NodeList = cr
                for (i in 0..cr.length - 1) {
                    when (cr.item(i).nodeName) {
                        "id" -> id = cr.item(i).textContent.toInt()
                        "description" -> desc = cr.item(i).textContent
                        "comics" -> nodo = cr.item(i).childNodes
                    }
                    for (i in 0..nodo.length - 1) {
                        if (nodo.item(i).nodeType == Node.ELEMENT_NODE) {
                            val nod = nodo.item(i) as Element
                            var res = nod.getElementsByTagName("resourceURI")
                            for (j in 0..res.length - 1) {
                                var listaString = res.item(j).textContent.split("/")
                                listaIDS.add(listaString[listaString.size - 1])
                            }
                        }

                    }
                }
                for (i in 0..listaIDS.size - 1) {
                    var comic = buscarComic(listaIDS[i].toInt())
                    comicList.add(comic)
                }
                personaje = Personaje(id, name, desc, comicList)
            } else {
                i += 1
            }
        }
        if (personaje != null) {
            codigoRespuesta("encontrado")
        } else {
            codigoRespuesta("noEncontrado")
        }
        return personaje
    }

    fun buscarComic(id: Int): Comic? {
        var comic: Comic? = null
        val comics = rootComics.getElementsByTagName("results")
        var i = 0
        while (comic == null && i < comics.length) {
            val cm = comics.item(i).childNodes
            val caracteristicas = comics.item(i).childNodes as Element
            val nom = caracteristicas.getElementsByTagName("name").item(0) as Element
            if (nom.textContent == id.toString()) {
                var id = id
                var title = ""
                var desc = ""
                for (i in 0..cm.length - 1) {
                    when (cm.item(i).nodeName) {
                        "description" -> desc = cm.item(i).textContent
                        "title" -> title = cm.item(i).textContent
                    }
                    comic = Comic(id, title, desc)
                }
            } else {
                i += 1
            }
        }
        if (comic != null) {
            codigoRespuesta("encontrado")
        } else {
            codigoRespuesta("noEncontrado")
        }
        return comic
    }

    fun codigoRespuesta(codigo: String): Number {
        var cod: Number = 0
        if (codigo == "encontrado") {
            cod = Codigo.ok
        } else {
            cod = Codigo.notFound
        }
        //println(cod)
        return cod
    }
}