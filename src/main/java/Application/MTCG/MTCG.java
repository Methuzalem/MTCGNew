package Application.MTCG;

import Application.MTCG.controller.PackageController;
import Application.MTCG.controller.UserController;
import Application.MTCG.controller.SessionController;
import Application.MTCG.controller.TransactionController;
import Application.MTCG.repositorys.CardRepo;
import Application.MTCG.repositorys.UserRepo;
import Application.MTCG.service.CardService;
import Application.MTCG.service.UserService;
import Application.MTCG.controller.Controller;
import Application.MTCG.data.ConnectionPool;
import Application.MTCG.exceptions.ControllerNotFound;
import Application.MTCG.routing.Router;

import Server.Application;
import Server.http.Request;
import Server.http.Response;
import Server.http.Status;

//import at.technikum.application.moodle.service.StudentService;
//import at.technikum.application.moodle.repository.StudentDbRepository;
//import at.technikum.application.moodle.repository.StudentRepository;

public class MTCG implements Application {

    private final Router router;

    public MTCG() {
        this.router = new Router();

        this.initializeRoutes();
    }

    @Override
    public Response handle(Request request) {

        try {
            Controller controller
                    = this.router.getController(request.getPath());
            return controller.handle(request);

        } catch (ControllerNotFound e) {
            // TODO: better exception handling, map exception to http code?
            Response response = new Response();
            response.setStatus(Status.NOT_FOUND);

            response.setHeader("Content-Type", "application/json");
            response.setBody(
                    "{\"error\": \"Path: %s not found\" }"
                            .formatted(e.getMessage())
            );

            return response;
        }
    }

    private void initializeRoutes() {
        ConnectionPool connectionPool = new ConnectionPool();
        UserRepo userRepo = new UserRepo(connectionPool);
        UserService userService = new UserService(userRepo);
        CardRepo cardRepo = new CardRepo(connectionPool);
        CardService cardService = new CardService(cardRepo, userService);

        this.router.addRoute("/users", new UserController(userService));
        this.router.addRoute("/sessions", new SessionController(userRepo));
        this.router.addRoute("/packages", new PackageController(cardService));
        this.router.addRoute("/transactions/packages", new TransactionController(cardService, userService));

/*
        StudentRepository studentRepository = new StudentDbRepository(connectionPool);
        StudentService studentService = new StudentService(studentRepository);

        this.router.addRoute("/users", new StudentController(studentService));
        this.router.addRoute("/wait", new WaitController());
        this.router.addRoute("/health", new HealthController());
 */
    }
}