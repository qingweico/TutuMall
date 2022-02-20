<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>欢迎页</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 数据统计 -->
    <el-row :gutter="10">
      <el-col :span="6">
        <div class="statistics" style="background: #409eff">
          <div class="title">
            <p>用户统计</p>
            <el-tag size="mini" type="success">实时</el-tag>
          </div>
          <div class="num">{{ totalUsers }}</div>
          <div class="tip">当前总用户数量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="statistics" style="background: #e6a23c">
          <div class="title">
            <p>商品统计</p>
            <el-tag size="mini" type="info">实时</el-tag>
          </div>
          <div class="num">{{ totalGoods }}</div>
          <div class="tip">当前总商品数量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="statistics" style="background: #67c23a">
          <div class="title">
            <p>订单统计</p>
            <el-tag size="mini" type="warning">实时</el-tag>
          </div>
          <div class="num">{{ totalOrders }}</div>
          <div class="tip">当前总订单数量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="statistics" style="background: #909399">
          <div class="title">
            <p>销售统计</p>
            <el-tag size="mini" type="primary">实时</el-tag>
          </div>
          <div class="num">{{ totalSales }}</div>
          <div class="tip">当前总销售额(元)</div>
        </div>
      </el-col>
    </el-row>

    <!-- Echarts图表 -->
    <div class="echarts-container">
      <el-card id="chart1"></el-card>
      <el-card id="chart2"></el-card>
    </div>
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  data() {
    return {
      totalUsers: 0,
      totalGoods: 0,
      totalOrders: 0,
      totalSales: 0,
      orderList: [],
    }
  },
  created() {
    this.getUserList()
    this.getGoodsList()
    this.getOrderList()
  },
  methods: {
    async getUserList() {
      const { data: res } = await this.$http.get('users', {
        params: {
          pagenum: 1,
          pagesize: 1,
        },
      })
      if (res.meta.status !== 200) {
        return this.$message.error('获取用户数据失败')
      }
      this.totalUsers = res.data.total
    },
    async getGoodsList() {
      const { data: res } = await this.$http.get('goods', {
        params: {
          pagenum: 1,
          pagesize: 1,
        },
      })
      if (res.meta.status !== 200) {
        return this.$message.error('获取商品数据失败')
      }
      this.totalGoods = res.data.total
    },
    async getOrderList() {
      const { data: res } = await this.$http.get('orders')
      if (res.meta.status !== 200) {
        return this.$message.error('获取订单数据失败')
      }
      this.totalOrders = res.data.total

      // 计算总销售额
      this.orderList = res.data.goods
      this.orderList.forEach((item) => {
        if (item.pay_status === '1') {
          this.totalSales += item.order_price
        }
      })

      // 展示图表
      this.initChart1()
      this.initChart2()
    },
    initChart1() {
      // 创建echarts实例
      var chart1 = echarts.init(document.getElementById('chart1'))

      var option = {
        title: {
          text: '订单趋势',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line',
          },
        },
        xAxis: {
          type: 'category',
          data: [], // ['03.20', '03.21'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            type: 'line',
            smooth: true,
            data: [], // [260, 406]
          },
        ],
      }

      /**
       *  对数据进行处理
       */
      // 升序排序
      this.orderList.sort(function (a, b) {
        return a.create_time - b.create_time
      })

      // 获取日期及订单总额
      let data = []
      this.orderList.forEach((item) => {
        if (item.pay_status === '1') {
          let key = this.$utils.formatToDate(item.create_time)
          if (!data[key]) {
            data[key] = item.order_price
          } else {
            data[key] += item.order_price
          }
        }
      })

      // 将数据添加到option中
      for (var key in data) {
        option.xAxis.data.push(key)
        option.series[0].data.push(data[key])
      }

      chart1.setOption(option)
    },
    initChart2() {
      var chart2 = echarts.init(document.getElementById('chart2'))

      var option = {
        title: {
          text: '订单状态',
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c} ({d}%)',
        },
        legend: {
          orient: 'vertical',
          left: 'right',
          data: [],
        },
        series: [
          {
            name: '订单状态',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      }

      /**
       *  对数据进行处理
       */
      var data = [
          {
              name:'已付款',
              value:0
          },
          {
              name:'未付款',
              value:0
          }
      ]

      this.orderList.forEach(item => {
          if(item.pay_status === '1'){
              data[0].value++
          }else{
              data[1].value++
          }
      })

      console.log(data)
      for(var item of data){
          option.legend.data.push(item.name)
          option.series[0].data.push(item)
      }

      chart2.setOption(option)
    },
  },
}
</script>

<style lang="less" scoped>
.el-row {
  padding: 0 9px;
  .statistics {
    color: white;
    height: 100px;
    border-radius: 4px;
    padding: 0 8px;
    .title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      p {
        font-size: 13px;
        font-weight: bold;
      }
    }
    .num {
      font-size: 20px;
      font-weight: bold;
      text-align: center;
    }
    .tip {
      font-size: 12px;
    }
  }
}
.echarts-container {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  .el-card {
    height: 400px;
    width: 50%;
    margin: 0 8px;
    padding: 20px;
  }
}
</style>