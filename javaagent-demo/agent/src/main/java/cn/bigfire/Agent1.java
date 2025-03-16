package cn.bigfire;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：xushu
 * @ Date   ：2024/10/30  23:12
 * @ Desc   ：
 */
public class Agent1 {

    /**
     * 可以运行在main方法启动前
     * @param agent             输入的参数
     */
    public static void premain(String agent){
        // 方法启动前
        System.out.println("增强:" + agent);
    }

}
