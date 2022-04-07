package Game;

import lombok.Getter;

import java.sql.SQLException;
import java.util.Random;

public class Battle {

    private static Combatant combatant_a;
    private static Combatant combatant_b;
    @Getter
    private static String log;


    public Battle() throws SQLException {
        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        combatant_a= DB.getCombatant();
        combatant_b= DB.getCombatant();
        log="";
        DB.closeConnection();

    }

    //Needed for testing.
    public Battle(int foo) throws SQLException {
        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        log="";
        DB.closeConnection();

    }

    public static boolean checkforTwoPlayers() throws SQLException {

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        boolean foo= DB.checkIfEnoughCombatants();
        DB.closeConnection();

        return foo;
    }

    public static float getFactor(Card card_a, Card card_b){


        if(card_a.getName().contains("Goblin") && card_b.getName().contains("Dragon")){
            return 0;
        }
        else if(card_a.getName().contains("Ork") && card_b.getName().contains("Wizzard")){
            return 0;
        }
        else if(card_a.getName().contains("Knight") && card_b.getName().contains("WaterSpell")){
            return 0;
        }
        else if(card_a.getName().contains("Spell") && card_b.getName().contains("Kraken")){
            return 0;
        }
        else if(card_a.getName().contains("Dragon") && card_b.getName().contains("FireElves")){
            return 0;
        }


        if(!(card_a.getType().equals("Monster") && card_b.getType().equals("Monster"))){

            if(card_a.getElement().equals("Water") && card_b.getElement().equals("Fire")){
                return 2;
            }
            else if(card_a.getElement().equals("Fire") && card_b.getElement().equals("Normal")){
                return 2;
            }
            else if(card_a.getElement().equals("Normal") && card_b.getElement().equals("Water")){
                return 2;
            }
            else if(card_a.getElement().equals("Fire") && card_b.getElement().equals("Water")){
                return (float) 0.5;
            }
            else if(card_a.getElement().equals("Normal") && card_b.getElement().equals("Fire")){
                return (float) 0.5;
            }
            else if(card_a.getElement().equals("Water") && card_b.getElement().equals("Normal")){
                return (float) 0.5;
            }

        }

        return 1;
    }


    public static String startBattle() throws SQLException {

        Random rng = new Random();
        int round=1;
        log+="log:\n";
        float damage_a;
        float damage_b;
        int r_a;
        int r_b;
        if (combatant_b.deck.size()!=4 || combatant_a.deck.size()!=4){

            return "At least one player does not have a full deck selected.";
        }



        while(combatant_a.deck.size()!=0 && combatant_b.deck.size()!=0 && round<=100){
            r_a=rng.nextInt(combatant_a.deck.size());
            r_b=rng.nextInt(combatant_b.deck.size());

            //calculate real damage
            damage_a=combatant_a.deck.get(r_a).getDamage() * getFactor(combatant_a.deck.get(r_a), combatant_b.deck.get(r_b));
            damage_b=combatant_b.deck.get(r_b).getDamage() * getFactor(combatant_b.deck.get(r_b), combatant_a.deck.get(r_a));

            log+="\nRound " +round+":\n\n";
            log+=combatant_a.name + ": " + combatant_a.deck.get(r_a).getName()+ "  <<Damage: " +combatant_a.deck.get(r_a).getDamage() + ">>";
            log+=combatant_b.name + ": " + combatant_b.deck.get(r_b).getName()+ "  <<Damage:  " +combatant_b.deck.get(r_b).getDamage() +  ">>" + "\n";
            log+=damage_a + " VS " + damage_b + "\n";


            if(damage_a>damage_b){
                log+= combatant_a.deck.get(r_a).getName() + " defeats " +combatant_b.deck.get(r_b).getName()+ "\n";
                log+= "\n\t" + combatant_a.name + " wins this round!\n";
                combatant_a.deck.add(combatant_b.deck.get(r_b));
                combatant_b.deck.remove(r_b);
            }else if (damage_a<damage_b){
                log+= combatant_b.deck.get(r_b).getName() + " defeats " +combatant_a.deck.get(r_a).getName()+ "\n";
                log+= "\n\t" + combatant_b.name + " wins this round!\n";
                combatant_b.deck.add(combatant_a.deck.get(r_a));
                combatant_a.deck.remove(r_a);
            }else{
                log+="This round is a draw!\n";
            }
            log+= "\t" + combatant_a.name + ": " + combatant_a.deck.size() + " cards left.\n";
            log+= "\t" + combatant_b.name + ": " + combatant_b.deck.size() + " cards left.\n\n";
            round++;
            if(combatant_a.deck.size()<1){
                break;
            }
            if(combatant_b.deck.size()<1){
                break;
            }
        }

        System.out.println(log);

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        if(combatant_a.deck.size()<1){
            log+= "\n\n\n***" + combatant_b.name + " WON THIS MATCH***\n";
            DB.exchangeCards(combatant_a.name, combatant_b.name);
            DB.changeElo(combatant_a.name, 3);
            DB.changeElo(combatant_b.name, -5);

        }else if(combatant_b.deck.size()<1){
            log+= "\n\n\n***" + combatant_a.name + " WON THIS MATCH***\n";
            DB.exchangeCards(combatant_b.name, combatant_a.name);
            DB.changeElo(combatant_b.name, 3);
            DB.changeElo(combatant_a.name, -5);
        }
        else{
            log+= "\n\n\n***NEITHER PLAYER WON THIS MATCH***\n";
            DB.changeElo(combatant_a.name, 0);
            DB.changeElo(combatant_b.name, 0);
        }
        DB.closeConnection();

        return log;



    }

    public void setCombatant_a(Combatant combatant_a) {
        Battle.combatant_a = combatant_a;
    }

    public void setCombatant_b(Combatant combatant_b) {
        Battle.combatant_b = combatant_b;
    }


}
