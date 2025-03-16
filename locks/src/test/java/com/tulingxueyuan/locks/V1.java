package com.tulingxueyuan.locks;

/***
 * @Author 徐庶
 * @Slogan 致敬大师，致敬未来的你
 *
 * 线程不安全——导致超卖
 */
public class V1 {

    public static Long stock=1L;

    /**
     * 下单
     */
    public static void placeOrder() throws Exception {
        if (stock > 0) {
            Thread.sleep(100);
            stock--;
            System.out.println(Thread.currentThread().getName() + "秒杀成功");
        } else {
            System.out.println(Thread.currentThread().getName() + "秒杀失败！库存不足");
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i=0;i<3;i++){
            new Thread(() -> {
                try {
                    placeOrder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
