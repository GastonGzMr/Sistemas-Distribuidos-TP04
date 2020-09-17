import java.net.*;
import java.util.Scanner;

/**
 *
 * @author Gastón
 */

//El objetivo de esta clase es ser el medio de ejecución del proceso
public class MainProceso {
    
    public static void main(String[] args) throws Exception{
        //Elementos del proceso.
        Scanner scanner = new Scanner(System.in);
        DatagramPacket paqueteRecibido;
        InetAddress direccionDelCoordinador;
        int puertoDelCoordinador;
        boolean resultado;
        String mensajeRecibido;
        String mensajeAEnviar;
        
        //Pide el puerto al usuario y crea una instancia del proceso.
        System.out.print("Abrir el puerto: ");
        Proceso proceso = new Proceso(Integer.parseInt(scanner.nextLine()));
        
        //Menu
        while(true){
            System.out.println("1 - Esperar mensajes del grupo");
            System.out.println("2 - Enviar mensaje a un grupo");
            System.out.print("Ingrese opción: ");
            switch(scanner.nextLine()) {
                
                //Espera mensajes del grupo
                case "1":
                    System.out.println("Esperando mensajes...");
                    paqueteRecibido = proceso.RecibirMensajes();
                    System.out.println("Se ha recibido un mensaje. Presione 'A' para aceptarlo");
                    resultado = false;
            
                    //Si se acepta el mensaje
                    if (scanner.nextLine().toUpperCase().equals("A")) {
                        resultado = proceso.AceptarYEsperar(paqueteRecibido.getAddress(), paqueteRecibido.getPort());
                    }
            
                    //Si puede aceptarse el mensaje
                    if (resultado) {
                        mensajeRecibido = new String(paqueteRecibido.getData());
                        System.out.println(paqueteRecibido.getAddress()+" dice: "+mensajeRecibido);
                    }
                    else {
                        System.out.println("Mensaje descartado.");
                    }
                    break;
                
                //Envía un mensaje a un grupo
                case "2":
                    //Solicita los datos al usuario
                    System.out.print("Ingrese el mensaje: ");
                    mensajeAEnviar = scanner.nextLine();
                    System.out.print("Ingrese la dirección del nodo coordinador: ");
                    direccionDelCoordinador = InetAddress.getByName(scanner.nextLine());
                    System.out.print("Ingrese el puerto del nodo coordinador: ");
                    puertoDelCoordinador = Integer.parseInt(scanner.nextLine());
                    
                    //Envía el mensaje al grupo y espera el resultado
                    resultado = proceso.EnviarAlGrupo(mensajeAEnviar, direccionDelCoordinador, puertoDelCoordinador);
                    
                    //Si el mensaje se envió correctamente
                    if (resultado) {
                        System.out.println("El mensaje se ha entregado con éxito");
                    }
                    
                    //En caso contrario
                    else {
                        System.out.println("Error. El mensaje no era atómico o ha habido otro problema.");
                    }
                    break;
                    
                //Si la opción ingresada no existe.
                default:
                    System.out.println("Error. Intente nuevamente.");
                    break;
            }
            
        }
    }
}
