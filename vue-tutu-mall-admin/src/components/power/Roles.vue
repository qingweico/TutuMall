<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>角色列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <!-- 添加按钮 -->
      <el-row>
        <el-col>
          <el-button type="primary" @click="addDialogVisible = true">添加角色</el-button>
        </el-col>
      </el-row>
      <!-- 角色列表 -->
      <el-table :data="rolesList" border stripe>
        <!-- 展开列 -->
        <el-table-column label="明细" type="expand">
            <template slot-scope="scope">
                <!-- 渲染一级权限 -->
                <el-row v-for="(item1,i1) in scope.row.children" :key="item1.id" :class="{bdbottom:true,bdtop:i1==0,vcenter:true}">
                    <el-col :span="5">
                        <el-tag closable @close="removeRightById(scope.row,item1.id)">{{ item1.authName }}</el-tag>
                        <i class="el-icon-caret-right"></i>
                    </el-col>
                    <el-col :span="19">
                        <!-- 渲染二级权限 -->
                        <el-row v-for="(item2,i2) in item1.children" :key="item2.id" :class="{bdtop:i2!=0,vcenter:true}">
                            <el-col :span="6">
                                <el-tag type="success" closable @close="removeRightById(scope.row,item2.id)">{{ item2.authName }}</el-tag>
                                <i class="el-icon-caret-right"></i>
                            </el-col>
                            <el-col :span="18">
                                <!-- 渲染三级权限 -->
                                <el-tag type="warning" closable v-for="item3 in item2.children" :key="item3.id" @close="removeRightById(scope.row,item3.id)">{{ item3.authName }}</el-tag>
                            </el-col>
                        </el-row>
                    </el-col>
                </el-row>
            </template>
        </el-table-column>
        <el-table-column label="序号" type="index"></el-table-column>
        <el-table-column label="角色名称" prop="roleName"></el-table-column>
        <el-table-column label="角色名称" prop="roleDesc"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="showEditDialog(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="removeRoleById(scope.row.id)">删除</el-button>
            <el-button size="mini" type="warning" icon="el-icon-setting" @click="showRightDialog(scope.row)">分配权限</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加角色的对话框 -->
    <el-dialog title="添加角色" :visible.sync="addDialogVisible" width="50%" @close="addRoleClosed">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="addForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="roleDesc">
          <el-input v-model="addForm.roleDesc"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addRole">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑角色的对话框 -->
    <el-dialog title="编辑角色" :visible.sync="editDialogVisible" width="50%" @close="editRoleClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="editForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="roleDesc">
          <el-input v-model="editForm.roleDesc"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editRole">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 分配权限的对话框 -->
    <el-dialog title="分配权限" :visible.sync="rightDialogVisible" width="50%" @close="rightDialogClosed">
      <el-tree ref="treeRef" :data="rightsList" :props="treeProps" node-key="id" show-checkbox default-expand-all :default-checked-keys="defaultKeys"></el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="rightDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="allotRights">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      rolesList: [],
      addDialogVisible: false,
      addForm: {
        roleName: '',
        roleDesc: '',
      },
      addFormRules: {
        roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        roleDesc: [{ required: true, message: '请输入角色描述', trigger: 'blur' }],
      },
      editDialogVisible:false,
      editForm:{},
      editFormRules: {
        roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        roleDesc: [{ required: true, message: '请输入角色描述', trigger: 'blur' }],
      },
      rightDialogVisible:false,
      rightsList:[],
      // 树形组件的属性绑定对象   
      treeProps:{
          label:'authName',
          children:'children'
      },
      // 默认选中的节点id值数组   
      defaultKeys:[],
      // 当前要分配权限的角色id
      roleId:''  
    }
  },
  created() {
    this.getRolesList()
  },
  methods: {
    //  获取角色列表
    async getRolesList() {
      const { data: res } = await this.$http.get('roles')
      if (res.meta.status !== 200) {
        return this.$message.error('获取角色列表失败')
      }
      this.rolesList = res.data
    },
    // 添加角色
    addRole() {
      this.$refs.addFormRef.validate(async (valid) => {
        if (!valid) {
          return
        }

        const { data: res } = await this.$http.post('roles', this.addForm)
        if (res.meta.status !== 201) {
          return this.$message.error('添加角色失败')
        }

        this.addDialogVisible = false
        await this.getRolesList()
        this.$message.success('添加角色成功')
      })
    },
    // 监听添加角色对话框的关闭事件
    addRoleClosed(){
        this.$refs.addFormRef.resetFields()
    },
    // 显示编辑角色的对话框
    async showEditDialog(id){
        const {data:res} = await this.$http.get(`roles/${id}`)
        if(res.meta.status !== 200){
          return this.$message.error('查询角色信息失败')
        }
        this.editForm = res.data
        this.editDialogVisible = true
    },
    // 修改角色
    editRole(){
        this.$refs.editFormRef.validate(async valid => {
            if(!valid){
                return
            }

            const {data:res} = await this.$http.put(`roles/${this.editForm.roleId}`,{
                roleName:this.editForm.roleName,
                roleDesc:this.editForm.roleDesc
            })
            if(res.meta.status !== 200){
              return this.$message.error('修改角色失败')
            }

            this.editDialogVisible =  false 
            await this.getRolesList()
            this.$message.success('修改角色成功！')
        })
    },
    // 监听编辑角色对话框的关闭事件
    editRoleClosed(){
        this.$refs.editFormRef.resetFields()
    },
    // 根据ID删除角色
    removeRoleById(id){
        this.$confirm('确定要删除该角色吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const {data:res} = await this.$http.delete(`roles/${id}`)  
          if(res.meta.status !== 200){
            return this.$message.error('删除角色失败')
          }
          this.getRolesList()
          this.$message.success('删除角色成功！')
        }).catch(() => {
          this.$message.info('已取消删除！')
        });
    },
    // 根据id删除角色下的权限
    removeRightById(role,rightId){
         this.$confirm('确定要删除该权限吗', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const {data:res} = await this.$http.delete(`roles/${role.id}/rights/${rightId}`)
          if(res.meta.status !== 200){
            return this.$message.error('删除权限失败')
          }
          //   this.getRolesList()  // 不建议重新获取所有角色列表，会导致页面刷新
          role.children = res.data // 重新设置当前角色下最新的权限数据
          this.$message.success('删除权限成功！')

        }).catch(() => {
          this.$message.info('已取消删除')
        });
    },
    // 显示分配权限的对话框
    async showRightDialog(role){
        // 获取所有权限列表
        const {data:res} = await this.$http.get(`rights/tree`)
        if(res.meta.status !== 200){
          return this.$message.error('获取权限列表失败')
        }
        this.rightsList = res.data 

        // 获取角色下所有三级权限的id
        this.getLeafKeys(role)

        // 存储当前要分配权限的角色id
        this.roleId = role.id 

        this.rightDialogVisible = true
    },
    // 获取角色下所有三级权限的id
    getLeafKeys(node){
        // 判断是否为三级权限
        if(!node.children){
            return this.defaultKeys.push(node.id)
        }
        // 如果不是三级权限，则递归操作
        node.children.forEach(item => {
            this.getLeafKeys(item)
        })
    },
    // 监听分配权限对话框的关闭事件
    rightDialogClosed(){
        this.defaultKeys = []
    },
    // 分配权限
    async allotRights(){
        const keys = this.$refs.treeRef.getCheckedKeys().concat(this.$refs.treeRef.getHalfCheckedKeys())
        const idStr = keys.join(',')

        const {data:res} = await this.$http.post(`roles/${this.roleId}/rights`,{
            rids:idStr
        })
        if(res.meta.status !== 200){
          return this.$message.error('分配权限失败')
        }
        this.rightDialogVisible = false 
        await this.getRolesList()
        this.$message.success('分配权限成功')
    }
  },
}
</script>

<style lang="less" scoped>
.el-tag{
    margin: 6px;
}
.bdbottom{
    border-bottom: 1px solid #eee;
}
.bdtop{
    border-top: 1px solid #eee;
}
.vcenter{
    display: flex;
    align-items: center;
}
</style>