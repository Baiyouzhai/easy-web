<template>
  <div class="edit-panel">
    <div id="editContainer" class="edit-container"></div>
    <!-- 保存弹窗 -->
    <el-dialog title="保存设置" :visible.sync="saveDialog.display">
      <el-form :model="saveDialog" label-position="right" label-width="80px">
        <el-form-item label="保存至">
          <el-select v-model="saveDialog.saveTo">
            <el-option label="本地" value="local"></el-option>
            <el-option label="服务器" value="webServer"></el-option>
          </el-select>
        </el-form-item>
        <!-- 本地保存 -->
        <el-form label-position="right" label-width="80px" v-if="saveDialog.saveTo === 'local'">
          <el-form-item label="文件名">
            <el-input type="text" class="input-default-style" v-model="saveDialog.saveName"></el-input>
          </el-form-item>
        </el-form>
        <!-- 服务器保存 -->
        <el-form
          label-position="right"
          label-width="80px"
          v-else-if="saveDialog.saveTo === 'webServer'"
        >
          <el-form-item label="请求地址">
            <el-input v-model="saveDialog.requestUrl"></el-input>
          </el-form-item>
          <el-form :inline="true" label-position="right" label-width="80px">
            <el-form-item label="请求方式">
              <el-select v-model="saveDialog.requestMethod">
                <el-option label="GET" value="get"></el-option>
                <el-option label="POST" value="post"></el-option>
              </el-select>
            </el-form-item>
            <!-- 服务器post请求 -->
            <el-form-item label="数据类型" v-if="saveDialog.requestMethod === 'post'">
              <el-select v-model="saveDialog.requestDataType">
                <el-option label="form-data" value="form-data"></el-option>
                <el-option label="x-www-form-urlencoded" value="x-www-form-urlencoded"></el-option>
                <el-option label="raw" value="raw"></el-option>
                <el-option label="binary" value="binary" :disabled="true"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label="格式"
              v-if="saveDialog.requestMethod === 'post' && saveDialog.requestDataType === 'raw'"
            >
              <el-select v-model="saveDialog.requestDataFormat">
                <el-option label="text" value="text" :disabled="true"></el-option>
                <el-option label="Javascript" value="javascript" :disabled="true"></el-option>
                <el-option label="JSON" value="json"></el-option>
                <el-option label="HTML" value="html" :disabled="true"></el-option>
                <el-option label="XML" value="xml" :disabled="true"></el-option>
              </el-select>
            </el-form-item>
          </el-form>

          <el-form
            :inline="true"
            label-position="right"
            label-width="80px"
            v-if="saveDialog.requestDataType === 'form-data' || saveDialog.requestDataType === 'x-www-form-urlencoded'"
          >
            <el-form-item label="key">
              <el-input
                type="text"
                class="input-default-style"
                :placeholder="defaultValue.saveDialog.contentNamePlaceholder"
                v-model="saveDialog.contentName"
              ></el-input>
              <span>
                <i class="el-icon-arrow-down el-icon--right">text</i>
              </span>
            </el-form-item>
            <el-form-item label="value">
              <el-input type="text" class="input-default-style" value="content" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item v-if="saveDialog.requestDataType !== 'raw'">
              <el-button @click="addRequestParam">新增</el-button>
            </el-form-item>

            <el-row v-for="(item, index) in saveDialog.requestParam" :key="index">
              <el-form-item label=" ">
                <el-input type="text" class="input-default-style" v-model="item.key"></el-input>
                <el-dropdown trigger="click" @command="setSaveDialogRequestParamType">
                  <span>
                    <i class="el-icon-arrow-down el-icon--right">{{item.type}}</i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item :command="{'index': index, 'type': 'text'}">text</el-dropdown-item>
                    <el-dropdown-item
                      :command="{'index': index, 'type': 'file'}"
                      :disabled="saveDialog.requestDataType !== 'form-data'"
                    >file</el-dropdown-item>
                    <el-dropdown-item
                      :command="{'index': index, 'type': 'files'}"
                      :disabled="saveDialog.requestDataType !== 'form-data'"
                    >files</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </el-form-item>
              <el-form-item label=" ">
                <el-input
                  :type="item.type === 'text' ? 'text' : 'file'"
                  class="input-default-style"
                  v-model="item.value"
                  :multiple="item.type==='files'"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button @click.prevent="removeRequestParam(index)">删除</el-button>
              </el-form-item>
            </el-row>
          </el-form>
        </el-form>
        <!-- 其它保存方式 -->
        <el-form v-else></el-form>
        <el-form-item label="输出设置">
          <el-input
            type="textarea"
            autosize
            :placeholder="saveDialog.placeholder"
            v-model="saveDialog.saveSetting"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="saveDialog.display = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as monaco from 'monaco-editor'

export default {
  name: 'edit',
  props: {
    height: Number,
    options: {
    },
    events: {
      save: Function
    }
  },
  data () {
    return {
      defaultValue: {
        edit: {
          options: {
            value: '// Enter your code \n',
            language: 'javascript',
            theme: 'vs-dark',
            selectOnLineNumbers: true,
            roundedSelection: false,
            readOnly: false,
            cursorStyle: 'line',
            automaticLayout: false,
            glyphMargin: true
          }
        },
        saveDialog: {
          localSaveSettingPlaceholder: `//默认执行不修改 content 的函数
(content) => { // content edit编辑好的内容
  return content; // 不修改输出的内容
}
/*(content) => { // 输出修改示例
  var time = new Date().getTime();
  var newContent = {
    createTime: time,
    // …… 更多内容
    content: content
  }
  return JSON.stringify(newContent); // 返回必须是字符串
}*/`,
          serverSaveSettingJsonPlaceholder: `//默认执行不修改 content 的函数
(content) => { // content edit编辑好的内容
  return content; // 不修改输出的内容
}
/*(content) => { // 输出修改示例
  var time = new Date().getTime();
  var newContent = {
    createTime: time,
    // …… 更多内容
    content: content
  }
  return newContent; // 返回json对象
}*/`,
          contentNamePlaceholder: '默认content'
        }
      },
      edit: {
        dom: null,
        instance: null
      },
      saveDialog: {
        display: false,
        placeholder: '',
        saveAs: false,
        saveTo: 'local',
        saveSetting: '',
        saveName: 'script.js',
        requestUrl: 'http://',
        requestMethod: 'get',
        requestDataType: 'x-www-form-urlencoded',
        requestDataFormat: '',
        contentName: '',
        requestParam: []
      }
    }
  },
  methods: {
    save () {
      var saveSetting = '(content) => {return content;}'
      if (this.saveDialog.saveSetting) {
        saveSetting = this.saveDialog.saveSetting
      }
      var content = this.runDialogSaveSettingFunction(saveSetting)
      if (content) {
        switch (this.saveDialog.saveTo) {
          case 'local':
            var link = document.createElement('a') // 创建隐藏的可下载链接
            link.style.display = 'none'
            document.body.appendChild(link)
            link.download = this.saveDialog.saveName
            var blob = new Blob([content]) // 字符内容转变成blob地址
            link.href = URL.createObjectURL(blob)
            link.click() // 触发点击
            document.body.removeChild(link) // 然后移除
            this.saveAs = true
            break
          case 'webServer':
            var url = this.saveDialog.requestUrl
            this.$http.post(url, content).then((response) => {
              console.info(response)
            }, (response) => {
              console.info(response)
            })
            this.saveAs = true
            break
          default:
            break
        }
      }
    },
    runDialogSaveSettingFunction (codeBlock) {
      const Fn = Function
      var def = null
      try {
        return new Fn('return ' + codeBlock).apply(def, []).apply(def, [this.edit.instance.getModel().getValue()])
      } catch (error) {
        this.$message.error('保存设置函数执行错误!')
        return null
      }
    },
    addRequestParam () {
      this.saveDialog.requestParam.push({
        key: '',
        type: 'text',
        value: ''
      })
    },
    removeRequestParam (index) {
      this.saveDialog.requestParam.splice(index, 1)
    },
    setSaveDialogRequestParamType ({ index, type }) {
      var item = this.saveDialog.requestParam[index]
      if (item.type !== type) {
        item.type = type
        item.value = ''
      }
    }
  },
  created () {
    this.saveDialog.placeholder = this.defaultValue.saveDialog.localSaveSettingPlaceholder
    this.$http.get('/edit/getProperties', { credentials: true }).then((response) => {
      console.info(response)
    }, (response) => {
      console.info(response)
    })
  },
  mounted () {
    const dom = document.getElementById('editContainer')
    this.edit.dom = dom
    dom.style.height = this.height + 'px'
    const editInstance = monaco.editor.create(
      dom,
      Object.assign({}, this.defaultValue.edit.options, this.options)
    )
    this.edit.instance = editInstance
    if (this.events.save) {
      editInstance.addAction({
        id: 'editor.action.clipboardSaveAction',
        label: 'Save',
        keybindings: [monaco.KeyMod.chord(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S)],
        precondition: null,
        keybindingContext: null,
        contextMenuGroupId: '1_modification',
        contextMenuOrder: 1.4,
        run: () => { this.saveDialog.display = true }
      })
    }
  },
  watch: {
    'saveDialog.requestMethod': {
      handler: function (val, oldVal) {
        if (val !== oldVal) {
          switch (val) {
            case 'get':
              this.saveDialog.requestDataType = 'x-www-form-urlencoded'
              this.saveDialog.requestDataFormat = ''
              break
            case 'post':
              this.saveDialog.requestDataType = 'form-data'
              this.saveDialog.requestDataFormat = ''
              break
            default:
              break
          }
        }
      },
      deep: true
    },
    'saveDialog.requestDataType': {
      handler: function (val, oldVal) {
        if (val !== oldVal) {
          switch (val) {
            case 'form-data':
              this.saveDialog.requestDataFormat = ''
              break
            case 'x-www-form-urlencoded':
              this.saveDialog.requestDataFormat = ''
              this.saveDialog.requestParam = this.saveDialog.requestParam.filter(item => { return item.type === 'text' })
              break
            case 'raw':
              this.saveDialog.requestDataFormat = 'json'
              break
            case 'binary':
              this.saveDialog.requestDataFormat = ''
              break
            default:
              break
          }
        }
      },
      deep: true
    },
    'saveDialog.requestDataFormat': {
      handler: function (val, oldVal) {
        if (val !== oldVal) {
          switch (val) {
            case 'text':
              this.saveDialog.placeholder = this.defaultValue.saveDialog.localSaveSettingPlaceholder
              break
            case 'javascript':
              this.saveDialog.placeholder = this.defaultValue.saveDialog.localSaveSettingPlaceholder
              break
            case 'json':
              this.saveDialog.placeholder = this.defaultValue.saveDialog.serverSaveSettingJsonPlaceholder
              break
            case 'html':
              this.saveDialog.placeholder = this.defaultValue.saveDialog.localSaveSettingPlaceholder
              break
            case 'xml':
              this.saveDialog.placeholder = this.defaultValue.saveDialog.localSaveSettingPlaceholder
              break
            default:
              break
          }
        }
      },
      deep: true
    }
  }
}
</script>

<style scoped>
.input-default-style {
  width: 217px;
}
</style>
