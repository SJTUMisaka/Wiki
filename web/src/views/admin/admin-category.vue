<template>
    <a-layout :style="{ display: 'flex' }">
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p>
                <a-form layout="inline">
                    <a-form-item>
                        <a-input-search
                                v-model:value="name"
                                placeholder="input search text"
                                enter-button="Search"
                                size="large"
                                @search="search()">
                        </a-input-search>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="add()" size="large">
                            Add
                        </a-button>
                    </a-form-item>
                </a-form>

            </p>
            <a-table
                    :columns="columns"
                    :row-key="record => record.id"
                    :data-source="categories"
                    :pagination="false"
                    :loading="loading"
            >
                <template #cover="{ text: cover }">
                    <img v-if="cover" :src="cover" alt="avatar"/>
                </template>
                <template v-slot:action="{ text, record }">
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)">
                            Edit
                        </a-button>
                        <a-popconfirm
                                title="Are you sure delete this category?"
                                ok-text="Yes"
                                cancel-text="No"
                                @confirm="handleDelete(record.id)"
                        >
                            <a-button type="danger">
                                Delete
                            </a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
    <a-modal
            title="Category Form"
            v-model:visible="modalVisible"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="Name">
                <a-input v-model:value="category.name"/>
            </a-form-item>
            <a-form-item label="Parent">
                <a-input v-model:value="category.parent"/>
                <a-select
                    v-model:value="category.parent"
                    ref="select">
                    <a-select-option value="0">
                        No Parent
                    </a-select-option>
                    <a-select-option v-for="c in categories" :key="c.id" :value="c.id" :disabled="category.id == c.id">
                        {{c.name}}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="Sort">
                <a-input v-model:value="category.sort"/>
            </a-form-item>
        </a-form>
    </a-modal>
</template>


<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';
    import {message} from "ant-design-vue";
    import {Tool} from "@/util/tool"

    export default defineComponent({
        name: 'AdminCategory',
        setup() {
            const name = ref('');
            const paramName = ref('');
            const categories = ref();
            const loading = ref(false);

            const columns = [
                {
                    title: 'Name',
                    dataIndex: 'name'
                },
                {
                    title: 'Parent',
                    key: 'parent',
                    dataIndex: 'parent'
                },
                {
                    title: 'Sort',
                    key: 'sort',
                    dataIndex: 'sort'
                },
                {
                    title: 'Action',
                    key: 'action',
                    slots: {customRender: 'action'}
                }
            ];

            /**
             * 数据查询
             **/
            const handleQuery = () => {
                loading.value = true;
                categories.value = [];
                axios.get("/category/all").then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        categories.value = Tool.array2Tree(data.content, 0);
                    } else {
                        message.error(data.message);
                    }
                });
            };


            const category = ref({});
            const modalVisible = ref(false);
            const modalLoading = ref(false);
            const handleModalOk = () => {
                modalLoading.value = true;
                axios.post("/category/save", category.value).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        modalVisible.value = false;
                        modalLoading.value = false;

                        handleQuery();
                    } else {
                        message.error(data.message);
                        modalLoading.value = false;
                        handleQuery();
                    }
                });
            };

            /**
             * Edit
             */
            const edit = (record: any) => {
                console.log("edit called");
                modalVisible.value = true;
                category.value = Tool.copy(record);
            };

            /**
             * Add
             */
            const add = () => {
                console.log("add called");
                modalVisible.value = true;
                category.value = {};
            };

            /**
             * Search
             */
            const search = () => {
                console.log("search called");
                paramName.value = name.value;
                handleQuery()
            };

            const handleDelete = (id: number) => {
                axios.delete("/category/delete/" + id).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        handleQuery();
                    }
                });
            };

            onMounted(() => {
                handleQuery();
            });

            return {
                categories: categories,
                columns,
                loading,

                edit,
                add,
                category,
                name,
                search,

                modalVisible,
                modalLoading,
                handleModalOk,
                handleDelete
            }
        }
    });
</script>