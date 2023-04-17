import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * servidorChanel
 */
public class servidorChanel {
    public final static int PUERTO = 7; //variable donde se ingrasara el puerto se esntrada
    public final static int TAM_MAXIMO = 65507; //maximo de datos que va a recibir el servidor

    public static void main(String[] args) {
        int port = PUERTO;  //declaracion del puerto de entrada

        try {
            DatagramChannel canal = DatagramChannel.open(); //en esta linea se crea el canal del datagrama y se abre para la entrda de datos
            canal.configureBlocking(false); //aqui se indica que el canal no sera de tipo bloquenate

            DatagramSocket socket = canal.socket(); //creacion del socket datagrama por el cual van a entrar o salir lo datos del canal
            SocketAddress direccion = new InetSocketAddress(port);  //aqui se declara el puerto de entrada que va a tener el servidor
            socket.bind(direccion); //aqui se pone en mocho escucha el puerto para el socket antes creado
            
            Selector selector = Selector.open();
            canal.register(selector, SelectionKey.OP_READ);
            // seccion donde se pone al canal en modo de lectura de datos, por medio del selector que va a iniciarse en la linea 30

            ByteBuffer buffer = ByteBuffer.allocate(TAM_MAXIMO);
            //creacion del buffer da datos que va a almacenar todos los datos del cliente

            while (true) {
                selector.select(5000); //se pone al selector del canal en espera de 5 segundos
                Set sk = selector.selectedKeys();   //se almacenan los canales registrados de modo que no existan dos repetidos en el sistema
                Iterator it = sk.iterator();    //se almacenan las conexiones del canal en un arreglo iterator

                while (it.hasNext()) {
                    //ciclo que pasa de conexion a conexion para poder terminar con los canales conales conectados
                    SelectionKey key = (SelectionKey)it.next(); //se hace la seleccion del canal que ahora va a ser usado
                    it.remove();    //se remueve la petiocion del canal del arreglo

                    if (key.isReadable()) {
                        //se pregunta al selectro se puede ser leido el contenido del canal
                        buffer.clear(); //se limpia el buffer de manera completa
                        SocketAddress client = canal.receive(buffer);   //se transquiben los datos recibidos al buffer para poder transcribirlos
                        buffer.flip();  //se regresa a la primera posicion del buffer

                        int eco = buffer.getInt();  //se obtiene el dato leido del buffer que ahora alamacena todos los datos que mando el cliente

                        if (eco == 1000) {
                            //eco es tiene un dato erroneo de de parte del cliente por ende se cierra el servidor
                            canal.close();  //se cierra el canal y por ende la conexion
                            System.exit(0); //termina el programa
                        }else{
                            System.out.println("Dato leido " + eco);    //se imprime los datos que recibe eco
                            buffer.flip();
                            canal.send(buffer, client); //respuesta de parte del servidro especificando que recibio el dato
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}