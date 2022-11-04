import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val api = APIMarvel()
    val busq = api.buscarPersonaje("3-D Man")
    println(api.buscarPersonaje("3-D Man")?.name)
}