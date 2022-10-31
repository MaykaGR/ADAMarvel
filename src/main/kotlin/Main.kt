import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val api = APIMarvel()
    println(api.buscarPersonaje("3-D Man"))
}