<template>
  <div id="standerEdit">
    <el-container>
      <!-- 头部工具栏 -->
      <el-header height="32" class="tools-header">
        <el-row type="flex" justify="center">
          <el-col :span="6">
            <el-button
              icon="el-icon-plus"
              size="small"
              @click="!display.toolFile"
              v-popover:toolFile
            >文件</el-button>
          </el-col>
          <el-col :span="6">
            <el-button icon="el-icon-plus" size="small">其它</el-button>
          </el-col>
          <el-col :span="6">
            <el-button icon="el-icon-plus" size="small">其它</el-button>
          </el-col>
          <el-col :span="3">
            <el-button icon="el-icon-plus" size="small">其它</el-button>
          </el-col>
          <el-col :span="3">
            <el-button-group>
              <el-button icon="el-icon-plus" size="small">Edit设置</el-button>
              <el-button icon="el-icon-plus" size="small">帮助</el-button>
              <el-button icon="el-icon-plus" size="small">其它</el-button>
            </el-button-group>
          </el-col>
        </el-row>
      </el-header>
      <!-- 编辑区域 -->
      <el-main>
        <el-tabs type="card" tab-position="left" closable @edit="handleTabsEdit">
          <el-tab-pane
            v-for="item in editors"
            :key="item.name"
            :label="item.title"
            :name="item.name"
          >{{item.content}}</el-tab-pane>
        </el-tabs>
      </el-main>
      <!-- 底部状态栏 -->
      <el-footer height="21">
        <el-row>
          <el-col :span="4">
            <div>
              <i class="el-icon-edit"></i>
            </div>
          </el-col>
          <el-col :span="16">
            <div></div>
          </el-col>
          <el-col :span="4">
            <div></div>
          </el-col>
        </el-row>
      </el-footer>
    </el-container>
    <!-- 左上角，文件按钮的下拉弹窗 -->
    <el-popover ref="toolFile" v-model="display.toolFile" placement="bottom" trigger="click">
      <el-menu>
        <el-menu-item v-popover:toolFileNewFile>新建</el-menu-item>
        <el-menu-item @click="toolFileHidden">打开文件</el-menu-item>
        <el-menu-item @click="toolFileHidden">打开文件夹</el-menu-item>
        <el-menu-item v-popover:toolFileRecentFiles>打开记录</el-menu-item>
      </el-menu>
    </el-popover>
    <!-- 新建弹窗 -->
    <el-popover
      ref="toolFileNewFile"
      v-model="display.toolFileNewFile"
      placement="right"
      trigger="hover"
    >
      <el-menu @select="selectNewFile">
        <el-menu-item v-for="item in edit.languages" :key="item" :index="item">{{item}}</el-menu-item>
      </el-menu>
    </el-popover>
    <!-- 打开记录弹窗 -->
    <el-popover ref="toolFileRecentFiles" placement="right" trigger="hover">
      <el-menu>
        <el-menu-item>记录1</el-menu-item>
        <el-menu-item>记录2</el-menu-item>
        <el-menu-item>记录3</el-menu-item>
        <el-menu-item>记录4</el-menu-item>
      </el-menu>
    </el-popover>
  </div>
</template>

<script>
import Language from '@/MonacoLanguage'

export default {
  name: 'standerEdit',
  data () {
    return {
      edit: Language,
      display: {
        toolFile: false,
        toolFileNewFile: false
      },
      global: {
        edit: {
          theme: 'vs'
        }
      },
      editors: [],

      editableTabsValue: '2',
      editableTabs: [
        {
          title: 'Tab 1',
          name: '1',
          content: 'Tab 1 content'
        },
        {
          title: 'Tab 2',
          name: '2',
          content: 'Tab 2 content'
        }
      ],
      tabIndex: 2
    }
  },
  created () {
    // let height = document.documentElement.clientHeight;
    // let width = document.documentElement.clientWidth;
    console.info(this)
  },
  mounted () {},
  methods: {
    selectNewFile: (index, indexPath) => {
      console.info(index, indexPath)
      this.toolFileHidden()
    },
    toolFileHidden: () => {
      this.display.toolFile = false
      this.display.toolFileNewFile = false
    },
    handleTabsEdit: (targetName, action) => {
      if (action === 'add') {
        const newTabName = ++this.tabIndex + ''
        this.editableTabs.push({
          title: 'New Tab',
          name: newTabName,
          content: 'New Tab content'
        })
        this.editableTabsValue = newTabName
      }
      if (action === 'remove') {
        const tabs = this.editableTabs
        let activeName = this.editableTabsValue
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              const nextTab = tabs[index + 1] || tabs[index - 1]
              if (nextTab) {
                activeName = nextTab.name
              }
            }
          })
        }

        this.editableTabsValue = activeName
        this.editableTabs = tabs.filter((tab) => tab.name !== targetName)
      }
    }
  },
  components: {}
}
</script>

<style scoped>
body {
  margin: 0px;
}
.el-container {
  margin: 0px;
}
.tools-header {
  padding: 0 !important;
}
.el-main {
  padding: 0px;
  overflow: hidden;
  height: 884px;
}
.el-footer {
  padding: 0px;
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.row-bg {
  background-color: #f9fafc;
}
</style>
