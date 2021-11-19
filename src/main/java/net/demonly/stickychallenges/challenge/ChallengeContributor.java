package net.demonly.stickychallenges.challenge;

import lombok.Data;

import java.util.UUID;

@Data
public class ChallengeContributor {
    private UUID uuid;
    private int challengeId;
    private long amount;
    private boolean claimed;
}
