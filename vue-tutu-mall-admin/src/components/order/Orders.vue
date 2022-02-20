<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>订单管理</el-breadcrumb-item>
      <el-breadcrumb-item>订单列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <!-- 搜索和添加 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input placeholder="请输入订单编号" v-model="queryInfo.query" clearable @clear="getOrderList">
            <el-button slot="append" icon="el-icon-search" @click="getOrderList"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <!-- 订单列表 -->
      <el-table :data="orderList" border stripe @expand-change="getOrderDetail">
        <el-table-column label="明细" type="expand">
          <template slot-scope="scope">
            <!-- 订单下的商品列表 -->
            <el-table :data="scope.row.detail" border stripe>
              <el-table-column label="封面" width="100px" header-align="center" align="center">
                <template slot-scope="sc">
                  <!-- 实现图片点击放大效果 -->
                  <el-popover placement="top-start" trigger="hover">
                    <img :src="sc.row.url" style="width: 150px; height: 150px" />
                    <img :src="sc.row.url" slot="reference" style="width: 50px; height: 50px" />
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column label="商品名称" prop="goods_name"></el-table-column>
              <el-table-column label="商品重量" prop="goods_weight" width="100px"></el-table-column>
              <el-table-column label="商品数量" prop="goods_number" width="100px"></el-table-column>
              <el-table-column label="商品价格(元)" prop="goods_price" width="100px"></el-table-column>
              <el-table-column label="总价(元)" prop="goods_total_price" width="100px"></el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column label="序号" type="index"></el-table-column>
        <el-table-column label="订单编号" prop="order_number"></el-table-column>
        <el-table-column label="订单价格(元)" prop="order_price"></el-table-column>
        <el-table-column label="是否付款">
          <template slot-scope="scope">
            <el-tag effect="dark" size="mini" type="success" v-if="scope.row.pay_status === '1'">已付款</el-tag>
            <el-tag effect="dark" size="mini" type="danger" v-else>未付款</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否发货" prop="is_send"></el-table-column>
        <el-table-column label="下单时间" prop="create_time"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="修改收货地址" placement="top" :enterable="false">
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="showAddressDialog(scope.row.order_id)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="查看物流信息" placement="top" :enterable="false">
              <el-button type="warning" size="mini" icon="el-icon-location" @click="showProgressDialog()"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[5, 10, 15]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 修改收货地址的对话框 -->
    <el-dialog title="修改收货地址" :visible.sync="addressVisible" width="50%" @close="addressDialogClosed">
      <el-form :model="addressForm" :rules="addressFormRules" ref="addressFormRef" label-width="100px">
        <el-form-item label="省市区/县" prop="address1">
          <el-cascader v-model="addressForm.address1" :options="cityData"></el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="address2">
          <el-input v-model="addressForm.address2"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addressVisible = false">取 消</el-button>
        <el-button type="primary" @click="editAddress">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 物流进度的对话框 -->
    <el-dialog title="物流进度" :visible.sync="progressVisible" width="50%">
      <el-timeline>
        <el-timeline-item v-for="(activity, index) in progressInfo" :key="index" :timestamp="activity.time">
          {{ activity.context }}
        </el-timeline-item>
      </el-timeline>
    </el-dialog>
  </div>
</template>

<script>
import cityData from './citydata'

export default {
  data() {
    return {
      queryInfo: {
        query: '',
        pagenum: 1,
        pagesize: 10,
      },
      orderList: [],
      total: 0,
      addressVisible: false,
      cityData,
      addressForm: {
        address1: [],
        address2: '',
      },
      addressFormRules: {
        address1: [{ required: true, message: '请选择省市区/县', trigger: 'blur' }],
        address2: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
      },
      order_id: null,
      progressVisible: false,
      progressInfo: [],
    }
  },
  created() {
    this.getOrderList()
  },
  methods: {
    async getOrderList() {
      const { data: res } = await this.$http.get('orders', {
        params: this.queryInfo,
      })
      if (res.meta.status !== 200) {
        return this.$message.error('获取订单列表失败')
      }
      res.data.goods.forEach((item) => {
        item.create_time = this.$utils.formatToTime(item.create_time)

        // 为订单对象添加detail字段
        item.detail = []
      })

      this.orderList = res.data.goods
      this.total = res.data.total
    },
    handleSizeChange(pagesize) {
      this.queryInfo.pagesize = pagesize
      this.getOrderList()
    },
    handleCurrentChange(pagenum) {
      this.queryInfo.pagenum = pagenum
      this.getOrderList()
    },
    // 获取订单详情
    async getOrderDetail(row) {
      const { data: res } = await this.$http.get(`orders/${row.order_id}`)
      if (res.meta.status !== 200) {
        return this.$message.error('获取订单明细失败')
      }
      row.detail = res.data.goods

      // 获取订单下的商品详情
      row.detail.forEach(async (item) => {
        const { data: res } = await this.$http.get(`goods/${item.goods_id}`)
        if (res.meta.status !== 200) {
          return this.$message.error('获取商品详情失败')
        }
        this.$set(item, 'goods_name', res.data.goods_name) // 为商品对象响应式的添加属性
        this.$set(item, 'goods_weight', res.data.goods_weight)
        this.$set(item, 'url', res.data.pics[0].pics_big_url)
      })

      console.log(row)
    },
    // 显示修改地址的对话框
    showAddressDialog(order_id) {
      this.order_id = order_id
      this.addressVisible = true
    },
    // 监听修改地址对话框的关闭事件
    addressDialogClosed() {
      this.$refs.addressFormRef.resetFields()
    },
    // 修改地址
    editAddress() {
      this.$refs.addressFormRef.validate(async (valid) => {
        if (!valid) {
          return
        }
        const { data: res } = await this.$http.put(`orders/${this.order_id}/address`, {
          consignee_addr: this.addressForm.address1.join(',') + ',' + this.addressForm.address2,
        })
        if (res.meta.status !== 200) {
          return this.$message.error('修改收货地址失败')
        }
        this.addressVisible = false
        this.getOrderList()
        this.$message.success('修改收货地址成功！')
      })
    },
    // 显示物流进度的对话框
    async showProgressDialog() {
      const { data: res } = await this.$http.get('/kuaidi/815294206237577')
      if (res.meta.status !== 200) {
        return this.$message.error('获取物流进度失败')
      }
      this.progressInfo = res.data
      this.progressVisible = true
    },
  },
}
</script>

<style lang="less" scoped>
.el-cascader {
  width: 100%;
}
</style>