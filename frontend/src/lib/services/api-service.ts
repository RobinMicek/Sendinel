import axios from 'axios';
import { preferedDatatablePageSizeStore, tokenStore } from '@/stores/store-factory';
import { BACKEND_API_BASE_URL, DEFAULT_DATATABLE_PAGE_SIZE } from '@/config';

abstract class APIService {
	api = axios.create({
		baseURL: BACKEND_API_BASE_URL
	});

	//constructor() {
	//	if (typeof process !== 'undefined' && process?.env?.DEVELOPMENT_BACKEND_URL) {
	//		this.api.defaults.baseURL = process.env.DEVELOPMENT_BACKEND_URL;
	//	}
	//}

	getHeaders () {
		return {
			"Authorization": "Bearer " + tokenStore.get(),
			"Content-Type": "application/json"
		}
	}

    getPreferedDatatablePageSize() {
        const preferedPageSize = preferedDatatablePageSizeStore.get()
        if (preferedPageSize) return preferedPageSize
        return DEFAULT_DATATABLE_PAGE_SIZE
    }
}

export default APIService;