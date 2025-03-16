package com.tulingxueyuan.order;

public abstract class PaymentTemplate {

    // 模板方法，定义支付流程
    public final void processPayment() {
        // 验证
        validatePaymentInfo();
        // 支付
        executePayment();
    }

    // 私有方法，提供默认的支付信息验证逻辑
    private void validatePaymentInfo() {

        // todo ...默认的验证逻辑
        // 处理验证
    }

    // 抽象方法，执行支付，子类必须实现此方法
    protected abstract void executePayment();
}