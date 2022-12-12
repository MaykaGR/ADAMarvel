import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val api = APIMarvel()
    var busq = api.buscarPersonaje("3-D Man")
    var personaje1: Personaje? = null
    if (busq.containsKey(200)) {
        personaje1 = busq.getValue(200)
        println(personaje1)
    } else println("Personaje no encontrado")

    busq = api.buscarPersonaje("Agent Zero")
    var personaje2: Personaje? = null
    if (busq.containsKey(200)) {
        personaje2 = busq.getValue(200)
        println(personaje2)
    } else println("Personaje no encontrado")

    println(api.popularidad(personaje1, personaje2)?.name)

    api.escribirXML(personaje2)

    var salir = false
    while (!salir) {
        println(
            "¿Qué deseas hacer ahora?: \n1 -Consultar un personaje \n2 -Comparar popularidad de dos personajes " +
                    "\n3 -Buscar un cómic\n Cualquier otra tecla -Salir"
        )
        var respuesta = readln()
        if (respuesta == "1") {
            println("Introduce el nombre del personaje: ")
            busq = api.buscarPersonaje(readln())
            var personaje3: Personaje? = null
            if (busq.containsKey(200)) {
                personaje3 = busq.getValue(200)
                println(personaje3)
                println("¿Deseas generar un xml del personaje? S/N")
                var sn = readln()
                if (sn.toUpperCase() == "S") {
                    api.escribirXML(personaje3)
                }
            } else println("Personaje no encontrado")
        } else if (respuesta == "2") {
            println("Introduce el nombre del personaje1: ")
            busq = api.buscarPersonaje(readln())
            if (busq.containsKey(200)) {
                personaje1 = busq.getValue(200)
                //println(personaje1)
            } else {
                println("Personaje no encontrado")
            }
            println("Introduce el nombre del personaje2: ")
            busq = api.buscarPersonaje(readln())
            if(busq.containsKey(200)){
                personaje2 = busq.getValue(200)
                //println(personaje2)
            }
            else{println("Personaje no encontrado")}
            println(api.popularidad(personaje1,personaje2)?.name)
        }
        else if(respuesta=="3"){
            println("Introduce el id del cómic: ")
            println(api.buscarComic(readln().toInt()))
        }
        else{
            salir = true
        }
    }


}
