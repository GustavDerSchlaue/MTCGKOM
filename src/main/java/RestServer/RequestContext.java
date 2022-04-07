package RestServer;

import Game.Battle;
import Game.CardManager;
import Game.DB;
import Game.User;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class RequestContext {


    private static Socket client;
    String request;
    MessagesesList list;

    public RequestContext(String receivedrequest, MessagesesList messagesesList, Socket listener) {
        request = receivedrequest;
        list = messagesesList;
        client = listener;
    }

    public void requestHandler() throws IOException, SQLException {


        Unwrap unw = new Unwrap(request);
        ResponseHandler responseHandler = new ResponseHandler(client);

        switch (unw.getVerb()) {
            case "POST":
                String tmp = unw.getLocation();


                switch (tmp) {

                    //System.out.println(tmp);
                    //if (tmp == "/users")
                    //{
                    //    System.out.println("Why you dont work?");
                    //}
                    case "/users":
                        if (User.addUserToDB(unw.getBody())) {

                            responseHandler.respond("Added User", 201);
                        } else {
                            responseHandler.respond("User could not be added", 400);
                        }

                        break;


                    case "/sessions":
                        String token = User.Login(unw.getBody());
                        if (token != "error") {

                            responseHandler.respond("Succesfully logged in, Token:" + token, 201);
                        } else {
                            responseHandler.respond("Username or password are wrong", 400);
                        }

                        break;

                    case "/packages":
                        if (unw.Authorization.equals(DB.getAdminToken())) {
                            CardManager.createPackage(unw.getBody());
                            responseHandler.respond("Added Package", 201);
                        } else {
                            responseHandler.respond("no permission", 400);
                        }


                        break;

                    case "/transactions/packages":
                        responseHandler.respond(CardManager.buyPack(unw.Authorization), 201);
                        break;


                    case "/battles":
                        if (unw.Authorization == "no") {

                            responseHandler.respond("Wrong Token", 400);
                        } else {
                            User.addToQueue(unw.Authorization);
                            boolean startBattle = Battle.checkforTwoPlayers();
                            if (startBattle) {
                                Battle battle = new Battle();
                                responseHandler.respond(Battle.startBattle(), 201);
                            }

                        }
                        break;
                    case "/tradings":
                        System.out.println("No trading");
                        break;


                }

                if (tmp.startsWith("/tradings")) {
                    System.out.println("No trading");
                }

                break;
            case "GET":
                tmp = unw.getLocation();


                switch (tmp) {

                    case "/cards":

                        if (unw.Authorization == "no") {

                            responseHandler.respond("Wrong Token", 400);
                        } else {
                            responseHandler.respond(CardManager.seeOwnedCards(unw.Authorization), 201);
                        }
                    case "/deck":
                        if (unw.Authorization == "no") {

                            responseHandler.respond("Wrong Token", 400);
                        } else {
                            responseHandler.respond(CardManager.seeDeck(unw.Authorization), 201);

                        }
                        break;
                    case "/stats":
                        if (unw.Authorization == "no") {

                            responseHandler.respond("Wrong Token", 400);
                        } else {
                            responseHandler.respond(User.getStats(unw.Authorization), 201);

                        }
                        break;
                    case "/score":
                        if (unw.Authorization == "no") {

                            responseHandler.respond("Wrong Token", 400);
                        } else {
                            responseHandler.respond(User.getBoard(unw.Authorization), 201);

                        }
                        break;
                    case "/tradings":
                        System.out.println("No trading");


                        break;


                }


                if (tmp.startsWith("/users/")) {
                    String[] foo = tmp.split("/", 3);
                    String name = foo[2];
                    responseHandler.respond(User.getUserData(name, unw.Authorization), 201);

                }
                break;


            case "PUT":
                tmp = unw.getLocation();

                switch (tmp) {

                    case "/deck":

                        if (unw.Authorization == "no") {

                            responseHandler.respond("Wrong Token", 404);
                        } else {
                            responseHandler.respond(CardManager.configureDeck(unw.Authorization, unw.getBody()), 201);

                        }
                        break;


                }

                if (tmp.startsWith("/users/")) {
                    String[] foo = tmp.split("/", 3);
                    String name = foo[2];
                    responseHandler.respond(User.changeUserData(name, unw.Authorization, unw.getBody()), 201);

                }

                break;
            case "DELETE":
                tmp = unw.getLocation();

                if (tmp.startsWith("/tradings/")) {
                    System.out.println("No trading");
                }

                if (tmp.equals("/reset")) {


                    if (unw.Authorization == "no") {

                        responseHandler.respond("Wrong Token", 404);
                    } else {
                        responseHandler.respond(User.Reset(unw.Authorization), 201);

                    }

                }

                break;
            default:
        }

    }
}
