import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val api = APIMarvel()
    var busq = api.buscarPersonaje("3-D Man")
    var personaje1: Personaje? = null
    if(busq.containsKey(200)){
        personaje1 = busq.getValue(200)
        println(personaje1)
    }
    else println("Personaje no encontrado")

    busq = api.buscarPersonaje("Agent Zero")
    var personaje2: Personaje? = null
    if(busq.containsKey(200)){
        personaje2 = busq.getValue(200)
        println(personaje2)
    }
    else println("Personaje no encontrado")

    println(api.popularidad(personaje1, personaje2)?.name)

    api.escribirXML(personaje2)
}