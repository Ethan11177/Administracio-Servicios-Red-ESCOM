import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente
 */
public class Cliente {

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception{

        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }

    public static void main(String[] args) throws Exception{
        
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese la direccion del Servidor: ");
        String ipServidor = sc.nextLine();

        System.out.println("Igrese el puerto del servidor: ");
        int portServidor = sc.nextInt();

        try {
            for(;;){
                
                Socket conexion = new Socket(ipServidor, portServidor);

                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());                 
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                byte[] buffer = new byte[10];
                read(entrada, buffer, 0, 10);
                System.out.println(new String(buffer, "UTF-8"));

                salida.write(buffer);

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}