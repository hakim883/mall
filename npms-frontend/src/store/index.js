import {createStore} from 'vuex'

export default createStore({
    state: {
        token: ''
    },
    mutations: {
        // token
        setToken(state, token) {
            state.token = token;
            localStorage.token = token;
        },
        // token
        delToken(state) {
            state.token = '';
            localStorage.removeItem('token');
        }
    },
    getters: {
        // token
        getToken(state) {
            if (!state.token) {
                state.token = localStorage.getItem('token');
            }
            return state.token;
        }
    },
    actions: {},
    modules: {}
});
