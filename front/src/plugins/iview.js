import Vue from 'vue'
import 'view-design/dist/styles/iview.css'
import { Card, Button, Row, Col, Form, FormItem, Input, Checkbox, DropdownMenu, DropdownItem, InputNumber, Icon, Tooltip, Dropdown, Table, Modal } from 'view-design';

// 引入IView, 按需引入
export default {
    install() {
        Vue.component('Card', Card);
        Vue.component('Button', Button);
        Vue.component('Row', Row);
        Vue.component('Col', Col);
        Vue.component('Form', Form);
        Vue.component('FormItem', FormItem);
        Vue.component('Input', Input);
        Vue.component('Checkbox', Checkbox);
        Vue.component('DropdownMenu', DropdownMenu);
        Vue.component('DropdownItem', DropdownItem);
        Vue.component('InputNumber', InputNumber);
        Vue.component('Icon', Icon);
        Vue.component('Tooltip', Tooltip);
        Vue.component('Dropdown', Dropdown);
        Vue.component('Table', Table);
        Vue.component('Modal', Modal);

        Vue.prototype.$Modal = Modal
    }
}