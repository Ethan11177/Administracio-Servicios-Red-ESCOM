import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws Exception {
        
        try {
            ServerSocket s = new ServerSocket(50000);

            for(;;){
                
                Socket cl = s.accept();
                System.out.println("conexion establecida desde: " + cl.getInetAddress() + ":" + cl.getPort());

                DataInputStream entrada = new DataInputStream(cl.getInputStream());
                
                byte[] b = new byte[1024];
                String nombre = entrada.readUTF();

                System.out.println("Recibimos el archivo:" + nombre);
                long tam = entrada.readLong();

                DataOutputStream salida  = new DataOutputStream(new FileOutputStream(nombre));

                long recibidos = 0;
                int n = 0 , porcentaje;

                while (recibidos < tam) {
                    
                    n = entrada.read(b);
                    salida.write(b, 0, n);
                    salida.flush();
                    recibidos += n;
                    porcentaje = (int)(recibidos*100/tam);
                    System.out.println("Progreso: " + porcentaje + "%");
                }

                System.out.println("\n\nArchivo recibido");

                salida.close();
                entrada.close();
                cl.close();

            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
