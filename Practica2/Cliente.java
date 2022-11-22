import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JFileChooser;

/**
 * Cliente
 */
public class Cliente {

    public static void main(String[] args) throws Exception {

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Escriba la direccion del servidor");
            String host = br.readLine();

            System.out.printf("\n\nEscribe el puerto:");
            int pto = Integer.parseInt(br.readLine());

            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {

                File f[] = jf.getSelectedFiles();

                for (File file : f) {
                    
                    Socket cl = new Socket(host, pto);

                    String archivo = file.getAbsolutePath();
                    String nombre = file.getName();
                    long tam = file.length();

                    DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                    DataInputStream dis = new DataInputStream(new FileInputStream(archivo));

                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();

                    byte[] b = new byte[1024];
                    long enviados = 0;
                    int porcentaje, n;

                    while (enviados < tam) {

                        n = dis.read(b);
                        dos.write(b, 0, n);
                        dos.flush();
                        enviados += n;
                        porcentaje = (int) (enviados * 100 / tam);
                        System.out.println("enviado: " + porcentaje + "%\r");
                    }
                    System.out.println("\n\nArchivo enviado");
                    dos.close();
                    dis.close();
                    cl.close();
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
