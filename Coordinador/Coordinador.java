import java.net.*;
import java.util.*;

/**
 *
 * @author Gastón
 */

//El objetivo de esta clase es representar al nodo coordinador para comunicación
//multicast, utilizando sockets UDP.
public class Coordinador {
    
    public ArrayList<Miembro> iListaDeMiembros;
    public DatagramSocket iSocket;
    
    //Constructor de la clase.
    public Coordinador(int pPuerto) throws Exception{
        iListaDeMiembros = new ArrayList<Miembro>();
        iSocket = new DatagramSocket(pPuerto);
    }
    
    //El propósito de este método es agregar la información de referencia para
    //un nuevo proceso que se une al grupo.
    public void AgregarMiembro(InetAddress pDireccion, int pPuerto) throws Exception{
        //Si la lista no está vacía, obtiene el id del nuevo miembro a partir
        //del id del último integrante del grupo
        int idNuevoMiembro;
        if (!iListaDeMiembros.isEmpty()) {
            //Obtiene el último ID de la lista y le asigna al nuevo miembro el
            //siguiente
            Miembro[] arrayDeMiembros = new Miembro[iListaDeMiembros.size()];
            iListaDeMiembros.toArray(arrayDeMiembros);
            idNuevoMiembro = arrayDeMiembros[arrayDeMiembros.length - 1].idMaquina + 1;
        }
        
        //Si no, le asigna 1
        else {
            idNuevoMiembro = 1;
        }
        //Crea el registro correspondiente al nuevo miembro
        Miembro nuevoMiembro = new Miembro(idNuevoMiembro, pDireccion, pPuerto);
        
        //Agrega al nuevo miembro al grupo
        iListaDeMiembros.add(nuevoMiembro);
    }
    
    public DatagramPacket RecibirMensajes() throws Exception{
        //Elementos del proceso
        byte[] buffer = new byte[1024];
        DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
        
        //Espera a recibir el mensaje y lo devuelve
        iSocket.receive(paqueteRecibido);
        return paqueteRecibido;
    }
    
    //El propósito de este método es enviar un mensaje recibido a todos los
    //miembros del grupo y notificar al proceso externo.
    public boolean EnviarATodos(DatagramPacket pPaqueteRecibido) throws Exception{
        //Elementos del proceso
        int maximoTiempoDeEspera = 10000;
        byte[] buffer = new byte[1024];
        DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
        DatagramPacket paqueteAEnviar;
        String mensaje = new String(pPaqueteRecibido.getData());
        InetAddress direccionDelProceso = pPaqueteRecibido.getAddress();
        int puertoDelProceso = pPaqueteRecibido.getPort();
        boolean enviadoCorrectamente;
        
        //Obtiene la lista como array para recorrerla con mayor facilidad
        String respuesta;
        Miembro[] arrayDeMiembros = new Miembro[iListaDeMiembros.size()];
        iListaDeMiembros.toArray(arrayDeMiembros);
        int indiceActual = 0;
        try {
            while (indiceActual < arrayDeMiembros.length) {
                iSocket.setSoTimeout(0);
                
                //Envia una copia del mensaje al miembro
                paqueteAEnviar = new DatagramPacket(mensaje.getBytes(), mensaje.length(), arrayDeMiembros[indiceActual].iDireccion, arrayDeMiembros[indiceActual].iPuerto);
                iSocket.send(paqueteAEnviar);
            
                //Espera durante maximoTiempoDeEspera para recibir una respuesta del
                //miembro
                iSocket.setSoTimeout(maximoTiempoDeEspera);
                iSocket.receive(paqueteRecibido);
                respuesta = new String(paqueteRecibido.getData());
                System.out.println(respuesta);
                
                //Según la respuesta
                if (respuesta.trim().equals("ACK")) {
                    indiceActual++;
                }
                else {
                    throw new SocketTimeoutException();
                }
                
            }
            iSocket.setSoTimeout(0);
            
            //Si los mensajes fueron entregados con éxito, avisa al proceso
            //externo
            mensaje = "ACK";
            paqueteAEnviar = new DatagramPacket(mensaje.getBytes(), mensaje.length(), direccionDelProceso, puertoDelProceso);
            iSocket.send(paqueteAEnviar);
            
            //La respuesta al main será true, avisando que los mensajes fueron
            //entregados correctamente
            enviadoCorrectamente = true;
        }
        
        //Si algún miembro tarda más que el tiempo establecido, avisa a los
        //demás que deben descartar el mensaje anterior.
        catch(SocketTimeoutException e){
            mensaje = "DES";
            for (int i = 0; i < indiceActual; i++) {
                paqueteAEnviar = new DatagramPacket(mensaje.getBytes(), mensaje.length(), arrayDeMiembros[i].iDireccion, arrayDeMiembros[i].iPuerto);
                iSocket.send(paqueteAEnviar);
            }
            mensaje = "NA";
            paqueteAEnviar = new DatagramPacket(mensaje.getBytes(), mensaje.length(), direccionDelProceso, puertoDelProceso);
            iSocket.send(paqueteAEnviar);
            iSocket.setSoTimeout(0);
        
            //La respuesta al main será false, avisando que ha habido un error
            enviadoCorrectamente = false;
        }
        
        //Avisa el resultado al main
        return enviadoCorrectamente;
    }
}
