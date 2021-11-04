<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item> <i class="el-icon-lx-cascades"></i> 登录记录 </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-table :data="tableData" border class="table" ref="Table" header-cell-class-name="table-header">
            <el-table-column prop="id" label="id" align="center"></el-table-column>
            <el-table-column prop="uid" label="uid" align="center"></el-table-column>
            <el-table-column prop="time" label="time" align="center"></el-table-column>
            <el-table-column prop="ip" label="ip" align="center"></el-table-column>
            <el-table-column prop="deviceId" label="deviceId" align="center"></el-table-column>
            <el-table-column label="操作" width="180" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination
                background
                layout="total, prev, pager, next"
                :current-page="query.pageIndex"
                :page-size="query.pageSize"
                :total="pageTotal"
                @current-change="handlePageChange"
            ></el-pagination>
        </div>
    </div>
</template>

<script>
import Aips from '../../api/account';
export default {
    components: {},
    props: {},
    data() {
        return {
            query: {
                pageIndex: 1,
                pageSize: 10
            },
            tableData: [],
            pageTotal: 0,
            editVisible: false,
            editForm: {},
            idex: 0,
            insertVisible: false,
            insertForm: {}
        };
    },
    watch: {},
    computed: {},
    methods: {
        getData() {
            let data={
                sessionId:localStorage.getItem('sessionId'),
                environment: {
                    ip: localStorage.getItem('ip'),
                    deviceId: localStorage.getItem('deviceID')
                }
            }
            Aips.getLoginRecord(data).then(res=>{
                if(res.code==0){
                    this.tableData=res.data
                }else{
                    this.$message.error('登录信息获取失败');
                }
            })
        },
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getOrderData();
        },
        handleEdit(index, row) {
            this.editForm = JSON.parse(JSON.stringify(row)); //深拷贝
            this.idex = index;
            this.editVisible = true;
        },
        saveEdit() {
            let tempPar = JSON.parse(JSON.stringify(this.editForm));
            this.$message.success('修改成功!');
            this.editVisible = false;
            this.$set(this.tableData, this.idex, tempPar);
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.$message.success('删除成功');
                    this.tableData.splice(index, 1);
                })
                .catch(() => {});
        },
        //处理新增订单弹窗
        insertNew() {
            this.insertVisible = true;
        },
        //保存新增订单
        saveInsert() {
            let tempPar = JSON.parse(JSON.stringify(this.insertForm));
            this.$message.success('新增成功!');
            this.insertVisible = false;
            this.tableData.push(JSON.parse(JSON.stringify(tempPar)));
        }
    },
    created() {
        this.getData();
    },
    mounted() {}
};
</script>
<style scoped>
.handle-box {
    margin: 10px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}
.table {
    width: 100%;
    font-size: 14px;
}
.red {
    color: #ff0000;
}
.mr10 {
    margin-right: 10px;
}
.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>