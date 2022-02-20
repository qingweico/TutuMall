<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>修改商品</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <!-- 提示信息 -->
      <el-alert title="修改商品信息" type="info" show-icon :closable="false" center> </el-alert>
      <!-- 步骤条 -->
      <el-steps :active="activeIndex - 0" finish-status="success" align-center :space="200">
        <el-step title="基本信息"></el-step>
        <el-step title="商品参数"></el-step>
        <el-step title="商品属性"></el-step>
        <el-step title="商品图片"></el-step>
        <el-step title="商品内容"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>
      <!-- 标签页 -->
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px" label-position="top">
        <el-tabs v-model="activeIndex" tab-position="left" :before-leave="beforeTabLeave" @tab-click="tabClicked">
          <el-tab-pane label="基本信息" name="0">
            <el-form-item label="商品名称" prop="goods_name">
              <el-input v-model="editForm.goods_name"></el-input>
            </el-form-item>
            <el-form-item label="商品价格" prop="goods_price">
              <el-input v-model.number="editForm.goods_price" type="number"></el-input>
            </el-form-item>
            <el-form-item label="商品重量" prop="goods_weight">
              <el-input v-model.number="editForm.goods_weight" type="number"></el-input>
            </el-form-item>
            <el-form-item label="商品数量" prop="goods_number">
              <el-input v-model.number="editForm.goods_number" type="number"></el-input>
            </el-form-item>
            <el-form-item label="商品分类">
              <el-cascader v-model="editForm.goods_cat" :options="cateList" :props="casacderProps" @change="handleChange"></el-cascader>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="商品参数" name="1">
            <el-form-item v-for="item in manyData" :key="item.attr_id" :label="item.attr_name">
              <el-checkbox-group v-model="item.attr_vals">
                <el-checkbox v-for="(value, i) in item.attr_vals" :key="i" :label="value" border></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="商品属性" name="2">
            <el-form-item v-for="item in onlyData" :key="item.attr_id" :label="item.attr_name">
              <el-input v-model="item.attr_vals"></el-input>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="商品图片" name="3">
            <!-- action指定图片要上传的后台API地址 -->
            <el-upload :action="uploadURL" :on-preview="handlePreview" :on-remove="handleRemove" 
            list-type="picture" :headers="headerObj" :on-success="handleSuccess" :file-list="fileList">
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-tab-pane>
          <el-tab-pane label="商品内容" name="4">
            <quill-editor v-model="editForm.goods_introduce" />
            <!-- 添加按钮 -->
            <el-button type="primary" class="btn-add" @click="editGoods()">修改商品</el-button>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-card>

    <!-- 图片预览 -->
    <el-dialog title="图片预览" :visible.sync="previewVisible" width="50%">
      <img :src="previewPath" class="preview-img" />
    </el-dialog>
  </div>
</template>

<script>
import _ from 'lodash'

export default {
  data() {
    return {
      activeIndex: '0',
      // 添加商品的表单对象
      editForm: {
        goods_name: '',
        goods_price: null,
        goods_weight: null,
        goods_number: null,
        // 商品所属分类的数组
        goods_cat: [],
        // 图片的数组
        pics: [],
        // 商品的内容
        goods_introduce: '',
        // 商品的参数
        attrs: [],
      },
      editFormRules: {
        goods_name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        goods_price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
        goods_weight: [{ required: true, message: '请输入商品重量', trigger: 'blur' }],
        goods_number: [{ required: true, message: '请输入商品数量', trigger: 'blur' }],
      },
      cateList: [],
      casacderProps: {
        label: 'cat_name',
        value: 'cat_id',
        children: 'children',
        expandTrigger: 'hover',
      },
      // 动态参数
      manyData: [],
      // 静态属性
      onlyData: [],
      // 上传图片的URL地址
      uploadURL: 'http://www.tangxiaoyang.vip:8888/api/v2/upload',
      // 文件上传时的请求头
      headerObj: {
        Authorization: JSON.parse(sessionStorage.getItem('userInfo')).token,
      },
      previewVisible: false,
      previewPath: '',
      fileList:[]
    }
  },
  created() {
    this.getGoodsById()
    this.getCateList()
  },
  methods: {
    // 根据id获取商品信息
    async getGoodsById() {
      const { data: res } = await this.$http.get(`goods/${this.$route.query.id}`)
      if (res.meta.status !== 200) {
        return this.$message.error('获取商品信息失败')
      }

      // 对商品分类数据进行处理，转换为数组
      res.data.goods_cat = res.data.goods_cat.split(',').map((item) => {
        return item * 1
      })

      // 对图片数据进行处理
      res.data.pics.forEach(item => {
        this.fileList.push({
          name: item.pics_id,
          url: item.pics_big_url.replace('127.0.0.1','www.tangxiaoyang.vip'),
          tmp_path: item.pics_big
        })

        item.pic = item.pics_big
      })

      this.editForm = res.data
    },
    // 获取分类列表
    async getCateList() {
      const { data: res } = await this.$http.get('categories')
      if (res.meta.status !== 200) {
        return this.$message.error('获取商品分类失败')
      }
      this.cateList = res.data
    },
    // 监听级联选择器的改变事件
    handleChange() {
      if (this.editForm.goods_cat.length !== 3) {
        this.editForm.goods_cat = []
      }
    },
    // tabs切换之前触发
    beforeTabLeave(activeName, oldActiveName) {
      if (this.editForm.goods_cat.length !== 3) {
        this.$message.error('请先选择商品分类！')
        return false
      }
    },
    // tabs被选中时触发
    async tabClicked() {
      // 判断是动态参数还是静态属性
      if (this.activeIndex === '1') {
        const { data: res } = await this.$http.get(`categories/${this.cateId}/attributes`, {
          params: { sel: 'many' },
        })
        if (res.meta.status !== 200) {
          return this.$message.error('获取动态参数失败')
        }
        res.data.forEach((item) => {
          item.attr_vals = item.attr_vals ? item.attr_vals.split(' ') : []
        })
        this.manyData = res.data
      } else if (this.activeIndex === '2') {
        const { data: res } = await this.$http.get(`categories/${this.cateId}/attributes`, {
          params: { sel: 'only' },
        })
        if (res.meta.status !== 200) {
          return this.$message.error('获取动态属性失败')
        }
        this.onlyData = res.data
      }
    },
    // 预览图片
    handlePreview(file) {
      // console.log(file)
      this.previewPath = file.url
      this.previewVisible = true
    },
    // 移除图片
    handleRemove(file) {
      // console.log(file)
      // 1.获取将要删除的图片的临时路径
      const filePath = file.tmp_path || file.response.data.tmp_path
      // 2.从pics数组中，找到这个图片对应的索引值
      const i = this.editForm.pics.findIndex((item) => {
        item.pic === filePath
      })
      // 3.从pics数组中移除
      this.editForm.pics.splice(i, 1)
    },
    // 监听图片上传成功
    handleSuccess(response) {
      this.editForm.pics.push({
        pic: response.data.tmp_path,
      })
    },
    // 修改商品
    editGoods() {
      this.$refs.editFormRef.validate(async (valid) => {
        if (!valid) {
          return
        }

        /**
         *  准备请求前的数据
         */
        // 使用lodash工具
        const form = _.cloneDeep(this.editForm) // 深拷贝
        form.goods_cat = form.goods_cat.join(',')

        // 处理动态参数
        this.manyData.forEach((item) => {
          form.attrs.push({
            attr_id: item.attr_id,
            attr_value: item.attr_vals.join(' '),
          })
        })

        // 处理静态属性
        this.onlyData.forEach((item) => {
          form.attrs.push({
            attr_id: item.attr_id,
            attr_value: item.attr_vals,
          })
        })

        /**
         *  发送请求，修改商品
         */
        const { data: res } = await this.$http.put(`goods/${this.editForm.goods_id}`, form)
        if (res.meta.status !== 200) {
          return this.$message.error('修改商品失败')
        }
        this.$message.success('修改商品成功！')
        this.$router.push('/goods')
      })
    },
  },
  computed: {
    cateId() {
      return this.editForm.goods_cat.length === 3 ? this.editForm.goods_cat[2] : null
    },
  },
}
</script>

<style lang="less" scoped>
.el-checkbox {
  margin-right: 10px;
}
.preview-img {
  width: 100%;
}
.btn-add {
  margin-top: 15px;
}
</style>