import java.net.*;

/**
 *
 * @author Gastón
 */

//El objetivo de esta clase es representar a un proyecto, el cual forma parte de
//un grupo.
public class Proceso {
    
    public DatagramSocket iSocket;
    
    //Constructor de la clase
    public Proceso(int pPuerto) throws Exception{
        iSocket = new DatagramSocket(pPuerto);
    }
    
    //El proposito de este método es recibir mensajes enviados al grupo
    public DatagramPacket RecibirMensajes() throws Exception{
        //Elementos del proceso
        byte[] buffer = new byte[1024];
        DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
        
        //Espera recibir mensajes
        iSocket.receive(paqueteRecibido);
        
        //Lo devuelve al main
        return paqueteRecibido;
    }
    
    //El propósito de este mensaje es responder al nodo coordinador si el
    //paquete recibido es aceptado o no, y esperar una confirmación respecto a
    //si el paquete debe ser descartado.
    public boolean AceptarYEsperar(InetAddress pDireccionCoordinador, int pPuertoCoordinador) throws Exception {
        //Elementos del proceso
        int maximoTiempoDeEspera = 15000;
        String mensaje = "ACK";
        DatagramPacket paqueteAEnviar = new DatagramPacket(mensaje.getBytes(), mensaje.length(), pDireccionCoordinador, pPuertoCoordinador);
        byte[] buffer = new byte[1024];
        DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
        String mensajeRecibido;
        boolean puedeAceptarse = true;
        
        //Avisa al nodo coordinador que acepta el mensaje
        iSocket.send(paqueteAEnviar);
            
        //Espera indicaciones de descarte durante maximoTiempoDeEspera
        try {
            iSocket.setSoTimeout(maximoTiempoDeEspera);
            iSocket.receive(paqueteRecibido);
            iSocket.setSoTimeout(0);
            mensajeRecibido = new String(paqueteRecibido.getData());
                
            //Si recibe la indicación de que debe descartar el mensaje
            //anterior, avisa al main.
            if (mensajeRecibido.trim().equals("DES")) {
                puedeAceptarse = false;
            }
        }
            
        //Si se agota el tiempo de espera, deja a puedeAceptarse como true
        catch (SocketTimeoutException e) {
            iSocket.setSoTimeout(0);
        }
        return puedeAceptarse;
    }
        

    
    //El propósito de este método es permitir enviar un mensaje a un grupo de
    //procesos a través de su nodo coordinador.
    public boolean EnviarAlGrupo(String pMensaje, InetAddress pDireccion, int pPuerto) throws Exception{
        //Elementos del proceso
        DatagramPacket paqueteAEnviar = new DatagramPacket(pMensaje.getBytes(), pMensaje.length(), pDireccion, pPuerto);
        byte[] buffer = new byte[1024];
        DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
        String mensajeRecibido;
        boolean entregaExitosa = false;
        
        //Envia el mensaje al nodo coordinador y espera su respuesta
        iSocket.send(paqueteAEnviar);
        iSocket.receive(paqueteRecibido);
        mensajeRecibido = new String(paqueteRecibido.getData());
        
        //Si recibe un ACK del coordinador, el mensaje fue entregado exitosamente
        if (mensajeRecibido.trim().equals("ACK")) {
            entregaExitosa = true;
        }
        
        //Devuelve el boolean indicando si el mensaje fue entregado con éxito
        return entregaExitosa;
    }
}
