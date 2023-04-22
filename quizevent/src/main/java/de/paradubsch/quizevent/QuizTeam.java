package de.paradubsch.quizevent;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuizTeam {
    private final String name;
    private int points;

    private final List<Player> players = new ArrayList<>();

    private final ChatColor teamColor;

    private Location pultLocation;

    private Location buzzerLocation;

    public QuizTeam(String name, ChatColor teamColor) {
        this.name = name;
        this.teamColor = teamColor;
        this.points = 0;
    }

    @Override
    public String toString() {
        return "QuizTeam{" +
                "name='" + name + '\'' +
                ", points=" + points +
                ", players=" + players +
                ", teamColor=" + teamColor +
                ", pultLocation=" + pultLocation +
                ", buzzerLocation=" + buzzerLocation +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public ChatColor getTeamColor() {
        return teamColor;
    }

    public Location getPultLocation() {
        return pultLocation;
    }

    public void setPultLocation(Location pultLocation) {
        this.pultLocation = pultLocation;
    }

    public Location getBuzzerLocation() {
        return buzzerLocation;
    }

    public void setBuzzerLocation(Location buzzerLocation) {
        this.buzzerLocation = buzzerLocation;
    }
}
