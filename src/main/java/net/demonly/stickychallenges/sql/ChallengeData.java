package net.demonly.stickychallenges.sql;

import net.demonly.stickychallenges.StickyChallengesPlugin;
import net.demonly.stickychallenges.challenge.Challenge;
import net.demonly.stickychallenges.challenge.ChallengeContributor;
import net.demonly.stickychallenges.challenge.ChallengeType;
import net.demonly.stickychallenges.util.Constants;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class ChallengeData {

    private SQLiteDataSource dataSource;
    private final StickyChallengesPlugin plugin;

    public ChallengeData(StickyChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(String.format("jdbc:sqlite:%s/%s",
                plugin.getDataFolder().getPath(),
                plugin.getConfig().getString("database_name"))
        );
        initTable(Constants.SQL_CREATE_CHALLENGE_TABLE);
        initTable(Constants.SQL_CREATE_CHALLENGE_CONTRIBUTORS_TABLE);
        initTable(Constants.SQL_CREATE_CHALLENGE_PREVIOUS_TABLE);
    }

    private void initTable(String sql) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR ON INIT TABLE", e);
        }
    }

    public void insertChallenge(Challenge challenge) {
        if (challenge != null) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_INSERT_CHALLENGE)) {
                statement.setInt(1, challenge.getId());
                statement.setString(2, challenge.getName());
                statement.setLong(3, challenge.getStartDate());
                statement.setLong(4, challenge.getEndDate());
                statement.setString(5, challenge.getType().name());
                statement.setString(6, challenge.getObjective());
                statement.setLong(7, challenge.getAmount());
                statement.setLong(8, challenge.getCurrentAmount());
                statement.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "ERROR INSERTING CHALLENGE", e);
            }
        }
    }

    public void insertPreviousChallenge(Challenge challenge) {
        if (challenge != null) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_INSERT_PREVIOUS_CHALLENGE)) {
                statement.setInt(1, challenge.getId());
                statement.setString(2, challenge.getName());
                statement.setLong(3, challenge.getStartDate());
                statement.setLong(4, challenge.getEndDate());
                statement.setString(5, challenge.getType().name());
                statement.setString(6, challenge.getObjective());
                statement.setLong(7, challenge.getAmount());
                statement.setLong(8, challenge.getCurrentAmount());
                statement.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "ERROR INSERTING PREVIOUS CHALLENGE", e);
            }
        }
    }

    public void insertContributor(ChallengeContributor contributor) {
        if (contributor != null) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_INSERT_CHALLENGE_CONTRIBUTOR)) {
                statement.setString(1, contributor.getUuid().toString());
                statement.setInt(2, contributor.getChallengeId());
                statement.setLong(3, contributor.getAmount());
                statement.setBoolean(4, contributor.isClaimed());
                statement.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "ERROR INSERTING CONTRIBUTOR", e);
            }
        }
    }

    public ChallengeContributor getContributor(UUID uuid, int challengeId) {
        if (uuid != null) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_GET_CONTRIBUTOR)) {
                ChallengeContributor contributor = new ChallengeContributor();

                statement.setString(1, uuid.toString());
                statement.setInt(2, challengeId);
                statement.execute();

                ResultSet rs = statement.getResultSet();
                if (rs.next()) {
                    contributor.setUuid(UUID.fromString(rs.getString("contributor_uuid")));
                    contributor.setChallengeId(rs.getInt("contributor_challenge_id"));
                    contributor.setAmount(rs.getLong("contributor_amount"));
                    contributor.setClaimed(rs.getBoolean("contributor_claimed_reward"));

                    return contributor;
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "ERROR GETTING CONTRIBUTOR FOR UUID: " + uuid.toString() + " FROM CHALLENGE ID: " + challengeId, e);
            }
        }
        return null;
    }

    public Challenge getCurrentChallenge() {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_GET_CURRENT_CHALLENGE)) {
            Challenge challenge = new Challenge();
            statement.execute();

            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                challenge.setId(rs.getInt("challenge_id"));
                challenge.setName(rs.getString("challenge_name"));
                challenge.setStartDate(rs.getLong("challenge_start_date"));
                challenge.setEndDate(rs.getLong("challenge_end_date"));
                challenge.setType(ChallengeType.valueOf(rs.getString("challenge_type")));
                challenge.setObjective(rs.getString("challenge_objective"));
                challenge.setAmount(rs.getLong("challenge_amount"));
                challenge.setCurrentAmount(rs.getLong("challenge_current_amount"));

                return challenge;
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR GETTING CURRENT CHALLENGE", e);
        }
        return null;
    }

    public Challenge getPreviousChallenge(int id) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_GET_PREVIOUS_CHALLENGE)) {
            Challenge challenge = new Challenge();
            statement.setInt(1, id);
            statement.execute();

            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                challenge.setId(rs.getInt("challenge_id"));
                challenge.setName(rs.getString("challenge_name"));
                challenge.setStartDate(rs.getLong("challenge_start_date"));
                challenge.setEndDate(rs.getLong("challenge_end_date"));
                challenge.setType(ChallengeType.valueOf(rs.getString("challenge_type")));
                challenge.setObjective(rs.getString("challenge_objective"));
                challenge.setAmount(rs.getLong("challenge_amount"));
                challenge.setCurrentAmount(rs.getLong("challenge_current_amount"));

                return challenge;
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR GETTING PREVIOUS CHALLENGE FOR ID " + id, e);
        }
        return null;
    }

    public void getTopContributors(int challengeId, HashMap<Integer, UUID> topContributors) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_GET_TOP_CONTRIBUTORS)) {
            statement.setInt(1, challengeId);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            int i = 1;
            while (rs.next()) {
                topContributors.put(i, UUID.fromString(rs.getString("contributor_uuid")));
                i++;
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR GETTING TOP CONTRIBUTORS FOR CHALLENGE ID " + challengeId, e);
        }
    }

    public void updateContributor(ChallengeContributor challengeContributor) {
        if (challengeContributor != null) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_UPDATE_CHALLENGE_CONTRIBUTOR)) {
                statement.setLong(1, challengeContributor.getAmount());
                statement.setBoolean(2, challengeContributor.isClaimed());
                statement.setString(3, challengeContributor.getUuid().toString());
                statement.setInt(4, challengeContributor.getChallengeId());

                if (statement.executeUpdate() == 0) {
                    insertContributor(challengeContributor);
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "ERROR UPDATING CONTRIBUTOR", e);
            }
        }
    }

    public void updateCurrentChallenge(Challenge challenge) {
        if (challenge != null) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(Constants.SQL_UPDATE_CURRENT_CHALLENGE)) {
                statement.setInt(1, challenge.getId());
                statement.setString(2, challenge.getName());
                statement.setLong(3, challenge.getStartDate());
                statement.setLong(4, challenge.getEndDate());
                statement.setString(5, challenge.getType().name());
                statement.setString(6, challenge.getObjective());
                statement.setLong(7, challenge.getAmount());
                statement.setLong(8, challenge.getCurrentAmount());
                statement.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "ERROR UPDATING CURRENT CHALLENGE", e);
            }
        }
    }
}