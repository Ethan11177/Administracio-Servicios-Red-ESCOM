import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;



public class Servidor {

    public static void main(String[] args) {
        try {
            
            ServerSocket servidor = new ServerSocket(50000);

            for(;;){

                Socket conexion = servidor.accept();
                System.out.println("conexion establecida desde: " + conexion.getInetAddress() + ":" + conexion.getPort());

                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                byte[] b = new byte[1024];
                String nombre = entrada.readUTF();

                System.out.println("Recibimos el archivo: " + nombre);

                long tam = entrada.readLong();

                DataOutputStream salida = new DataOutputStream(new FileOutputStream(nombre));

                long recibidos = 0;
                int n = 0, porcentaje;

                while (recibidos < tam) {
                    
                    n = entrada.read(b);
                    salida.write(b, 0, n);
                    salida.flush();

                    recibidos += n;
                    porcentaje = (int)(recibidos*100/tam);
                    System.out.println("Progreso: " + porcentaje + "%");
                }
                System.out.println("\n\nArchivo recibido");

                ObjectInputStream entradaObj = new ObjectInputStream(new FileInputStream("prueba.obj"));
                
                PruebaEjercicio prueRec = (PruebaEjercicio) entradaObj.readObject();
                System.out.println(prueRec.getNombre() + " " + prueRec.getCiudad() + " " + prueRec.getEdad() + " " + prueRec.getNum() + " " + prueRec.getTam() + " " + prueRec.isBan());
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
