
/*
 * ESCOM - Escuela Superior de Computacion
 * Autores: 
 * Rodriguez Alvares Gustavo Adrian
 * Vaquera Aguilera Ethan Emiliano
 * Fecha: 29/11/2022 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    // seccion que lee el archivo que tiene la descripcion de los datos, ademas de
    // sus correspondientes direcciones
    public static String[] ObtenerImagenes() throws IOException {

        int i = 0, j = 0;

        // File archivo = new
        // File("C:\\Users\\eevae\\OneDrive\\Documentos\\GitHub\\Administracio_Servicios_Red\\Proyecto1\\archivosServer\\imagenesServer_Cliente.txt");
        File archivo = new File("archivosServer\\imagenesServer_Cliente.txt");
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        BufferedReader br2 = new BufferedReader(fr);

        String linea = br.readLine();
        String[] arreglo1 = new String[100];

        while (linea != null) {
            arreglo1[i] = linea;
            linea = br.readLine();
            i++;
        }

        String[] arreglo = new String[i];

        while (j < i) {
            arreglo[j] = arreglo1[j];
            j++;
        }

        br.close();
        br2.close();

        return arreglo;
    }

    // inicio de la serializacion del objeto para poder transferirlo al cliente
    public static void SerializacionObjeto(Productos[] objPro) throws Exception {

        try {

            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("archivosServer\\objetoS.txt"));
            salida.writeObject(objPro);
            salida.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static Productos[] DeserializacionProductos()
            throws FileNotFoundException, IOException, ClassNotFoundException {

        ObjectInputStream entradaObj = new ObjectInputStream(
                new FileInputStream("archivosServer\\objetoC.txt"));

        Productos[] prueRec = (Productos[]) entradaObj.readObject();

        entradaObj.close();

        return prueRec;
    }

    // seccion que manda el archivo serializado al cliente donde van a estar
    // contenidos los datos del producto
    public static void MandarArchivo(File[] f, int contM, Productos[] objP) throws Exception {

        try {

            // seccion que se encarga de la ejecucion del servidor

            ServerSocket servidor = new ServerSocket(3013);

            for (;;) {
                Socket conexion = servidor.accept();

                DataOutputStream salida1 = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada1 = new DataInputStream(conexion.getInputStream());

                salida1.writeInt(contM);

                for (int i = 0; i <= contM; i++) {

                    File arcSe;

                    if (i == contM) {
                        arcSe = new File("archivosServer\\objetoS.txt");
                    } else {
                        arcSe = new File("archivosServer\\" + f[i].getName());
                    }

                    String archivo = arcSe.getAbsolutePath();
                    String nombre = arcSe.getName();
                    long tam = arcSe.length();

                    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                    DataInputStream entrada = new DataInputStream(new FileInputStream(archivo));

                    salida.writeUTF(nombre);
                    salida.flush();

                    salida.writeLong(tam);
                    salida.flush();

                    byte[] b = new byte[1024];
                    long enviados = 0;
                    int porcentaje = 0, n = 0;

                    while (enviados < tam) {

                        n = entrada.read(b);
                        salida.write(b, 0, n);
                        salida.flush();

                        enviados += n;
                        porcentaje = (int) (enviados * 100 / tam);
                        System.out.println("enviado: " + porcentaje + "%\r");

                    }
                    System.out.println("\nArchivo enviado " + nombre + "\n");

                    salida.flush();

                    Thread.sleep(500);
                }

                HacerBackUp(objP, conexion);

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void HacerBackUp(Productos[] objP, Socket conexion) {

        try {
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            byte[] b = new byte[1024];
            String nombre = entrada.readUTF();

            System.out.println("Recibimos el archivo: " + nombre);

            long tam = entrada.readLong();

            DataOutputStream salida = new DataOutputStream(
                    new FileOutputStream("archivosServer\\" + nombre));

            long recibidos = 0;
            int n = 0, porcentaje;

            while (recibidos < tam) {

                n = entrada.read(b);
                salida.write(b, 0, n);
                salida.flush();

                recibidos += n;
                porcentaje = (int) (recibidos * 100 / tam);
                System.out.println("Progreso: " + porcentaje + "%");
            }
            System.out.println("\nArchivo recibido " + nombre + "\n");
            salida.flush();

            Productos[] objPfinal = DeserializacionProductos();

            /*for (int i = 0; i < objPfinal.length; i++) {

                File f = new File("archivosCliente\\" + objPfinal[i].getNombre() + ".jpg");

                System.out.println("No. Productos: " + objPfinal[i].getId());
                System.out.println("Nombre Producto: " + objPfinal[i].getNombre());
                System.out.println("Descripcion Producto: " + objPfinal[i].getDescripcion());
                System.out.println("Precio Producto: " + objPfinal[i].getPrecio());
                System.out.println("Existencias: " + objPfinal[i].getExistencia());
                System.out.println();

            }*/

            String sFichero = "archivosServer\\imagenesServer_Cliente.txt";
            File fichero = new File(sFichero);

            BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero));

            for (int i = 0; i < objPfinal.length; i++) {
                bw.write(objPfinal[i].getId() + "\n");
                bw.write(objPfinal[i].getNombre() + "\n");
                bw.write(objPfinal[i].getDescripcion() + "\n");
                bw.write(objPfinal[i].getPrecio() + "\n");
                bw.write(objPfinal[i].getExistencia() + "\n");
                bw.write(objPfinal[i].getUrl()+ "\n\n");
            }
            bw.close();

            SerializacionObjeto(objPfinal);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // metodo principal que manda a llamer a todos los metodos y funciones necearias
    // para el programa
    public static void main(String[] args) throws Exception {

        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        String[] lineas = ObtenerImagenes();
        Productos[] objP = new Productos[lineas.length / 6];

        int a = 0;

        // seccion que se encarga de formar todo para el objeto que luego sera
        // serializado
        for (int i = 0; i < lineas.length / 6; i++) {

            File file = new File(lineas[a + 5]);

            objP[i] = new Productos(i, lineas[a + 1], lineas[a + 2], Integer.parseInt(lineas[a + 3]),
                    Integer.parseInt(lineas[a + 4]), file);

            a += 7;
        }

        // llamda a la funcion que se encarga de crear el archivo de serializacion
        SerializacionObjeto(objP);

        File[] f = new File[objP.length];

        for (int i = 0; i < objP.length; i++) {
            f[i] = objP[i].getUrl();
        }

        MandarArchivo(f, objP.length, objP);
    }
}
