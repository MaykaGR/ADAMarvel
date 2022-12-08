import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val api = APIMarvel()
    val busq = api.buscarPersonaje("3-D Man")
    var c0d: Number = 0
    if(busq != null){
        c0d = Codigo.ok
    }
    else if(busq == null){
        c0d = Codigo.notFound
    }
    println(api.buscarPersonaje("3-D Man")?.name)
}