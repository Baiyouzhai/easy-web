<template>
  <div>
    <Edit :height="edit.height" :options="edit.options" :events="edit.events"></Edit>
    <div class="edit-tool">
      <el-row>
        <el-col :span="4">
          <div>1</div>
        </el-col>
        <el-col :span="16">
          <div>
            <i class="el-icon-edit"></i>
            已保存
          </div>
        </el-col>
        <el-col :span="4">
          <div>2</div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import Edit from '@/components/Edit'

export default {
  name: 'simpleEdit',
  data () {
    return {
      edit: {
        height: 1080,
        options: {
          value: '',
          language: 'javascript',
          theme: 'vs'
        },
        events: {
          save: this.savePanelDisplay
        }
      }
    }
  },
  created () {
    this.edit.height = document.documentElement.clientHeight - 24
  },
  mounted () {
  },
  methods: {
    savePanelDisplay (value) {
      this.dialogFormVisible = true
      console.info(value)
    }
  },
  components: {
    Edit
  }
}
</script>

<style scoped>
.edit-tool {
  height: 21px;
}
</style>
