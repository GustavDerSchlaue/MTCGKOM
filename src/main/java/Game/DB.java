package Game;


import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DB {

    private static Connection conn;


    public static void openConnection(String url, String user, String password) throws SQLException {
        conn = DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection() throws SQLException {
        conn.close();
        conn = null;
    }


    public static boolean checkIfUserExists(String username) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("""
                select Username from "mtcg2".players 
                where(Username= ?)
                """);

        stmt.setString(1, username);
        ResultSet set = stmt.executeQuery();
        boolean entry = set.next();
        return entry;

    }

    public static boolean checkPassword(String username, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("""
                select Username from "mtcg2".players 
                where(Username= ? and Password= ?)
                """);

        stmt.setString(1, username);
        stmt.setString(2, password);


        ResultSet set = stmt.executeQuery();
        boolean entry = set.next();
        return entry;

    }


    public static void createUser(String username, String password) {
        String token = "Basic " + username + "-mtcgToken";
        try {

            conn
                    .createStatement();


            PreparedStatement stmt = conn.prepareStatement("""
                    insert into "mtcg2".players 
                    (Username, Password, coins, token ) 
                    values(?, ?, ?, ?)
                    """);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, 20);
            stmt.setString(4, token);
            stmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addPack(List<CardManager> pack) {

        try {
            int packID = getPackID() + 1;

            conn
                    .createStatement();


            PreparedStatement stmt = conn.prepareStatement("""
                    insert into "mtcg2".cards 
                    (cardid, name, damage, element, type, owner, packid) 
                    values(?, ?, ?, ?, ?, ?, ?)
                    """);

            for (var item : pack) {
                stmt.setString(1, item.Id);
                stmt.setString(2, item.Name);
                stmt.setInt(3, item.Damage);
                stmt.setString(4, item.element);
                stmt.setString(5, item.type);
                stmt.setString(6, "admin");
                stmt.setInt(7, packID);

                stmt.execute();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static int getPackID() throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                select max(packid) from "mtcg2".cards 
                """);

        ResultSet set = stmt.executeQuery();
        int id = 0;
        while (set.next()) {

            id = set.getInt(1);

        }
        return id;

    }

    private static int getOldestPack() throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                select min(packid) from "mtcg2".cards 
                where owner= ?
                """);

        stmt.setString(1, "admin");
        ResultSet set = stmt.executeQuery();
        int id = 0;
        while (set.next()) {

            id = set.getInt(1);
        }
        System.out.println(id);
        return id;

    }


    public static String buyPack(String token) throws SQLException {

        int packID = getOldestPack();

        if (packID == 0) {

            return "no more packs available";

        }

        String owner = getUsernameByToken(token);
        String response = "Error";

        try {
            int coins = checkCoins(owner);

            if (coins >= 5) {

                conn
                        .createStatement();

                PreparedStatement stmt = conn.prepareStatement("""
                        update "mtcg2".cards 
                        set owner = ?
                        where packid = ?
                        """);

                stmt.setString(1, owner);
                stmt.setInt(2, packID);
                stmt.execute();

            } else {
                return "not enough coins";
            }

            changeCoins(owner, 5);
            response = "Successfully bought a Pack";

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return response;

    }

    public static int checkCoins(String username) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                select coins from "mtcg2".players 
                where username = ?
                """);
        stmt.setString(1, username);
        ResultSet set = stmt.executeQuery();
        int coins = 0;
        while (set.next()) {

            coins = set.getInt(1);
        }
        return coins;

    }

    public static void changeCoins(String owner, int coins) throws SQLException {

        conn
                .createStatement();

        PreparedStatement stmt = conn.prepareStatement("""
                update "mtcg2".players 
                set coins = coins -?
                where username = ?
                """);

        stmt.setInt(1, coins);
        stmt.setString(2, owner);

        stmt.execute();

    }

    public static String getUsernameByToken(String token) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                select username from "mtcg2".players 
                where token = ?
                """);
        stmt.setString(1, token);
        ResultSet set = stmt.executeQuery();
        String owner = "";
        while (set.next()) {

            owner = set.getString(1);
        }
        return owner;

    }

    public static String getTokenByUsername(String username) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                select token from "mtcg2".players 
                where username = ?
                """);
        stmt.setString(1, username);
        ResultSet set = stmt.executeQuery();
        String owner = "";
        while (set.next()) {

            owner = set.getString(1);
        }
        return owner;

    }

    public static String AllOwnedCards(String token) throws SQLException {

        String owner = getUsernameByToken(token);
        if (owner == "") {
            return "No Cards found for that user";
        }
        String response = "Owned Cards:\n";

        PreparedStatement stmt = conn.prepareStatement("""
                select cardid, name, damage, element, type from "mtcg2".cards
                where owner= ?
                """);
        stmt.setString(1, owner);
        ResultSet set = stmt.executeQuery();
        while (set.next()) {

            response += "ID: " + set.getString(1);
            response += ", Name: " + set.getString(2);
            response += ", Damage: " + set.getInt(3);
            response += ", Element: " + set.getString(4);
            response += ", Type: " + set.getString(5) + "\n";

        }

        return response;
    }

    public static String getDeck(String token) throws SQLException {

        String owner = getUsernameByToken(token);

        if (owner == "") {
            return "No Deck found for that user";
        }
        String response = "Cards in Deck:\n";
        PreparedStatement stmt = conn.prepareStatement("""
                select cardid, name, damage, element, type from "mtcg2".cards
                where (owner= ? and indeck= 1)
                """);
        stmt.setString(1, owner);
        ResultSet set = stmt.executeQuery();
        while (set.next()) {

            response += "ID: " + set.getString(1);
            response += ", Name: " + set.getString(2);
            response += ", Damage: " + set.getInt(3);
            response += ", Element: " + set.getString(4);
            response += ", Type: " + set.getString(5) + "\n";

        }
        System.out.println("*********************************************************************************************************************************");
        System.out.println(response);
        System.out.println("*********************************************************************************************************************************");
        return response;


    }


    public static String getUserBio(String name, String token) throws SQLException {

        String username = getUsernameByToken(token);
        if (!username.equals(name)) {

            return "no permission";
        }
        String response = "";

        PreparedStatement stmt = conn.prepareStatement("""
                select Name, bio, image from "mtcg2".players
                where token = ?
                """);
        stmt.setString(1, token);
        ResultSet set = stmt.executeQuery();
        while (set.next()) {


            response += "Name: \n" + set.getString(1);
            response += "\nBio: \n" + set.getString(2);
            response += "\nImage: \n" + set.getString(3);


        }
        return response;

    }

    public static String updateUserBio(String name, String token, BioContent changes) throws SQLException {

        String username = getUsernameByToken(token);
        if (!username.equals(name)) {

            return "no permission";
        }
        String response = "";

        PreparedStatement stmt = conn.prepareStatement("""
                update "mtcg2".players 
                set Name = ?, Bio = ?, Image=?
                where token = ?
                """);
        stmt.setString(1, changes.Name);
        stmt.setString(2, changes.Bio);
        stmt.setString(3, changes.Image);
        stmt.setString(4, token);


        stmt.execute();
        return "Bio updatet";

    }


    public static String getElo(String token) throws SQLException {


        String username = getUsernameByToken(token);
        if (username.equals("")) {

            return "no user found";
        }
        String response = ("ELO for User: " + username);

        PreparedStatement stmt = conn.prepareStatement("""
                select elo,games, wins, losses from "mtcg2".players
                where username = ?
                """);
        stmt.setString(1, username);
        ResultSet set = stmt.executeQuery();
        while (set.next()) {


            response += "\nElo: " + set.getInt(1);
            response += "\nGames: " + set.getInt(2);
            response += "\nWins: " + set.getInt(3);
            response += "\nLosses: " + set.getInt(4);

        }
        return response;

    }

    public static String getScoreboard(String token) throws SQLException {

        String username = getUsernameByToken(token);
        if (username.equals("")) {

            return "permisson";
        }
        String response = ("Scoreboard:\n");

        PreparedStatement stmt = conn.prepareStatement("""
                select Name, elo, wins, losses from "mtcg2".players
                order by elo
                """);
        ResultSet set = stmt.executeQuery();
        PreparedStatement copystmt = conn.prepareStatement("""
                select Name, elo, wins, losses from "mtcg2".players
                order by elo
                """);
        ResultSet copy = copystmt.executeQuery();
        int counter = 0;
        int numberofentries = 0;
        while (copy.next()) {
            numberofentries++;
        }
        while (set.next()) {

            response += (numberofentries - counter) + "." + " Name: " + set.getString(1);
            response += " Elo: " + set.getInt(2);
            response += " W/L: " + set.getInt(3);
            response += "/" + set.getInt(4);

            response += "\n";
            counter++;

        }
        return response;


    }

    public static String addToBattleQueue(String token) throws SQLException {

        String username = getUsernameByToken(token);
        if (username.equals("")) {

            return "no permission";
        }
        String response = "";

        PreparedStatement stmt = conn.prepareStatement("""
                update "mtcg2".players 
                set battle= ?
                where token = ?
                """);
        stmt.setInt(1, 1);
        stmt.setString(2, token);


        stmt.execute();
        return "added to battle";

    }

    public static boolean checkIfEnoughCombatants() throws SQLException {


        PreparedStatement stmt = conn.prepareStatement("""
                select count(battle) from "mtcg2".players
                where battle = 1
                """);
        int counter = 0;
        ResultSet set = stmt.executeQuery();
        while (set.next()) {

            counter = set.getInt(1);

        }

        return counter >= 2;
    }

    public static Combatant getCombatant() throws SQLException {

        Combatant combatant = new Combatant();
        String duellistName = getNameCombatant();

        combatant.name = duellistName;

        combatant.deck = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("""
                select  cardid, name, damage, element, type
                from "mtcg2".cards
                where (owner = ? and indeck = 1)
                """);
        stmt.setString(1, duellistName);
        ResultSet set = stmt.executeQuery();
        List<Card> cards = new ArrayList<>();
        while (set.next()) {
            Card deckcard = new Card(
                    set.getString(1),
                    set.getString(2),
                    set.getInt(3),
                    set.getString(4),
                    set.getString(5));


            cards.add(deckcard);
        }
        combatant.deck = cards;

        setBattleToFalse(duellistName);


        return combatant;

    }

    private static void setBattleToFalse(String name) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                update "mtcg2".players 
                set battle= ?
                where username = ?
                """);
        stmt.setInt(1, 0);
        stmt.setString(2, name);
        stmt.execute();

    }

    private static String getNameCombatant() throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                select username from "mtcg2".players
                where battle = 1
                """);
        String name = "should not have this name";
        ResultSet set = stmt.executeQuery();
        while (set.next()) {

            name = set.getString(1);

        }
        return name;
    }

    public static String getAdminToken() throws SQLException {

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");


        PreparedStatement stmt = conn.prepareStatement("""
                select token from "mtcg2".players
                where username= ?
                """);
        String token = "";
        stmt.setString(1, "admin");
        ResultSet set = stmt.executeQuery();
        while (set.next()) {

            token = set.getString(1);

        }

        DB.closeConnection();


        return token;
    }

    public static void exchangeCards(String winner, String loser) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                update "mtcg2".cards
                set owner= ?, indeck = 0
                where (owner = ? and indeck = 1)
                """);
        stmt.setString(1, winner);
        stmt.setString(2, loser);
        stmt.execute();
    }

    public static void changeElo(String name, int score) throws SQLException {

        int win = 0;
        int loss = 0;
        if (score > 0) {
            win = 1;
        }
        if (score < 0) {
            loss = 1;
        }
        else {
            score = 1;
        }
        //System.out.println("*******************************************************************************************");
        //System.out.println(name);
        //System.out.println(i);
        //System.out.println(win);
        //System.out.println(loss);
        //System.out.println("*******************************************************************************************");
        PreparedStatement stmt = conn.prepareStatement("""
                update "mtcg2".players
                set elo = elo + ?, games= games + 1, wins= wins + ?, losses = losses + ?
                where username = ?
                """);
        stmt.setInt(1, score);

        stmt.setInt(2, win);
        stmt.setInt(3, loss);
        stmt.setString(4, name);
        stmt.execute();


    }

    private static int checkCardsPossession(String owner, String cardid) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("""
                select count(*) from "mtcg2".cards
                where (owner= ? and cardid= ?)
                """);
        stmt.setString(1, owner);
        stmt.setString(2, cardid);
        ResultSet set = stmt.executeQuery();
        int response = 0;
        while (set.next()) {

            response = set.getInt(1);

        }
        return response;

    }





    public static String createDeck(String token, List<String> deck) throws SQLException {

        String owner = getUsernameByToken(token);
        if (owner == "") {
            return "Error, User doesnt own these cards";
        }
        for (int i = 0; i < deck.size(); i++) {

            if (checkCardsPossession(owner, deck.get(i)) == 0) {
                return "Error, User doesnt own these cards";
            }

        }

        deleteDeck(owner);

        try {

            conn
                    .createStatement();


            PreparedStatement stmt = conn.prepareStatement("""
                    update "mtcg2".cards 
                    set indeck = 1
                    where (owner = ? and cardid = ?)
                    """);


            Iterator iterator = deck.iterator();
            while (iterator.hasNext()) {

                System.out.println(owner);
                stmt.setString(1, owner);
                stmt.setString(2, String.valueOf(iterator.next()));

                stmt.execute();
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return "Success";
    }

    public static void deleteDeck(String owner) throws SQLException {

        conn
                .createStatement();

        PreparedStatement stmt = conn.prepareStatement("""
                update "mtcg2".cards 
                set indeck = 0
                where owner = ?
                """);

        stmt.setString(1, owner);

        stmt.execute();

    }


    public static String reset(String token) throws SQLException {

        String owner = getUsernameByToken(token);
        //System.out.println("***********************************************");
        //System.out.println(owner);
        //System.out.println("***********************************************");
        if (owner == "") {
            System.out.println(owner);
            return "User does not exist";
        }
        PreparedStatement stmt = conn.prepareStatement("""
                delete from "mtcg2".cards
                where owner = ?
                """);
        stmt.setString(1, owner);
        stmt.execute();
        changeCoins(owner, -20);
        return "Removed all Cards and added 20 coins.\n";
    }

}