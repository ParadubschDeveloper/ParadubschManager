package de.paradubsch.paradubschmanager.lifecycle.jobs;

import lombok.Getter;

public enum JobLevel {
    ONE("Level 1", 1, 1),
    TWO("Level 2", 2, 1.5f),
    THREE("Level 3", 3, 1.8f),
    FOUR("Level 4", 7, 2f),
    FIVE("Level 5", 15, 2.5f),
    SIX("Level 6", 32, 2.8f),
    SEVEN("Level 7", 96, 3f),
    EIGHT("Level 8", 256, 3.8f),
    NINE("Level 9", 1024, 4.7f),
    TEN("Level 10", 4096, 5f),
    MAX("Maximum", 0, 0);

    @Getter
    private final String name;

    @Getter
    private final int difficulty;

    @Getter
    private final float multiplier;

    JobLevel(String name, int difficulty, float multiplier) {
        this.name = name;
        this.difficulty = difficulty;
        this.multiplier = multiplier;
    }

    public JobLevel nextLevel() {
        switch (this) {
            case ONE:
                return TWO;
            case TWO:
                return THREE;
            case THREE:
                return FOUR;
            case FOUR:
                return FIVE;
            case FIVE:
                return SIX;
            case SIX:
                return SEVEN;
            case SEVEN:
                return EIGHT;
            case EIGHT:
                return NINE;
            case NINE:
                return TEN;
            case TEN:
            default:
                return MAX;
        }
    }
}
