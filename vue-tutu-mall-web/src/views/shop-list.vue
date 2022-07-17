<template>
  <div class="shop-list">
    <van-nav-bar title="店铺列表" left-text="返回" left-arrow/>
    <van-search
        v-model="value"
        show-action
        placeholder="请输入搜索关键词"
        @search="onSearch"
        @cancel="onCancel"
    />

    <van-dropdown-menu>
      <van-dropdown-item v-model="value1" :options="option1"/>
      <van-dropdown-item v-model="value2" :options="option2"/>
    </van-dropdown-menu>
    <van-nav-bar>店铺1</van-nav-bar>
    <van-divider/>
    <van-card
        num="2"
        price="2.00"
        desc="描述信息"
        title="商品标题"
        thumb="https://img01.yzcdn.cn/vant/ipad.jpeg"
    >
      <template #footer>
        <van-button type="default">点击查看</van-button>
      </template>
    </van-card>
  </div>


</template>

<script>
export default {
  name: 'shop-list',
  components: {},
  data() {
    return {
      value: '',
      value1: 0,
      value2: 'a',
      option1: [
        {text: '全部商品', value: 0},
        {text: '新款商品', value: 1},
        {text: '活动商品', value: 2},
      ],
      option2: [
        {text: '默认排序', value: 'a'},
        {text: '好评排序', value: 'b'},
        {text: '销量排序', value: 'c'},
      ],
    };
  },
  methods: {
    onSearch(val) {
      Toast(val);
    },
    onCancel() {
      Toast('取消');
    },
  },
}
</script>
<style>

</style>
