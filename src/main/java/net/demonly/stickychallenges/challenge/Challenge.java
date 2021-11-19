package net.demonly.stickychallenges.challenge;

import lombok.Data;

@Data
public class Challenge {
    private int id;
    private String name;
    private long startDate;
    private long endDate;
    private ChallengeType type;
    private String objective;
    private long amount;
    private long currentAmount;
}
