
CREATE TABLE `pay_order` (
  `id` bigint DEFAULT NULL COMMENT '主键ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `pay_order_id` varchar(32)  NOT NULL COMMENT '支付单号',
  `payer_id` varchar(32) NOT NULL COMMENT '付款方iD',
  `payer_type` varchar(32) NOT NULL COMMENT '付款方类型',
  `payee_id` varchar(32) NOT NULL COMMENT '收款方id',
  `payee_type` varchar(32) NOT NULL COMMENT '收款方类型',
  `biz_no` varchar(128) NOT NULL COMMENT '业务单号',
  `biz_type` varchar(32) NOT NULL COMMENT '业务单类型',
  `order_amount` decimal(18,6) NOT NULL COMMENT '订单金额',
  `paid_amount` decimal(18,6) NOT NULL COMMENT '已支付金额',
  `channel_stream_id` varchar(64) DEFAULT NULL COMMENT '渠道流水号',
  `pay_url` varchar(512) DEFAULT NULL COMMENT '支付地址',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付方式',
  `memo` varchar(512) DEFAULT NULL COMMENT '备注',
  `order_state` varchar(64) DEFAULT NULL COMMENT '单据类型',
  `pay_succeed_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `pay_expire_time` datetime DEFAULT NULL COMMENT '支付超时时间',
  `deleted` tinyint DEFAULT NULL COMMENT '逻辑删除标识',
  `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号'
) ;