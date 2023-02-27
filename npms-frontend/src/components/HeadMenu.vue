<template>
    <div class="head-menu">
        <el-space wrap size="large">
        
            <el-popover trigger="hover" placement="bottom" :width="200">
                <template #reference>
                    <el-badge type="danger" is-dot> 
                        <i class="el-icon-message"/>
                    </el-badge>
                </template>
                <div>消息待定</div>
            </el-popover>

            <el-popover trigger="hover" placement="bottom" :width="220">
                <template #reference>
                    <el-badge type="danger" is-dot>
                        <i class="el-icon-bell"/>
                    </el-badge>
                </template>
                <div>
                    <el-tabs v-model="tabsActive" @tab-click="onTab">
                        <el-tab-pane label="通知" name="notice">
                            <el-row v-for="(item,n) in notices" :key="n" @click="onManage(item.id)">
                                <el-col :span="6">
                                    <el-avatar :icon="item.icon" :size="40"></el-avatar>
                                </el-col>
                                <el-col :span="18">
                                    <div>{{ item.message }}</div>
                                    <div>{{ item.dateTime }}</div>
                                </el-col>
                                <el-divider></el-divider>
                            </el-row>
                        </el-tab-pane>
                        <el-tab-pane label="消息" name="news">
                            <el-row v-for="(item,n) in news" :key="n" @click="onManage(item.id)">
                                <el-col :span="6">
                                    <el-avatar :icon="item.icon" :size="40"></el-avatar>
                                </el-col>
                                <el-col :span="18">
                                    <div>{{ item.message }}</div>
                                    <div>{{ item.dateTime }}</div>
                                </el-col>
                                <el-divider></el-divider>
                            </el-row>
                        </el-tab-pane>
                        <el-tab-pane label="待办" name="untreated">
                            <el-row v-for="(item,u) in untreateds" :key="u" @click="onManage(item.id)">
                                <el-col :span="6">
                                    <el-avatar :icon="item.icon" :size="40"></el-avatar>
                                </el-col>
                                <el-col :span="18">
                                    <div>{{ item.message }}</div>
                                    <div>{{ item.dateTime }}</div>
                                </el-col>
                                <el-divider></el-divider>
                            </el-row>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </el-popover>
            <el-dropdown>
                    <span class="el-dropdown-link">
                        <span>{{ empId }}</span>
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item icon="el-icon-switch-button" @click="logout">退出</el-dropdown-item>
                        <el-dropdown-item icon="el-icon-unlock" @click="onPassword">修改密码</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </el-space>
    </div>
</template>

<script>
    import {onMounted, reactive, toRefs} from 'vue'
    import {useRouter} from 'vue-router'

    export default {
        setup() {
            const router = useRouter()
            const state = reactive({
                tabsActive: 'notice',
                notices: [],
                news: [],
                empId:'',
                untreateds: [],
            })

            onMounted(()=>{
                state.empId = window.sessionStorage.getItem('username')
                state.notices = [
                    {id:1,icon:'el-icon-chat-dot-round',message:'通知1',dateTime:'2021-07-22 12:15:21'},
                    {id:2,icon:'el-icon-chat-dot-round',message:'通知2',dateTime:'2021-07-22 12:15:21'},
                    {id:3,icon:'el-icon-chat-dot-round',message:'通知3',dateTime:'2021-07-22 12:15:21'},
                    {id:4,icon:'el-icon-chat-dot-round',message:'通知4',dateTime:'2021-07-22 12:15:21'},
                ]

                state.news = [
                    {id:1,icon:'el-icon-chat-dot-round',message:'消息1',dateTime:'2021-07-22 12:15:21'},
                    {id:2,icon:'el-icon-chat-dot-round',message:'消息2',dateTime:'2021-07-22 12:15:21'},
                    {id:3,icon:'el-icon-chat-dot-round',message:'消息3',dateTime:'2021-07-22 12:15:21'},
                    {id:4,icon:'el-icon-chat-dot-round',message:'消息4',dateTime:'2021-07-22 12:15:21'},
                ]

                state.untreateds = [
                    {id:1,icon:'el-icon-chat-dot-round',message:'待办1',dateTime:'2021-07-22 12:15:21'},
                    {id:2,icon:'el-icon-chat-dot-round',message:'待办2',dateTime:'2021-07-22 12:15:21'},
                    {id:3,icon:'el-icon-chat-dot-round',message:'待办3',dateTime:'2021-07-22 12:15:21'},
                    {id:4,icon:'el-icon-chat-dot-round',message:'待办4',dateTime:'2021-07-22 12:15:21'},
                ]
            })

            const onManage = (id) =>{
                console.log(id)
            }

            const onTab = (tab, event) => {
                console.log(tab, event);
            }

            const logout = () => {
                window.sessionStorage.setItem('username','unknown')
                window.sessionStorage.setItem('token','')
                window.sessionStorage.setItem('position','')
                router.push({path: 'login'})
            }

            const onPassword = () => {
                router.push({path: 'user-password'})
            }

            return {
                ...toRefs(state),
                logout,
                onPassword,
                onTab,
                onManage
            }
        }
    }
</script>

<style lang="less">
    .head-menu {
        text-align: right;
        cursor: pointer;
        margin-right: -24px;
        .el-badge__content.is-fixed.is-dot {
            top: 15px;
        }
    }
</style>