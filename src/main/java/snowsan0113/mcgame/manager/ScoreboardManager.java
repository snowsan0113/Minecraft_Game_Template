package snowsan0113.mcgame.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import snowsan0113.mcgame.Main;

import java.util.Set;

public class ScoreboardManager {

    private static final Scoreboard new_scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private static Objective game_obj = new_scoreboard.getObjective("BuildBattle");
    private static BukkitRunnable board_runnable;

    public static Objective getObjective() {
        if (game_obj != null) {
            createObjective();
        }

        return game_obj;
    }

    public static Scoreboard getScoreboard() {
        if (game_obj == null) {
            createObjective();
        }

        return game_obj.getScoreboard();
    }

    private static Objective createObjective() {
        if (game_obj == null) {
            game_obj = new_scoreboard.registerNewObjective("BuildBattle", "dummy");
            game_obj.setDisplayName("BuildBattle");

            Bukkit.getLogger().info("[ScoreboardManager] スコアボードを作成しました。");
        }

        return game_obj;
    }

    public static void setScoreboard(GameManager.GameStatus status) {
        if (game_obj != null) {
            resetScore();
            getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);

            if (status == GameManager.GameStatus.WAITING) {
                //ゲーム開始前
            }
            else if (status == GameManager.GameStatus.RUNNING) {
                //ゲーム中
            }
            else if (status == GameManager.GameStatus.ENDING) {
                //ゲーム終了後
                game_obj.getScore(" ").setScore(29);
                game_obj.getScore( ChatColor.GOLD + "ゲーム終了!!").setScore(28);
                game_obj.getScore("   ").setScore(22);
            }

            updateScoreboard();
            Bukkit.getLogger().info("[ScoreboardManager] スコアボードを設定しました。（type=" + status.name() + ")");
        }
        else {
            createObjective();
            setScoreboard(status);
        }
    }

    private static void updateScoreboard() {
        if (board_runnable == null) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    //スコアボードセット
                    Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(ScoreboardManager.getScoreboard()));

                    board_runnable=this;
                }
            }.runTaskTimer(Main.getPlugin(Main.class), 0L, 2L);
        }
    }

    public static void resetScore() {
        Set<String> scores = getScoreboard().getEntries();

        if (scores != null) {
            for (String score : scores) {
                getScoreboard().resetScores(score);
            }
        }
    }

}
