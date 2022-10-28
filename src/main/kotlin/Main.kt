import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val xmlPersonajes = File("resources${System.getProperty("file.separator")}personajesMarvel.xml")
    val xmlComics = File("resources${System.getProperty("file.separator")}comicsMarvel.xml")
    val rootPersonajes = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlPersonajes)
    val rootComics = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlComics)
}