import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * Servidor
 */
public class Servidor {

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception{

        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }

    public static void main(String[] args) throws Exception{
        try (ServerSocket servidor = new ServerSocket(50000)) {
            System.out.println("Esperando cliente...");

            for(;;){
                
                Socket conexion = servidor.accept();
                System.out.println("Conexion establecida desde " + conexion.getInetAddress() + ":" + conexion.getPort());

                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                salida.write("Hola mundo".getBytes());

                byte[] buffer = new byte[10];
                read(entrada, buffer, 0, 10);
                System.out.println(new String(buffer, "UTF-8"));

                conexion.close();
                servidor.close();

            } 
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}