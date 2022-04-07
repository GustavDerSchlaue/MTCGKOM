package Tests;

import Game.Battle;
import Game.Card;
import Game.Combatant;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleTest {

    //MONSTER MULTIPLIER
    @Test
    public void BattleTestWatervsFireMonster()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "WaterGoblin", 10, "Water", "Monster" );
        Card card_b = new Card("foo", "FireGoblin", 10, "Fire", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 1, true);
    }
    @Test
    public void BattleTestFirevsWaterMonster()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "FireGoblin", 10, "Fire", "Monster" );
        Card card_b = new Card("foo", "WaterGoblin", 10, "Water", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 1, true);
    }
    @Test
    public void BattleTestWatervsWaterMonster()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "WaterGoblin", 10, "Water", "Monster" );
        Card card_b = new Card("foo", "WaterGoblin", 10, "Water", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 1, true);
    }


    //SPELL MULTIPLIER

    @Test
    public void BattleTestWatervsWater()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "WaterGoblin", 10, "Water", "Monster" );
        Card card_b = new Card("foo", "WaterGoblin", 10, "Water", "Spell" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 1, true);
    }
    @Test
    public void BattleTestFirevsFire()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "FireGoblin", 10, "Fire", "Monster" );
        Card card_b = new Card("foo", "FireGoblin", 10, "Fire", "Spell" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 1, true);
    }
    @Test
    public void BattleTestNormalvsNormal()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "Goblin", 10, "Normal", "Monster" );
        Card card_b = new Card("foo", "Goblin", 10, "Normal", "Spell" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 1, true);
    }
    @Test
    public void BattleTestWatervsFire()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "WaterGoblin", 10, "Water", "Monster" );
        Card card_b = new Card("foo", "Firespell", 55, "Fire", "Spell" );
        float multiplier = battle.getFactor(card_a, card_b);
        System.out.println(multiplier);
        assertEquals(multiplier == 2, true);
    }
    @Test
    public void BattleTestFirevsWater()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "Firespell", 55, "Fire", "Spell" );
        Card card_b = new Card("foo", "WaterGoblin", 10, "Water", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 0.5, true);
    }
    @Test
    public void BattleTestFirevsNormal()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "Firespell", 55, "Fire", "Spell" );
        Card card_b = new Card("foo", "NormalOrc", 10, "Normal", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 2.0, true);
    }
    @Test
    public void BattleTestNormalvsFire()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "NormalOrc", 10, "Normal", "Monster" );
        Card card_b = new Card("foo", "Firespell", 55, "Fire", "Spell" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 0.5, true);
    }



    //SPECIALITIES
    @Test
    public void BattleTestGoblinOnDragon()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "NormalGoblin", 10, "Normal", "Monster" );
        Card card_b = new Card("foo", "WaterDragon", 55, "Water", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 0, true);
    }
    @Test
    public void BattleTestWizzardOnOrcs()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "NormalOrk", 10, "Normal", "Monster" );
        Card card_b = new Card("foo", "WaterWizzard", 55, "Water", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 0, true);
    }
    @Test
    public void BattleTestKnightOnWater()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "Knight", 55, "Normal", "Monster" );
        Card card_b = new Card("foo", "WaterSpell", 10, "Water", "Spell" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 0, true);
    }
    @Test
    public void BattleTestSpellOnKraken()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "WaterSpell", 10, "Water", "Spell" );
        Card card_b = new Card("foo", "Kraken", 10, "Water", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 0, true);
    }
    @Test
    public void BattleTestFireElvesOnDragons()  throws SQLException {
        Battle battle;
        battle = new Battle();
        Card card_a = new Card("foo", "Dragon", 10, "Normal", "Monster" );
        Card card_b = new Card("foo", "FireElves", 10, "Fire", "Monster" );
        float multiplier = battle.getFactor(card_a, card_b);
        assertEquals(multiplier == 0, true);
    }




    //FULL MATCH TESTS

    @Test
    public void BattleTestVictoryA()  throws SQLException {
        boolean doescontain = false;
        Combatant combatant_a = new Combatant();
        combatant_a.name = "a";
        combatant_a.deck = new ArrayList<>();
        Combatant combatant_b = new Combatant();
        combatant_b.name = "b";
        combatant_b.deck = new ArrayList<Card>();
        for (int i = 0; i<4; i++)
        {
            combatant_a.deck.add(new Card("foo", "WaterGoblin", 10000, "Water", "Spell"));
            combatant_b.deck.add(new Card("foo", "FireBaby", 1, "Fire", "Monster"));
        }
        Battle battle = new Battle(1);
        battle.setCombatant_a(combatant_a);
        battle.setCombatant_b(combatant_b);
        String log = battle.startBattle();
        if (log.contains("***a WON THIS MATCH***"))
        {
            doescontain = true;
        }
        assertEquals( doescontain, true);

    }
    @Test
    public void BattleTestVictoryB()  throws SQLException {
        boolean doescontain = false;
        Combatant combatant_a = new Combatant();
        combatant_a.name = "a";
        combatant_a.deck = new ArrayList<>();
        Combatant combatant_b = new Combatant();
        combatant_b.name = "b";
        combatant_b.deck = new ArrayList<Card>();
        for (int i = 0; i<4; i++)
        {
            combatant_a.deck.add(new Card("foo", "FireBaby", 1, "Fire", "Monster"));
            combatant_b.deck.add(new Card("foo", "WaterGoblin", 10000, "Water", "Spell"));
        }
        Battle battle = new Battle(1);
        battle.setCombatant_a(combatant_a);
        battle.setCombatant_b(combatant_b);
        String log = battle.startBattle();
        if (log.contains("***b WON THIS MATCH***"))
        {
            doescontain = true;
        }
        assertEquals( doescontain, true);

    }
    @Test
    public void BattleTestDraw()  throws SQLException {
        boolean doescontain = false;
        Combatant combatant_a = new Combatant();
        combatant_a.name = "a";
        combatant_a.deck = new ArrayList<>();
        Combatant combatant_b = new Combatant();
        combatant_b.name = "b";
        combatant_b.deck = new ArrayList<Card>();
        for (int i = 0; i<4; i++)
        {
            combatant_a.deck.add(new Card("foo", "WaterGoblin", 10000, "Water", "Spell"));
            combatant_b.deck.add(new Card("foo", "WaterGoblin", 10000, "Water", "Spell"));
        }
        Battle battle = new Battle(1);
        battle.setCombatant_a(combatant_a);
        battle.setCombatant_b(combatant_b);
        String log = battle.startBattle();
        if (log.contains("***NEITHER PLAYER WON THIS MATCH***"))
        {
            doescontain = true;
        }
        assertEquals( doescontain, true);

    }
    @Test
    public void BattleTestNotEnoughCards()  throws SQLException {
        boolean doescontain = false;
        Combatant combatant_a = new Combatant();
        combatant_a.name = "a";
        combatant_a.deck = new ArrayList<>();
        Combatant combatant_b = new Combatant();
        combatant_b.name = "b";
        combatant_b.deck = new ArrayList<Card>();
        for (int i = 0; i<3; i++)
        {
            combatant_a.deck.add(new Card("foo", "WaterGoblin", 10000, "Water", "Spell"));
            combatant_b.deck.add(new Card("foo", "FireBaby", 1, "Fire", "Monster"));
        }
        Battle battle = new Battle(1);
        battle.setCombatant_a(combatant_a);
        battle.setCombatant_b(combatant_b);
        String log = battle.startBattle();
        if (log.contains("At least one player does"))
        {
            doescontain = true;
        }
        assertEquals( doescontain, true);

    }
}
