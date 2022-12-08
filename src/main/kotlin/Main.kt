import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val api = APIMarvel()
    val busq = api.buscarPersonaje("3-D Man")
    var personaje: Personaje? = null
    if(busq.containsKey("200")){
        personaje = busq.getValue("200")
        println(personaje)
        println(personaje?.comics?.size)
    }
    else{
        println("Personaje no encontrado")
    }
    val busq2 = api.buscarPersonaje("Agent Zero")
    var personaje2: Personaje? = null
    if(busq2.containsKey("200")){
        personaje2 = busq2.getValue("200")
        println(personaje2)
        println(personaje2?.comics?.size)
    }
    else{
        println("Personaje no encontrado")
    }
    println(api.popularidad())
}