# 使用greys进行线上问题排查

1. jsp 看java进程
2. ./greys.sh 12148 pid
3. 几个有用的命令：

    1. trace  *InvoiceOcrController  InvoiceOcrController
    2. monitor -c 5 *InvoiceOcrController  InvoiceOcrController
    3. sm -d *InvoiceOcrController
