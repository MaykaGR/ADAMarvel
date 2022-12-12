import org.w3c.dom.*
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class APIMarvel {
    //Crea el file de personas
    val xmlPersonajes = File("resources${System.getProperty("file.separator")}personajesMarvel.xml")
    //Crea el file de cómics
    val xmlComics = File("resources${System.getProperty("file.separator")}comicsMarvel.xml")
    //Crea el árbol de personajes
    val rootPersonajes = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlPersonajes)
    //Crea el árbol de cómics
    val rootComics = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlComics)
    //Función que busca un personaje por su nombre, y devuelve un mapa con el código de respuesta y el personaje
    fun buscarPersonaje(nombre: String): Map<Number,Personaje?> {
        //Obtiene la lista de results, o personajes
        val personajes = rootPersonajes.getElementsByTagName("results")
        var personaje: Personaje? = null
        var codigo: Number = 0
        var i = 0
        var listaIDS = mutableListOf<String>()
        //Mientras no se haya encontrado el personaje ni rebasado el número de personajes del xml
        while (personaje == null && i < personajes.length) {
            //Se coge cada personaje y se obtienen sus elementos
            val cr = personajes.item(i).childNodes
            var nom = cr.item(0)
            //Se busca el elemento name
            for (i in 0..cr.length - 1) {
                if (cr.item(i).nodeName == "name") {
                    nom = cr.item(i)
                }
            }
            //Una vez se ha encontrado el elemento con la etiqueta name, se comprueba si el texto
            //corresponde con el nombre que buscamos
            //Cuando coincida, se entrará en el if, sino coincide seguirá buscando en el resto de results
            if (nom.textContent == nombre) {
                var id = 0
                var name = nombre
                var desc = ""
                val comicList = mutableListOf<Comic?>()
                var nodo: NodeList = cr
                //Una vez se tiene el personaje que se busca, se cogen los datos que interesan
                for (i in 0..cr.length - 1) {
                    when (cr.item(i).nodeName) {
                        "id" -> id = cr.item(i).textContent.toInt()
                        "description" -> desc = cr.item(i).textContent
                        "comics" -> nodo = cr.item(i).childNodes
                    }
                    //El elemento comics se pasa a un nodo para poder extraerle los ids
                    for (i in 0..nodo.length - 1) {
                        if (nodo.item(i).nodeType == Node.ELEMENT_NODE) {
                            val nod = nodo.item(i) as Element
                            var res = nod.getElementsByTagName("resourceURI")
                            //Se recorren los elementos resourceURI, se obtiene la parte correspondiente al id
                            // y si no se encontraba ya en la lista, se añade
                            for (j in 0..res.length - 1) {
                                var listaString = res.item(j).textContent.split("/")
                                if(listaString[listaString.size-1] !in listaIDS){
                                listaIDS.add(listaString[listaString.size - 1])}
                            }
                        }

                    }
                }
                //Los ids que están en sourceURI no coinciden con ningún id en cómic, no sé cómo relacionarlos
                //Creo que no están los mismos cómics en ambos xml
                for (i in 0..listaIDS.size - 1) {
                    var comic = buscarComic(listaIDS[i].toInt())
                    if(comic.containsKey(200)){
                        comicList.add(comic.getValue(200))
                    }
                    //Le pongo que le asigne el cómic nulo aunque no lo encuentre para poder tener el número de
                    //apariciones, a pesar de no tener los cómics
                    else if(comic.containsKey(404)){
                       comicList.add(comic.getValue(404))
                    }

                }
                personaje = Personaje(id, name, desc, comicList)
            } else {
                i += 1
            }
        }
        //Si el personaje ya contiene datos, se pide el código de encontrado, y se pide el de noEncontrado
        //en caso de que siga siendo nulo
        if (personaje != null) {
            codigo = codigoRespuesta("encontrado")
        } else {
            codigo = codigoRespuesta("noEncontrado")
        }
        var result = mapOf(codigo to personaje)
        return result
    }

    //Función que busca un cómic por su id, y devuelve un mapa con el código de respuesta y el cómic
    fun buscarComic(id: Int): Map<Number,Comic?> {
        var comic: Comic? = null
        //Se obtiene una lista de los results, que corresponden a cada cómic
        val comics = rootComics.getElementsByTagName("results")
        var codigo: Number = 0
        var i = 0
        //Mientras el cómic siga siendo nulo e i no rebase la cantidad de results
        while (comic == null && i < comics.length) {
            //Se obtiene de cada results sus características
            val cm = comics.item(i).childNodes
            val caracteristicas = comics.item(i).childNodes as Element
            //Se extrae todos los elementos id del results en el que estamos
            //Como cada uno sólo posee un id, se coge el primer elemento
            val nom = caracteristicas.getElementsByTagName("id").item(0) as Element
            //Cuando el texto que contiene el elemento corresponda al id que estamos buscando,
            //se asignan sus características a cómic
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
        //Si el cómic ya contiene datos, se pide el código de encontrado, y se pide el de noEncontrado
        //en caso de que siga siendo nulo
        if (comic != null) {
            codigo = codigoRespuesta("encontrado")
        } else {
            codigo = codigoRespuesta("noEncontrado")
        }
        val result = mapOf(codigo to comic)
        return result
    }
    //Función que devuelve el código de respuesta que corresponde en caso de que se haya encontrado lo que se buscaba o no
    fun codigoRespuesta(codigo: String): Number {
        var cod: Number = 0
        if (codigo == "encontrado") {
            cod = Codigo.ok
        } else {
            cod = Codigo.notFound
        }
        return cod
    }

    //Compara la cantidad de apariciones en cómics de dos personajes, para mostrar su popularidad
    fun popularidad(personaje1: Personaje?, personaje2: Personaje?): Personaje?{
        if(personaje1?.comics?.size?:0>personaje2?.comics?.size?:0){
            return personaje1
        }
        else return personaje2
    }

    //Devuelve la lista de cómics de un personaje
    fun comicsPorPersonaje(personaje: Personaje?): MutableList<Comic?>{
        return personaje?.comics?: mutableListOf()
    }

    //Función que escribe un XML con la información de un personaje dado
    fun escribirXML(character: Personaje?){
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        val imp: DOMImplementation = builder.domImplementation


        val personajes : Document = imp.createDocument(null,"personajes",null)

        val personaje = personajes.createElement("personaje")
        val id = personajes.createElement("id")
        var texto = personajes.createTextNode(character?.id.toString())
        id.appendChild(texto)
        personaje.appendChild(id)

        val nombre = personajes.createElement("name")
        texto = personajes.createTextNode(character?.name)
        nombre.appendChild(texto)
        personaje.appendChild(nombre)

        var descrip = personajes.createElement("description")
        texto = personajes.createTextNode(character?.description)
        descrip.appendChild(texto)
        personaje.appendChild(descrip)

        val comics = personajes.createElement("comics")
        //Esta parte la dejo comentada porque, al estar los comics, como nulo, al intentar acceder a sus características
        //da una excepción, creo que si tuviera cómics funcionaría
        /*
        for(i in 0..(character?.comics?.size?:1)-1){
            val comic = personajes.createElement("comic")
            val idC = personajes.createElement("id")
            texto = personajes.createTextNode((character!!.comics[i]!!.id).toString())
            idC.appendChild(texto)
            comic.appendChild(idC)
            val title = personajes.createElement("title")
            texto = personajes.createTextNode(character!!.comics[i]!!.title)
            title.appendChild(texto)
            comic.appendChild(title)
            val desc = personajes.createElement("description")
            texto = personajes.createTextNode(character!!.comics[i]!!.description)
            desc.appendChild(texto)
            comic.appendChild(desc)
            comics.appendChild(comic)
        }
        */


        personaje.appendChild(comics)


        personajes.documentElement.appendChild(personaje)


        val source: Source = DOMSource(personajes)
        val result = StreamResult(File("resources/personajeGenerado"+character?.name?.replace(" ","_")+".xml"))
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT,"yes")

        transformer.transform(source, result)
    }
}