<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品分类</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <!-- 添加按钮 -->
      <el-row>
        <el-col>
          <el-button type="primary" @click="showAddDialog">添加分类</el-button>
        </el-col>
      </el-row>

      <!-- tree-table表格 -->
      <tree-table class="tree-table" :data="cateList" :columns="columns" :selection-type="false" :expand-type="false" show-index border>
        <!-- 是否有效 -->
        <template slot="isOk" slot-scope="scope">
          <i class="el-icon-error" style="color: red" v-if="scope.row.cat_deleted"></i>
          <i class="el-icon-success" style="color: lightgreen" v-else></i>
        </template>
        <!-- 排序 -->
        <template slot="order" slot-scope="scope">
          <el-tag size="mini" v-if="scope.row.cat_level === 0">一级</el-tag>
          <el-tag size="mini" type="success" v-else-if="scope.row.cat_level === 1">二级</el-tag>
          <el-tag size="mini" type="warning" v-else>三级</el-tag>
        </template>
        <!-- 操作 -->
        <template slot="operate" slot-scope="scope">
          <el-button size="mini" type="primary" class="el-icon-edit" @click="showEditDialog(scope.row.cat_id)">编辑</el-button>
          <el-button size="mini" type="danger" class="el-icon-delete" @click="removeCateById(scope.row.cat_id)">删除</el-button>
        </template>
      </tree-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[3, 5, 10, 15]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 添加分类的对话框 -->
    <el-dialog title="添加分类" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <el-form-item label="分类名称" prop="cat_name">
          <el-input v-model="addForm.cat_name"></el-input>
        </el-form-item>
        <el-form-item label="父级分类">
          <el-cascader v-model="selectedKeys" :options="parentCateList" :props="cascaderProps" @change="handleChange" clearable></el-cascader>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCate">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑分类的对话框 -->
    <el-dialog title="编辑分类" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <el-form-item label="分类名称" prop="cat_name">
          <el-input v-model="editForm.cat_name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editCate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 查询分类列表的参数对象
      queryInfo: {
        type: 3,
        pagenum: 1,
        pagesize: 5,
      },
      // 分类列表
      cateList: [],
      // 总记录数
      total: 0,
      // tree-table的列定义
      columns: [
        {
          label: '分类名称',
          prop: 'cat_name',
        },
        {
          label: '是否有效',
          // 将当前列定义为模板列
          type: 'template',
          // 当前列使用的模板名称
          template: 'isOk',
        },
        {
          label: '排序',
          // 将当前列定义为模板列
          type: 'template',
          // 当前列使用的模板名称
          template: 'order',
        },
        {
          label: '操作',
          // 将当前列定义为模板列
          type: 'template',
          // 当前列使用的模板名称
          template: 'operate',
        },
      ],
      addDialogVisible: false,
      addForm: {
        cat_name: '', // 分类名称
        cat_pid:0, // 父级分类的id
        cat_level:0 // 分类的等级，默认为一级分类
      },
      addFormRules: {
        cat_name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
      },
      // 父级分类的列表  
      parentCateList:[],
      // 级联选择器的配置对象
      cascaderProps:{
          label:'cat_name',
          value:'cat_id',
          children:'children',
          expandTrigger:'hover',
          checkStrictly:true // 选择任意一个选项
      },
      // 级联选择器选中的id数组
      selectedKeys:[],
      editDialogVisible:false,
      editForm:{},
      editFormRules: {
        cat_name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
      },
    }
  },
  created() {
    this.getCateList()
  },
  methods: {
    async getCateList() {
      const { data: res } = await this.$http.get('categories', {
        params: this.queryInfo,
      })
      if (res.meta.status !== 200) {
        return this.$message.error('获取商品分类失败')
      }
      this.cateList = res.data.result
      this.total = res.data.total
    },
    handleSizeChange(pagesize) {
      this.queryInfo.pagesize = pagesize
      this.getCateList()
    },
    handleCurrentChange(pagenum) {
      this.queryInfo.pagenum = pagenum
      this.getCateList()
    },
    // 获取父级分类的数据列表
    async getParentCateList(){
        const {data:res} = await this.$http.get('categories',{
            params:{ type:2 }
        })
        if(res.meta.status !== 200){
          return this.$message.error('获取父级分类数据失败')
        }
        // console.log(res.data)
        this.parentCateList = res.data
    },
    // 显示添加分类的对话框
    showAddDialog() {
      this.getParentCateList()  
      this.addDialogVisible = true
    },
    // 监听级联选择器的改变事件
    handleChange(){
        // console.log(this.selectedKeys)
        // 判断是否选择了父级分类
        if(this.selectedKeys.length>0){
            this.addForm.cat_pid = this.selectedKeys[this.selectedKeys.length-1]
            this.addForm.cat_level = this.selectedKeys.length
        }else{
            this.addForm.cat_pid = 0
            this.addForm.cat_level = 0
        }
    },
    // 添加分类
    addCate(){
        this.$refs.addFormRef.validate(async valid => {
            if(!valid){
                return
            }
            const {data:res} = await this.$http.post('categories',this.addForm)
            if(res.meta.status !== 201){
              return this.$message.error('添加分类失败')
            }
            this.addDialogVisible = false 
            this.getCateList()
            this.$message.success('添加分类成功！')
        })
    },
    // 监听添加对话框的关闭事件
    addDialogClosed(){
        this.$refs.addFormRef.resetFields()
        this.selectedKeys = []
        this.addForm.cat_pid = 0
        this.addForm.cat_level = 0
    },
    editDialogClosed(){
        this.$refs.editFormRef.resetFields()
    },
    async showEditDialog(cateId){
        const {data:res} = await this.$http.get(`categories/${cateId}`)
        if(res.meta.status !== 200){
          return this.$message.error('查询分类失败')
        }
        this.editForm = res.data
        this.editDialogVisible = true
    },
    editCate(){
        this.$refs.editFormRef.validate(async valid => {
            if(!valid){
                return
            }
            const {data:res} = await this.$http.put(`categories/${this.editForm.cat_id}`,{
                cat_name:this.editForm.cat_name
            })
            if(res.meta.status !== 200){
              return this.$message.error('修改分类失败')
            }
            this.editDialogVisible = false 
            this.getCateList()
            this.$message.success('修改分类成功！')
        })
    },
    removeCateById(cateId){
        this.$confirm('确定要删除该分类吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const {data:res} = await this.$http.delete(`categories/${cateId}`)
          if(res.meta.status !== 200){
            return this.$message.error('删除分类失败')
          }
          this.getCateList()
          this.$message.success('删除分类成功！')
        }).catch(() => {
          this.$message.info('已取消删除！')       
        });
    }
  },
}
</script>

<style lang="less" scoped>
.el-cascader{
    width: 100%;
}
.tree-table{
    margin-top: 15px;
}
</style>