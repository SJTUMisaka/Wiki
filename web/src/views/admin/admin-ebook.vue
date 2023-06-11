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
                                @search="handleQuery({
                            page: 1,
                            size: pagination.pageSize,
                        })">
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
                    :data-source="ebooks"
                    :pagination="pagination"
                    :loading="loading"
                    @change="handleTableChange"
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
                                title="Are you sure delete this ebook?"
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
            title="Ebook Form"
            v-model:visible="modalVisible"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="Cover">
                <a-input v-model:value="ebook.cover"/>
            </a-form-item>
            <a-form-item label="Name">
                <a-input v-model:value="ebook.name"/>
            </a-form-item>
            <a-form-item label="Category1">
                <a-input v-model:value="ebook.category1Id"/>
            </a-form-item>
            <a-form-item label="Category2">
                <a-input v-model:value="ebook.category2Id"/>
            </a-form-item>
            <a-form-item label="Description">
                <a-input v-model:value="ebook.description" type="textarea"/>
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
        name: 'AdminEbook',
        setup() {
            const name = ref('');
            const ebooks = ref();
            const pagination = ref({
                current: 1,
                pageSize: 2,
                total: 0
            });
            const loading = ref(false);

            const columns = [
                {
                    title: 'Cover',
                    dataIndex: 'cover',
                    slots: {customRender: 'cover'}
                },
                {
                    title: 'Name',
                    dataIndex: 'name'
                },
                {
                    title: 'Category1Id',
                    key: 'category1Id',
                    dataIndex: 'category1Id'
                },
                {
                    title: 'Category2Id',
                    key: 'category2Id',
                    dataIndex: 'category2Id'
                },
                {
                    title: 'DocCount',
                    dataIndex: 'docCount'
                },
                {
                    title: 'ViewCount',
                    dataIndex: 'viewCount'
                },
                {
                    title: 'VoteCount',
                    dataIndex: 'voteCount'
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
            const handleQuery = (params: any) => {
                loading.value = true;
                ebooks.value = [];
                axios.get("/ebook/list", {
                    params: {
                        page: params.page,
                        size: params.size,
                        name: name.value,
                    }
                }).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        ebooks.value = data.content.list;
                        // Reset Pagination
                        pagination.value.current = params.page;
                        pagination.value.total = data.content.total;
                    } else {
                        message.error(data.message);
                    }
                });
            };

            /**
             * 表格点击页码时触发
             */
            const handleTableChange = (pagination: any) => {
                handleQuery({
                    page: pagination.current,
                    size: pagination.pageSize
                });
            };

            const ebook = ref({});
            const modalVisible = ref(false);
            const modalLoading = ref(false);
            const handleModalOk = () => {
                modalLoading.value = true;
                axios.post("/ebook/save", ebook.value).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        modalVisible.value = false;
                        modalLoading.value = false;

                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    } else {
                        message.error(data.message);
                        modalLoading.value = false;
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    }
                });
            };

            /**
             * Edit
             */
            const edit = (record: any) => {
                console.log("edit called");
                modalVisible.value = true;
                ebook.value = Tool.copy(record);
            };

            /**
             * Add
             */
            const add = () => {
                console.log("add called");
                modalVisible.value = true;
                ebook.value = {};
            };


            const handleDelete = (id: number) => {
                axios.delete("/ebook/delete/" + id).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    }
                });
            };

            onMounted(() => {
                handleQuery({
                    page: 1,
                    size: pagination.value.pageSize
                });
            });

            return {
                ebooks,
                pagination,
                columns,
                loading,
                handleTableChange,

                edit,
                add,
                ebook,
                name,
                handleQuery,

                modalVisible,
                modalLoading,
                handleModalOk,
                handleDelete
            }
        }
    });
</script>