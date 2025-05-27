package snowsan0113.mcgame.manager;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import snowsan0113.mcgame.Main;
import snowsan0113.mcgame.util.ChatUtil;

public class GameManager {

    private static BukkitTask task;
    private static int time;
    private static GameStatus status;

    static {
        time = 60*15;
        status = GameStatus.WAITING;
    }

    /**
     * ゲームを開始するメゾット
     * @return 0: 正常、1：既に始まってる、それ以外：？？
     */
    public static int startGame() {
        if (status == GameStatus.WAITING) {
            final int[] count = {10};
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (status == GameStatus.WAITING || status == GameStatus.COUNTING) {
                        status = GameStatus.COUNTING;
                        if (count[0] <= 0) {
                            ChatUtil.sendGlobalMessage("ゲーム開始!!");
                            status = GameStatus.RUNNING;
                        }
                        else {
                            ChatUtil.sendGlobalMessage(String.format("ゲーム開始まであと%d秒", count[0]));
                            count[0]--;
                        }
                    }
                    else {
                        if (time == 0) {
                            ChatUtil.sendGlobalMessage("ゲーム終了!!");
                            this.cancel();
                        }

                        time--;
                    }
                }
            }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);

            return 0;
        }

        return 1;
    }

    public static void resetGame() {
        if (task != null) {
            task.cancel();
            status = GameStatus.WAITING;
            time = 60*15;
        }
    }

    public static int getTime() {
        return time;
    }

    public static void setTime(int time) {
        GameManager.time = time;
    }

    public static GameStatus getStatus() {
        return status;
    }

    public static void setStatus(GameStatus status) {
        GameManager.status = status;
    }

    public static BukkitTask getTask() {
        return task;
    }

    public enum GameStatus {
        WAITING,
        COUNTING,
        RUNNING,
        ENDING,
        UNKNOWN
    }

}
