//import Application.Echo.EchoApplication;
import Application.MTCG.MTCG;
import Server.Server;


public class Main {
    public static void main(String[] args) {
        Server server = new Server(new MTCG());
        server.start();
    }
}