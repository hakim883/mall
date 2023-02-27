import axios from '../utils/axios'

// =======================start==============================
/**
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
export function login(params) {
    return axios.post('/login', params);
}

// =======================end==============================

// ===========================start=========================
/**
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
export function outstandingIssues(params) {
    return axios.post('/admin/home/outstandingIssues', params);
}

/**
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
export function revenueAnalysis(params) {
    return axios.post('/admin/home/revenueAnalysis', params);
}

/**
 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
export function overview(params) {
    return axios.post('/admin/home/overview', params);
}

// ===========================end=========================

// =======================start==============================
/**

 * @param params
 * @returns {Promise<AxiosResponse<T>>}
 */
 export function getMainPage(params) {
    return axios.get('http://localhost:8848/cusManagement/customer/getMainPage', params);
}

// =======================end==============================
