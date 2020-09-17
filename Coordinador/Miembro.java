import java.net.*;

/**
 *
 * @author Gastón
 */

//El objetivo de esta clase es contener los datos referidos a un proceso miembro
//de un grupo.
public class Miembro {
    
    public int idMaquina;
    public InetAddress iDireccion;
    public int iPuerto;
    
    //Constructor de la clase
    public Miembro(int pIdMaquina, InetAddress pDireccion, int pPuerto) {
        idMaquina = pIdMaquina;
        iDireccion = pDireccion;
        iPuerto = pPuerto;
    }
}
