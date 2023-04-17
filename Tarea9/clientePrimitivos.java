import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class clientePrimitivos {
    public static void main(String[] args) {
        try {
            int puerto = 2000;  //variable que indica el puerto de entrada para el servidor

            InetAddress dst = InetAddress.getByName("127.0.0.1");   //direccion fija que usa el servidor de manera local
            DatagramSocket cliente = new DatagramSocket();  //declaracion del socket de tipo datagrama para el cliente   

            ByteArrayOutputStream baos = new ByteArrayOutputStream();   //arreglo de bytes que van a ser mandados para el servidor
            DataOutputStream salida = new DataOutputStream(baos);   //conexion que envia los datos al servidor y los almacena en el arreglo de bytes
            
            salida.writeInt(4); //escritura del entero en el arreglo de bytes
            salida.flush();
            
            salida.writeFloat(4.1f);    //escritura del float en el arreglo de bytes
            salida.flush();

            salida.writeLong(72);       //escritura del long en el arreglo de bytes
            salida.flush();               //liberacion del espacion de memoria temporal

            byte[] b = baos.toByteArray();  //conversion del arreglo de bytes a bytes para el envio de datos
            DatagramPacket p = new DatagramPacket(b, b.length, dst, puerto);    
            //envio del paquete datagrama con parametros del paquete, el tamaño del paquete, la direccion destino y el puerto destido del servidor 

            cliente.send(p);    //envio de los paquetes
            cliente.close();    //se cierra la conexion
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
