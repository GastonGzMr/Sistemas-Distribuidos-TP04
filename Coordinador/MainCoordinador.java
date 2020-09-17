import java.net.*;
import java.util.Scanner;

/**
 *
 * @author Gastón
 */

//El objetivo de esta clase es ser el medio de ejecución del coordinador
public class MainCoordinador {
    
    public static void main(String[] args) throws Exception{
        //Elementos del proceso.
        Scanner scanner = new Scanner(System.in);
        InetAddress direccion;
        boolean respuesta;
        DatagramPacket paqueteRecibido;
        int puerto;
        
        //Crea una instancia del coordinador.
        System.out.print("Abrir el puerto: ");
        Coordinador coordinador = new Coordinador(Integer.parseInt(scanner.nextLine()));
        
        //Menu
        while(true){
            System.out.println("1 - Agregar al grupo");
            System.out.println("2 - Esperar mensaje");
            System.out.print("Ingrese opción: ");
            switch(scanner.nextLine()){
                
                //Agrega un nuevo miembro al grupo
                case "1":
                    //Solicita los datos del proceso al usuario
                    System.out.print("Dirección: ");
                    direccion = InetAddress.getByName(scanner.nextLine());
                    System.out.print("Puerto: ");
                    puerto = Integer.parseInt(scanner.nextLine());
                    
                    //Agrega al nuevo miembro al grupo y avisa al usuario
                    coordinador.AgregarMiembro(direccion, puerto);
                    System.out.println("Se ha agregado al nuevo miembro.");
                    break;
                
                //Espera un mensaje de un proceso exterior y lo reenvía a los
                //miembros del grupo
                case "2":
                    //Espera un mensaje
                    System.out.println("Esperando mensajes...");
                    paqueteRecibido = coordinador.RecibirMensajes();
                    
                    //Notifica al usuario y lo reenvia al grupo
                    System.out.println(paqueteRecibido.getAddress()+" dice: "+new String(paqueteRecibido.getData()));
                    System.out.println("Reenviando a los miembros del grupo...");
                    respuesta = coordinador.EnviarATodos(paqueteRecibido);
                    
                    //Según el resultado obtenido
                    if (respuesta) {
                        System.out.println("El mensaje fue entregado con éxito.");
                    }
                    else {
                        System.out.println("Ha habido un problema.");
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
