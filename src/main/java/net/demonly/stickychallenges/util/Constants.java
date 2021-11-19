package net.demonly.stickychallenges.util;

public class Constants {

    public static String SQL_GET_CURRENT_CHALLENGE = "SELECT * FROM challenge";
    public static String SQL_GET_PREVIOUS_CHALLENGE = "SELECT * FROM challenges_previous WHERE challenge_id = ?";
    public static String SQL_GET_CONTRIBUTOR = "SELECT * FROM challenge_contributors WHERE contributor_uuid = ? and contributor_challenge_id=?";
    public static String SQL_GET_TOP_CONTRIBUTORS = "SELECT contributor_uuid FROM challenge_contributors WHERE contributor_challenge_id = ? and contributor_amount > 0 ORDER BY contributor_amount DESC LIMIT 3";
    public static String SQL_INSERT_PREVIOUS_CHALLENGE = "INSERT INTO challenges_previous (challenge_id, challenge_name, challenge_start_date, challenge_end_date, challenge_type, challenge_objective, challenge_amount, challenge_current_amount) VALUES (?,?,?,?,?,?,?,?)";
    public static String SQL_INSERT_CHALLENGE = "INSERT INTO challenge (challenge_id, challenge_name, challenge_start_date, challenge_end_date, challenge_type, challenge_objective, challenge_amount, challenge_current_amount) VALUES (?,?,?,?,?,?,?,?)";
    public static String SQL_INSERT_CHALLENGE_CONTRIBUTOR = "INSERT INTO challenge_contributors (contributor_uuid, contributor_challenge_id, contributor_amount, contributor_claimed_reward) VALUES(?,?,?,?)";
    public static String SQL_UPDATE_CURRENT_CHALLENGE = "UPDATE challenge SET challenge_id=?, challenge_name=?, challenge_start_date=?, challenge_end_date=?, challenge_type=?, challenge_objective=?, challenge_amount=?, challenge_current_amount=?";
    public static String SQL_UPDATE_CHALLENGE_CONTRIBUTOR = "UPDATE challenge_contributors SET contributor_amount=?, contributor_claimed_reward=? WHERE contributor_uuid=? and contributor_challenge_id=?";
    public static String SQL_CREATE_CHALLENGE_TABLE = "CREATE TABLE IF NOT EXISTS challenge ( " +
                    "challenge_id INTEGER NOT NULL, " +
                    "challenge_name TEXT NOT NULL, " +
                    "challenge_start_date INTEGER NOT NULL, " +
                    "challenge_end_date INTEGER NOT NULL, " +
                    "challenge_type TEXT NOT NULL, " +
                    "challenge_objective TEXT NOT NULL, " +
                    "challenge_amount INTEGER NOT NULL," +
                    "challenge_current_amount INTEGER NOT NULL" +
                " )";
    public static String SQL_CREATE_CHALLENGE_PREVIOUS_TABLE = "CREATE TABLE IF NOT EXISTS challenges_previous ( " +
                    "challenge_id INTEGER NOT NULL, " +
                    "challenge_name TEXT NOT NULL, " +
                    "challenge_start_date INTEGER NOT NULL, " +
                    "challenge_end_date INTEGER NOT NULL, " +
                    "challenge_type TEXT NOT NULL, " +
                    "challenge_objective TEXT NOT NULL, " +
                    "challenge_amount INTEGER NOT NULL," +
                    "challenge_current_amount INTEGER NOT NULL" +
                " )";
    public static String SQL_CREATE_CHALLENGE_CONTRIBUTORS_TABLE = "CREATE TABLE IF NOT EXISTS challenge_contributors ( " +
                    "contributor_uuid TEXT NOT NULL," +
                    "contributor_challenge_id INTEGER NOT NULL," +
                    "contributor_amount INTEGER NOT NULL," +
                    "contributor_claimed_reward BOOLEAN NOT NULL CHECK (contributor_claimed_reward IN (0, 1))" +
                " )";

}
