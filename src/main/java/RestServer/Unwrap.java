package RestServer;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Unwrap {


    @Getter
    String header;

    @Getter
    String body;

    @Getter
    String verb;

    @Getter
    String location;

    @Getter
    String version;

    @Getter
    private Map<String, String> map;

    @Getter
    String Authorization;




    private String request;

    public Unwrap(String rec_request){
        location="empty";
        request=rec_request;
        map= new HashMap<>();
        split();


    }

    private void getFirstLine(String nextline){
        String[] array = nextline.split(" ",3);
        verb = array[0];
        location=array[1];
        version=array[2];
    }

    private void addToMap(String nextline){


        nextline.trim();
        String[] foo = nextline.split(": ", 2);
        map.put(foo[0],foo[1]);
    }


    private void split(){

        Scanner scaner = new Scanner(request);

        StringBuilder newhead = new StringBuilder();

        String nextline;

        nextline = scaner.nextLine();
        getFirstLine(nextline);
        while (scaner.hasNextLine()) {
            nextline = scaner.nextLine();
            if(nextline.equals("")){break;}
            if(nextline.startsWith("Authorization")){
                String[] array = nextline.split(": ",2);
                Authorization=array[1];
                System.out.println("Auth: " + Authorization);
            }


            newhead.append(nextline);
            addToMap(nextline);

        }
        header=newhead.toString();
        if(Authorization==null)
            Authorization="no";

        StringBuilder newBody = new StringBuilder();
        while (scaner.hasNextLine()) {
            nextline = scaner.nextLine();

            if (nextline.equals("")) {
                break;
            }
            newBody.append(nextline);
        }
        body= newBody.toString();
        System.out.println("body: " + body);

        scaner.close();

    }


}
