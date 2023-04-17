import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * servidor
 */
public class servidorPrimitivos {

    public static void main(String[] args) {

        try {
            DatagramSocket servidor = new DatagramSocket(2000); //inicio del servidor datagrama, donde se especifica que el puerto de entrada es el 2000
            System.out.println("Servidor iniciado, esperando cliente");

            for (;;) {
                DatagramPacket paquete = new DatagramPacket(new byte[2000], 2000);
                //inicializacion del paquete, donde se tiene la entrada del nuevo paquete y el tamaño maximo que este va a tener
                servidor.receive(paquete);
                //recepcion del paquete datagrama

                System.out.println("Datagrama recibido desde" + paquete.getAddress() + " : " + paquete.getPort());
                DataInputStream entrada = new DataInputStream(new ByteArrayInputStream(paquete.getData()));
                //inicializacion de la entrada donde se van a recibir ahora los datos que estan en el paquete

                int x = entrada.readInt();  //entrda del numero entero
                float f = entrada.readFloat();  //entrda del float
                long z = entrada.readLong();    //entrada del long

                System.out.println("\n\nEntero: " + x + " Flotante: " + f + " Long: " + z);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}